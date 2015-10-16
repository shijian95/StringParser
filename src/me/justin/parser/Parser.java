package me.justin.parser;

import com.sxb.parase.data.Account;
import com.sxb.parase.data.Alarm;
import com.sxb.parase.data.Memo;

public class Parser {
    
    public static ParseResult AccountParser(String string) {
        AccountParserResult  result2 =  AccountParser_v1.parse(string);
        ParseResult result = new ParseResult();
        if (result2.isAccount()) {
            Account account = new Account();
            if (result2.isExpand()) {
                account.setType(Account.TYPE_EXPAND);
            } else if (result2.isIncome()) {
                account.setType(Account.TYPE_INCOME);
            }
            account.setContent(string);
            account.setAmount(result2.getAmount());
            result.setObject(account);
            result.setType(ParseResult.TYPE_ACCOUNT);
        }
        else {
            Memo memo = new Memo();
            memo.setContent(string);
            result.setType(ParseResult.TYPE_MEMO);
            result.setObject(memo);
        }
        return result;
    }
    
    public static ParseResult RemindParser(String recFullString) {
        ParseResult result;
        Alarm alarm = ReminderParser
                .parseReminderResult(recFullString);
        if (alarm.type == Alarm.ALARM_TYPE_FAILED) {
            Memo memo = new Memo();
            memo.setContent(recFullString);
            result = new ParseResult(ParseResult.TYPE_MEMO, memo);
        } else {
            alarm.label = recFullString;
            if (alarm.type == Alarm.ALARM_TYPE_RELATIVE
                    && alarm.repeatType == Alarm.ALARM_REPEAT_TYPE_NONE) {
                alarm.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
            }
            result = new ParseResult(ParseResult.TYPE_REMIND, alarm);
        }
        return result;
    }
    
    /**
     * 判断语义类型
     * @param content
     * @return
     */

    public static int paraseContentType(String content) {
        int typeId = 0;

        if (typeId == 0)
            for (String string : KeywordsConf.reminder_keywords) {
                if (content.contains(string)) {
                    typeId = ParseResult.TYPE_REMIND;
                    break;
                }
            }

        // 优先关键字判断
        if (content.startsWith(KeywordsConf.expand_key)) {
            typeId = ParseResult.TYPE_EXPAND;
        } else if (content.startsWith(KeywordsConf.income_key)) {
            typeId = ParseResult.TYPE_INCOME;
        }

        if (typeId == 0)
            for (String string : KeywordsConf.income_keywords) {
                if (content.contains(string)) {
                    typeId = ParseResult.TYPE_INCOME;
                    break;
                }
            }
        if (typeId == 0)
            for (String string : KeywordsConf.expand_keywords) {
                if (content.contains(string)) {
                    typeId = ParseResult.TYPE_EXPAND;
                    break;
                }
            }
        if (typeId == 0)
            for (String string : KeywordsConf.expand_pattern_keywords) {
                if (content.matches(string)) {
                    typeId = ParseResult.TYPE_EXPAND;
                }
            }

        if (typeId == 0) {
            typeId = ParseResult.TYPE_MEMO;
        }
        return typeId;
    }
    
    public  static ParseResult paraseContent(String content, int prefer_type) {
        ParseResult result = paraseContent(content);
        if (result.getType() == ParseResult.TYPE_MEMO) {
            if (prefer_type == ParseResult.TYPE_REMIND) {
                result = RemindParser(content);
            } else if (prefer_type == ParseResult.TYPE_ACCOUNT) {
                result = AccountParser(content);
            }
        }
        
        return result;
    }
    
    public  static ParseResult paraseContent(String content) {
        if (content == null) {
            return null;
        }
        // 去除空格
        String recFullString = content.replace(" ", "");
        if (recFullString.length() < 1) {
            return null;
        }
        
        ParseResult result = null;
        int type = paraseContentType(recFullString);
        switch (type) {
        case ParseResult.TYPE_REMIND:
            Alarm alarm = ReminderParser
                    .parseReminderResult(recFullString);
            if (alarm.type == Alarm.ALARM_TYPE_FAILED) {
                Memo memo = new Memo();
                memo.setContent(recFullString);
                result = new ParseResult(ParseResult.TYPE_MEMO, memo);
            } else {
                alarm.label = recFullString;
                if (alarm.type == Alarm.ALARM_TYPE_RELATIVE
                        && alarm.repeatType == Alarm.ALARM_REPEAT_TYPE_NONE) {
                    alarm.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
                }
                result = new ParseResult(ParseResult.TYPE_REMIND, alarm);
            }
            break;
        case ParseResult.TYPE_MEMO: {
            Memo memo = new Memo();
            memo.setContent(recFullString);
            result = new ParseResult(ParseResult.TYPE_MEMO, memo);
            }
            break;
        case ParseResult.TYPE_EXPAND:
        case ParseResult.TYPE_INCOME:
            {
                result  = AccountParser(content);
            }
            break;
        default:
            System.err.println("Parase with Wrong type!");
            break;
        }
        return result;
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
