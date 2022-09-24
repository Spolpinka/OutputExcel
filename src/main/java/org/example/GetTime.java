package org.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class GetTime {
    public String getTime() {
        Calendar gc = new GregorianCalendar();
        String time = gc.get(Calendar.DAY_OF_MONTH) + "-" + gc.get(Calendar.HOUR) + "-" +
                gc.get(Calendar.MINUTE) + "-" + gc.get(Calendar.SECOND);
        return time;
    }
}
