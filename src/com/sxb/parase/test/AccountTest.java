package com.sxb.parase.test;

import static org.junit.Assert.assertEquals;
import me.justin.parser.ParseResult;
import me.justin.parser.Parser;

import org.junit.Test;

import com.sxb.parase.data.Account;

public class AccountTest {
	private void testAccout(String content, double expect_amount, int except_type) {
		ParseResult result = Parser.paraseContent(content);
		assertEquals(ParseResult.TYPE_ACCOUNT, result.getType());
		Account account = (Account) result.getObject();
		assertEquals(expect_amount , account.getAmount(), 0.001);
		assertEquals(except_type, account.getType());
	}
	
	@Test
	public void testAccount1() {
		testAccout("今天收入100元", 100, Account.TYPE_INCOME);
	}

	@Test
	public void testAccount2() {
		ParseResult result = Parser.paraseContent("今上午交电费70元");
		assertEquals(ParseResult.TYPE_ACCOUNT, result.getType());
		Account account = (Account) result.getObject();
		assertEquals(70.00 , account.getAmount(), 0.001);
		assertEquals(Account.TYPE_EXPAND, account.getType());
	}
	
	@Test
	public void testAccount3() {
		ParseResult result = Parser.paraseContent("赌马中彩10000元");
		assertEquals(ParseResult.TYPE_ACCOUNT, result.getType());
		Account account = (Account) result.getObject();
		assertEquals(10000 , account.getAmount(), 0.001);
		assertEquals(Account.TYPE_INCOME, account.getType());
	}
	@Test
	public void testAccount4() {
		testAccout("查收货款1300000元", 1300000.00, Account.TYPE_INCOME);
	}
	@Test
	public void testAccount5() {
		testAccout("刚才买菜花了一百二。其中一个鸡就九十八。", 120.00, Account.TYPE_EXPAND);
	}
	@Test
	public void testAccount6() {
		testAccout("昨天下午花2151块买了个三脚架", 2151.00, Account.TYPE_EXPAND);
	}
}
