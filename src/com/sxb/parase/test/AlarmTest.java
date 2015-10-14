package com.sxb.parase.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import me.justin.parser.ParseResult;
import me.justin.parser.Parser;

import org.junit.Before;
import org.junit.Test;

import com.sxb.parase.data.Alarm;

public class AlarmTest {

	@Before
	public void setUp() throws Exception {
	}
	
    static boolean equals(Alarm a, Alarm b) {
        boolean ret = false;
        int a_days = a.daysOfWeek.getCoded();
        int b_days = b.daysOfWeek.getCoded();
        
        assertEquals(a.type, b.type);
        assertEquals(a.year, b.year);
        assertEquals(a.month, b.month);
        assertEquals(a.day, b.day);
        assertEquals(a.hour, b.hour);
        assertEquals(a.minutes, b.minutes);
        assertEquals(a.second, b.second);
        assertEquals(a_days, b_days);
        assertEquals(a.repeatType, b.repeatType);
        assertEquals(a.interval, b.interval);
        assertEquals(a.repeatTimes, b.repeatTimes);
        
        ret = (a.type == b.type 
                && a.year == b.year
                && a.month == b.month
                && a.day == b.day 
                && a.hour == b.hour 
                && a.minutes == b.minutes
                && a.second == b.second 
//                && a.ampm == b.ampm 
                && a_days == b_days
                && a.repeatType == b.repeatType 
                && a.interval == b.interval 
                && a.repeatTimes == b.repeatTimes);
        return ret;
    }
    
    public static void assertReminder(String input, Alarm expect) {
    	ParseResult item = Parser.paraseContent(input);
         Alarm alarm;
         if (item.getType() == ParseResult.TYPE_REMIND) {
             alarm = (Alarm) item.getObject();
         } else {
             System.err.print("Wrong Type:" + input);
             return;
         }
        long interval_hours = alarm.interval / (1000 * 60 * 60);
        long interval_minutes = alarm.interval / (1000 * 60) % 60;
        if (equals(expect , alarm)) {
            System.out
                    .format("type:%d repattype:%d,"
                            + "year:%d, month:%d, day:%d, hour:%d, minutes:%d second:%d "
                            + "ampm:%d, dayofweek:%s "
                            + "interval:%d小时%d分 次数:%d\n", alarm.type,
                            alarm.repeatType, alarm.year, alarm.month,
                            alarm.day, alarm.hour, alarm.minutes, alarm.second,
                            alarm.ampm, alarm.daysOfWeek.toString(true),
                            interval_hours, interval_minutes, alarm.repeatTimes);
        } else {
            System.err.println(input); 
            System.err
                    .format("current:type:%d repattype:%d,"
                            + " year:%d, month:%d, day:%d, hour:%d, minutes:%d second:%d "
                            + "ampm:%d, dayofweek:%s "
                            + "interval:%d小时%d分 次数:%d\n", alarm.type,
                            alarm.repeatType, alarm.year, alarm.month,
                            alarm.day, alarm.hour, alarm.minutes, alarm.second,
                            alarm.ampm, alarm.daysOfWeek.toString(true),
                            interval_hours, interval_minutes, alarm.repeatTimes);
            long expect_interval_hours = expect.interval / (1000 * 60 * 60);
            long expect_interval_minutes = expect.interval / (1000 * 60) % 60;
            System.err
                    .format("expect :type:%d repattype:%d,"
                            + " year:%d, month:%d, day:%d, hour:%d, minutes:%d second:%d "
                            + "ampm:%d, dayofweek:%s "
                            + "interval:%d小时%d分 次数:%d\n", expect.type,
                            expect.repeatType, expect.year, expect.month,
                            expect.day, expect.hour, expect.minutes,
                            expect.second, expect.ampm,
                            expect.daysOfWeek.toString(true),
                            expect_interval_hours, expect_interval_minutes,
                            expect.repeatTimes);
            // assert(false);
        }
    }
	
	@Test
	public void testAlarm1() {
        // 周六上午九点提醒我
        Alarm expect = new Alarm();
        com.sxb.parase.data.Alarm.DaysOfWeek daysofWeek;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int day = 0;
        int curDay = c.get(Calendar.DAY_OF_YEAR);
        int curWeekday = c.get(Calendar.DAY_OF_WEEK);
        int maxMonthDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        int nowSecond = c.get(Calendar.SECOND);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        day = c.get(Calendar.DAY_OF_YEAR);
        if (day < curDay) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.type = Alarm.ALARM_TYPE_ABSOLUTE;
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.hour = 9;
        expect.minutes = 0;
        if ((day == curDay)
                && (expect.hour < nowHour || expect.hour == nowHour
                        && expect.minutes <= nowMinute || expect.hour == nowHour
                        && expect.minutes == nowMinute
                        && expect.second <= nowSecond)) {
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        expect.year = c.get(Calendar.YEAR);
        expect.month = c.get(Calendar.MONTH) + 1;
        expect.day = c.get(Calendar.DAY_OF_MONTH);
        expect.ampm = 1;
        assertReminder("周六上午九点提醒我", expect);
	}
}
