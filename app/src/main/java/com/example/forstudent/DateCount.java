package com.example.forstudent;

import java.util.Calendar;

public class DateCount {

    protected long Today;
    protected long setday;
    protected long left;
    protected int result=0;

    Calendar tcalendar = Calendar.getInstance();
    Calendar dcalendar = Calendar.getInstance();

    public DateCount(){
    }

    public DateCount(Calendar dcalendar){
        this.dcalendar = dcalendar;
    }


    public int calcDday(){
        //현재 날짜

        Today = tcalendar.getTimeInMillis()/(24*60*60*1000);
        setday = dcalendar.getTimeInMillis()/(24*60*60*1000);

        left = (setday - Today);
        result = (int)left;

        return result;
    }
}
