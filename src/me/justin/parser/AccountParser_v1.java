package me.justin.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 识别结果判断
 * 
 * 
 */
public class AccountParser_v1 {

    private final static String TAG = "AccountParser";
    private final static boolean DEBUG = false;
    public static String typeString = "";
    public final static int TYPE_REMIND = 1;
    public final static int TYPE_INCOME = 2;
    public final static int TYPE_EXPAND = 3;
    public final static int TYPE_UNKNOWN = 4;

    /**
     * 此处关键字可以配置
     */
    final static String reminder_keywords[] = { "提醒", "闹铃", "倒计时", "喊", "叫" };

    public final static String expand_key = "支出一笔";
    public final static String income_key = "收入一笔";
    final static String expand_keywords[] = { "支出", "支付", "开销一笔", "借出一笔",
            "花销一笔", "开支一笔", "借款一笔", "付出一笔", "消费", "花了", "买了", "花销", "扣款",
            "支出一笔", "花费了", "花费", "开销了", "开销", "开销一笔", "亏了", "亏损", "用了", "吃了",
            "充了", "订购", "损失", "掉了", "分摊", "随礼", "礼金", "交了", "缴纳", "缴了", "汇出",
            "汇了", "借出", "还了", "得到" };
    final static String expand_pattern_keywords[] = {
        ".*交.{0,3}费.*",
        "花[一两二三四五六七八九十零\\d]"};
    final static String income_keywords[] = { "增收一笔", "进账一笔", "收入", "得到一笔",
            "收入一笔", "存入", "收到一笔", "赢利了", "赢利", "赢利一笔", "赢了", "盈利了", "盈利",
            "盈利一笔", "赚了", "入账", "进账", "查收", "中奖", "中彩", "分红", "领工资", "挣了",
            "借了", "收到", "给", "给了", "拾到", "捡了","工资" };
    // 出现以下关键字，视为负收入
    final static String income_negative_keywords[] = { "归还", "还款", "还贷" };

    final static String pre_expand_amount_keywords[] = { "支出", "花了", "付了",
            "交了", "用了", "用掉", "花销", "开了", "狂宰我", "吃了我", "宰了" };
    final static String pre_income_amount_eywords[] = { "得到", "收到", "收入", "交给我" };
    final static String pos_avoid_amount_keywords[] = { "笔", "吨", "人", "次",
            "回", "支" };
    final static String pos_amount_keywords[] = { "钱" };
    final static char valid_keywords[] = { '元', '毛', '角', '分' };
    /*******************************************/

    final static String zh_numbers[] = { "零", "一", "二", "三", "四", "五", "六",
            "七", "八", "九", "十", "百", "千", "万", "亿", "毛", "分" };
    final static float digit_numbers[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            100, 1000, 10000, 100000000, (float) 0.1, (float) 0.01 };
    final static char zh_digit_keywords[] = { '零', '一', '二', '三', '四', '五',
            '六', '七', '八', '九', '两' };

    final static char zh_digit_start_keywords[] = { '十', '一', '二', '三', '四',
            '五', '六', '七', '八', '九', '两' };

    final static char punctations[] = { ',', '.', ':', '，', '：' };

