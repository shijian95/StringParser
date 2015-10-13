package com.sxb.parase.test;

import me.justin.parser.AccountParserResult;
import me.justin.parser.AccountParser_v1;
import me.justin.parser.AsrResultJudge;
import me.justin.parser.TwoValue;



public class TestAccount {
    final static String type_strs[] = { "", "提醒", "收入", "支出", "备忘" };

    public static void testAccout() {
        /*
         * 买了5块肥皂，用掉11块88 买了10块地砖，用了128 昨天银行扣款2350元房贷。 买了一对枕头，支出99 今天还银行卡2750
         * 只是出现“归还、还款、还贷”等归还的字眼，就视同负收入 昨天买了四条毛巾花了三十四块七毛五
         */
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
        test("买了寿山石花", "0.00");
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
        AccountParserResult result = AccountParser_v1.parse(input);
        String amountString = String.format("%.2f", result.getAmount());
        if (!amountString.equals(expect)) {
            System.err.format("Input:%s \tOutput: type:%s \t%s != %s \n",
                    input, result.getTypedescription(), amountString, expect);
        } else {
            System.out.format("Input:%s \tOutput: type:%s \t%s == %s \n",
                    input, result.getTypedescription(), amountString, expect);
        }
    }
    public static void main(String[] args) {
//        testAccout();
        test("买了寿山石花", "0.00");
//        test("支出一笔：刚才买菜花了一百三。一个鸡九十，三种菜花了四十。", "130.00");
//        test("收入一笔：老陈交给我五千，说里面有老李的三千和老陈的两千", "5000.00");
    }
}
