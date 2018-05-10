package com.innext.szqb;

import com.innext.szqb.util.TimeUtil;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        System.out.print(ExampleUnitTest.class.getName());
        //assertEquals(4, 2 + 2);
        //System.out.print(TimeUtil.getCurrentDateByOffset("yyyy-MM-dd 00:00:00", Calendar.DAY_OF_MONTH,25));
        boolean today = TimeUtil.isToday("2018-01-02 10:10:00");
        Assert.assertTrue(today);
    }

    @Test
    public void testTime() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr1 = "2018-02-03 10:00:00";
        Date date1 = format.parse(dateStr1);
        int offectDay = TimeUtil.getOffectDay(date1.getTime(), new Date().getTime());
        System.out.println(offectDay);
        Assert.assertEquals(1, offectDay);
    }
}