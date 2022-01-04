package com.nsa.edvoraassessmentapp.adapters;

public class GetDate {
    public static String getDate(String date){
        String[] arrr=date.split("T");
        String[] dates=arrr[0].split("-");
                return dates[2]+":"+dates[1]+":"+dates[0];
    }
}
