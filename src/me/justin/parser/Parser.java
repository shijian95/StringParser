package me.justin.parser;

import com.sxb.parase.data.Account;
import com.sxb.parase.data.Alarm;
import com.sxb.parase.data.Memo;

public class Parser {
    
    public static AccountParserResult AccountParser(String string) {
        return AccountParser_v1.parse(string);
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
                Account account = new Account();
                TwoValue<Integer, String> two = AsrResultJudge
                        .paraseContent(recFullString);
                int type1 = two.a;
                if (type1 == AsrResultJudge.TYPE_EXPAND ||
                        type1 == AsrResultJudge.TYPE_INCOME) {
                    if (type1 == AsrResultJudge.TYPE_EXPAND ) {
                        account.setType(Account.TYPE_EXPAND);
                    } else if (type1 == AsrResultJudge.TYPE_INCOME) {
                        account.setType(Account.TYPE_INCOME);
                    }
                    account.setContent(recFullString);
                    account.setAmount(Float.parseFloat(two.b));
                    result = new ParseResult(ParseResult.TYPE_ACCOUNT, account);
                } else {
                    Memo memo = new Memo();
                    memo.setContent(recFullString);
                    result = new ParseResult(ParseResult.TYPE_MEMO, memo);
                }
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
