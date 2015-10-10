package me.justin.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sxb.parase.TwoValue;

import junit.framework.Test;

public class AccountParser {
    final static char valid_keywords[] = { '元', '毛', '角', '分' };
    final static char zh_digit_keywords[] = { '零', '一', '二', '三', '四', '五',
        '六', '七', '八', '九', '两' };
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
    
    final static String[] zh_number_reg = {
        "[12][0123456789]",
        "3[01]",
        "0?[1\\-9]",
        
        "[一二三四五六七八九十]",
        "二十[百千万亿块元毛角分]",
        "三十",
        "三十一",
        "[一二三四五六七八九十]",
        
        "[１２][０１２３４５６７８９]",
        "[３][０１]",
        "[１２３４５６７８９]",
    };
    
//    "(([一二三四五六七八九十零\\d])+[万亿]?[百千万亿块元毛角分])+[块元]?([一二三四五六七八九十零\\d]+[毛角分]*[一二三四五六七八九十零\\d]*)?"
    final static String[] REGS_STRINGS={
        "([一两二三四五六七八九十零\\d]+[百千万]?)+[亿]([一两二三四五六七八九十零\\d]+[百千]?)+[万]([一两二三四五六七八九十零\\d]+[百千]*)+[块元]?[一二三四五六七八九十零\\d]*[毛角分]*[一二三四五六七八九十零\\d]*"
          + "|"+
        "([一两二三四五六七八九十零\\d]+[百千万]?)+[亿]([一两二三四五六七八九十零\\d]+[百千]?)+[万][一两二三四五六七八九十零\\d]*[百千块元毛角分]*[一二三四五六七八九十零\\d]*"
        + "|"+
        "([一两二三四五六七八九十零\\d]+[百千]?)+[万]([一两二三四五六七八九十零\\d]+[百千]*)+[块元]?[一二三四五六七八九十零\\d]*[毛角分]*[一二三四五六七八九十零\\d]*"
        + "|"+
        "([一两二三四五六七八九十零\\d]+[百千]?)+[万][一两二三四五六七八九十零\\d]*[百千万亿块元毛角分]*[一二三四五六七八九十零\\d]*"
        + "|"+
       "([一两二三四五六七八九十零\\d]+[百千]?)+[块元][一二三四五六七八九十零\\d]*[毛角分]*[一二三四五六七八九十零\\d]*"
       + "|"+
        "([一两二三四五六七八九十零\\d]+[百千块元毛角分])+[一二三四五六七八九十零\\d]*"
        + "|"+
        "[一两二三四五六七八九十零\\d]+"
        ,
    };
    final static String[] REGS_STRINGS_TEST={
        "[一两二三四五六七八九十零\\d]+[笔把个位人次]"
    };
    private static boolean isZhDigit(char c) {
        for (char a : zh_digit_keywords) {
            if (c == a)
                return true;
        }
        return false;
    }
    // 判断一个字符串是否都为数字
    public static boolean isDigit(String strNum) {
        return strNum.matches("[0-9,.]{1,}");
    }
    private static boolean isDigit(char c) {
        if (c >= '0' && c <= '9' || c == '.') {
            return true;
        }
        return false;
    }
    private static boolean isValid(char c) {
        for (char a : valid_keywords) {
            if (c == a)
                return true;
        }
        return false;
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
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        test("今年国家收入三万8千6百五十亿两千六百万八千四", "1000000.00");
        test("今年国家收入3800亿2600万8400", "1000000.00");
        test("买房一千两百万四千四", "1000000.00");
        test("买房1200万4400", "1000000.00");
        test("吃饭花了八千四百五十块8毛", "1000000.00");
        test("一百万八千四", "1000000.00");
        test("收入1万九千零八亿九千八百七十六万五千四百元一毛二", "1900898765400.12");
        test("昨天买了四条毛巾花了三十四块七毛五", "34.75");
        test("上午交电费70元", "70.00");
        test("昨天科室会餐，每人分摊70元", "70.00");
        test("早上去菜市场，掉了20元", "20.00");
        test("降价销售即将到保质期的商品，损失2000元", "2000.00");
        test("公司年底分红50000元", "50000.00");
        test("赌马中彩10000元", "10000.00");
        test("彩票中奖5600元", "5600.00");
        test("查收货款1300000元", "1300000.00");
        test("进账50000元", "50000.00");
        test("入账一笔，300000元", "300000.00");
        test("10月10日订购的一批书，100元。", "100.00");
        test("朋友结婚随礼五百块", "500.00");
        test("昨天收到1万亿", "1000000000000.00");
        test("买了块豆腐三块三,支出一笔", "3.30");
        test("收入一笔，买菜花了1200块", "1200.00");
        test("支出200万8千零50", "2008050.00");
        test("买了5块肥皂，用掉11块88", "11.88");
        test("昨天买了四条毛巾花了三十四块七毛五", "34.75");
        test("今天银行存入7000元是公司工资", "7000.00");
        test("昨天银行扣款2350元房贷", "2350.00");
        test("十万块钱存款到期，收入一笔存款利息325元", "325.00");
        test("买了寿山石花", "0");
        test("花了7200，买了一块寿山石", "7200.00");
        test("买了一块寿山石花了7200", "7200.00");
        test("小秘跑步回家买了3块糖给老杜吃了2块拿了5块钱", "5.00");
        test("收到一笔，用了1.356", "1.36");
        test("收到一笔：银行利息1.126元", "1.13");
        test("支出一笔：刚才买菜花了一百二。其中一个鸡就九十八。", "120.00");
        test("支出一笔：刚才买菜花了一百三。一个鸡九十，三种菜花了四十。", "130.00");
        test("收入一笔：老陈交给我五千，说里面有老李的三千和老陈的两千", "5000.00");
        test("支出一笔：今天狂宰我一万六，二十多个朋友爆吃两顿。", "16000.00");
        test("支出一笔：昨天请一伙朋友吃了三场，共吃了我六千六百多", "6600.00");
        test("支出一笔：五十人三次吃了六百", "600.00");
        test("支出一笔：今天花了八千三，请二十三人吃了两顿饭", "8300.00");
        test("支出8千1百", "8100.00");
        test("收入516万", "5160000.00");
        test("收入1万九千零八亿九千八百七十六万五千四百元一毛二", "1900898765400.12");
        test("支出10万八千", "108000.00");
        test("收入六十亿八百零三分", "6000000800.03");
        test("支出200万8千零50块3毛2", "2008050.32");
        test("得到2万8千零54块3毛2", "28054.32");
        test("消费2万8千零54块3毛2分", "28054.32");
        test("收入200万8千零5十", "2008050.00");

        test("支出一笔：我们五十人三次吃了六百", "600.00");
        test("支出1千8", "1800.00");
        test("支出1千", "1000.00");
        test("支出1千零8", "1008.00");
        test("支出一千零八", "1008.00");
        test("花了12块8", "12.80");
        test("支出十三块八", "13.80");
        test("今天老张孩子结婚，随礼500元", "500.00");
        test("老张孩子结婚，礼金500元。", "500.00");
    }

    private static void test(String input, String expect) {
        Pattern pattern = null;
        Matcher matcher = null;
        System.out.print("" + input + " :");
        int i = 0;
        for (String reg:REGS_STRINGS_TEST) {
            pattern = Pattern.compile(reg);
            String strs[] = pattern.split(input);
//             strs = input.split("[一两二三四五六七八九十零\\d]");
//            for(String str:strs) {
//                System.out.print(" |"+i++ + ":" + str); 
//            }
            matcher = pattern.matcher(input);
            System.out.print(" " + i++ + ":"); 
            while(matcher.find()) { 
                System.out.print(matcher.group()+";"); 
           }
        }
        System.out.print("\n");
 
        
//        System.out.println("input:" + input);
//        if(matcher.find()){
//            System.out.println("matcher.groupCount()" + matcher.groupCount());
//            System.out.println("group " + matcher.group());
//            //System.out.println("parser_multiple_time_unit value=" + value.toString());
//            for(int i=0; i<matcher.groupCount(); i++ ){
//                System.out.println("result " + matcher.group(i));
//            }
//        }
//        else {
//            System.out.println("Can not find!");
//        }
    }
    
    
}
