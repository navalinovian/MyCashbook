package com.navalinovian.mycashbook;

import java.util.ArrayList;

public class Cashflow {
    private Integer mReport_code;
    private String mAmount, mDate, mDescription;

    public Cashflow(Integer report_code, String amount, String date, String description){
        mReport_code = report_code;
        mAmount = amount;
        mDate = date;
        mDescription = description;
    }

    public Integer getmReport_code() {
        return mReport_code;
    }

    public String getmAmount() {
        return mAmount;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmDescription() {
        return mDescription;
    }

}
