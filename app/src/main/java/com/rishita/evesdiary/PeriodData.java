package com.rishita.evesdiary;

import java.util.Date;
import java.util.List;

public class PeriodData {
    public List<PeriodDates> periodList;
    public PeriodData(){
    }
    public PeriodData(List<PeriodDates> periodList){
        this.periodList = periodList;
    }

    public boolean isOnPeriod(Date currDate){
        if(periodList.isEmpty()){
            return false;
        }

        PeriodDates lastDates = periodList.get(periodList.size()-1);
        return lastDates.startDate.compareTo(currDate) * currDate.compareTo(lastDates.endDate) >= 0;
    }

    public List<PeriodDates> getPeriodList() {
        return periodList;
    }

//    public void setPeriodList(List<PeriodDates> periodList) {
//        this.periodList = periodList;
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
