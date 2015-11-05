package com.sxb.parase.test;

import me.justin.parser.AccountParserResult;
import me.justin.parser.AccountParser_v1;
import me.justin.parser.AsrResultJudge;
import me.justin.parser.TwoValue;



public class TestAccount {
    final static String type_strs[] = { "", "提醒", "收入", "支出", "备忘" };
    public final static int TYPE_INCOME = 2;
    public final static int TYPE_EXPAND = 3;
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
        test("买了块豆腐三块三", "3.30");
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
        test("小秘跑步回家买了3块糖给老杜吃了2块花了5块钱", "5.00");
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
        test("寿山石花五十块买了一块糖", "50.00");
        test("今天发了200块钱的补贴", "200.00");
        test("收到购物汇款356块钱", "356.00", AccountParser_v1.TYPE_INCOME);
        test("收到购物汇款356块钱", "356.00", AccountParser_v1.TYPE_INCOME);
        test("领岗位津贴343块", "343.00", AccountParser_v1.TYPE_INCOME);
        test("领交通补贴341块", "341.00", AccountParser_v1.TYPE_INCOME);
        test("打牌赢钱334块3毛", "334.30", AccountParser_v1.TYPE_INCOME);
        test("小陈还人民币338块", "338.00", AccountParser_v1.TYPE_INCOME);
        test("今天有333块3毛3的入账", "333.33", AccountParser_v1.TYPE_INCOME);
        test("老陈借我300元钱，刚才还我了", "300.00", AccountParser_v1.TYPE_INCOME);
        test("领取暖费316块1毛", "316.10", AccountParser_v1.TYPE_INCOME);
        test("今天我跟老四等三人，每人得了100块赏钱", "100.00", AccountParser_v1.TYPE_INCOME);
        test("收到购物定金300", "300.00", AccountParser_v1.TYPE_INCOME);
        test("3000块的手镯款卖了", "3000.00", AccountParser_v1.TYPE_INCOME);
        test("做生意分利9万元", "90000.00", AccountParser_v1.TYPE_INCOME);
        test("黄总给我1000，说是生日礼物", "1000.00", AccountParser_v1.TYPE_INCOME);
        test("6件藏品拍卖了7万", "70000.00", TYPE_INCOME);
        test("办公室的废品卖了157块", "157.00", TYPE_INCOME);
        test("卖旧彩电一台回收149块6毛", "149.60", TYPE_INCOME);
        test("今天去三元桥，要回500元。", "500.00", TYPE_INCOME);
        test("今天到三元桥，打的花了50", "50.00", TYPE_EXPAND);
        test("今天去四角楼喝茶，花费80", "80.00", TYPE_EXPAND);
        test("本来5块钱的白菜，却花了我8块钱", "8.00", TYPE_EXPAND);
        test("菠菜八毛一斤，我买了5斤，总共4块钱。", "4.00", TYPE_EXPAND);
        test("三毛来我家，还200块钱", "200.00", TYPE_INCOME);
        test("今天炒股赚了598块零2分", "598.02", TYPE_INCOME);
        test("收到银行利息592块", "592.00", TYPE_INCOME);
        test("过节费发了一千元", "1000.00", TYPE_INCOME);
        test("领过节费，共计2581元", "2581.00", TYPE_INCOME);
        test("报销本月打的费569块", "569.00", TYPE_INCOME);
        test("借给老张的565块钱，今天还我了", "565.00", TYPE_INCOME);
        test("卖了一盒产品563块", "563.00", TYPE_INCOME);
        test("卖出去一盒产品563块", "563.00", TYPE_INCOME);
        test("飞机票报销556块", "556.00", TYPE_INCOME);
        test("领这月奖金555块", "555.00", TYPE_INCOME);
        test("公司分红554块", "554.00", TYPE_INCOME);
        test("孩子给我发红包547块", "547.00", TYPE_INCOME);
        test("捡到10块钱", "10.00", TYPE_INCOME);
        test("收到523块7毛6的房租费", "523.76", TYPE_INCOME);
        test("今天销售进账522块", "522.00", TYPE_INCOME);
        test("卖菜收入521块7毛钱", "521.70", TYPE_INCOME);
        test("长途车费报销512块", "512.00", TYPE_INCOME);
        test("收到银行利息502块", "502.00", TYPE_INCOME);
        test("下午打牌，赢了496元5角", "496.50", TYPE_INCOME);
        test("这月买了5支股票赚了3万元", "30000.00", TYPE_INCOME);
        test("卖掉2支股票赚了490块8", "490.80", TYPE_INCOME);
        test("卖了35把扇子进账486元", "486.00", TYPE_INCOME);
        test("小陈还钱484块，不再欠了", "484.00", TYPE_INCOME);
        test("卖废品得了25元", "25.00", TYPE_INCOME);
        test("报销8张的士票，共475块", "475.00", TYPE_INCOME);
        test("7个朋友3天里给了我6次钱，共2100块", "2100.00", TYPE_INCOME);
        test("领工资4000块加上补贴1000块，总共进账5000块钱", "5000.00", TYPE_INCOME);
        test("今天网店进账451块", "451.00", TYPE_INCOME);
        test("转让3件藏品，收回438块", "438.00", TYPE_INCOME);
        test("今天老板拍给我300元，让我买烟抽", "300.00", TYPE_INCOME);
        test("卖了5套产品，净赚200块", "200.00", TYPE_INCOME);
        test("老板发我红包800元", "800.00", TYPE_INCOME);
        test("领了409块，是20天的值班费", "409.00", TYPE_INCOME);
        test("帮了老张3天工，给了我800。", "800.00", TYPE_INCOME);
        test("收到房租385块8毛", "385.80", TYPE_INCOME);
        test("定存到期收到利息383块8", "383.80", TYPE_INCOME);
        test("结算回来376块7毛6", "376.76", TYPE_INCOME);
        test("收7人的礼金700元", "700.00", TYPE_INCOME);
        test("又到了一笔工程款四万元", "40000.00", TYPE_INCOME);
        test("老板给我们一万块，我分了四千。", "4000.00", TYPE_INCOME);
        test("收入一笔500。", "500.00", TYPE_INCOME);
        test("公司发奖金1500元", "1500.00", TYPE_INCOME);
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
    
    private static void test(String input, String expect, int type) {
        AccountParserResult result = AccountParser_v1.parse(input);
        String amountString = String.format("%.2f", result.getAmount());
        if (!amountString.equals(expect) || result.getType()!=type) {
            System.err.format("Input:%s \tOutput: type:%s \t%s != %s \n",
                    input, result.getTypedescription(), amountString, expect);
        } else {
            System.out.format("Input:%s \tOutput: type:%s \t%s == %s \n",
                    input, result.getTypedescription(), amountString, expect);
        }
    }
    
    public static void main(String[] args) {
        testAccout();
        
//        test("又到了一笔工程款四万元", "40000.00", TYPE_INCOME);
        
    }
}
