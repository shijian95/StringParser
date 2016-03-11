package me.justin.parser;

public class KeywordsConf {

    /**
     * 此处关键字可以配置
     */
	public final static String reminder_keywords[] = { "提醒", "闹铃", "倒计时","定时"};
	
	/*
	 * 记账关键字配置
	 */
    public final static String expand_key = "支出一笔";
    public final static String income_key = "收入一笔";
    public final static String expand_keywords[] = { "支出", "支付", "开销一笔", "借出一笔",
            "花销一笔", "开支一笔", "借款一笔", "付出一笔", "消费", "花了", "买了", "花销", "扣款",
            "支出一笔", "花费了", "花费", "开销了", "开销", "开销一笔", "亏了", "亏损", "用了", "吃了",
            "充了", "订购", "损失", "掉了", "分摊", "随礼", "礼金", "交了", "缴纳", "缴了", "汇出",
            "汇了", "借出", "还了", "得到" };
    public final static String expand_pattern_keywords[] = { ".*交.{0,3}费.*" };
    public final static String income_keywords[] = { "增收一笔", "进账一笔", "收入", "得到一笔",
            "收入一笔", "存入", "收到一笔", "赢利了", "赢利", "赢利一笔", "赢了", "盈利了", "盈利",
            "盈利一笔", "赚了", "入账", "进账", "查收", "中奖", "中彩", "分红", "领工资", "挣了",
            "借了", "收到", "给", "给了", "拾到", "捡了","工资" };
    // 出现以下关键字，视为负收入
    public final static String income_negative_keywords[] = { "归还", "还款", "还贷" };

    public final static String pre_expand_amount_keywords[] = { "支出", "花了", "付了",
            "交了", "用了", "用掉", "花销", "开了", "狂宰我", "吃了我", "宰了" };
    public final static String pre_income_amount_eywords[] = { "得到", "收到", "收入", "交给我" };
    public final static String pos_avoid_amount_keywords[] = { "笔", "吨", "人", "次",
            "回", "支" };
    
    
    public final static String NOUNS_3[] = {"三脚架","三角架","四道口","五里庙"};
    public final static String NOUNS_4[] = {"三脚架","三角架"};
}
