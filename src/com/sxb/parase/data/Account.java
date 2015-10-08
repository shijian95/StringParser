package com.sxb.parase.data;

public class Account {
    //记录类型
    public final static int TYPE_INCOME = 1;
    public final static int TYPE_EXPAND = 2;
    public final static int TYPE_BORROW = 3;
    public final static int TYPE_LEND = 4;
    public final static int TYPE_MEMO = 7;
    public final static int TYPE_BALANCE = 9;
	
	private int type; //类型，收入，支出等
	private String content = ""; // 内容
	private double amount;  //金额
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
