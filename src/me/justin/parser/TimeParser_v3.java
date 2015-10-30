package me.justin.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TimeParser_v3 {
	TimeParser_v3(){
		if(rule_year_month_date_to_pattern == null)
			rule_year_month_date_to_pattern = this.rule_to_pattern(rule_year_month_date);
		
		if(rule_hourofday_minute_second_to_pattern == null)
			rule_hourofday_minute_second_to_pattern = this.rule_to_pattern(rule_hourofday_minute_second);
		
		if(rule_set_to_pattern == null)
			rule_set_to_pattern = this.rule_to_pattern(rule_set);
	}
	
	// //////////////////// STRUCTURE begin ///////////////////////
	// BASIC DIFINE
	
	// time unit
	class time_unit{
		final static int UNIT_UNKOWN = -1;
		
		final static int UNIT_YEAR = 1; 
		final static int UNIT_MONTH = 2;
		final static int UNIT_DATE = 3;
		final static int UNIT_WEEK_OF_YEAR = 4;
		final static int UNIT_DAY_OF_WEEK = 5;
		
		final static int UNIT_HOUR = 6;
		final static int UNIT_MINUTE = 7;
		final static int UNIT_SECOND = 8;
		
		final static int UNIT_AMPM = 9;
		final static int UNIT_HALFHOUR = 10;
		final static int UNIT_QUARTERHOUR = 11;
		
		final static int UNIT_HOUR_OF_DAY = 12;
		
		// relative
		final static int UNIT_RELATIVEYEAR = 21;
		final static int UNIT_RELATIVEMONTH = 22;
		final static int UNIT_RELATIVEDATE = 23;
		final static int UNIT_RELATIVEWEEK_OF_YEAR = 24;
		final static int UNIT_RELATIVEDAY = 25;
		
		final static int UNIT_RELATIVEHOUR = 26;
		final static int UNIT_RELATIVEMINUTE = 27;
		final static int UNIT_RELATIVESECOND = 28;
		
		final static int UNIT_RELATIVEMONTHEND = 28;
		
	}
	
	// type of time
	class time_type{
		final static int TYPE_TIMEPOINT = 1;
		//final static int TYPE_TIMEPOINT_RELATIVE = 2;
		final static int TYPE_DURATION = 3;
		final static int TYPE_SET = 4;
	}
	
	// result of parser_single_time_unit
	class parser_result{
		int unit;
		String str_v;

		parser_result(){
			unit = time_unit.UNIT_UNKOWN;
			str_v = "";
		}
	}
	
	class parser_results{
		boolean matched;
		HashMap<Integer, String> p_result;

		parser_results(){
			matched = false;
			p_result = new HashMap<Integer, String>();
		}
	}
	
	class time_interval{
		int type; // 
		int value;
		
		time_interval(){
			type = -1; // Calendar.
			value = -1;
		}
	}
	
	// string duration keywords
	final static String DURATION_KEYWORDS = "至|到";
	
	// string set keywords
	final static String SET_KEYWORDS = "每隔|每";
	
	// NORMALIZATION
	final static char zh_digit_keywords[] = { '零', '一', '二', '三', '四', '五',
        '六', '七', '八', '九', '两' };
	final static Map<Character, Float> zh_map_char = new HashMap<Character, Float>() {
		private static final long serialVersionUID = 1L;
	    {
	        put('零', 0f);
	        put('一', 1f);
	        put('二', 2f);
	        put('两', 2f);
	        put('三', 3f);
	        put('四', 4f);
	        put('五', 5f);
	        put('六', 6f);
	        put('七', 7f);
	        put('八', 8f);
	        put('九', 9f);
	        put('十', 10f);
	        put('百', 100f);
	        put('千', 1000f);
	        put('万', 10000f);
	        put('亿', 100000000f);
	    }
	};
	
	final static Map<String, Integer> map_string2timeunit = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
	    {
	        put("year", time_unit.UNIT_YEAR);
	        put("month", time_unit.UNIT_MONTH);
	        put("date", time_unit.UNIT_DATE);
	        
	        put("weekofyear", time_unit.UNIT_WEEK_OF_YEAR);
	        put("dayofweek", time_unit.UNIT_DAY_OF_WEEK);
	        
	        put("hourofday", time_unit.UNIT_HOUR_OF_DAY); // 24H
	        put("minute", time_unit.UNIT_MINUTE);
	        put("second", time_unit.UNIT_SECOND);
	        
	        put("halfhour", time_unit.UNIT_HALFHOUR);
	        put("quarterhour", time_unit.UNIT_QUARTERHOUR);
	        
	        put("relativeyear", time_unit.UNIT_RELATIVEYEAR);
	        put("relativemonth", time_unit.UNIT_RELATIVEMONTH);
	        put("relativedate", time_unit.UNIT_RELATIVEDATE);
	        put("relativeweekofyear", time_unit.UNIT_RELATIVEWEEK_OF_YEAR);

	        put("relativehour", time_unit.UNIT_RELATIVEHOUR);
	        put("relativeminute", time_unit.UNIT_RELATIVEMINUTE);
	        put("relativesecond", time_unit.UNIT_RELATIVESECOND);
	        
	        put("ampm", time_unit.UNIT_AMPM);
	        put("relativemonthend", time_unit.UNIT_RELATIVEMONTHEND);
	        
	    }
	};
	
	// RELATIVE_THISNEXT
	final static Map<String, Integer> norRelativeThisNext = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
	    {
	    	put("THIS", 0);
	        put("NEXT", 1);
	        put("NEXXT", 2);
	        put("NEXXXT", 3);
	    }
	};
	final static Map<String, Integer> norConstant = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
	    {
	    	put("ZERO", 0);
	    }
	};
	final static Map<String, Integer> norAMPM = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
	    {
	    	put("上午", Calendar.AM); //0 上午
	    	put("下午", Calendar.PM); //1 下午
	    	put("晚上", Calendar.PM); //1 晚上
	    }
	};
	final static Map<String, Integer> norDayofweek = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
	    {
	    	put("DAYOFWEEK1", Calendar.MONDAY); //星期一, 2
	    	put("DAYOFWEEK2", Calendar.TUESDAY);
	    	put("DAYOFWEEK3", Calendar.WEDNESDAY);
	    	put("DAYOFWEEK4", Calendar.THURSDAY);
	    	put("DAYOFWEEK5", Calendar.FRIDAY);
	    	put("DAYOFWEEK6", Calendar.SATURDAY);
	    	put("DAYOFWEEK7", Calendar.SUNDAY); // 星期日，1 
	    }
	};
	final static Map<String, Integer> norIntervalunit = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
	    {
	    	put("YEAR", Calendar.YEAR);
	    	put("MONTH", Calendar.MONTH);
	    	put("WEEK", Calendar.WEEK_OF_YEAR);
	    	put("DATE", Calendar.DATE);
	    	
	    	put("HOUR", Calendar.HOUR_OF_DAY);
	    	put("MINUTE", Calendar.MINUTE);
	    	put("SECOND", Calendar.SECOND);
	    }
	};
	
	// RE-PARTTERN
	
    // this-next-last, relative time point
	final static String[] this_reg = {
		"今"
	};
	final static String[] next_reg = {
		"明",
		"下"
	};
	final static String[] nexxt_reg = {
		"后"
	};
	final static String[] nexxxt_reg = {
		"大后"
	};
	final static String[] ampm_reg = {
		"上午",
		"下午",
		"晚上",
	};
	
	// 五年后提醒我
	final static String[] number_reg = {
		"[\\d]+",
		"[一二三四五六七八九十]+",
	};
    
    //year
    final static String[] year4Digit_reg = {
		"[12]\\d\\d\\d",
		"[一二１２][零一二三四五六七八九０１２３４５６７８９]{3}"
	};
    
    //month
    final static String[] MonthNumber_reg = {
    	"1[012]",
    	"0?[1\\-9]",
		
		"１[０１２]",
		"０?[１２３４５６７８９]",
		
		"十[一二]",
		"[一二三四五六七八九十]",
    };
    
    //date
    final static String[] DateNumber_reg = {
		"[12][0123456789]",
		"3[01]",
		"0?[1\\-9]",
		
		"[十][一二三四五六七八九]",
		"二十[一二三四五六七八九]",
		"三十",
		"三十一",
		"[一二三四五六七八九十]",
		
    	"[１２][０１２３４５６７８９]",
		"[３][０１]",
		"[１２３４５６７８９]",
    };
    /*
    final static String[] DateWord_reg = {
    	"大后天",
    	"后天",
    };*/
    
    //week
    
    //day
    final static String[] DayofweekNumber_reg = {
		"[1\\-7]",
		"[１２３４５６７]",
		"[一二三四五六七]",
    };
    
    //hour
    final static String[] HourofdayNumber_reg = {
    	"0[1\\-9]",
    	"00",
		"[1][0123456789]",
		"[2][01234]",
		"[1\\-9]",
		
		"二十[一二三四]",
		"零[零一二三四五六七八九]",
		"十[一二三四五六七八九]?",
		"[零一二三四五六七八九]",
    };

    
    //minute
    final static String[] MinuteNumber_reg = {
    	"0[1\\-9]",
    	"00",
    	"[1\\-5][0123456789]",
		"60",

		"零[零一二三四五六七八九]",
		"[二三四五]?十[一二三四五六七八九]",
		"六十",
		"[零一二三四五六七八九]",
    };
    
    //second
    final static String[] SecondNumber_reg = {
    	"0[1\\-9]",
    	"00",
    	"[1\\-5][0123456789]",
		"60",
		
		"零[零一二三四五六七八九]",
		"[二三四五]?十[一二三四五六七八九]",
		"六十",
		"[零一二三四五六七八九]",
    };
    final static LinkedHashMap<String, String[]> repattern_all = new LinkedHashMap<String, String[]>() {
        private static final long serialVersionUID = 1L;
        {
            put("reYear4Digit", year4Digit_reg);
            put("reMonthNumber", MonthNumber_reg);
            put("reDateNumber", DateNumber_reg);
            //put("reDateWord_reg", DateWord_reg);
            put("reNext", next_reg);
            put("reNexxt", nexxt_reg);
            put("reNexxxt", nexxxt_reg);
            put("reNumber", number_reg);
            put("reThis", this_reg);
            
            put("reDayofweekNumber", DayofweekNumber_reg);
            
            put("reHourofdayNumber", HourofdayNumber_reg);
            put("reMinuteNumber", MinuteNumber_reg);
            put("reSecondNumber", SecondNumber_reg);
            
            put("reAMPM", ampm_reg);
        }
    };

	// RULE
    //year, month, date
    final static LinkedHashMap<String, String> rule_year_month_date = new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
        	/* EXTRACTION ,      NORM_VALUE*/
        	put("%reYear4Digit年%reMonthNumber月%reDateNumber(日|号)", "year=group(1);month=group(2);date=group(3)");
        	put("%reYear4Digit年%reMonthNumber月", "year=group(1);month=group(2)");
        	put("%reNext年%reMonthNumber月%reDateNumber(日|号)", "relativeyear=%NEXT;month=group(2);date=group(3)");
        	put("%reNexxt年%reMonthNumber月%reDateNumber(日|号)", "relativeyear=%NEXXT;month=group(2);date=group(3)");
        	put("%reNext月%reDateNumber(日|号){0,1}", "relativemonth=%NEXT;date=group(2)");
        	
            put("%reYear4Digit年", "year=group(1);");
            put("%reYear2Digit年", "year=group(1);");
            put("%reNext年", "relativeyear=%NEXT;");
            put("%reNexxt年", "relativeyear=%NEXXT;");
            put("%reNumber年后", "relativeyear=group(1)");
            put("%reThis年", "relativeyear=THIS");
            
            put("%reMonthNumber月", "month=group(1)");
            
            put("%reNext月末", "relativemonth=%NEXT;relativemonthend=%1;"); // relativemonthend=%1 means relativemonthend is true
            
            put("%reNext月", "relativemonth=%NEXT;");
            put("%reNext(周|礼拜|星期)%reDayofweekNumber", "relativeweekofyear=%NEXT;dayofweek=%DAYOFWEEKgroup(3);");
            put("%reNext(周|礼拜|星期)(天|日)", "relativeweekofyear=%NEXT;dayofweek=%DAYOFWEEK7;");
            
            put("%reNexxxt天", "relativedate=%NEXXXT;");
            put("%reNext天", "relativedate=%NEXT;");
            put("%reNexxt天", "relativedate=%NEXXT;");
            put("%reDateNumber(日|号)", "date=group(1);");
            put("月%reDateNumber", "date=group(1);");
            
            put("(星期|礼拜|周)%reDayofweekNumber", "dayofweek=%DAYOFWEEKgroup(2);");
            put("(星期|礼拜|周)(天|日)", "dayofweek=%DAYOFWEEK7;");
            
            put("月末", "relativemonthend=%1;"); // boolean
            
            put("现在", "year=%NOW;month=%NOW;date=%NOW;");
        }
    };
    LinkedHashMap<String, String> rule_year_month_date_to_pattern = null;
    
    
    //hour
    final static LinkedHashMap<String, String> rule_hourofday_minute_second = new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
        	/* EXTRACTION ,      NORM_VALUE*/
            put("%reHourofdayNumber(:|：)%reMinuteNumber(:|：)%reSecondNumber", "hourofday=group(1);minute=group(3);second=group(5);");
            put("%reAMPM%reHourofdayNumber点%reMinuteNumber分%reSecondNumber秒", "ampm=%AMPMgroup(1);hourofday=group(2);minute=group(3);second=group(4);");
            put("%reHourofdayNumber点%reMinuteNumber分%reSecondNumber秒", "hourofday=group(1);minute=group(2);second=group(3);");
        	put("%reHourofdayNumber点%[过]{0,1}reMinuteNumber分", "hourofday=group(1);minute=group(2);");
        	put("%reHourofdayNumber(:|：)%reMinuteNumber", "hourofday=group(1);minute=group(3);second=%ZERO;");
        	
        	put("%reAMPM%reHourofdayNumber点半", "ampm=%AMPMgroup(1);hourofday=group(2);minute=%30;second=%ZERO;");
        	put("%reAMPM%reHourofdayNumber点", "ampm=%AMPMgroup(1);hourofday=group(2);minute=%ZERO;second=%ZERO;");
        	put("%reHourofdayNumber点半", "hourofday=group(1);minute=%30;second=%ZERO;");
            put("%reHourofdayNumber点", "hourofday=group(1);minute=%ZERO;second=%ZERO;");
            
            put("%reMinuteNumber分%reSecondNumber秒", "minute=group(1);second=group(2);");

            put("%reNumber[个]?半小时后", "relativehour=group(1);relativeminute=%30;");
            put("%reNumber[个]?小时后", "relativehour=group(1);");
            put("%reNumber分钟后", "relativeminute=group(1);");
            put("%reMinuteNumber分", "minute=group(1);");
            put("%reSecondNumber秒", "second=group(1);");
            put("一会", "relativeminute=%5;");
            
            put("现在", "hourofday=%NOW;minute=%NOW;second=%NOW;");
        }
    };
    LinkedHashMap<String, String> rule_hourofday_minute_second_to_pattern=null;
    
  //hour
    final static LinkedHashMap<String, String> rule_set = new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
        	/* EXTRACTION ,      NORM_VALUE*/
        	// 每(隔)一个半小时，每(隔)?半小时，每半小时，每过几分钟，每过多少秒
        	put("每隔%reNumber[个]?半小时", "relativehour=group(1);relativeminute=%30;"); // relativehour: means repeat on after every relativehour
        	put("每隔%reNumber[个]?小时", "relativehour=group(1);");
        	put("每隔半小时", "relativeminute=%30;");
        	
        	// 每年七月八日，每周五，每月多少号，每天
        	put("每年%reMonthNumber月%reDateNumber(日|号)", "relativeyear=%1;month=group(1);date=group(2)");
        	put("每(星期|礼拜|周)%reDayofweekNumber", "relativeweek=%1;month=group(1);date=group(2)");
        }
    };
    LinkedHashMap<String, String> rule_set_to_pattern=null;
    
	//\\\\\\\\\\\\\\\\\\\\ STRUCTURE end \\\\\\\\\\\\\\\\\\\\\\\\
	
	// /////////////////// FUNCTION begin /////////////////////////
    private static boolean isDigit(char c) {
        if (c >= '0' && c <= '9' || c == '.') {
            return true;
        }
        return false;
    }
    
    private static boolean isZhDigit(char c) {
        for (char a : zh_digit_keywords) {
            if (c == a)
                return true;
        }
        return false;
    }
    
    private static String convertZhToNumber(String zh_number) {
        double result = 0;
        double pre_unit = 0f;
        double unit = 0;
        double num = 0;
        double billon = 0;
        char a = '0';
        boolean hasZero = false;
        
        //System.out.println("convertZhToNumber input= " + zh_number);
        
        if(zh_number.length() == 4)
        {
        	Pattern pattern_v = null;
			Matcher matcher_v = null;
			pattern_v = Pattern.compile("[零一二三四五六七八九]{4}"); 
			matcher_v = pattern_v.matcher(zh_number);
			if(matcher_v.find()){
				double u1, u2, u3, u4;
				u1 = zh_map_char.get(zh_number.charAt(0));
				u2 = zh_map_char.get(zh_number.charAt(1));
				u3 = zh_map_char.get(zh_number.charAt(2));
				u4 = zh_map_char.get(zh_number.charAt(3));
				return Integer.toString((int) (u1*1000+u2*100+u3*10+u4));
			}
        }
        
        StringBuffer digit = new StringBuffer();
        for (int i = 0; i < zh_number.length(); i++) {
            a = zh_number.charAt(i);
            // 这是阿拉伯数字
            if (isDigit(a)) {
                digit.append(a);
                unit = 0; // 不需要转换，转换单位清0
                num = 0;
            } else {
                // 中文表示需要取转换的值
                unit = zh_map_char.get(a);
                // 如果是零，做了记录，如果后面没有单位出现，那么数字直接乘以1.比如一千零八
                if (unit == 0) {
                    hasZero = true;
                    continue;
                }
                // 如果是中文数字
                if (isZhDigit(a)) {
                    // is a digit
                    num = unit;
                } else if (unit == 10 && num == 0 && digit.length() == 0) {// 如果'十'是第一位，
                                                                           // 比如对应十五块八
                    result = 10;
                } else {
                    // 如果是金额单位，
                    // 如果之前有阿拉伯数字，那么使用阿拉伯数字。
                    if (num == 0 && digit.length() != 0) {
                        num = Double.valueOf(digit.toString());
                        digit = new StringBuffer();
                    }
                    // 出现了单位，清除零的标识
                    hasZero = false;
                    // 记录当前金额单位，用于金额最后没有单位的时候，取上一个单位*0.1
                    pre_unit = unit;
                    if (unit == 100000000) {
                        result += num;
                        result = result * unit;
                        billon = result;
                        result = 0;
                        num = 0;
                    } else if (unit == 10000) {
                        result += num;
                        result = result * unit;
                        num = 0;
                    } else if (unit >= 10) {
                        result = result + num * unit;
                        num = 0;
                    } else if (unit <= 1) {
                        result = result + num * unit;
                        num = 0;
                    } else {
                        // not unit, it's a digit
                        System.err.println("出错了");
                    }
                }
            }
        }

        if (digit.length() != 0) {
            num = Double.valueOf(digit.toString());
        }

        if ((pre_unit != 0f) && unit == 0f && !hasZero) {
            if (digit.length() > 1) {
                pre_unit = pre_unit * Math.pow(0.1, (digit.length() - 1));
            }
            num = num * pre_unit * 0.1f;
        }
        if ((pre_unit != 0f) && isZhDigit(a) && !hasZero) {
            num = num * pre_unit * 0.1f;
        }
        result = billon + result + num;
        int ret = (int) result;
        
        //System.out.println("convertZhToNumber output= " + result);
        
        return Integer.toString(ret);
    }
    
    // translate rule to regular expression
    public LinkedHashMap<String, String> rule_to_pattern(LinkedHashMap<String, String> rule_input)
    {
    	LinkedHashMap<String, String> rule_output = new LinkedHashMap<String, String>();
    	LinkedHashMap<String, String> rule_output_tmp;
    	ArrayList<LinkedHashMap<String, String>> rule_output_list = new ArrayList<LinkedHashMap<String, String>>();
    	
    	LinkedHashMap<String, String[]> repattern = repattern_all;
    	
    	String regular_expression = null;
				
		int i,j;
    	
    	Iterator mapite = null;
		mapite=rule_input.entrySet().iterator();
		
		// go-through the rule map
		while(mapite.hasNext()){
			ArrayList<String> extraction_str = new ArrayList<String>(); // String[] for one rule
			ArrayList<String> extraction_str_tmp = new ArrayList<String>();
			
			Map.Entry mapentry=(Map.Entry)mapite.next();
			
			Object key=mapentry.getKey(); // %reYear4Digit年;%reMonthNumber月;%reDateNumber(日|号)
			Object value=mapentry.getValue(); // year=group(1);month=group(2);date=group(3)
			
			Object key_repattern;
			
			//convert EXTRACTION to regular expression
			extraction_str.add(key.toString());
			
			Pattern pattern_extra = null;
			Matcher matcher_extra = null;
			pattern_extra = Pattern.compile("%(re[a-zA-Z0-9]*)"); // find reYear4Digit...
			matcher_extra = pattern_extra.matcher(key.toString());
			
			// loop ReXxx in one rule
			while(matcher_extra.find()){
				key_repattern = matcher_extra.group(1);
				//System.out.print("rule_to_pattern key_repattern = " + key_repattern + "\n");

				// convert ReXxx to regular expression
				if(repattern.containsKey(key_repattern))
				{
					String[] repattern_str = repattern.get(key_repattern); // regular expression list[]
					extraction_str_tmp = (ArrayList<String>) extraction_str.clone();
					extraction_str = new ArrayList<String>();
					
					// try to match input with regular expression one by one
					//System.out.println("rule_to_pattern repattern_str.length = " + repattern_str.length);
					//System.out.println("rule_to_pattern extraction_str_tmp.size() = " + extraction_str_tmp.size());
					for(i=0; i<repattern_str.length; i++ ){
						//System.out.println("rule_to_pattern repattern_str["+i+"]="+repattern_str[i]);
						//System.out.println("rule_to_pattern i = " + i);
						
						for(j=0; j<extraction_str_tmp.size(); j++){
							regular_expression = (String) extraction_str_tmp.get(j);
							regular_expression = regular_expression.replaceAll("%"+key_repattern.toString(), 
									"("+repattern_str[i]+")");
							//System.out.println("rule_to_pattern regular_expression="+regular_expression);
							//System.out.println("rule_to_pattern j = " + j);
							extraction_str.add(regular_expression);
						}
					}
				}
			}
			
			for(i=0; i<extraction_str.size(); i++){
				//System.out.println("rule_to_pattern extraction_str["+i+"]="+ extraction_str.get(i));
				rule_output_tmp = new LinkedHashMap<String, String>();
				rule_output_tmp.put(extraction_str.get(i), value.toString());
				rule_output_list.add(rule_output_tmp);
			}
		}
		
		for(i=0; i<rule_output_list.size(); i++){
			rule_output.putAll(rule_output_list.get(i));
		}
    	
		//System.out.println("rule_to_pattern rule_output.size()" + rule_output.size());
    	return rule_output;
    }
    
    public static int String2TimeUnit(String str){
    	int result = time_unit.UNIT_UNKOWN;
    	
    	if(map_string2timeunit.containsKey(str)){
    		return map_string2timeunit.get(str);
    	}
    	
    	return result;
    }
    
	// parser multiple time unit
    // year, month, date
    // hour, minute, second
	public parser_results parser_multiple_time_unit(
			String input,
			Map<String, String> rule,
			Map<String, String[]> repattern
			)
	{
		//System.out.println("parser_multiple_time_unit input=" + input);
		
		parser_results p_results = new parser_results();
		int group_num = 1; // int[], for NORM_VALUE
		
		int i;
		
		Iterator mapite = null;
		mapite=rule.entrySet().iterator();
		
		// go-through the rule map
		while(mapite.hasNext()){
			Map.Entry mapentry=(Map.Entry)mapite.next();
			
			Object key=mapentry.getKey(); 
			Object value=mapentry.getValue(); 
			
			Pattern pattern = null;
			Matcher matcher = null;
			pattern = Pattern.compile(key.toString());
			matcher = pattern.matcher(input);
			
			if(matcher.find()){
				//System.out.println("parser_multiple_time_unit key=" + key.toString());
				//System.out.println("parser_multiple_time_unit value=" + value.toString());
				for(i=0; i<matcher.groupCount(); i++ ){
					//System.out.println("parser_multiple_time_unit " + matcher.group(i+1));
				}
				
				String value_str = value.toString();
				String[] value_str_split = value_str.split(";");
				for(i=0; i<value_str_split.length; i++){
					parser_result pr = new parser_result();
					String[] expression_value = value_str_split[i].split("="); // year=group(1)

					pr.unit = String2TimeUnit(expression_value[0]); // year
					
					if(expression_value[1].startsWith("%")){
						pr.str_v = expression_value[1].substring(0);
						
						Pattern pattern_v = null;
						Matcher matcher_v = null;
						pattern_v = Pattern.compile("group\\((\\d)\\)"); 
						matcher_v = pattern_v.matcher(expression_value[1]);
						if(matcher_v.find()){
							group_num = Integer.parseInt(matcher_v.group(1));
							pr.str_v =  pr.str_v.replaceAll("group\\("+matcher_v.group(1)+"\\)", matcher.group(group_num));
						}
					}
					else{
						Pattern pattern_v = null;
						Matcher matcher_v = null;
						pattern_v = Pattern.compile("group\\((\\d)\\)"); 
						matcher_v = pattern_v.matcher(expression_value[1]);
						if(matcher_v.find()){
							group_num = Integer.parseInt(matcher_v.group(1));
						}
						pr.str_v = matcher.group(group_num); // group(1)
					}
					
					//System.out.println("parser_multiple_time_unit pr.unit str = " + expression_value[0]);
					//System.out.println("parser_multiple_time_unit pr.str_v = " + pr.str_v);
					
					p_results.p_result.put(pr.unit, pr.str_v);
				}
				
				p_results.matched = true;
				return p_results;
			}
		}
		
		return p_results;
	}
	
	// if duration
	public static boolean if_duration(String input){
		boolean result = false;
		Pattern pattern = null;
		Matcher matcher = null;
		
		pattern = Pattern.compile(DURATION_KEYWORDS);
		matcher = pattern.matcher(input);
		
		if(matcher.find()){
			result = true;
		}
		
		return result;
	}
	
	// if set, a set of time-point means repeat
	public static boolean if_set(String input){
		boolean result = false;
		Pattern pattern = null;
		Matcher matcher = null;
		
		pattern = Pattern.compile(SET_KEYWORDS);
		matcher = pattern.matcher(input);
		
		if(matcher.find()){
			result = true;
		}
		
		return result;
	}
	
	public int normalize_time(String input, int unit){ // time_unit
		int output = -1;
		
		//System.out.println("normalize_time input=" + input + "  unit= " + unit );
		
		Calendar cal = Calendar.getInstance();
		
		if(input==null){
			return output;
		}
		
		if(input.indexOf("%")!=-1)
		{
			String value_str = input.substring(1);
			
			if(norRelativeThisNext.containsKey(value_str)){
				output = norRelativeThisNext.get(value_str);
			} else if(norConstant.containsKey(value_str)){
				output = norConstant.get(value_str);
			} else if(input.indexOf("%DAYOFWEEK")!=-1){
				// day of week
				String Zh = value_str.split("DAYOFWEEK")[1];
				if(norDayofweek.containsKey("DAYOFWEEK"+convertZhToNumber(Zh))){
					output = norDayofweek.get("DAYOFWEEK"+convertZhToNumber(Zh));
				}
			} else if(input.indexOf("%AMPM")!=-1){
				String ampm = value_str.split("AMPM")[1];
				//System.out.println("normalize_time ampm=" + ampm);
				if(norAMPM.containsKey(ampm)){
					output = norAMPM.get(ampm);
				}
			} else if(input.indexOf("%UNIT")!=-1){
				String interval_unit = value_str.split("UNIT")[1];
				if(norIntervalunit.containsKey(interval_unit)){
					output = norIntervalunit.get(interval_unit);
				}
			} else if(input.indexOf("%NOW")!=-1) {
				switch (unit) {
				case time_unit.UNIT_YEAR:
					output = cal.get(Calendar.YEAR);
					break;
				case time_unit.UNIT_MONTH:
					output = cal.get(Calendar.MONTH);
					break;
				case time_unit.UNIT_DATE:
					output = cal.get(Calendar.DATE);
					break;
				case time_unit.UNIT_HOUR_OF_DAY:
					output = cal.get(Calendar.HOUR_OF_DAY);
					break;
				case time_unit.UNIT_MINUTE:
					output = cal.get(Calendar.MINUTE);
					break;
				case time_unit.UNIT_SECOND:
					output = cal.get(Calendar.SECOND);
					break;
				}
					
			}else {
				// %1, %2...
				output = Integer.parseInt(convertZhToNumber(value_str));
			}
			
		}else{
			String result = null;
			result = convertZhToNumber(input);
			//System.out.println("normalize_time result = " + result);
			output = Integer.parseInt(result);
		}

		//System.out.println("normalize_time output = " + output);
		return output;
	}
	
	// parser time point
	public String parser_time_point( String input )
	{
		String output = "";
		
		// calendar of current time, we use this for calculating
		Calendar cal = Calendar.getInstance();
		
		// time-point and relative value from input
		int year = -1;
		int month = -1;
		int date = -1;
		int weekofyear=-1;
		int dayofweek=-1;
		int ampm = -1;
		int hourofday = -1;
		int minute = -1;
		int second = -1;
		int relativeyear = -1;
		int relativemonth = -1;
		int relativedate = -1;
		int relativeweekofyear = -1;
		int relativedayofweek = -1;
		int relativehour = -1;
		int relativeminute = -1;
		int relativesecond = -1;
		// 月末
		int relativemonthend = -1; // 1 is true
		
		parser_results prs = null;
		
		// year, month, date
		prs = parser_multiple_time_unit(input, rule_year_month_date_to_pattern, repattern_all);
		year = normalize_time(prs.p_result.get(time_unit.UNIT_YEAR), time_unit.UNIT_YEAR);
		month = normalize_time(prs.p_result.get(time_unit.UNIT_MONTH), time_unit.UNIT_MONTH);
		date = normalize_time(prs.p_result.get(time_unit.UNIT_DATE), time_unit.UNIT_DATE);
		//weekofyear, NOT USED at present
		dayofweek = normalize_time(prs.p_result.get(time_unit.UNIT_DAY_OF_WEEK), time_unit.UNIT_DAY_OF_WEEK); // day of week
		relativeyear = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEYEAR), time_unit.UNIT_RELATIVEYEAR);
		relativemonth = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEMONTH), time_unit.UNIT_RELATIVEMONTH);
		relativedate = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEDATE), time_unit.UNIT_RELATIVEDATE);
		relativeweekofyear = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEWEEK_OF_YEAR), time_unit.UNIT_RELATIVEWEEK_OF_YEAR);
		relativemonthend = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEMONTHEND), time_unit.UNIT_RELATIVEMONTHEND);
		
		// hour, minute, second
		prs = parser_multiple_time_unit(input, rule_hourofday_minute_second_to_pattern, repattern_all);
		ampm = normalize_time(prs.p_result.get(time_unit.UNIT_AMPM), time_unit.UNIT_AMPM);
		hourofday = normalize_time(prs.p_result.get(time_unit.UNIT_HOUR_OF_DAY), time_unit.UNIT_HOUR_OF_DAY);
		minute = normalize_time(prs.p_result.get(time_unit.UNIT_MINUTE), time_unit.UNIT_MINUTE);
		second = normalize_time(prs.p_result.get(time_unit.UNIT_SECOND), time_unit.UNIT_SECOND);
		relativehour = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEHOUR), time_unit.UNIT_RELATIVEHOUR);
		relativeminute = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEMINUTE), time_unit.UNIT_RELATIVEMINUTE);
		relativesecond = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVESECOND), time_unit.UNIT_RELATIVESECOND);
	
		// calculate calendar
		// set calendar according to absolute value
		if(year!=-1) cal.set(Calendar.YEAR, year);
		if(month!=-1) cal.set(Calendar.MONTH, month-1);
		if(date!=-1) cal.set(Calendar.DATE, date);
		if(ampm!=-1){ // set hour according to ampm
			if(hourofday!=-1) cal.set(Calendar.HOUR, hourofday);
			cal.set(Calendar.AM_PM, ampm);
		}else{
			if(hourofday!=-1) cal.set(Calendar.HOUR_OF_DAY, hourofday);
		}
		if(minute!=-1) cal.set(Calendar.MINUTE, minute);
		if(second!=-1) cal.set(Calendar.SECOND, second);
		
		// set calendar according to relative value	
		if(relativeyear!=-1) cal.add(Calendar.YEAR, relativeyear);
		if(relativemonth!=-1) cal.add(Calendar.MONTH, relativemonth);
		if(relativedate!=-1) cal.add(Calendar.DATE, relativedate);
		if(relativeweekofyear!=-1) cal.add(Calendar.WEEK_OF_YEAR, relativeweekofyear);
		
		if(relativemonthend==1){ // 月末
			int monthend = cal.getActualMaximum(Calendar.DATE);
			cal.set(Calendar.DATE, monthend);
		}
		
		if(relativehour!=-1) cal.add(Calendar.HOUR_OF_DAY, relativehour);
		if(relativeminute!=-1) cal.add(Calendar.MINUTE, relativeminute);
		if(relativesecond!=-1) cal.add(Calendar.SECOND, relativesecond);
		
		// if there is no info of hour, minute, second, set them to 0
		if(relativehour==-1 && relativeminute==-1 && relativesecond==-1 &&
				hourofday==-1 && minute==-1 && second==-1){
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
		}
		if(dayofweek!=-1) cal.set(Calendar.DAY_OF_WEEK, dayofweek);
		
		// adjust
		// in China Monday is the first day of a week, so 本周日 means next Monday when today is not Sunday
		if((cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY) && dayofweek==Calendar.SUNDAY) 
			cal.add(Calendar.WEEK_OF_YEAR, 1);
		
		int month_forprint = cal.get(Calendar.MONTH) +1; // month need +1 for print, due to January is 0
		
		output = cal.get(Calendar.YEAR)+ "-" + month_forprint + "-" + cal.get(Calendar.DATE) + " " + 
				cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
	
		//System.out.println("parser_time_point output= " + output);
		
		return output;
	}
	
	// parser time point
	public String parser_time_set( String input )
	{
		String output = "";
		// start point
		String start_point = "";
		String interval_v = "";
		
		// time-point and relative value from input
		int year = -1;
		int month = -1;
		int date = -1;
		int weekofyear=-1;
		int dayofweek=-1;
		int ampm = -1;
		int hourofday = -1;
		int minute = -1;
		int second = -1;
		int relativeyear = -1;
		int relativemonth = -1;
		int relativedate = -1;
		int relativeweekofyear = -1;
		int relativedayofweek = -1;
		int relativehour = -1;
		int relativeminute = -1;
		int relativesecond = -1;
		// 月末
		int relativemonthend = -1; // 1 is true
		
		// interval, year, month, weekofyear, date, hour, minute, second
		time_interval interval = new time_interval();
		
		parser_results prs = null;
		prs = parser_multiple_time_unit(input, rule_set_to_pattern, repattern_all);
		
		year = normalize_time(prs.p_result.get(time_unit.UNIT_YEAR), time_unit.UNIT_YEAR);
		month = normalize_time(prs.p_result.get(time_unit.UNIT_MONTH), time_unit.UNIT_MONTH);
		date = normalize_time(prs.p_result.get(time_unit.UNIT_DATE), time_unit.UNIT_DATE);
		//weekofyear, NOT USED at present
		dayofweek = normalize_time(prs.p_result.get(time_unit.UNIT_DAY_OF_WEEK), time_unit.UNIT_DAY_OF_WEEK); // day of week
		relativeyear = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEYEAR), time_unit.UNIT_RELATIVEYEAR);
		relativemonth = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEMONTH), time_unit.UNIT_RELATIVEMONTH);
		relativedate = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEDATE), time_unit.UNIT_RELATIVEDATE);
		relativeweekofyear = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEWEEK_OF_YEAR), time_unit.UNIT_RELATIVEWEEK_OF_YEAR);
		relativemonthend = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEMONTHEND), time_unit.UNIT_RELATIVEMONTHEND);
		// hour, minute, second
		ampm = normalize_time(prs.p_result.get(time_unit.UNIT_AMPM), time_unit.UNIT_AMPM);
		hourofday = normalize_time(prs.p_result.get(time_unit.UNIT_HOUR_OF_DAY), time_unit.UNIT_HOUR_OF_DAY);
		minute = normalize_time(prs.p_result.get(time_unit.UNIT_MINUTE), time_unit.UNIT_MINUTE);
		second = normalize_time(prs.p_result.get(time_unit.UNIT_SECOND), time_unit.UNIT_SECOND);
		relativehour = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEHOUR), time_unit.UNIT_RELATIVEHOUR);
		relativeminute = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVEMINUTE), time_unit.UNIT_RELATIVEMINUTE);
		relativesecond = normalize_time(prs.p_result.get(time_unit.UNIT_RELATIVESECOND), time_unit.UNIT_RELATIVESECOND);
		
		// convert YEAR, MONTH, WEEK, DATE to calendar unit
		interval_v = (relativeyear==-1?0:relativeyear) + "-" + (relativemonth==-1?0:relativemonth) + "-" + (relativedate==-1?0:relativedate) + " " + 
				(relativehour==-1?0:relativehour) + ":" + (relativeminute==-1?0:relativeminute) + ":" + (relativesecond==-1?0:relativesecond);
		
		start_point = (year==-1?0:year) + "-" + (month==-1?0:month) + "-" + (date==-1?0:date) + " " + 
				(hourofday==-1?0:hourofday) + ":" + (minute==-1?0:minute) + ":" + (second==-1?0:second);
		
		output = "parser_time_set start_point=" + start_point +" interval_v= " + interval_v;
		//System.out.println(output);
		
		return output;
	}
	
	// parser time
	public String parser_time( String input )
	{
		String output = "";
		
		System.out.println("parser_time input = " + input);
		
		// duration, (time-point2 - time-point1), in seconds
		int duration;
		String[] str_array_duration = null;
		String str_begin, str_end, duration_begin_output, duration_end_output;
		
		// set, every time period, in seconds
		int set;
		String[] str_array_set = null;
		String str_set, str_set_output;
		
		//time point
		String str_timepoint;
		
		// if duration
		if(if_duration(input)){
			str_array_duration = input.split(DURATION_KEYWORDS);
			if(str_array_duration!=null && str_array_duration.length == 2){
				str_begin = str_array_duration[0];
				str_end = str_array_duration[1]; // TODO deal with SET_KEYWORDS  从今天下午3点到明天下午4点每隔一小时提醒我
				
				//System.out.println("str_begin = " + str_begin);
				duration_begin_output = parser_time_point(str_begin);
				System.out.println("parser_time output duration_begin = " + duration_begin_output);

				if(if_set(str_end)){
					String[] str_array = str_end.split(SET_KEYWORDS);
					//System.out.println("str_array[0] = " + str_array[0]);
					duration_end_output = parser_time_point(str_array[0]);
				}else{
					duration_end_output = parser_time_point(str_end);
				}
				System.out.println("parser_time output duration_end = " + duration_end_output);
			}
		}
		
		if(if_set(input)){
			// set
			Pattern pattern = Pattern.compile("(("+SET_KEYWORDS+").*)");
			Matcher matcher = pattern.matcher(input);
			if(matcher.find()){
				str_set = matcher.group(0);
				//System.out.println("str_set = " + str_set);
				str_set_output = parser_time_set(str_set);
				System.out.println("parser_time output set = " + str_set_output);
			}
		}
		
		if(!if_duration(input) && !if_set(input)){
			// not duration, not set, so it is normal time point
			str_timepoint = parser_time_point(input);
			System.out.println("parser_time output timepoint = " + str_timepoint);
		}
		
		return output;
	}
	
	// \\\\\\\\\\\\\\\\\\\ FUNCTION end \\\\\\\\\\\\\\\\\\\\\\\\\\
	
	public static void main(String[] args) {
		/*
		if(false){
			Pattern pattern = Pattern.compile("([1\\-9])");
			Matcher matcher = pattern.matcher("161");
			if(matcher.find()){
				String output;
				output = matcher.group(0);
				System.out.println("------------- output  = " + output);
			}
		}
		*/
		
		TimeParser_v3 tp = new TimeParser_v3();
		tp.parser_time("一九九七年八月十五日");
		tp.parser_time("九点十三分五十五秒");
		tp.parser_time("十三分五十五秒");
		tp.parser_time("一九九七年七月十五日九点十三分五十五秒");
		tp.parser_time("明年七月八号");
		tp.parser_time("后年七月八号");
		tp.parser_time("五年后");

		tp.parser_time("下月十五");
		tp.parser_time("5:59:16");
		tp.parser_time("5:59");
		tp.parser_time("明天十二点");
		
		tp.parser_time("一小时后");
		tp.parser_time("五小时后"); // need am pm 24hrs
		
		tp.parser_time("周五九点");
		tp.parser_time("周五上午九点");
		tp.parser_time("周五下午九点");
		tp.parser_time("周日");
		tp.parser_time("下周日");
		
		//tp.parser_time("从今年三月到明年三月"); //duration
		
		tp.parser_time("九月十三八点提醒我");

		tp.parser_time("今晚9:25提醒我开会");
		tp.parser_time("晚上六点提醒我朋友请吃饭");

		tp.parser_time("周六上午九点提醒我");
		tp.parser_time("周一五点提醒我");
		tp.parser_time("星期天下午五点半提醒我");
		tp.parser_time("周六23：59分提醒我");
		tp.parser_time("周一下午3点25分16秒提醒我");

		tp.parser_time("后天十点半提醒");
		tp.parser_time("大后天十点半提醒");
		tp.parser_time("大后天12:35:55提醒");
		tp.parser_time("明天上午十点提醒我");

		tp.parser_time("一个半小时后提醒我");
		tp.parser_time("五个小时后提醒我");
		tp.parser_time("十分钟后提醒我");
		
		tp.parser_time("一会去骑自行车");

		tp.parser_time("下月末提醒我锻炼");
		tp.parser_time("月末提醒我面试");
		tp.parser_time("每隔一个半小时提醒我");
		tp.parser_time("从现在起到晚上11点，每隔半小时提醒我");
		
		tp.parser_time("周一到周五每天上午8点提醒我");
		tp.parser_time("每天上午8点提醒我");
		// 九点差五分
		// 九点一刻
		//tp.parser_time("");
	}
}
