package com.example.evesdiary;

import android.text.style.TtsSpan;
import android.util.Pair;

import java.util.Date;
import java.util.List;

public class PeriodData {
    public List<PeriodDates> periodList;
    public boolean onPeriod;

    public PeriodData(){

    }
    public PeriodData(List<PeriodDates> periodList, boolean onPeriod){
        this.periodList = periodList;
        this.onPeriod = onPeriod;
    }

    public List<PeriodDates> getPeriodList() {
        return periodList;
    }

    public boolean getOnPeriod() {
        return onPeriod;
    }

//    public void setPeriodList(List<PeriodDates> periodList) {
//        this.periodList = periodList;
//    }
//
//    public void setOnPeriod(boolean onPeriod) {
//        this.onPeriod = onPeriod;
//    }

    public static class PeriodDates {
        public Date startDate;
        public Date endDate;

        public PeriodDates(){

        }

        public PeriodDates(Date startDate, Date endDate){
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Date getStartDate() {
            return startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

//        public void setStartDate(Date startDate) {
//            this.startDate = startDate;
//        }
//
//        public void setEndDate(Date endDate) {
//            this.endDate = endDate;
//        }
    }
}
