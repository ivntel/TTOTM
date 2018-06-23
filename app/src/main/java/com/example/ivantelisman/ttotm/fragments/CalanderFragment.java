package com.example.ivantelisman.ttotm.fragments;


import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.ivantelisman.ttotm.MainActivity;
import com.example.ivantelisman.ttotm.MainActivityViewModel;
import com.example.ivantelisman.ttotm.R;
import com.example.ivantelisman.ttotm.db.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalanderFragment extends Fragment {
    public CalendarView mCalendarView;
    public boolean mInFragment = false;
    TextView mTextViewInfo;
    TextView mTextViewDateInfo;
    Date mSelectedDate = new Date();
    Date mEstimatedDate = new Date();
    String mEstimatedDateText, mSelectedDateText;
    Calendar mCalendarEstimatedtDate = Calendar.getInstance();
    Calendar mCalendarSelectedDate = Calendar.getInstance();
    Calendar mCalendarCurrentDate = Calendar.getInstance();
    private MainActivityViewModel mainActivityViewModel;

    public CalanderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calander, container, false);
        mCalendarView = view.findViewById(R.id.calender);
        mTextViewInfo = view.findViewById(R.id.info);
        mTextViewDateInfo = view.findViewById(R.id.dateInfo);
        // Get a reference to the ViewModel for this screen.
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        subscribeUiUsersDates();
        mInFragment = true;
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                getFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                mCalendarView.setDate(mEstimatedDate.getTime());
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Select a new period start date?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void subscribeUiUsersDates() {
        mainActivityViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@NonNull final List<User> users) {
                showDateInUi(users);
            }
        });
    }

    private void showDateInUi(final @NonNull List<User> users) {
        mSelectedDateText = users.get(0).date;
        mEstimatedDateText = users.get(0).estimatedStartDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        try {
            mSelectedDate = dateFormat.parse(mSelectedDateText);
            mEstimatedDate = dateFormat.parse(mEstimatedDateText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mCalendarView.setDate(mEstimatedDate.getTime());
        mCalendarSelectedDate.setTime(mSelectedDate);

        mCalendarEstimatedtDate.setTime(mEstimatedDate);
        MainActivity.mDiffInDays = mCalendarCurrentDate.get(Calendar.DAY_OF_YEAR) /*calendarSelectedDate.get(Calendar.DAY_OF_YEAR)*/ - mCalendarEstimatedtDate.get(Calendar.DAY_OF_YEAR);
        Log.d("showDateInUiD: ", String.valueOf(mCalendarCurrentDate.get(Calendar.DAY_OF_YEAR)) + " - " + mEstimatedDate.toString() + " = " + String.valueOf(MainActivity.mDiffInDays));
        String oldString = String.valueOf(MainActivity.mDiffInDays);
        String newString = oldString.replaceAll("-", "");

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mTextViewDateInfo.setText("Last period was: " + sdf.format(mSelectedDate) + " Next one should be: " + sdf.format(mEstimatedDate));
        mTextViewInfo.setText(newString + " days until the next period!");
        if (MainActivity.mDiffInDays == -1) {
            mTextViewInfo.setText(newString + " day until the next period!");
        } else if (MainActivity.mDiffInDays == 0) {
            mTextViewInfo.setText("The period began today!");
        } else if (MainActivity.mDiffInDays > 0) {
            mTextViewInfo.setText("Go to the previous screen and select the day that the period began!");
        }
    }

}
