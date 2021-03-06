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
        "[一两二三四五六七八九十零\\d]+[笔把个位人次批伙场双件斤盒张天子套支台下期副幅]" 
        + "|" + "[一两二三四五六七八九十零\\d]+月[一两二三四五六七八九十零\\d]+日"
        + "|" + "[一两二三四五六七八九十零\\d]+年",
        "[\\d][笔把个位人次批伙场双件斤盒张天子套支台下期副幅]"
    };
    
    
    final static String REGEXS_ACCOUNT[][] = {
        { "(花)(\\d+\\.\\d{2})", "amount=group(2);type=expand" },
        { "(花了)(\\d+\\.\\d{2})", "amount=group(2);type=expand" },
        { "(亏了)(\\d+\\.\\d{2})", "amount=group(2);type=expand" },
        { "(还给\\D*)(\\d+\\.\\d{2})", "amount=group(2);type=expand" },
        { "(赚回)(\\d+\\.\\d{2})", "amount=group(2);type=income" },
        { "(赚)(\\d+\\.\\d{2})", "amount=group(2);type=income" },
        { "(收来)(\\d+\\.\\d{2})", "amount=group(2);type=income" },
        
        { "给了我\\D*(\\d+\\.\\d{2})\\D*红包", "amount=group(1);type=income" },
        { "给我\\D*(\\d+\\.\\d{2})\\D*红包", "amount=group(1);type=income" },
        { "给了\\D*(\\d+\\.\\d{2})\\D*红包", "amount=group(1);type=expand" },
        { "给\\D*(\\d+\\.\\d{2})\\D*红包", "amount=group(1);type=expand" },

        { "给\\D*低保费(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "销售(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "我还了(\\d+\\.\\d{2})", "amount=group(1);type=expand" },
        { "还了(\\d+\\.\\d{2})给我", "amount=group(1);type=income" },
        { "还了(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "还了我(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "返还\\D*(\\d+\\.\\d{2})", "amount=group(1);type=expand" },
        { "订购\\D*(\\d+\\.\\d{2})", "amount=group(1);type=expand" },
        { "报销.*费(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "打的\\D*(\\d+\\.\\d{2})", "amount=group(1);type=expand" },
        { "售出(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "掉了(\\d+\\.\\d{2})", "amount=group(1);type=expand" },
        { "丢了(\\d+\\.\\d{2})", "amount=group(1);type=expand" },
        { "少了(\\d+\\.\\d{2})", "amount=group(1);type=expand" },
        { "损失(\\d+\\.\\d{2})", "amount=group(1);type=expand" },
        { "中奖(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "中彩(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "拿到(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "拿了\\D+(\\d+\\.\\d{2})", "amount=group(1);type=expand" },
        { "抢了\\D+(\\d+\\.\\d{2})", "amount=group(1);type=expand" },
        { "小费\\D+(\\d+\\.\\d{2}).*给我", "amount=group(1);type=income" },
        { "(\\d+\\.\\d{2})钱.*给我了", "amount=group(1);type=income" },
        { "费交了(\\d+\\.\\d{2})", "amount=group(1);type=expand" },
        { "盈利\\D*(\\d+\\.\\d{2})\\D*", "amount=group(1);type=income" },
        { "收到\\D*(\\d+\\.\\d{2})\\D*", "amount=group(1);type=income" },
        { "拿(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "拿了(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "捡了(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "拿过来(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "拿回来(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "拿钱回来(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "取回来(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "取(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "提款(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "提款回来(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "取钱(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "取款(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "存款(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "存进来(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "出售\\D*(\\d+\\.\\d{2})\\D*", "amount=group(1);type=income" },
        { "收押金(\\d+\\.\\d{2})\\D*", "amount=group(1);type=income" },
        { "发福利(\\d+\\.\\d{2})\\D*", "amount=group(1);type=income" },
        { "发薪水(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "到帐(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "借了(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "给了(\\d+\\.\\d{2})", "amount=group(1);type=income" },
        { "省了(\\d+\\.\\d{2})", "amount=group(1);type=income" },
      
//        { "买\\D*(\\d+\\.\\d{2})\\D*(\\d+\\.\\d{2})", "amount=group(2);type=expand" },
//        { "卖\\D*(\\d+\\.\\d{2})\\D*(\\d+\\.\\d{2})", "amount=group(2);type=income" },
//        { "(买\\D*)(\\d+\\.\\d{2})", "amount=group(2);type=expand" },
//        { "(卖\\D*)(\\d+\\.\\d{2})", "amount=group(2);type=income" },
        };
    
    
    final static String PATTERNS_EXPAND[] = {
        "(?<=花)\\d+\\.\\d{2}",
//        "(?<=花了)\\d+\\.\\d{2}",
        "(?<=花费)\\d+\\.\\d{2}",
        "(?<=付款)\\d+\\.\\d{2}",
        "(?<=支)\\d+\\.\\d{2}",
        "(?<=亏)\\d+\\.\\d{2}",
        "(?<=用了)\\d+\\.\\d{2}",
        "(?<=用去)\\d+\\.\\d{2}",
        "(?<=花去)\\d+\\.\\d{2}",
        "(?<=付了)\\d+\\.\\d{2}",
        "(?<=付出)\\d+\\.\\d{2}",
        "(?<=付)\\d+\\.\\d{2}",
        "(?<=打出)\\d+\\.\\d{2}",
        "(?<=用掉)\\d+\\.\\d{2}",
        "(?<=支用)\\d+\\.\\d{2}",
        "(?<=划走)\\d+\\.\\d{2}",
        "(?<=随礼)\\d+\\.\\d{2}",
        "(?<=消费)\\d+\\.\\d{2}",
        "(?<=花掉)\\d+\\.\\d{2}",
        };
    
    final static String PATTERNS_INCOME[] = {
        "(?<=给我)\\d+\\.\\d{2}",   //匹配 给我200.00  匹配结果是200.00
        "(?<=给了我)\\d+\\.\\d{2}",
        "(?<=拾到)\\d+\\.\\d{2}",
        "(?<=赢钱)\\d+\\.\\d{2}",
        "(?<=借我)\\d+\\.\\d{2}",
        "(?<=得了)\\d+\\.\\d{2}",
        "(?<=分利)\\d+\\.\\d{2}",
        "(?<=回收)\\d+\\.\\d{2}",
        "(?<=要回)\\d+\\.\\d{2}",
        "(?<=赚了)\\d+\\.\\d{2}",
        "(?<=赚)\\d+\\.\\d{2}",
        "(?<=还)\\d+\\.\\d{2}",
        "(?<=捡到)\\d+\\.\\d{2}",
        "(?<=收到)\\d+\\.\\d{2}",
        "(?<=收入)\\d+\\.\\d{2}",
        "(?<=赢了)\\d+\\.\\d{2}",
        "(?<=还人民币)\\d+\\.\\d{2}",
        "(?<=还钱)\\d+\\.\\d{2}",
        "(?<=进账)\\d+\\.\\d{2}",
        "(?<=收回)\\d+\\.\\d{2}",
        "(?<=我分了)\\d+\\.\\d{2}",
        "(?<=获利)\\d+\\.\\d{2}",
        "(?<=退了我)\\d+\\.\\d{2}",
        "(?<=退)\\d+\\.\\d{2}",
        "(?<=退回)\\d+\\.\\d{2}",
        "(?<=赢)\\d+\\.\\d{2}",
        "(?<=得)\\d+\\.\\d{2}",
        "(?<=盈利)\\d+\\.\\d{2}",
        "(?<=进款)\\d+\\.\\d{2}",
        "(?<=赚回)\\d+\\.\\d{2}",
        "(?<=还我)\\d+\\.\\d{2}",
        "(?<=退我)\\d+\\.\\d{2}",
        "(?<=得到)\\d+\\.\\d{2}",
        "(?<=收进)\\d+\\.\\d{2}",
        "(?<=抢了)\\d+\\.\\d{2}",
        };
    
    final static String PATTERNS_KEYWORD_INCOME[] = {
        "报销.*费",
        "收到.*汇款",
        "收.*礼金",
        "收到.*利息",
        "收到.*款",
        "领.*津贴",
        "领.*补贴",
        "领.*过节费",
        "领.*奖金",
        "发.*奖金",
        "领.*费",
        "收到.*定金",
        "给我.*发红包",
        "给了我.*钱",
        "发我红包",
        "到了.*款",
        "发.*费",
        "增收.*费",
        "赢(.*)\\d+\\.\\d{2}",
        "款到了",
        "拿到.*补贴",
        "获得.*利息\\d+\\.\\d{2}",
        "(卖\\D*)(\\d+\\.\\d{2})",
        "查收.*款",
        };
    
    final static String PATTERNS_KEYWORD_EXPAND[] = {
        "买(.*)(\\d+\\.\\d{2})",
        "支付.*费",
        "送.*费",
        "交.*费",
        "(买\\D*)(\\d+\\.\\d{2})"
        };
    /*
     * 
     * */
    final static Map<String, Integer> KEY_WORD_2 = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 2L;
        {
            put("买了", EXPAND_WORD);
            put("亏了", EXPAND_WORD);
            put("花了", EXPAND_WORD);
            put("开销", EXPAND_WORD);
            put("还了", EXPAND_WORD);
            put("支出", EXPAND_WORD);
            put("礼金", EXPAND_WORD);
            put("分摊", EXPAND_WORD);
            put("吃了", EXPAND_WORD);
            put("花销", EXPAND_WORD);
            put("扣款", EXPAND_WORD);
            put("工资", INCOME_WORD);
            put("发了", INCOME_WORD);
            put("存入", INCOME_WORD);
            put("赢利", INCOME_WORD);
            put("赢钱", INCOME_WORD);
            put("入账", INCOME_WORD);
            put("进账", INCOME_WORD);
            put("分红", INCOME_WORD);
            put("挣了", INCOME_WORD);
            put("卖了", INCOME_WORD);
            put("报销", INCOME_WORD);
            put("收入", INCOME_WORD);
        }
    };
    final static Map<String, Integer> KEY_WORD_3 = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 3L;
        {
            put("中彩票", INCOME_WORD);
            put("交给我", INCOME_WORD);
            put("还我了", INCOME_WORD);
            put("领工资", INCOME_WORD);
            put("发奖金", INCOME_WORD);
            put("赢利了", INCOME_WORD);
            put("卖出去", INCOME_WORD);
            put("开销了", EXPAND_WORD);
            put("狂宰我", EXPAND_WORD);
            put("三脚架", EXCULDE_WORD);
            put("四道口", EXCULDE_WORD);
            put("三元桥", EXCULDE_WORD);
            put("四角楼", EXCULDE_WORD);
            put("转发了", EXCULDE_WORD);
        }
    };
    final static Map<String, Integer> KEY_WORD_4 = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 4L;
        {
            put("中了彩票", INCOME_WORD);
            put("得到一笔", INCOME_WORD);
            put("增收一笔", INCOME_WORD);
            put("进账一笔", INCOME_WORD);
            put("收入一笔", INCOME_WORD);
            put("收到房租", INCOME_WORD);
            put("收到利息", INCOME_WORD);
            put("结算回来", INCOME_WORD);
            put("领取暖费", INCOME_WORD);
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
    
    public static AccountParserResult parse(String content,boolean is_prefer) {
        AccountParserResult result = new AccountParserResult();
        Integer type = TYPE_UNKNOWN;
        result.setType(type);
        String str1 = removeExcludeWords(content); //去掉干扰词汇
        ArrayList<Element>  words1 = split_by_key_word(str1); //根据关键字分词
        ArrayList<Element>  words2 = new ArrayList<>();
        for (Element word: words1) {
            if (word.type == CONTENT) {
                ArrayList<Element>  words3 = split_by_digit(word.content); //提取其中的数字
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
                String amountStr = convert2Amount(element.content);  //数字转换成金额
                if (amountStr!=null && !amountStr.equals("")){
                    element.content = amountStr;
                    amount = Double.parseDouble(amountStr);
                    amounts.add(new AmountPos(count, amount));
                }
            } else if (element.type == INCOME_WORD) {
                type = TYPE_INCOME; //出现收入关键字
            } else if (element.type == EXPAND_WORD) {
                type = TYPE_EXPAND;//出现支出关键字
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
        StringBuilder sBuilder = new StringBuilder();
        for (Element e : words2) {
            sBuilder.append(e.content);
        }
        String last_string = sBuilder.toString();
        if (DEBUG) {
            System.out.println("得到的字符串：" + sBuilder.toString());
        }
        AccountParserResult ret = parse_amount(last_string);
        if (ret != null) {
            type = ret.getType();
            amount = ret.getAmount();
        }
        
        if (ret == null && amounts.size() !=0) {
            TwoValue<Integer, Double> retTwoValue = parseType(last_string);
            if (retTwoValue.a != TYPE_UNKNOWN) {
                type = retTwoValue.a;
            }
            if (retTwoValue.b != null) {
                amount = retTwoValue.b;
            }
            if (amounts.size() > 1 && retTwoValue.b==null) {
                int size = amounts.size();
                for (int i = 0; i < size; i++) {
                    AmountPos ap = amounts.get(i);
                    int pos = ap.postion;
                    if (pos > 0) {
                        Element preElement = words2.get(pos - 1);
                        if (preElement.type == EXPAND_WORD
                                || preElement.type == INCOME_WORD) {
                            amount = ap.amount;
                            break;
                        }
                    }
                }
            }
        } 
        if (is_prefer && type == TYPE_UNKNOWN) {
            //没有解析出类型，默认为支出
            type = TYPE_EXPAND;
        }
        result.setType(type);
        result.setAmount(amount);
        return result;
    }
    
    private static TwoValue<Integer, Double> parseType(String last_string) {
        boolean pattern_matched = false;
        TwoValue<Integer, Double> ret;
        int type = TYPE_UNKNOWN;
        Double amount = null;
        //判断类型
        for (String regex : PATTERNS_EXPAND) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(last_string);
            if (matcher.find()) {
                type = TYPE_EXPAND;
                String s = matcher.group();
                if (DEBUG) {
                System.out.println("Out金额：" + s);
                }
                amount = Double.parseDouble(s);
                pattern_matched = true;
                break;
            }
        }
      //判断类型
        if (!pattern_matched)
            for (String regex : PATTERNS_INCOME) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(last_string);
                if (matcher.find()) {
                    type = TYPE_INCOME;
                    String s = matcher.group();
                    if (DEBUG) {
                    System.out.println("In金额：" + s);
                    }
                    amount = Double.parseDouble(s);
                    pattern_matched = true;
                    break;
                }
            }
        if (!pattern_matched)
            for (String regex : PATTERNS_KEYWORD_INCOME) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(last_string);
                if (matcher.find()) {
                    type = TYPE_INCOME;
                    pattern_matched = true;
                    break;
                }
            }
        if (!pattern_matched)
            for (String regex : PATTERNS_KEYWORD_EXPAND) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(last_string);
                if (matcher.find()) {
                    type = TYPE_EXPAND;
                    pattern_matched = true;
                    break;
                }
            }
        if (DEBUG)
            System.out.println("parseType " + type + " : " + amount);
        return new TwoValue<Integer, Double>(type, amount);
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
    
    private static boolean isMixedDigit(char c) {
        boolean ret = false;
        if (isDigit(c)) {
            ret = true;;
        } else {
            for (char b: zh_digit_start_keywords) {
                if (b == c) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }
    
    static ArrayList<Element> split_by_digit(String content){
        ArrayList<Element> words = new ArrayList<Element>();
        StringBuffer sb = new StringBuffer();
        char start = content.charAt(0);
        int type; 
        if (isMixedDigit(start)) {
            type = DIGIT;
        } else {
            type = LETTER;
        }
//        int type = getCharType(start);
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

    

    public static AccountParserResult parse_amount(String content) {
//        System.out.println("Regular parser:" + content);
        AccountParserResult result = null;
        HashMap<String, String> value_map = new HashMap<String, String>();
        boolean found = false;
        for (int i = 0; i < REGEXS_ACCOUNT.length; i++) {
            String regex1 = REGEXS_ACCOUNT[i][0];
//            System.out.println(regex1);
            Pattern pattern = Pattern.compile(regex1);
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                if (DEBUG) {
                    System.out.println("group count:" + matcher.groupCount());
                    System.out.println(matcher.group());
                }
                String find_str = matcher.group();
                int index = content.indexOf(find_str);
                int last_index = index+find_str.length();
                if (DEBUG) {
//                System.out.println(index+ " : " + last_index);
//                String begin = content.substring(0, index);
//                String end = content.substring(last_index, content.length());
//                System.out.println(begin+ " : " +find_str+ " : " + end);

//                System.out.println(matcher.group(1));
//                System.out.println(matcher.group(2));
                }
                String value = REGEXS_ACCOUNT[i][1];
                String[] values = value.split(";"); // daysofWeek=group(2)
                for (String s : values) {
                    String[] expressions = s.split("="); // daysofWeek=group(2)
                    Pattern pattern_v = null;
                    Matcher matcher_v = null;
                    pattern_v = Pattern.compile("group\\((\\d)\\)");
                    matcher_v = pattern_v.matcher(expressions[1]);
                    if (matcher_v.find()) {
//                        System.out.println("group[0] = " + matcher_v.group(0));
//                        System.out.println("group[1] = " + matcher_v.group(1));
//                        System.out.println("group[2] = " + matcher.group(2));
//                        System.out.println("groupCount = " + matcher_v.groupCount());
//                        System.out.println("" + matcher.group(group_num));
                        int group_num = Integer.parseInt(matcher_v.group(1));
                        value_map.put(expressions[0], matcher.group(group_num));
                    } else {
                        value_map.put(expressions[0], expressions[1]);
                    }
                }
                found = true;
                break;
            }
            else {
               // nothing to do
            }
        }
        if (!found) {
//            System.out.println("Regular result：nothing");
        } else {
            result = new AccountParserResult();
            String type = value_map.get("type");
            String amount = value_map.get("amount");
            if (type.equalsIgnoreCase("expand")) {
                result.setType(AccountParser_v1.TYPE_EXPAND);
            } else {
                result.setType(AccountParser_v1.TYPE_INCOME);
            }
            result.setAmount(Double.parseDouble(amount));
//            System.out.println("Regular result：" + type +":"+ amount);
       }
       return result;
    }
}
