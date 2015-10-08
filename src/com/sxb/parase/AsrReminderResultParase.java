package com.sxb.parase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.sxb.parase.data.Alarm;
import com.sxb.parase.data.Alarm.DaysOfWeek;

public class AsrReminderResultParase {
    private final static boolean DEBUG = true;

    final static int LETTER = 0;
    final static int DIGIT = 1;
    // used for time
    final static int TIME_UNIT = 5;  //时间单位 小时，分钟等
    final static int TIME_TIME = 6;  //比如 明天 上午等
    final static int DURATION = 7;
    final static int ASSIST = 8;
    final static int TIME_DIGIT = 9; // 比如 半
    final static int TIME_REPEAT = 10; // 比如 半
    
    final static int TIME_COLON = 20;
    
    final static int DEFAULTHOUR = 9;
    /**
     * 
     */
    final static char zh_digit_keywords[] = { '零', '一', '二', '三', '四', '五',
            '六', '七', '八', '九', '两' };
    final static Map<Character, Float> zh_map_char = new HashMap<Character, Float>() {
        /**
         * 
         */
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
    final static Map<String, Integer> week_map = new HashMap<String, Integer>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            put("每周一", 1);
            put("每周二", 2);
            put("每周三", 3);
            put("每周四", 4);
            put("每周五", 5);
            put("每周六", 6);
            put("每周日", 7);
            put("每周天", 7);
        }
    };
    private static int[] WEEK_DAY_MAP = new int[] { Calendar.MONDAY,
            Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY,
            Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY, };
    /**
     * Alarm Reminder Parase
     * */
    // ,"每周一","每周二","每周三","每周四","每周五","每周六","每周日","每周天"
    private final static char time_zh_digit_keywords[] = { '十', '一', '二', '三',
            '四', '五', '六', '七', '八', '九', '两' };
    private final static String[] year_strs = { "年" };
    private final static String[] month_strs = { "月" };
    private final static String[] day_strs = { "天", "号", "日" };
    private final static String[] hour_strs = { "时", "小时", "点",":" };
    private final static String[] minute_strs = { "分", "分钟" };
    private final static String[] second_strs = { "秒" };
    
    final static Map<String, Integer> time_key_type_map_1 = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 1L;
        {
            put("时", TIME_UNIT);
            put("刻", TIME_UNIT);
            put("分", TIME_UNIT);
            put("秒", TIME_UNIT);
            put("半", TIME_UNIT);
            put("号", TIME_UNIT);
            put("月", TIME_UNIT);
            put("周", TIME_UNIT);
            put("点", TIME_UNIT);
            put(":", TIME_COLON);
            put("：", TIME_COLON);
            put("至", DURATION);
            put("到", DURATION);
        }
    };
    final static Map<String, Integer> time_key_type_map_2 = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 1L;
        {
            put("提前", ASSIST);
            put("往后", ASSIST);
            
            put("小时", TIME_UNIT);
            put("分钟", TIME_UNIT);
            put("晚上", TIME_UNIT);
            
            put("现在", TIME_TIME);
            put("明天", TIME_TIME);
            put("早晨", TIME_TIME);
            put("上午", TIME_TIME);
            put("早上", TIME_TIME);
            put("下午", TIME_TIME);
            put("后天", TIME_TIME);
            
            put("凌晨", TIME_TIME);
            put("午夜", TIME_TIME);
            put("黎明", TIME_TIME);
            put("破晓", TIME_TIME);
            put("清晨", TIME_TIME);
            put("半晌", TIME_TIME);
            put("晌午", TIME_TIME);
            put("正午", TIME_TIME);
            put("中午", TIME_TIME);
            put("黄昏", TIME_TIME);
            put("傍晚", TIME_TIME);
            put("夜间", TIME_TIME);
            put("半夜", TIME_TIME);
            put("明早", TIME_TIME);
            put("头晌", TIME_TIME);
            put("后晌", TIME_TIME);
            put("今晚", TIME_TIME);
            
            put("一会", TIME_TIME);
            put("年末", TIME_TIME);
            put("月末", TIME_UNIT);
            put("周末", TIME_TIME);
            put("周日", TIME_TIME);
            put("周天", TIME_TIME);
            put("每天", TIME_REPEAT);
            put("每周", TIME_UNIT);
            put("每月", TIME_REPEAT);
            put("每年", TIME_REPEAT);
            put("每隔", TIME_REPEAT);
            put("周一", TIME_TIME);
            put("周二", TIME_TIME);
            put("周三", TIME_TIME);
            put("周四", TIME_TIME);
            put("周五", TIME_TIME);
            put("周六", TIME_TIME);
            
            put("立刻", TIME_TIME);
            put("立马", TIME_TIME);
            put("立即", TIME_TIME);
            put("稍等", TIME_TIME);
            put("稍候", TIME_TIME);
            put("稍后", TIME_TIME);
            put("马上", TIME_TIME);
            put("待会", TIME_TIME);
            put("呆会", TIME_TIME);
            put("回头", TIME_TIME);
            put("及时", TIME_TIME);
            put("等会", TIME_TIME);
            put("尽快", TIME_TIME);
            put("尽早", TIME_TIME);
            put("一会", TIME_TIME);
            
            put("子时", TIME_TIME);
            put("丑时", TIME_TIME);
            put("寅时", TIME_TIME);
            put("卯时", TIME_TIME);
            put("辰时", TIME_TIME);
            put("巳时", TIME_TIME);
            put("午时", TIME_TIME);
            put("未时", TIME_TIME);
            put("申时", TIME_TIME);
            put("酉时", TIME_TIME);
            put("戌时", TIME_TIME);
            put("亥时", TIME_TIME);
            
        }
    };
    final static Map<String, Integer> time_key_type_map_3 = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 1L;
        {
            
            put("个小时", TIME_UNIT);
            put("等一会", TIME_TIME);
            put("等一下", TIME_TIME);
            put("一会儿", TIME_TIME);
            put("待一会", TIME_TIME);
            put("呆一会", TIME_TIME);
            put("过一会", TIME_TIME);
            put("呆一会", TIME_TIME);
            put("下月末", TIME_TIME);
            put("下周末", TIME_TIME);
            put("半小时", TIME_TIME);
            put("半分钟", TIME_TIME);
            put("每周日", TIME_REPEAT);
            put("每周天", TIME_REPEAT);
            put("每周末", TIME_REPEAT);
            put("每周末", TIME_REPEAT);
            
            put("早饭后", TIME_TIME);
            put("午饭后", TIME_TIME);
            put("前半晌", TIME_TIME);
            put("后半晌", TIME_TIME);
            
            put("一两点", TIME_TIME);
            put("两三点", TIME_TIME);
            put("三四点", TIME_TIME);
            put("四五点", TIME_TIME);
            put("五六点", TIME_TIME);
            put("六七点", TIME_TIME);
            put("七八点", TIME_TIME);
            put("八九点", TIME_TIME);
            put("九十点", TIME_TIME);
            
            put("现在起", TIME_TIME);
            
            put("星期一", TIME_TIME);
            put("星期二", TIME_TIME);
            put("星期三", TIME_TIME);
            put("星期四", TIME_TIME);
            put("星期五", TIME_TIME);
            put("星期六", TIME_TIME);
            put("星期日", TIME_TIME);
            put("星期天", TIME_TIME);
            
            put("大后天", TIME_TIME);
            
        }
    };
    final static Map<String, Integer> time_key_type_map_4 = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 1L;
        {
            put("个半小时", TIME_UNIT);
            put("半个小时", TIME_TIME);
            put("回头马上", TIME_TIME);
            put("中午以后", TIME_TIME);
            put("十一二点", TIME_TIME);

        }
    };
    
    final static String[][] time_digit_unit_map = {
        {"D年",    "year=D"},
        {"D月",    "month=D"},
        {"D天",    "ay=D"},
        {"D号",    "day=D;type=2"},
        {"D日",    "day=D;type=2"},
        {"D:",    "hour=D;type=2"},
        {"D：",    "hour=D;type=2"},
        {"D小时",   "hour=D"},
        {"D时",    "hour=D"},
        {"D点",    "hour=D;type=2"},
        {"D分",    "minute=D"},
        {"D分钟",   "minute=D"},
        {"D秒",    "second=D"},
        {"D月末",   "month=D;day=-1;type=2;defaultHour=9"},
        {"D个半小时", "hour=D;minute=30"},
        {"D个小时",  "hour=D"}
    };
    final static String[][] time_unit_digit_map = {
        {"周D", "weekday=D"},
        {"星期D", "weekday=D"}
    };
    final static Map<String, String> default_time_key_map = new HashMap<String, String>() {
        /**
         *   public static final int ALARM_TYPE_RELATIVE = 1;
             public static final int ALARM_TYPE_ABSOLUTE = 2;
         */
        private static final long serialVersionUID = 1L;
        {
            put("现在",  "hour=now;minute=now");
            put("现在起", "hour=now;minute=now");
            put("明天",  "addDay=1;type=2");
            put("后天",  "addDay=2;type=2");
            put("大后天", "addDay=3;type=2");
            
            //固定时间的模糊字词,用户可以设置
            put("今晚", "defaultHour=20;type=2;ampm=2");
            put("下午",  "defaultHour=14;type=2;ampm=2");
            put("早晨",  "defaultHour=7;type=2;ampm=1");
            put("早上",  "defaultHour=7;type=2;ampm=1");
            put("凌晨", "defaultHour=0;type=2");
            put("午夜", "defaultHour=1;type=2");
            put("黎明", "defaultHour=5;type=2");
            put("破晓", "defaultHour=5;type=2");
            put("清晨", "defaultHour=7;type=2");
            put("上午", "defaultHour=8;ampm=1;type=2");
            put("半晌", "defaultHour=10;type=2");
            put("晌午", "defaultHour=10;type=2");
            put("正午", "defaultHour=12;type=2");
            put("中午", "defaultHour=12;type=2");
            put("黄昏", "defaultHour=18;ampm=2;type=2");
            put("傍晚", "defaultHour=18;ampm=2;type=2");
            put("晚上", "defaultHour=20;ampm=2;type=2");
            put("夜间", "defaultHour=22;ampm=2;type=2");
            put("半夜", "defaultHour=0;type=2");
            put("明早", "addDay=1;defaultHour=7;type=2");
            put("头晌",  "defaultHour=9;type=2");
            put("后晌",  "defaultHour=16;type=2;ampm=2");
            put("前半晌", "defaultHour=9;type=2");
            put("后半晌", "defaultHour=16;defaultMinute=30;type=2;ampm=2");
            
            put("早饭后", "defaultHour=7;defaultMinute=30");
            put("午饭后", "defaultHour=13;ampm=2");
            put("中午以后","defaultHour=12;ampm=2");
            
            put("周一", "weekday=1;type=2");
            put("周二", "weekday=2;type=2");
            put("周三", "weekday=3;type=2");
            put("周四", "weekday=4;type=2");
            put("周五", "weekday=5;type=2");
            put("周六", "weekday=6;type=2");
            put("周日",  "weekday=7;type=2");
            put("周天",  "weekday=7;type=2;defaultHour=9");
            put("周末",  "weekday=6;type=2;defaultHour=9");
            put("星期一", "weekday=1;type=2");
            put("星期二", "weekday=2;type=2");
            put("星期三", "weekday=3;type=2");
            put("星期四", "weekday=4;type=2");
            put("星期五", "weekday=5;type=2");
            put("星期六", "weekday=6;type=2");
            put("星期日", "weekday=7;type=2");
            put("星期天", "weekday=7;type=2;defaultHour=9");
            
            put("月末",  "day=-1;type=2;defaultHour=9");   //-1代表一个月的最后一天
            put("年末",  "month=12;day=31;type=2;defaultHour=9"); 
            put("半小时", "minute=30");
            put("半分钟", "second=30");
            put("半个小时","minute=30");
            
            put("星期天", "weekday=7;type=2");
            put("星期日", "weekday=7;type=2");
            put("下月末", "addMonth=1;day=-1;type=2");   //-1代表一个月的最后一天
            //相对时间
            put("一会儿", "defaultMinute=5");
            put("等一会", "defaultMinute=10");
            put("等一下", "defaultMinute=10");
            put("待一会", "defaultMinute=10");
            put("呆一会", "defaultMinute=10");
            put("过一会", "defaultMinute=20");
            put("回头马上","defaultMinute=15");
            put("立刻", "defaultMinute=3");
            put("立马", "defaultMinute=3");
            put("立即", "defaultMinute=3");
            put("稍等", "defaultMinute=5");
            put("稍候", "defaultMinute=5");
            put("稍后", "defaultMinute=5");
            put("一会", "defaultMinute=5");
            put("马上", "defaultMinute=10");
            put("待会", "defaultMinute=10");
            put("呆会", "defaultMinute=10");
            put("回头", "defaultMinute=15");
            put("及时", "defaultMinute=15");
            put("等会", "defaultMinute=30");
            put("尽快", "defaultMinute=30");
            put("尽早", "defaultMinute=30");
            
            put("一两点", "defaultHour=1;type=2");
            put("两三点", "defaultHour=2;type=2");
            put("三四点", "defaultHour=3;type=2");
            put("四五点", "defaultHour=4;type=2");
            put("五六点", "defaultHour=5;type=2");
            put("六七点", "defaultHour=6;type=2");
            put("七八点", "defaultHour=7;type=2");
            put("八九点", "defaultHour=8;type=2");
            put("九十点", "defaultHour=9;type=2");
            put("十一二点", "defaultHour=11;type=2");
            
            put("子时", "defaultHour=23;ampm=2;type=2");
            put("丑时", "defaultHour=1;type=2");
            put("寅时", "defaultHour=3;type=2");
            put("卯时", "defaultHour=5;type=2");
            put("辰时", "defaultHour=7;type=2");
            put("巳时", "defaultHour=9;type=2");
            put("午时", "defaultHour=11;type=2");
            put("未时", "defaultHour=13;type=2;ampm=2");
            put("申时", "defaultHour=15;type=2;ampm=2");
            put("酉时", "defaultHour=17;type=2;ampm=2");
            put("戌时", "defaultHour=19;type=2;ampm=2");
            put("亥时", "defaultHour=21;type=2;ampm=2");
        }
    };
    
    final static String[][] repeat_type_map = {
/*      public static final int ALARM_REPEAT_TYPE_NONE = 0;
        public static final int ALARM_REPEAT_TYPE_DAY = 1;
        public static final int ALARM_REPEAT_TYPE_WEEK = 2;
        public static final int ALARM_REPEAT_TYPE_MONTH = 3;
        public static final int ALARM_REPEAT_TYPE_YEAR = 4;
        public static final int ALARM_REPEAT_TYPE_INTERVAL = 5;
        public static final int ALARM_REPEAT_TYPE_STOPWATCH = 6;*/
        /*周一为0， 周日为6*/
        {"每天",  "repeatType=1"},
        {"每月",  "repeatType=3"},
        {"每年",  "repeatType=4"},
        {"每隔",  "repeatType=5"},
        {"每周天", "repeatType=2;daysofWeek=6"},
        {"每周日", "repeatType=2;daysofWeek=6"},
    };
    private static class TimeInfo {
        public int repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
        public int year = 0;
        public int month = 0;
        public int day = 0;
        public int hour = -1;
        public int defaultHour = -1;
        public int minute = -1;
        public int defaultMinute = -1;
        public int second = 0;
        public int ampm = 0;
        public int len = 1;
        public int type = 0;
        public int addMonth = 0;
        public int addDay = 0;
        public int addWeek = 0;
        public int addHour = 0;
        public int addMinute = 0;
        public int addSecond = 0;
        public int weekday = -1;
        DaysOfWeek daysofWeek = new DaysOfWeek(0);
    };

    private TimeInfo readValues_temp(String string, int digit) {
        TimeInfo info = new TimeInfo();
        boolean ret = false;
        HashMap<String, Integer> map = paraseMeanings(string, digit);
        Integer value ;
        Calendar c = Calendar.getInstance();
        value = map.get("defaultHour");
        if (value!=null) {
            info.defaultHour = value;
        }
        value = map.get("defaultMinute");
        if (value!=null) {
            info.defaultMinute = value;
        }
        value = map.get("weekday");
        if (value!=null) {
            info.weekday = getWeekDay(value);
        }
        value = map.get("type");
        if (value!=null) {
            info.type = value;
        }
        value = map.get("year");
        if (value!=null) {
            info.year = value;
        }
        value = map.get("month");
        if (value!=null) {
            info.month = value;
        }
        value = map.get("addMonth");
        if (value!=null) {
            info.addMonth = value;
        }
        value = map.get("addDay");
        if (value!=null) {
            info.addDay = value;
        }
        value = map.get("addWeek");
        if (value!=null) {
            info.addWeek = value;
        }
        value = map.get("month");
        if (value!=null) {
            info.month = value;
        }
        value = map.get("day");
        if (value!=null) {
            info.day = value;
        }
        value = map.get("hour");
        if (value!=null) {
            info.hour = value;
            if (value==0xFF) {
                info.hour = c.get(Calendar.HOUR_OF_DAY);
            }
        }
        value = map.get("minute");
        if (value!=null) {
            info.minute = value;
            if (value==0xFF) {
                info.minute = c.get(Calendar.MINUTE);
            }
        }
        value = map.get("addMinute");
        if (value!=null) {
            info.addMinute = value;
        }
        value = map.get("second");
        if (value!=null) {
            info.second = value;
        }
        value = map.get("ampm");
        if (value!=null) {
            info.ampm = value;
        }
        value = map.get("repeatType");
        if (value!=null) {
            info.repeatType = value;
        }
        value = map.get("daysofWeek");
        if (value!=null) {
            info.daysofWeek.set(value,true);
        }
        return info;
    }

    private HashMap<String, Integer> paraseMeanings(String string, int digit) {
        String []strs = string.split(";");
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (String s: strs) {
            int mid = s.indexOf('=');
            String key = s.substring(0, mid);
            String v= s.substring(mid+1);
            int value;
            if (v.equals("D")) {
                value = digit;
            } else if(v.equals("now")) {
                value=0xFF;
            }
            else {
                value = Integer.parseInt(v);
            }
            map.put(key, value);
        }
        return map;
    }

    private boolean readValues(String string, int digit) {
        boolean ret = false;
        HashMap<String, Integer> map = paraseMeanings(string, digit);
        Integer value ;
        Calendar c = Calendar.getInstance();
        value = map.get("defaultHour");
        if (value!=null) {
            defaultHour = value;
        }
        value = map.get("defaultMinute");
        if (value!=null) {
            defaultMinute = value;
        }
        value = map.get("weekday");
        if (value!=null) {
            weekday = getWeekDay(value);
        }
        value = map.get("type");
        if (value!=null) {
            type = value;
        }
        value = map.get("year");
        if (value!=null) {
            year = value;
        }
        value = map.get("month");
        if (value!=null) {
            month = value;
        }
        value = map.get("addMonth");
        if (value!=null) {
            addMonth = value;
        }
        value = map.get("addDay");
        if (value!=null) {
            addDay = value;
        }
        value = map.get("addWeek");
        if (value!=null) {
            addWeek = value;
        }
        value = map.get("month");
        if (value!=null) {
            month = value;
        }
        value = map.get("day");
        if (value!=null) {
            day = value;
        }
        value = map.get("hour");
        if (value!=null) {
            hour = value;
            if (value==0xFF) {
                hour = c.get(Calendar.HOUR_OF_DAY);
            }
        }
        value = map.get("minute");
        if (value!=null) {
            minute = value;
            if (value==0xFF) {
                minute = c.get(Calendar.MINUTE);
            }
        }
        value = map.get("addMinute");
        if (value!=null) {
            addMinute = value;
        }
        value = map.get("second");
        if (value!=null) {
            second = value;
        }
        value = map.get("ampm");
        if (value!=null) {
            ampm = value;
        }
        value = map.get("repeatType");
        if (value!=null) {
            repeatType = value;
        }
        value = map.get("daysofWeek");
        if (value!=null) {
            daysofWeek.set(value,true);
        }
        return true;
    }
    
    private boolean readValues(String string) {
        return readValues(string , -1);
    }
    int year = 0;
    int month = 0;
    int day = 0;
    int hour = -1;
    int defaultHour = -1;
    int minute = -1;
    int defaultMinute = -1;
    int second = 0;
    int ampm = 0;
    int len = 1;
    int type = Alarm.ALARM_TYPE_RELATIVE;
    int addMonth = 0;
    int addDay = 0;
    int addWeek = 0;
    int addHour = 0;
    int addMinute = 0;
    int addSecond = 0;
    int weekday = -1;
    DaysOfWeek daysofWeek = new DaysOfWeek(0);
    int repeatType = Alarm.ALARM_REPEAT_TYPE_NONE;
    int intervalHour = -1;
    int intervalMinute = -1;
    int intervalSecond = 0;
    int startHour = -1;
    int startMinute = -1;
    int startAmPm = 0;
    
    boolean duration_flag = false;

    private static class Element {
        public Element(int type, String str) {
            this.type = type;
            if (type == DIGIT) {
                content = convertZhToNumber(str);
            } else {
                content = str;
            }
        }
        int type;
        String content;
    };

    private static int time_getCharType(char c) {
        int type;
        if (isDigit(c)) {
            type = DIGIT;
        } else if (isContainChar(time_zh_digit_keywords, c) == true) {
            type = DIGIT;
        } else {
            type = LETTER;
        }
        return type;
    }

    private static int time_getCharType(char c, char pre_c) {
        int type;
        if (c == '.' && pre_c >= '0' && pre_c <= '9') {
            type = DIGIT;
        }
        else if (c >= '0' && c <= '9') {
            type = DIGIT;
        } else if (isContainChar(time_zh_digit_keywords, c) == true) {
            type = DIGIT;
        } else {
            type = LETTER;
        }
        return type;
    }
    
    public static Alarm parseReminderResult(String content) {
        ArrayList<Element> words = split1(content);
        words = split2(words);
        AsrReminderResultParase parase = new AsrReminderResultParase();
        return parase.pickTime(content, words);
    }

    private static boolean parase_digit_unit(TimeInfo time, Element current,
            Element next) {
        boolean handled = false;
        if (handled == false && current.type == DIGIT && next.type == TIME_UNIT) {
            time.len = 2;
            handled = true;
            if (next.content.equals("月末")) {
                time.month = Integer.parseInt(current.content);
                time.day = -1;
                time.type = Alarm.ALARM_TYPE_ABSOLUTE;
            } else if (next.content.equals("刻")) {
                int num = Integer.parseInt(current.content);
                time.minute = 15 * num;
            } else if (equalKeywords(next.content, year_strs)) {
                time.year = Integer.parseInt(current.content);
            } else if (equalKeywords(next.content, month_strs)) {
                time.month = Integer.parseInt(current.content);
            } else if (equalKeywords(next.content, day_strs)) {
                time.day = Integer.parseInt(current.content);
                time.type = Alarm.ALARM_TYPE_ABSOLUTE;
            } else if (equalKeywords(next.content, hour_strs)) {
                time.hour = Integer.parseInt(current.content);
                if (next.content.equalsIgnoreCase("点")||
                        next.content.equalsIgnoreCase(":")) {
                    time.type = Alarm.ALARM_TYPE_ABSOLUTE;
                }
            } else if (equalKeywords(next.content, minute_strs)) {
                time.minute = Integer.parseInt(current.content);
            } else if (equalKeywords(next.content, second_strs)) {
                time.second = Integer.parseInt(current.content);
            } else {
                // need check pre string
                handled = false;
                System.err.println("unexpected unit");
            }
        }
        return handled;
    }

    private static int getLocalWeekDay(int n) {
        int ret = -1;
        switch (n) {
        case Calendar.MONDAY:
            ret = 0;
            break;
        case Calendar.TUESDAY:
            ret = 1;
            break;
        case Calendar.WEDNESDAY:
            ret = 2;
            break;
        case Calendar.THURSDAY:
            ret = 3;
            break;
        case Calendar.FRIDAY:
            ret = 4;
            break;
        case Calendar.SATURDAY:
            ret = 5;
            break;
        case Calendar.SUNDAY:
            ret = 6;
            break;
        }
        return ret;
    }
    
    private static int getWeekDay(int n) {
        int ret = 0;
        switch (n) {
        case 1:
            ret = Calendar.MONDAY;
            break;
        case 2:
            ret = Calendar.TUESDAY;
            break;
        case 3:
            ret = Calendar.WEDNESDAY;
            break;
        case 4:
            ret = Calendar.THURSDAY;
            break;
        case 5:
            ret = Calendar.FRIDAY;
            break;
        case 6:
            ret = Calendar.SATURDAY;
            break;
        case 7:
            ret = Calendar.SUNDAY;
            break;
        }
        return ret;
    }

    private static boolean parase_week_unit_digit(TimeInfo time,
            Element current, Element next) {
        boolean handled = false;
        if (current.type == TIME_UNIT && next.type == DIGIT) {
            if (current.content.equalsIgnoreCase("周")) {
                int n = Integer.parseInt(next.content);
                time.weekday = getWeekDay(n);
                time.type = Alarm.ALARM_TYPE_ABSOLUTE;
                handled = true;
                time.len = 2;
            }
        }
        return handled;
    }

    private Alarm pickTime(String content, ArrayList<Element> words) {
         Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int today = c.get(Calendar.DAY_OF_MONTH);
        System.out.print("第三次分词\n");

        if (content.contains("倒计时")) {
            repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
        }
        boolean handled = false;
        int first_colon = 0;
        for (int i = 0; i < words.size() - 1; i += len) {
            Element previous = null;
            Element current = null;
            Element next = null;
            handled = false;
            if (i > 0) {
                previous = words.get(i - 1);
            }
            current = words.get(i);
            next = words.get(i + 1);

            //处理：的
            if (handled==false && current.type == TIME_COLON 
                    && previous!=null && previous.type == DIGIT
                    && next!=null && next.type == DIGIT) {
                Element pre_pre = null;
                if (i > 1) {
                    pre_pre = words.get(i - 2);
                }
                if (first_colon == 0) {
                    first_colon = 1;
                    hour = Integer.parseInt(previous.content);
                    minute = Integer.parseInt(next.content);
                    len = 2;
                    type = Alarm.ALARM_TYPE_ABSOLUTE;
                }else if (pre_pre!=null && pre_pre.type == TIME_COLON){
                    //第二个":"后面跟的是秒
                    second = Integer.parseInt(next.content);
                    len = 2;
                }
            }
            
            if (handled == false && current.type == DIGIT
                    && next.type == TIME_UNIT) {
                String str = getValue_D_U(next.content,time_digit_unit_map);
                if (str!=null) {
                    if(repeatType == Alarm.ALARM_REPEAT_TYPE_INTERVAL
                            &&intervalHour==-1
                            &&intervalMinute==-1
                            &&intervalSecond==0) {
                        TimeInfo interval_info = readValues_temp(str, Integer.parseInt(current.content));
                        if (interval_info.hour!=-1) intervalHour = interval_info.hour;
                        if (interval_info.minute!=-1) intervalMinute = interval_info.minute;
                        if (interval_info.second!=-1) intervalSecond = interval_info.second;
                    } else {
                        readValues(str, Integer.parseInt(current.content));
                    }
                    handled = true;
                    len = 2;
                }
            }
            //处理 刻的情况
            if (handled == false && current.type == DIGIT
                    && next.type == TIME_UNIT) {
                if (next.content.equals("刻")) {
                    int num = Integer.parseInt(current.content);
                    minute = 15 * num;
                    handled = true;
                } 
            }
            
            if (handled == false && current.type == TIME_UNIT
                    && next.type == DIGIT) {
                TimeInfo time = new TimeInfo();
                handled = parase_week_unit_digit(time, current, next);
                if (handled) {
                    if (time.weekday != -1)
                        weekday = time.weekday;
                    if (time.type != 0)
                        type = time.type;
                    if (time.len != 1)
                        len = time.len;
                }
            }
            
            //获取时间默认值
            if (handled == false && current.type == TIME_UNIT) {
                String str = default_time_key_map.get(current.content);
                if (str!=null) {
                    readValues(str);
                    handled = true;
                    len = 1;
                }
            }
          //获取时间默认值
            if (handled == false && current.type == TIME_TIME) {
                String str = default_time_key_map.get(current.content);
                if (str!=null) {
                    if(repeatType == Alarm.ALARM_REPEAT_TYPE_INTERVAL
                            &&intervalHour==-1
                            &&intervalMinute==-1
                            &&intervalSecond==0) {
                        TimeInfo interval_info = readValues_temp(str, -1);
                        if (interval_info.hour!=-1) intervalHour = interval_info.hour;
                        if (interval_info.minute!=-1) intervalMinute = interval_info.minute;
                        if (interval_info.second!=-1) intervalSecond = interval_info.second;
                    } else {
                        readValues(str);
                    }
                    handled = true;
                    len = 1;
                }
            }

            if (handled == false && current.type == TIME_REPEAT) {
                String str = getValue(current.content, repeat_type_map);
                if (str!=null) {
                    readValues(str);
                    handled = true;
                    len = 1;
                    if(current.content.equalsIgnoreCase("每隔")) {
                        //处理固定间隔的情况
                    }
                }
            }

            if (handled == false && current.type == ASSIST) {
                handled = true;
                Element next_next = null;
                if (current.content.equalsIgnoreCase("提前")) {
                    if (i < words.size() - 1) {
                        next_next = words.get(i + 2);
                    }
                    TimeInfo time = new TimeInfo();
                    if (next_next != null
                            && parase_digit_unit(time, next, next_next)) {
                        len = 1 + time.len;
                        if (time.hour != -1) {
                            addHour = 0 - time.hour;
                        }
                        if (time.minute != -1) {
                            addMinute = 0 - time.minute;
                        }
                        if (time.second != -1) {
                            addSecond = 0 - time.second;
                        }
                    } else if (next.type == TIME_TIME) {
                        len = 2;
                        String str = default_time_key_map.get(next.content);
                        TimeInfo temp = readValues_temp(str, -1);
                        if (temp.hour!=-1) addHour = 0 - temp.hour;
                        if (temp.minute!=-1) addMinute = 0 - temp.minute;
                        if (temp.second!=-1) addSecond = 0 - temp.second;
                    }
                    else {
                        len = 1;
                        handled = false;
                    }
                } else {
                    handled = false;
                }
            }
            
            //处理从周一到周五的情况
            if (handled == false && current.type == DURATION
                    &&previous != null && previous.type == TIME_TIME
                    &&next != null && next.type == TIME_TIME) {
                // 向前找两位
                handled = true;
                int start = 0;
                int end = 0;
                String str_pre = default_time_key_map.get(previous.content);
                TimeInfo info_start = readValues_temp(str_pre, -1);
                if (info_start.weekday != -1) start = info_start.weekday;
                String str_next = default_time_key_map.get(next.content);
                TimeInfo info_end = readValues_temp(str_next, -1);
                if (info_end.weekday != -1) end = info_end.weekday;
                start = getLocalWeekDay(start);
                end = getLocalWeekDay(end);
                // 周一为0,周日为6
                if (start != -1 && end != -1) {
                    int m = start;
                    daysofWeek = new DaysOfWeek(0);
                    while (m != end && m <= 6 && end <= 6) {
                        daysofWeek.set(m, true);
                        m++;
                        if (m > 6) {
                            m = 0;
                        }
                    }
                    daysofWeek.set(end, true);
                    repeatType = Alarm.ALARM_REPEAT_TYPE_WEEK;
                }
                if (start == 0 || end == 0) {
                    handled = false;
                    len = 1;
                }
            }


            if (handled == false && current.type == DURATION
                    &&previous!=null
                    &&previous.type != LETTER 
                    &&(hour!=-1 || minute != -1)
                    &&next.type !=LETTER) {
                duration_flag = true;
                startHour = hour;
                if (ampm == 2 && hour<12) {
                    startHour+=12;
                }
                startAmPm = ampm;
                startMinute = minute;
                handled = true;
                len = 1; 
            }
            
            if (handled == false && current.type == TIME_UNIT
                    && next.type == DIGIT) {
                handled = true;
                int next_digit = Integer.parseInt(next.content);
                if (current.content.equalsIgnoreCase("每周") && next_digit < 7) {
                    daysofWeek.set(next_digit - 1, true);
                    repeatType = Alarm.ALARM_REPEAT_TYPE_WEEK;
                    len = 2;
                } else {
                    handled = false;
                }
            }

            if (handled == false && current.type == TIME_UNIT) {
                handled = true;
                if (current.content.equalsIgnoreCase("每周天")
                        || current.content.equalsIgnoreCase("每周日")) {
                    daysofWeek.set(6, true);
                    repeatType = Alarm.ALARM_REPEAT_TYPE_WEEK;
                    len = 1;
                } else if (current.content.equalsIgnoreCase("每天")) {
                    type = Alarm.ALARM_TYPE_RELATIVE;
                    repeatType = Alarm.ALARM_REPEAT_TYPE_DAY;
                    defaultHour = 8;
                } else if (current.content.equalsIgnoreCase("每月")) {
                    type = Alarm.ALARM_TYPE_RELATIVE;
                    repeatType = Alarm.ALARM_REPEAT_TYPE_MONTH;
                    defaultHour = 8;
                } else if (current.content.equalsIgnoreCase("每年")) {
                    type = Alarm.ALARM_TYPE_RELATIVE;
                    repeatType = Alarm.ALARM_REPEAT_TYPE_YEAR;
                    defaultHour = 8;
                } else {
                    handled = false;
                }
            }
            if (handled == false && current.type == TIME_UNIT) {
                handled = true;
                if (current.content.equalsIgnoreCase("后天")) {
                    day = today + 2;
                    type = Alarm.ALARM_TYPE_ABSOLUTE;
                } else if (current.content.equalsIgnoreCase("明天")) {
                    day = today + 1;
                    type = Alarm.ALARM_TYPE_ABSOLUTE;
                } else if (current.content.equalsIgnoreCase("上午")) {
                    ampm = 1;
                    type = Alarm.ALARM_TYPE_ABSOLUTE;
                    defaultHour = 8;
                } else if (current.content.equalsIgnoreCase("下午")) {
                    ampm = 2;
                    type = Alarm.ALARM_TYPE_ABSOLUTE;
                    defaultHour = 14;
                } else if (current.content.equalsIgnoreCase("晚上")) {
                    ampm = 2;
                    type = Alarm.ALARM_TYPE_ABSOLUTE;
                    defaultHour = 20;
                }

                else {
                    handled = false;
                }
                len = 1;
            }
            //8点半
            if (handled == false && previous != null
                    && current.type == TIME_UNIT && previous.type == TIME_UNIT) {
                if (current.content.equalsIgnoreCase("半")) {
                    if (equalKeywords(previous.content, hour_strs)) {
                        minute = 30;
                        handled = true;
                    }
                } else {
                    handled = false;
                }
                len = 1;
            }
            //8点15
            if (handled == false && previous != null
                    && current.type == DIGIT && previous.type == TIME_UNIT) {
                if (equalKeywords(previous.content, hour_strs)) {
                        minute = Integer.parseInt(current.content);
                        handled = true;
                } else {
                    handled = false;
                }
                len = 1;
            }
            
            if (!handled) {
                len = 1;
            }
        }
        if (DEBUG) {
            System.out
                    .format("type:%d, year:%d, month:%d, day:%d, hour:%d,deaulthour:%d" +
                    		" minutes:%d second:%d " +
                    		"addDay:%d \n",
                            repeatType, year, month, day, hour,defaultHour, 
                            minute,  second, addDay);
        }
        

        return pickTime2 ();
    }
