package com.sxb.parase;

import com.sxb.parase.data.Account;
import com.sxb.parase.data.Alarm;
import com.sxb.parase.data.Memo;
import com.sxb.parase.data.ParaseResult;

public class AsrResultParase {
    public static final String TAG = "AsrResultParase";
    
    public  static ParaseResult paraseContent(String content) {
        if (content == null) {
            return null;
        }
        // 去除空格
        String recFullString = content.replace(" ", "");
        if (recFullString.length() < 1) {
            return null;
        }
        
        ParaseResult result = null;
        int type = paraseContentType(recFullString);
        switch (type) {
        case ParaseResult.TYPE_REMIND:
            Alarm alarm = AsrReminderResultParase
                    .parseReminderResult(recFullString);
			if (alarm.type == Alarm.ALARM_TYPE_FAILED) {
				Memo memo = new Memo();
				memo.setContent(recFullString);
				result = new ParaseResult(ParaseResult.TYPE_MEMO, memo);
			} else {
	            alarm.label = recFullString;
	            if (alarm.type == Alarm.ALARM_TYPE_RELATIVE
	                    && alarm.repeatType == Alarm.ALARM_REPEAT_TYPE_NONE) {
	                alarm.repeatType = Alarm.ALARM_REPEAT_TYPE_STOPWATCH;
	            }
	            result = new ParaseResult(ParaseResult.TYPE_REMIND, alarm);
			}
            break;
        case ParaseResult.TYPE_MEMO: {
			Memo memo = new Memo();
			memo.setContent(recFullString);
			result = new ParaseResult(ParaseResult.TYPE_MEMO, memo);
            }
            break;
        case ParaseResult.TYPE_EXPAND:
        case ParaseResult.TYPE_INCOME:
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
                	result = new ParaseResult(ParaseResult.TYPE_ACCOUNT, account);
                } else {
        			Memo memo = new Memo();
        			memo.setContent(recFullString);
        			result = new ParaseResult(ParaseResult.TYPE_MEMO, memo);
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
     * 判断语义类型
     * @param content
     * @return
     */

    public static int paraseContentType(String content) {
        int typeId = 0;

        if (typeId == 0)
            for (String string : KeywordsConf.reminder_keywords) {
                if (content.contains(string)) {
                    typeId = ParaseResult.TYPE_REMIND;
                    break;
                }
            }

        // 优先关键字判断
        if (content.startsWith(KeywordsConf.expand_key)) {
            typeId = ParaseResult.TYPE_EXPAND;
        } else if (content.startsWith(KeywordsConf.income_key)) {
            typeId = ParaseResult.TYPE_INCOME;
        }

        if (typeId == 0)
            for (String string : KeywordsConf.income_keywords) {
                if (content.contains(string)) {
                    typeId = ParaseResult.TYPE_INCOME;
                    break;
                }
            }
        if (typeId == 0)
            for (String string : KeywordsConf.expand_keywords) {
                if (content.contains(string)) {
                    typeId = ParaseResult.TYPE_EXPAND;
                    break;
                }
            }
        if (typeId == 0)
            for (String string : KeywordsConf.expand_pattern_keywords) {
                if (content.matches(string)) {
                    typeId = ParaseResult.TYPE_EXPAND;
                }
            }

        if (typeId == 0) {
            typeId = ParaseResult.TYPE_MEMO;
        }
        return typeId;
    }
}
