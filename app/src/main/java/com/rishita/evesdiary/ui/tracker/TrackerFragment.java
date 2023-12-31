package com.rishita.evesdiary.ui.tracker;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rishita.evesdiary.PeriodData;
import com.rishita.evesdiary.R;
import com.rishita.evesdiary.databinding.FragmentTrackerBinding;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrackerFragment extends Fragment {

    private FragmentTrackerBinding binding;
    private DatabaseReference databaseReference;
    private String uuid;

    private TextView year;
    private TextView month;
    private TextView periodDueText;
    private TextView periodDue;
    private TextView prevLen;
    private TextView prevDays;
    private TextView cycleVar;

    private CompactCalendarView compactCalendar;

    private PeriodData periodInfo;
    private Date currDate;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        TrackerViewModel trackerViewModel =
                new ViewModelProvider(this).get(TrackerViewModel.class);

        binding = FragmentTrackerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        year = root.findViewById(R.id.year);
        month = root.findViewById(R.id.month);
        periodDueText = root.findViewById(R.id.period_due_text);
        periodDue = root.findViewById(R.id.due_days);
        prevLen = root.findViewById(R.id.prev_len);
        prevDays = root.findViewById(R.id.prev_days);
        cycleVar = root.findViewById(R.id.length);
        compactCalendar = root.findViewById(R.id.calendar);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        LocalDate currentdate = LocalDate.now();

        year.setText(""+currentdate.getYear());
        month.setText(""+currentdate.getMonth());
        currDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        try {
            currDate = dateFormat.parse(dateFormat.format(currDate));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        fetchPeriodData();


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
                Log.d("currDate", dateFormat.format(currDate));
                Log.d("clickedDate", dateFormat.format(dateClicked));

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateClicked);
                Log.d("formatted Clicked Date", dateFormat.format(cal.getTime()));

                try {
                    dateClicked = dateFormat.parse(dateFormat.format(dateClicked));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

//                Toast.makeText(getActivity(), String.valueOf(dateClicked.equals(currDate)), Toast.LENGTH_LONG).show();
                if(periodInfo.getPeriodList().isEmpty()){   // first period
                    Calendar c = Calendar.getInstance();
                    c.setTime(dateClicked);
                    c.add(Calendar.DATE, 4);
                    periodInfo.getPeriodList().add(new PeriodData.PeriodDates(dateClicked, c.getTime()));

                    markPeriod(dateClicked, c.getTime());
                    uploadData(periodInfo);
                    updateViews(periodInfo);
                }

                else{
                    PeriodData.PeriodDates lastDates = periodInfo.getPeriodList().get(periodInfo.getPeriodList().size() - 1);

                    if(compactCalendar.getEvents(dateClicked).size() == 1){ // undo functionality
                        if(dateClicked.before(lastDates.startDate)){    // date clicked before last period
                            Toast.makeText(getActivity(), "Cannot update a completed cycle!", Toast.LENGTH_LONG).show();
                        }

                        else if(dateClicked.equals((lastDates.endDate))){   // unmark from end date
                            Calendar prevDate = Calendar.getInstance();
                            prevDate.setTime(dateClicked);
                            prevDate.add(Calendar.DATE, -1);
                            Date pDate = prevDate.getTime();

                            List<PeriodData.PeriodDates> updatedList = periodInfo.getPeriodList();
                            updatedList.remove(updatedList.size()-1);
                            updatedList.add(new PeriodData.PeriodDates(lastDates.startDate, pDate));

                            periodInfo.periodList = updatedList;

                            uploadData(periodInfo);
                            updateViews(periodInfo);
                        }

                        else{   // clicked on mid way cycle
                            Toast.makeText(getActivity(), "Kindly unmark dates sequentially from the end date!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{   // clicked on a new date
                        Calendar prevDate = Calendar.getInstance();
                        prevDate.setTime(dateClicked);
                        prevDate.add(Calendar.DATE, -1);
                        Date pDate = prevDate.getTime();

                        // date clicked is before start date of last period
                        if (dateClicked.before(lastDates.startDate)) {
                            Toast.makeText(getActivity(), "Cannot update a completed cycle!", Toast.LENGTH_LONG).show();
                        }

                        // extend by 1 day
                        else if(pDate.equals(lastDates.endDate)){
                            Calendar c = Calendar.getInstance();
                            c.setTime(dateClicked);
                            c.add(Calendar.DATE, 1);

                            List<PeriodData.PeriodDates> updatedList = periodInfo.getPeriodList();
                            updatedList.remove(updatedList.size()-1);
                            updatedList.add(new PeriodData.PeriodDates(lastDates.startDate, dateClicked));

                            periodInfo.periodList = updatedList;

//                            markPeriod(dateClicked, dateClicked);  will update by itself in updateViews
                            uploadData(periodInfo);
                            updateViews(periodInfo);
                        }

                        else{
                            Calendar c = Calendar.getInstance();
                            c.setTime(dateClicked);
                            c.add(Calendar.DATE, 4);
                            periodInfo.getPeriodList().add(new PeriodData.PeriodDates(dateClicked, c.getTime()));

//                            markPeriod(dateClicked, c.getTime());
                            uploadData(periodInfo);
                            updateViews(periodInfo);
                        }
                    }
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                year.setText(""+(1900+firstDayOfNewMonth.getYear()));

                Calendar cal = Calendar.getInstance();
                cal.setTime(firstDayOfNewMonth);
                month.setText(new SimpleDateFormat("MMM").format(cal.getTime()));

            }
        });

        return root;
    }

    public void fetchPeriodData(){
        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        periodInfo = new PeriodData(new ArrayList<>());

        databaseReference.child("periodData").child(uuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                PeriodData periodData = dataSnapshot.getValue(PeriodData.class);
                if(periodData != null){
                    Log.d("firebase", String.valueOf(periodData.getPeriodList()));
                    List<PeriodData.PeriodDates> periodDates = periodData.getPeriodList();
                    Date endDate = periodDates.get(periodData.getPeriodList().size() - 1).getEndDate();

                    long diffInMillies = Math.abs(endDate.getTime() - currDate.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                    periodInfo = periodData;

                    updateViews(periodData);

                    progress.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebase", "The read failed: " + databaseError.getCode());
                progress.dismiss();
            }
        });

        progress.dismiss();
    }

    public void uploadData(PeriodData periodData){
        databaseReference.child("periodData").child(uuid).setValue(periodData);

    }


    public void updateViews(PeriodData periodData){
        compactCalendar.removeAllEvents();  //

        List<PeriodData.PeriodDates> periodList = periodData.getPeriodList();
        if(periodList.size() == 0) return;

        long pCLen = -1, pDays = -1, minLen = 365, maxLen = 0;
        long avgLen = 30;

        for (int i = 0; i < periodList.size(); i++) {
            PeriodData.PeriodDates periodDates = periodList.get(i);

            Date endDate = periodDates.getEndDate();
            Date startDate = periodDates.getStartDate();

            markPeriod(startDate, endDate);

            if(i == 0){
                continue;
            }
            Date pCycleStartDate = periodList.get(i-1).getStartDate();

            long diffInMillies = Math.abs(startDate.getTime() - pCycleStartDate.getTime());
            long pCLength = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            minLen = min(minLen, pCLength);
            maxLen = max(maxLen, pCLength);

            // if currently onPeriod, then no need to include this cycle for updation
            if(!periodData.isOnPeriod(currDate) && i == periodList.size()-1){
                pCLen = pCLength+1;
            }

            else if (periodData.isOnPeriod(currDate) && i == periodList.size()-2){
                pCLen = pCLength+1;
            }
        }
        if(periodList.size() == 1){
            maxLen = minLen = 28;
        }
        avgLen = (maxLen + minLen)/2;

        if(periodInfo.isOnPeriod(currDate)){
            List<PeriodData.PeriodDates> periodDates = periodInfo.getPeriodList();
            Date endDate = periodDates.get(periodInfo.getPeriodList().size() - 1).getEndDate();

            long diffInMillies = Math.abs(endDate.getTime() - currDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;

            periodDueText.setText("Period Ends in - ");
            periodDue.setText(String.valueOf(diff) + " days");
        }
        else{
            List<PeriodData.PeriodDates> periodDates = periodInfo.getPeriodList();
            Date startDate = periodDates.get(periodInfo.getPeriodList().size() - 1).getStartDate();

            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            c.add(Calendar.DATE, (int) avgLen);
            Date predicted = c.getTime();

            long diffInMillies = Math.abs(predicted.getTime() - currDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;

            periodDueText.setText("Period Due in - ");
            periodDue.setText(String.valueOf(diff) + " days");
        }

        if(!periodData.isOnPeriod(currDate)){
            long diffInMillies = Math.abs(periodList.get(periodList.size()-1).getEndDate().getTime() - periodList.get(periodList.size()-1).getStartDate().getTime());
            pDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        }

        // if onPeriod then this cycle may change so do not take this into considerations
        else if(periodData.isOnPeriod(currDate) && periodList.size() >= 2) {
            long diffInMillies = Math.abs(periodList.get(periodList.size()-2).getEndDate().getTime() - periodList.get(periodList.size()-2).getStartDate().getTime());
            pDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        }


        prevLen.setText("Previous Cycle Length: "+String.valueOf(pCLen));
        prevDays.setText("Previous Period Length: "+String.valueOf(pDays+1));
        cycleVar.setText("Cycle Length Variation: "+String.valueOf(minLen)+"-"+String.valueOf(maxLen));
    }

    public void markPeriod(Date startDate, Date endDate){
        Calendar calendar = getCalendarWithoutTime(startDate);
        Calendar endCalendar = getCalendarWithoutTime(endDate);

        Event ev1 = new Event(Color.parseColor("#FFCAD8"), startDate.getTime());
        compactCalendar.addEvent(ev1);
        calendar.add(Calendar.DATE, 1);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            Log.e("calender_date", String.valueOf(result.getDate()));
            Event ev2 = new Event(Color.parseColor("#FFCAD8"), result.getTime());
            compactCalendar.addEvent(ev2);
            calendar.add(Calendar.DATE, 1);
        }

        Event ev3 = new Event(Color.parseColor("#FFCAD8"), endDate.getTime());
        compactCalendar.addEvent(ev3);
    }

    private static Calendar getCalendarWithoutTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}