//  TimeInfo timeInfo = new TimeInfo();
//  timeInfo.repeatType = repeatType;
//  timeInfo.type = type;
//  timeInfo.year = year;
//  timeInfo.month = month;
//  timeInfo.day = day;
//  timeInfo.hour = hour;
//  timeInfo.defaultHour = defaultHour;
//  timeInfo.minute = minute;
//  timeInfo.second = second;
//  timeInfo.ampm = ampm;
//  timeInfo.len = 1;
//  timeInfo.type = type;
//  timeInfo.addHour = addHour;
//  timeInfo.addMinute = addMinute;
//  timeInfo.weekday = weekday;
//  timeInfo.addSecond = addSecond;
//  timeInfo.daysofWeek = daysofWeek;
    
//  int repeatType = timeInfo.repeatType;
//  int year = timeInfo.year;
//  int month = timeInfo.month;
//  int day = timeInfo.day;
//  int hour = timeInfo.hour;
//  int defaultHour = timeInfo.defaultHour;
//  int minute = timeInfo.minute;
//  int second = timeInfo.second;
//  int ampm = timeInfo.ampm;
//  int len = timeInfo.len;
//  int type = timeInfo.type;
//  int addHour = timeInfo.addHour;
//  int addMinute = timeInfo.addMinute;
//  int weekday = timeInfo.weekday;
//  int addSecond = timeInfo.addSecond;
//  DaysOfWeek daysofWeek = timeInfo.daysofWeek;
    static void assignDate(Alarm alarm) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.YEAR, alarm.year);
        c.set(Calendar.MONTH, alarm.month-1);
        int maxday = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (alarm.day > maxday) {
            c.set(Calendar.YEAR, alarm.year);
            c.set(Calendar.MONTH, alarm.month-1);
            c.set(Calendar.DAY_OF_MONTH, alarm.day);
            alarm.year = c.get(Calendar.YEAR);
            alarm.month = c.get(Calendar.MONTH) + 1;
            alarm.day = c.get(Calendar.DAY_OF_MONTH);
        }
    }
    private  Alarm pickTime2 () {
        Alarm alarm = new Alarm();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int nowMonth = c.get(Calendar.MONTH) + 1;
        int nowMonthDay = c.get(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        int lastDayOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        if (hour != -1) hour += addHour;
        if (minute != -1) minute += addMinute;
        
        second += addSecond;
        if (ampm == 2 && hour != -1 && hour<=12) {
            hour += 12;
            if (type == 0)
                type = Alarm.ALARM_TYPE_ABSOLUTE;
        }
        if (hour == -1 && defaultHour != -1) {
            hour = defaultHour;
        }
        if (minute == -1 && defaultMinute != -1) {
            minute = defaultMinute;
        }
        
        //分钟没有值，那么就从小时去借时间
        if (minute == -1 && addMinute !=0) {
            if(addMinute>0) {
                minute = addMinute;
            } else {
                hour -= 1;
                minute = 60 + addMinute;
            }
        }
        
        if (repeatType != Alarm.ALARM_REPEAT_TYPE_NONE) {
            type = Alarm.ALARM_TYPE_RELATIVE;
            alarm.daysOfWeek = daysofWeek;
        }

        alarm.type = type;
        alarm.repeatType = repeatType;

        //如果是固定间隔
        if (repeatType == Alarm.ALARM_REPEAT_TYPE_INTERVAL) {
            if (intervalHour==-1) intervalHour = 0;
            if (intervalMinute==-1) intervalMinute = 0;
            alarm.interval = (intervalHour*60*60 + intervalMinute * 60 + intervalSecond)*1000;
            if (alarm.interval == 0) {
                alarm.type = Alarm.ALARM_TYPE_FAILED;
            }
            if(duration_flag && alarm.interval!=0) {
                int endHour=0, endMinute=0;
                if(hour!=-1)endHour=hour;
                if(minute!=-1)endMinute=0;
                hour = startHour;
                minute = startMinute;
                ampm = startAmPm;
                if (startHour==-1) startHour = 0;
                if (startMinute==-1) startMinute = 0;
                long end = (endHour*60*60 + endMinute * 60)*1000;
                long start = (startHour*60*60 + startMinute * 60)*1000;
                long times = (end - start)/alarm.interval;
                if (times > 0) {
                    alarm.repeatTimes = (int) times;
                }
            }
            else {
                alarm.repeatTimes = -1;
            }
        }
        // 处理周的情况，需要根据周计算日期，这种情况下 年，月，日应该都是为0
        if (type == Alarm.ALARM_TYPE_ABSOLUTE && weekday != -1) {
            int curDay = c.get(Calendar.DAY_OF_YEAR);
            c.set(Calendar.DAY_OF_WEEK, weekday);
            int d = c.get(Calendar.DAY_OF_YEAR);
            if (d < curDay){
                c.add(Calendar.WEEK_OF_YEAR, 1);
            }
            alarm.year = c.get(Calendar.YEAR);
            alarm.month = c.get(Calendar.MONTH) + 1;
            alarm.day = c.get(Calendar.DAY_OF_MONTH);
        }
        if (type == Alarm.ALARM_TYPE_ABSOLUTE) {
            if (year != 0)
                alarm.year = year;
            if (month != 0)
                alarm.month = month;
            if (addMonth != 0) {
                alarm.month += addMonth;
                if (alarm.month > 12) {
                    alarm.year+=1;
                    alarm.month = 1;
                }
            }
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.MONTH, alarm.month-1);
            int maxday = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (day == -1) { //月末
                c.setTimeInMillis(System.currentTimeMillis());
                c.set(Calendar.MONTH, alarm.month-1);
                alarm.day = maxday;
            } else if (day != 0) {
                alarm.day = day;
            } else {

            }
            alarm.day += addDay;
            if (alarm.day > maxday) {
                assignDate(alarm);
            }
            if (hour == -1 && addHour == 0) {
                // 对于绝对时间，必须有时间值，默认为上午8：00
                hour = DEFAULTHOUR;
            }
            if (hour != -1) {
                alarm.hour = hour;
                alarm.minutes = 0;
                alarm.second = 0;
            }
            if (minute != 0)
                alarm.minutes = minute;
            if (second != 0)
                alarm.second = second;
            if (ampm != 0)
                alarm.ampm = ampm;
            
            //本月末的情况,如果时间已经过去，那么顺延到下月末
            if (day == -1 && alarm.month == nowMonth) {
                if (isTimePassed(alarm)) {
                    addOneMonth(alarm);
                }
            } else
            //周的情况，前面已经处理了，这里不用再次处理
            if (nowMonthDay == alarm.day && weekday == -1) {
                // 如果day == 0，默认为当前 
                //如果是2点12分提醒我，但是时间已经过去了。那么天数需要加1
                if (isTimePassed(alarm)) {
                    addOneDay(alarm);
                }
            } else if (weekday != -1) {
                //处理周的情况
                c.setTimeInMillis(System.currentTimeMillis());
                int curDay = c.get(Calendar.DAY_OF_YEAR);
                c.set(Calendar.DAY_OF_WEEK, weekday);
                int d = c.get(Calendar.DAY_OF_YEAR);
                if (d < curDay ||
                        (d == curDay&&isTimePassed(alarm))){
                    c.add(Calendar.WEEK_OF_YEAR, 1);
                }
                alarm.year = c.get(Calendar.YEAR);
                alarm.month = c.get(Calendar.MONTH) + 1;
                alarm.day = c.get(Calendar.DAY_OF_MONTH);
            }
        } else if (repeatType == Alarm.ALARM_REPEAT_TYPE_WEEK
                || repeatType == Alarm.ALARM_REPEAT_TYPE_DAY
                || repeatType == Alarm.ALARM_REPEAT_TYPE_MONTH
                || repeatType == Alarm.ALARM_REPEAT_TYPE_YEAR) {
            alarm.year = year;
            alarm.month = month;
            alarm.day = day;
            if (hour == -1) {
                hour = DEFAULTHOUR;
            }
            alarm.hour = hour;
            alarm.minutes = minute;
            alarm.second = second;
            alarm.ampm = ampm;
        } else {
            alarm.year = year;
            alarm.month = month;
            alarm.day = day;
            if (day != 0) {
                if (hour == -1 && defaultHour != -1) {
                    hour = defaultHour;
                    alarm.minutes = 0;
                    alarm.second = 0;
                }
            }
            alarm.hour = hour;
            alarm.minutes = minute;
            alarm.second = second;
            alarm.ampm = ampm;
        }

        if (alarm.repeatType == Alarm.ALARM_REPEAT_TYPE_INTERVAL 
                && alarm.interval != 0) {
            c.setTimeInMillis(System.currentTimeMillis());
            alarm.year = c.get(Calendar.YEAR);
            alarm.month = c.get(Calendar.MONTH) + 1;
            alarm.day = c.get(Calendar.DAY_OF_MONTH);
            alarm.hour = c.get(Calendar.HOUR_OF_DAY);
            alarm.minutes = c.get(Calendar.MINUTE);
            if (year != 0)
                alarm.year = year;
            if (month != 0)
                alarm.month = month;
            if (addMonth != 0) {
                alarm.month += addMonth;
            }
            if (day == -1) { //月末
                c.setTimeInMillis(System.currentTimeMillis());
                c.set(Calendar.MONTH, alarm.month-1);
                alarm.day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            } else if (day != 0) {
                alarm.day = day;
            } else {

            }
            alarm.day += addDay;
            if (hour != -1) {
                alarm.hour = hour;
            }
            if (minute != -1) alarm.minutes = minute;
            if (second !=0) alarm.second = 0;
        }
        else if (alarm.hour == -1 && alarm.minutes == -1 && alarm.second == 0) {
            alarm.type = Alarm.ALARM_TYPE_FAILED;
        }
        if (alarm.hour == -1) alarm.hour = 0;
        if (alarm.minutes == -1) alarm.minutes = 0;
        return alarm;
    }
    private static ArrayList<Element> split1(String content) {
        StringBuffer sb = new StringBuffer();
        ArrayList<Element> words = new ArrayList<Element>();
        String str = content;
        char c = str.charAt(0);
        int len = 1;
        int str_len = str.length();
        Integer type;
        boolean handled = false;
        for (int j = 0; j < str_len; j += len) {
            c = str.charAt(j);
            String one = str.substring(j, j + 1);
            String two = null;
            String three = null;
            String four = null;
            if (j < str_len - 1) {
                two = str.substring(j, j + 2);
            }
            if (j < str_len - 2) {
                three = str.substring(j, j + 3);
            }
            if (j < str_len - 3) {
                four = str.substring(j, j + 4);
            }
            
            handled = false;
            if (!handled && four!=null) {
                type = time_key_type_map_4.get(four);
                if (type != null) {
                    sb = addElement(sb, words);
                    len = 4;
                    Element add = new Element(type, four);
                    words.add(add);
                    handled = true;
                }
            } 
            
            if (!handled && three!=null) {
                type = time_key_type_map_3.get(three);
                if (type != null) {
                sb = addElement(sb, words);
                len = 3;
                Element add = new Element(type, three);
                words.add(add);
                handled = true;
                }
            } 
            if (!handled && two!=null) {
                type = time_key_type_map_2.get(two);
                if (type != null) {
                    sb = addElement(sb, words);
                    len = 2;
                    Element add = new Element(type, two);
                    words.add(add);
                    handled = true;
                }
            } 
            if (!handled && one!=null) {
                type = time_key_type_map_1.get(one);
                if (type != null) {
                    sb = addElement(sb, words);
                    len = 1;
                    Element add = new Element(type, one);
                    words.add(add);
                    handled = true;
                }
            } 
            if (!handled ) {
                sb.append(c);
                len = 1;
            }
        }
        sb = addElement(sb, words);
        System.out.print("第一次分词\n");
        for (Element e1 : words) {
            System.out.format("%d:%s \t", e1.type,e1.content );
        }
        System.out.print("\n");
        return words;
    }

    private static StringBuffer addElement(StringBuffer sb,
            ArrayList<Element> words) {
        if (sb.length() != 0) {
            String s = sb.toString();
            Element add = new Element(LETTER, s);
            words.add(add);
            sb = new StringBuffer();
        }
        return sb;
    }

    private static ArrayList<Element> split2(ArrayList<Element> list) {
        ArrayList<Element> words = new ArrayList<Element>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            Element current = list.get(i);
            Element previous = null;
            Element next = null;
            if (i > 0) {
                previous = list.get(i - 1);
            }
            if (i < list.size()-1) {
                next = list.get(i + 1);
            }
            
            if (current.type == LETTER) {
                String str = current.content;
                char start = str.charAt(0);
                int type = time_getCharType(start);
                int current_type;
                for (int j = 0; j < str.length(); j++) {
                    char a = str.charAt(j);
                    char pre_a = 0;
                    if (j>=1) {
                        pre_a = str.charAt(j-1);
                    }
                    current_type = time_getCharType(a, pre_a);
                    if (current_type != type) {
                        // 类型变了，先存放当前的字段
                        if (sb != null) {
                            String s = sb.toString();
                            Element e = new Element(type, s);
                            words.add(e);
                            sb = new StringBuffer();
                        }
                        type = time_getCharType(a);
                    }
                    sb.append(a);
                }
                if (sb.length() != 0) {
                    String s = sb.toString();
                    if (type == DIGIT && 
                            previous != null && previous.content.equals("月") &&
                            next != null && next.content.equals("点")) {
                        //如果是数字，并且前后的内容是月，和点
                        if (s.length()==3 || s.length()==4){
                            String s1 = s.substring(0,2);
                            Element e1 = new Element(DIGIT, s1);
                            words.add(e1);
                            
                            Element e2 = new Element(TIME_UNIT, "号");
                            words.add(e2);
                            
                            String s3 = s.substring(2);
                            Element e3 = new Element(DIGIT, s3);
                            words.add(e3);
                        }
                    } else {
                        Element e = new Element(type, s);
                        words.add(e);
                    }
                    sb = new StringBuffer();
                } 
            } else {
                words.add(current);
            }
        }

        System.out.print("第二次分词\n");
        for (Element e1 : words) {
            System.out.format("%d:%s \t", e1.type,e1.content );
        }
        System.out.print("\n");
        return words;
    }
    
    private static ArrayList<Element> split3(ArrayList<Element> list) {
        ArrayList<Element> words = new ArrayList<Element>();
        for (int i = 0; i < list.size(); i++) {
            Element current = list.get(i);
            Element previous = null;
            Element next = null;
            if (i > 0) {
                previous = list.get(i - 1);
            }
            if (i < list.size()-1) {
                next = list.get(i + 1);
            }
            
            if (current.type == DIGIT && 
                    previous != null && previous.content.equals("月") &&
                    next != null && next.content.equals("点")) {
                String str = current.content;
                if (str.length()==3 || str.length()==4){
                    String s1 = str.substring(0,2);
                    Element e1 = new Element(DIGIT, s1);
                    words.add(e1);
                    
                    Element e2 = new Element(TIME_UNIT, "号");
                    words.add(e2);
                    
                    String s3 = str.substring(2);
                    Element e3 = new Element(DIGIT, s3);
                    words.add(e3);
                }
                else if(str.length()==2){
                    String s1 = str.substring(0,1);
                    Element e1 = new Element(DIGIT, s1);
                    words.add(e1);
                    
                    Element e2 = new Element(TIME_UNIT, "号");
                    words.add(e2);
                    
                    String s3 = str.substring(1);
                    Element e3 = new Element(DIGIT, s3);
                    words.add(e3);
                }

            } else {
                words.add(current);
            }
        }

        System.out.print("第三次分词\n");
        for (Element e1 : words) {
            System.out.format("%d:%s \t", e1.type,e1.content );
        }
        System.out.print("\n");
        return words;
    }
    
    private static String split_pre(String content) {
        StringBuffer sb = new StringBuffer();
        StringBuffer result = new StringBuffer();
        char start = content.charAt(0);
        int type = time_getCharType(start);
        for (int i = 0; i < content.length(); i++) {
            char a = content.charAt(i);
            if (time_getCharType(a) != type) {
                // 类型变了，先存放当前的字段
                if (sb != null) {
                    String str = sb.toString();
                    if (type == DIGIT) {
                        String digit_str = convertZhToNumber(str);
                        result.append(digit_str);
                    } else {
                        result.append(str);
                    }
                    sb = new StringBuffer();
                }
                type = time_getCharType(a);
            } else {
                type = time_getCharType(a);
            }
            sb.append(a);
        }
        if (sb.length() != 0) {
            String str = sb.toString();
            if (type == DIGIT) {
                String digit_str = convertZhToNumber(str);
                result.append(digit_str);
            } else {
                result.append(str);
            }
            sb = null;
        }
        System.out.println("预分词：" + result.toString());
        return result.toString();
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
        return Integer.toString(ret);
    }

    private static boolean startByKeywords(String content, String[] keywords) {
        for (String key : keywords)
            if (content.startsWith(key)) {
                return true;
            }
        return false;
    }

    private static boolean hasKeywords(String content, String[] keywords) {
        for (String key : keywords)
            if (content.contains(key)) {
                return true;
            }
        return false;
    }

    private static boolean equalKeywords(String content, String[] keywords) {
        for (String key : keywords)
            if (content.equals(key)) {
                return true;
            }
        return false;
    }
    private static String getValue(String key, String[][] keyMap) {
        int len = keyMap.length;
        for (int i = 0; i < len; i++) {
            if (key.equals(keyMap[i][0]))
                return keyMap[i][1];
        }
        return null;
    }
    public static boolean equalsIgnoreDigit(String str1 ,int offset1, String str2, int offset2) {
        if (str1 == str2) {
            return true;
        }
        if (str2 == null || str1 == null) {
            return false;
        }
        int o1 = offset1, o2 = offset2;
        int end1 = str1.length();
        int end2 = str2.length();
        if (end1-o1 != end2-o2) {
            return false;
        }
        char[] value = str1.toCharArray();
        char[] target = str2.toCharArray();
        while (o1 < end1 && o2 < end2) {
            char c1 = value[o1++];
            char c2 = target[o2++];
            if (c1 != c2 ) {
                return false;
            }
        }
        return true;
    }    
    private static String getValue_D_U(String key, String[][] keyMap) {
        int len = keyMap.length;
        for (int i = 0; i < len; i++) {
            String str = keyMap[i][0];
            if (equalsIgnoreDigit(key,0, str,1))
                return keyMap[i][1];
        }
        return null;
    }
    private static boolean isContainChar(char array[], char c) {
        for (char a : array) {
            if (c == a)
                return true;
        }
        return false;
    }

    private static boolean isDigit(char c) {
        if (c >= '0' && c <= '9' || c == '.') {
            return true;
        }
        return false;
    }
    static void addOneDay(Alarm alarm) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_YEAR, 1);
        alarm.year = c.get(Calendar.YEAR);
        alarm.month = c.get(Calendar.MONTH) + 1;
        alarm.day = c.get(Calendar.DAY_OF_MONTH);
    }
    
    public static void addOneWeek(Alarm alarm){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, alarm.year);
            c.set(Calendar.MONTH, alarm.month - 1);
            c.set(Calendar.DAY_OF_MONTH, alarm.day);
            c.add(Calendar.WEEK_OF_YEAR, 1);
            alarm.year = c.get(Calendar.YEAR);
            alarm.month = c.get(Calendar.MONTH) + 1;
            alarm.day = c.get(Calendar.DAY_OF_MONTH);
    }
    
    static void addOneMonth(Alarm alarm) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, alarm.year);
        c.set(Calendar.MONTH, alarm.month);
        c.set(Calendar.DAY_OF_MONTH, alarm.day);
        alarm.year = c.get(Calendar.YEAR);
        alarm.month = c.get(Calendar.MONTH) + 1;
        alarm.day = c.get(Calendar.DAY_OF_MONTH);
    }
    static boolean isTimePassed(Alarm alarm){
        boolean ret = false;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        if (alarm.hour < nowHour  ||
                alarm.hour == nowHour && alarm.minutes <= nowMinute ||
                        alarm.hour == nowHour && alarm.minutes == nowMinute && alarm.second<=nowSecond) {
            ret = true;
        }
        return ret;
    }
}