    /**
     * 
     */
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
            put('块', 1f);
            put('元', 1f);
            put('毛', 0.1f);
            put('角', 0.1f);
            put('分', 0.01f);
        }
    };

    public static boolean isPunctation(char c) {
        for (char a : punctations) {
            if (c == a) {
                return true;
            }
        }
        return false;
    }
    final static String[] REGS_STRINGS_EXCULDE={
        "[一两二三四五六七八九十零\\d]+[笔把个位人次批伙场双]" 
        + "|" + "[一两二三四五六七八九十零\\d]+月[一两二三四五六七八九十零\\d]+日"
        + "|" + "[一两二三四五六七八九十零\\d]+年"
    };
    
    final static String REGS_EXPAND[] = {
        ".*交.{0,3}费.*",
        "花[\\d.]",
        "花了[\\d.]"
        };
    /*
     * 
     * */
    final static Map<String, Integer> KEY_WORD_2 = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 2L;
        {
            put("花了", EXPAND_WORD);
            put("开销", EXPAND_WORD);
            put("还了", EXPAND_WORD);
            put("支出", EXPAND_WORD);
            put("礼金", EXPAND_WORD);
            put("分摊", EXPAND_WORD);
            put("吃了", EXPAND_WORD);
            put("工资", INCOME_WORD);
        }
    };
    final static Map<String, Integer> KEY_WORD_3 = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 3L;
        {
            put("交给我", INCOME_WORD);
            put("领工资", INCOME_WORD);
            put("开销了", EXPAND_WORD);
            put("狂宰我", EXPAND_WORD);
            put("三脚架", EXCULDE_WORD);
            put("四道口", EXCULDE_WORD);
        }
    };
    final static Map<String, Integer> KEY_WORD_4 = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 4L;
        {
            put("得到一笔", INCOME_WORD);
            put("增收一笔", INCOME_WORD);
            put("进账一笔", INCOME_WORD);
            put("收入一笔", INCOME_WORD);
            
            put("支出一笔", EXPAND_WORD);
            put("开销一笔", EXPAND_WORD);
        }
    };
    
    
    /**
     * 解析数字，得出金额
     * 
     * @param content
     * @return
     */

    final static int LETTER = 0;
    final static int DIGIT = 1;
    final static int ZH_DIGIT = 2;
    final static int MIXED_DIGIT = 3;
    final static int EXCULDE_WORD = 4;
    final static int CONTENT = 5;  //内容，还需要进一步分词
    final static int INCOME_WORD = 6;  //
    final static int EXPAND_WORD = 7;  //

    final static String [] TYPE_DESCRIPTION  = {
            "字符",
            "数字",
            "中文数字",
            "混合数字",
            "排除的字符",
            "内容",
            "收入关键字",
            "支出关键字"
    };
    /**
     * Interface:解析类型和内容
     * 
     * @param content
     * @return
     */
    static class AmountPos{
        AmountPos(int pos, double a){
            postion = pos;
            amount = a;
        }
        int postion;
        double amount;
    }
    
    public static AccountParserResult parse(String content) {
        AccountParserResult result = new AccountParserResult();
        int type = TYPE_UNKNOWN;
        result.setType(type);
        String str1 = removeExcludeWords(content);
        
        ArrayList<Element>  words1 = split_by_key_word(str1);
        
        ArrayList<Element>  words2 = new ArrayList<>();
        for (Element word: words1) {
            if (word.type == CONTENT) {
                ArrayList<Element>  words3 = split_by_digit(word.content);
                words2.addAll(words3);
            } else {
                words2.add(word);
            }
        }
        
        if (DEBUG) {
            System.out.println("查找金额");
        }
        int count = 0;
        Double amount = 0.00;
        ArrayList<AmountPos> amounts = new ArrayList<>();
        for (Element element : words2) {
            if (DEBUG) System.out.format("%s:%s \t\n", TYPE_DESCRIPTION[element.type],element.content );
            if (element.type == DIGIT) {
                String amountStr = convert2Amount(element.content);
                if (amountStr!=null && !amountStr.equals("")){
                    element.content = amountStr;
                    amount = Double.parseDouble(amountStr);
                    amounts.add(new AmountPos(count, amount));
                }
            } else if (element.type == INCOME_WORD) {
                type = TYPE_INCOME;
            } else if (element.type == EXPAND_WORD) {
                type = TYPE_EXPAND;
            }
            count++;
        }
        if (DEBUG) {
            System.out.println("最终结果");
            for (Element e : words2) {
                System.out.format("%d:%s:%s \t\n", e.type,
                        TYPE_DESCRIPTION[e.type], e.content);
            }
        }
        if (amounts.size() == 0){
            //没有找到任何金额
            result.setType(TYPE_UNKNOWN);
            result.setAmount(0.00);
            return result;
        }
        
        if (amounts.size() == 1){
            result.setType(type);
            result.setAmount(amounts.get(0).amount);
        } else if (amounts.size() > 1) {
            int size = amounts.size();
            for (int i = 0; i < size; i++) {
                AmountPos ap = amounts.get(i);
                int pos = ap.postion;
                if (pos>0) {
                    Element preElement = words2.get(pos-1);
                    if (preElement.type == EXPAND_WORD || preElement.type == INCOME_WORD) {
                        amount = ap.amount;
                        break;
                    }
                }
            }
        }
        if (type == TYPE_UNKNOWN) {
            StringBuilder sBuilder = new StringBuilder();
            for (Element e : words2) {
                sBuilder.append(e.content);
            }
            System.out.println("结果：" + sBuilder.toString());
            for (String regex : expand_pattern_keywords) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(sBuilder.toString());
                if (matcher.find()) {
                    type = TYPE_EXPAND;
                }
            }
        }
        //if can't find the type, then set a default type
        if (type == TYPE_UNKNOWN) {
            type = TYPE_EXPAND;
        }

        result.setType(type);
        result.setAmount(amount);
        return result;
    }
    public static TwoValue<Integer, String> paraseContent(String content) {
        int type = paraseContentType(content);
        String result = null;
        if (type == TYPE_INCOME || type == TYPE_EXPAND) {
            result = parseAccountResult(content);
        }
        if (result == null) {
            type = TYPE_UNKNOWN;
            result = "0";
        }
        TwoValue<Integer, String> ret = new TwoValue<Integer, String>(type,
                result);
        return ret;
    }

    public static int paraseContentType(String content) {
        int typeId = 0;

        if (typeId == 0)
            for (String string : reminder_keywords) {
                if (content.contains(string)) {
                    typeId = TYPE_REMIND;
                    break;
                }
            }

        // 优先关键字判断
        if (content.startsWith(expand_key)) {
            typeId = TYPE_EXPAND;
        } else if (content.startsWith(income_key)) {
            typeId = TYPE_INCOME;
        }

        if (typeId == 0)
            for (String string : income_keywords) {
                if (content.contains(string)) {
                    typeId = TYPE_INCOME;
                    break;
                }
            }
        if (typeId == 0)
            for (String string : expand_keywords) {
                if (content.contains(string)) {
                    typeId = TYPE_EXPAND;
                    break;
                }
            }
        if (typeId == 0)
            for (String string : expand_pattern_keywords) {
                if (content.matches(string)) {
                    typeId = TYPE_EXPAND;
                }
            }

        if (typeId == 0) {
            typeId = TYPE_UNKNOWN;
        }
        return typeId;
    }

    /**
     * 判断是否包含数字
     * 
     * @param content
     * @return
     */
    private static boolean hasDigit(String content) {

        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");

        Matcher m = p.matcher(content);

        if (m.matches()) {
            flag = true;
        } else {
            String number[] = { "一", "二", "三", "四", "五", "六", "七", "八", "九",
                    "十", "半", "两" };
            boolean hasFlag = false;
            for (String string : number) {
                if (content.contains(string)) {
                    hasFlag = true;
                    break;
                }
            }
            flag = hasFlag;
        }
        return flag;

    }

    private static boolean isDigit(char c) {
        if (c >= '0' && c <= '9' || c == '.') {
            return true;
        }
        return false;
    }


    
    public static int getCharType(char c) {
        int type;
        if (isDigit(c)) {
            type = DIGIT;
        } else if (zh_map_char.get(c) != null) {
            type = DIGIT;
        } else {
            type = LETTER;
        }
        return type;
    }

    public static class Element {
        public Element(int type, String str) {
            this.type = type;
            content = str;
        }

        int type;
        String content;
    };

    private static boolean isValid(char c) {
        for (char a : valid_keywords) {
            if (c == a)
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

    private static boolean isContainChar(char array[], char c) {
        for (char a : array) {
            if (c == a)
                return true;
        }
        return false;
    }

    // 判断一个字符串是否都为数字
    public static boolean isDigit(String strNum) {
        return strNum.matches("[0-9,.]{1,}");
    }

    public static boolean hasAbraicDigit(String content) {

        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");

        Matcher m = p.matcher(content);

        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 
     * @param zh_number
     * @return
     */
    private static TwoValue<String, Boolean> convertZhToNumber(String zh_number) {
        double result = 0;
        double pre_unit = 0f;
        double unit = 0;
        double num = 0;
        double billon = 0;
        char a = '0';
        boolean valid = false;
        boolean hasZero = false;
        StringBuffer digit = new StringBuffer();
        for (int i = 0; i < zh_number.length(); i++) {
            a = zh_number.charAt(i);
            if (isValid(a)) {
                // 这是一个有效的钱数
                valid = true;
            }
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
        // return Float.toString(result);
        TwoValue<String, Boolean> two = new TwoValue<String, Boolean>(
                String.format("%.2f", result), valid);
        return two;
    }
    //删除干扰字
    private static String removeExcludeWords(String content) {
        String ret;
        if (DEBUG) {
            System.out.println("ExcludeWords-1:" + content + " :");
        }
        int i = 0;
        ret = content.replaceAll(REGS_STRINGS_EXCULDE[0], "|");
        
        if (DEBUG) {
            System.out.println("ExcludeWords-2:" + ret + " :");
        }
        return ret;
    }
    private static StringBuffer addElement(StringBuffer sb,
            ArrayList<Element> words) {
        if (sb.length() != 0) {
            String s = sb.toString();
            Element add = new Element(CONTENT, s);
            words.add(add);
            sb = new StringBuffer();
        }
        return sb;
    }
    
    private static ArrayList<Element> split_by_key_word(String content) {
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
                type = KEY_WORD_4.get(four);
                if (type != null) {
                    sb = addElement(sb, words);
                    len = 4;
                    Element add = new Element(type, four);
                    words.add(add);
                    handled = true;
                }
            } 
            
            if (!handled && three!=null) {
                type = KEY_WORD_3.get(three);
                if (type != null) {
                sb = addElement(sb, words);
                len = 3;
                Element add = new Element(type, three);
                words.add(add);
                handled = true;
                }
            } 
            if (!handled && two!=null) {
                type = KEY_WORD_2.get(two);
                if (type != null) {
                    sb = addElement(sb, words);
                    len = 2;
                    Element add = new Element(type, two);
                    words.add(add);
                    handled = true;
                }
            } 
/*            if (!handled && one!=null) {
                type = time_key_type_map_1.get(one);
                if (type != null) {
                    sb = addElement(sb, words);
                    len = 1;
                    Element add = new Element(type, one);
                    words.add(add);
                    handled = true;
                }
            } */
            if (!handled ) {
                sb.append(c);
                len = 1;
            }
        }
        sb = addElement(sb, words);
        
        if (DEBUG) {
            System.out.print("根据关键字\n");
            for (Element e1 : words) {
                System.out.format("%d:%s:%s \t", e1.type,
                        TYPE_DESCRIPTION[e1.type], e1.content);
            }
            System.out.print("\n");
        }
        return words;
    }
    
    /**
     * 根据规则分词
     * @param content
     * @param regx
     * @param words
     * @return
     */
    private static String[] splitWord(String content, String regx, ArrayList<Element> words) {
        Matcher matcher;
        String[] strs;
        Pattern pattern = Pattern.compile(regx);
        matcher = pattern.matcher(content);
        if(matcher.find()) { 
            strs = new String[3];
            strs[0] = content.substring(0,matcher.start());
            strs[1] = content.substring(matcher.start(),matcher.end());
            strs[2] = content.substring(matcher.end());
         } else {
            strs = new String[1];
            strs[0] = content;
         }
        return strs;
    }
    
    private static ArrayList<Element> split_exclude(String content) {
        ArrayList<Element> words = new ArrayList<Element>();
        Pattern pattern = null;
        Matcher matcher = null;
        System.out.print("" + content + " :");
        int i = 0;
        for (String reg:REGS_STRINGS_EXCULDE) {
            pattern = Pattern.compile(reg);
            searchExculdeWord(content, pattern, words);
        }
        if (DEBUG) {
            System.out.print("\n");
            for (Element e : words) {
                System.out.println(i++ + "查找干扰字:cotent:" + e.content + " 类型："
                        + TYPE_DESCRIPTION[e.type]);
            }
            System.out.print("\n");
        }
        return words;
    }
    private static int searchExculdeWord(String content, Pattern pattern, ArrayList<Element> words) {
        Matcher matcher;
        matcher = pattern.matcher(content);
        if(matcher.find()) { 
            String b = content.substring(0,matcher.start());
            String m = content.substring(matcher.start(),matcher.end());
            String e = content.substring(matcher.end());
            System.out.print(" exclude:" +b + "|"+m + "|" +e); 
            searchExculdeWord(b,pattern, words);
            words.add(new Element(EXCULDE_WORD, m));
            searchExculdeWord(e,pattern, words);
         } else {
            words.add(new Element(CONTENT, content));
         }
        return 0;
    }
    
    static ArrayList<Element> split_by_digit(String content){
        ArrayList<Element> words = new ArrayList<Element>();
        StringBuffer sb = new StringBuffer();
        char start = content.charAt(0);
        int type = getCharType(start);
        for (int i = 0; i < content.length(); i++) {
            char a = content.charAt(i);
            if (type == LETTER && zh_map_char.get(a) != null
                    && !isContainChar(zh_digit_start_keywords, a)) {
                // 必须是遇到数字开头的，才认为类型变了，否则仍然为字符，比如毛巾，角落等。
                type = LETTER;
            } else if (getCharType(a) != type) {
                // 类型变了，先存放当前的字段
                if (sb != null) {
                    String str = sb.toString();
                    Element e = new Element(type, str);
                    words.add(e);
                    sb = new StringBuffer();
                }
                type = getCharType(a);
            } else {
                type = getCharType(a);
            }
            sb.append(a);
        }
        if (sb.length() != 0) {
            String str = sb.toString();
            Element e = new Element(type, str);
            words.add(e);
            sb = null;
        }
        if (DEBUG) {
            int i = 0;
            System.out.println("分词-数字:");
            for (Element e : words) {
                System.out.println(i++ + ":cotent:" + e.content + " 类型："
                        + e.type);
            }
        }
        return words;
    }

    static String convert2Amount(String word) {
        if (DEBUG) System.out.println("convert2Amount-1:"+word);
        // 下面开始判断数字是否为有效金额
        String number = null;
        String backup_number = null; // 备选数字，如果前面有前置的关键字，那么认为是金额
        boolean isValid;
        String str = word;
        if (isDigit(str)) {
            // 纯数字，进行格式化
            Double d = Double.parseDouble(str);
            number = String.format("%.2f", d);
        } else {
            // 含中文数字
            TwoValue<String, Boolean> two = convertZhToNumber(str);
            number = (String) two.a;
            if ((Boolean) two.b) {
                isValid = true;
            }
        }
        if (DEBUG) System.out.println("convert2Amount-2:"+number);
        return number;
    }
    
    public static String parseAccountResult(String content) {
        ArrayList<Element> words = new ArrayList<Element>();
        StringBuffer sb = new StringBuffer();
        char start = content.charAt(0);
        int type = getCharType(start);
        for (int i = 0; i < content.length(); i++) {
            char a = content.charAt(i);
            if (type == LETTER && zh_map_char.get(a) != null
                    && !isContainChar(zh_digit_start_keywords, a)) {
                // 必须是遇到数字开头的，才认为类型变了，否则仍然为字符，比如毛巾，角落等。
                type = LETTER;
            } else if (getCharType(a) != type) {
                // 类型变了，先存放当前的字段
                if (sb != null) {
                    String str = sb.toString();
                    Element e = new Element(type, str);
                    words.add(e);
                    sb = new StringBuffer();
                }
                type = getCharType(a);
            } else {
                type = getCharType(a);
            }
            sb.append(a);

        }
        if (sb.length() != 0) {
            String str = sb.toString();
            Element e = new Element(type, str);
            words.add(e);
            sb = null;
        }

        // 下面开始判断数字是否为有效金额
        String number = null;
        String backup_number = null; // 备选数字，如果前面有前置的关键字，那么认为是金额
        boolean isValid = false;
        int len = words.size();
        for (int i = 0; i < words.size(); i++) {
            Element e = words.get(i);
            String str = e.content;
            if (e.type == DIGIT && isDigit(str)) {
                // 纯数字，进行格式化
                Double d = Double.parseDouble(str);
                number = String.format("%.2f", d);
            } else if (e.type == DIGIT) {
                // 含中文数字
                TwoValue<String, Boolean> two = convertZhToNumber(str);
                number = (String) two.a;
                if ((Boolean) two.b) {
                    isValid = true;
                    break;
                }
            }
            // 判断后置字符
            if (e.type == DIGIT && i < len - 1) {
                Element pos_e = words.get(i + 1);
                if (pos_e.type == LETTER) {
                    if (startByKeywords(pos_e.content,
                            pos_avoid_amount_keywords)) {
                        // 这不是金额，放弃
                        number = null;
                    } else if (startByKeywords(pos_e.content,
                            pos_amount_keywords)) {
                        // 这是金额
                        // break;
                        backup_number = number;
                    } else {
                        backup_number = number;
                    }
                }
            }
            // 判断前置字符
            if (number != null && e.type == DIGIT && i >= 1) {
                Element pre_e = words.get(i - 1);
                if (pre_e.type == LETTER) {
                    if (endByKeywords(pre_e.content, pre_expand_amount_keywords)) {
                        // 前面包含支出关键字，那么这是一个金额,先记录下来,
                        backup_number = number;
                        break;
                    }
                    if (endByKeywords(pre_e.content, pre_income_amount_eywords)) {
                        // 前面包含收入关键字，那么这是一个金额,先记录下来
                        backup_number = number;
                        break;
                    }
                }
            }
        }
        if (!isValid && backup_number != null) {
            number = backup_number;
        }
        return number;
    }

    private static boolean endByKeywords(String content, String[] keywords) {
        for (String key : keywords)
            if (content.endsWith(key)) {
                return true;
            }
        return false;
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

}
