package com.example.ivantelisman.ttotm.fragments;


import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

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
    private int mDiffInDays;
    private TextView mTextViewInfo;
    private TextView mTextViewDateInfo;
    private Date mSelectedDate = new Date();
    private Date mEstimatedDate = new Date();
    private String mEstimatedDateText, mSelectedDateText;
    private Calendar mCalendarEstimatedtDate = Calendar.getInstance();
    private Calendar mCalendarSelectedDate = Calendar.getInstance();
    private Calendar mCalendarCurrentDate = Calendar.getInstance();
    private MainActivityViewModel mainActivityViewModel;

    public CalanderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calander, container, false);

        // Get a reference to the ViewModel for this screen.
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setUpUi(view);

        return view;
    }

    private void setUpUi(View view) {
        mCalendarView = view.findViewById(R.id.calender);
        mTextViewInfo = view.findViewById(R.id.info);
        mTextViewDateInfo = view.findViewById(R.id.dateInfo);
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
    }

    private void subscribeUiUsersDates() {
        mainActivityViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@NonNull final List<User> users) {
                submitDateInfoListAndShowDateInUi(users);
            }
        });
    }

    private void submitDateInfoListAndShowDateInUi(final @NonNull List<User> users) {
        formatDateInfoToBeShownInUi(users);
        mCalendarView.setDate(mEstimatedDate.getTime());
        formatDateIntoTextInfoAndShow();
    }

    private void formatDateInfoToBeShownInUi(List<User> users) {
        mSelectedDateText = users.get(0).date;
        mEstimatedDateText = users.get(0).estimatedStartDate;
        mDiffInDays = users.get(0).differenceInDays;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        try {
            mSelectedDate = dateFormat.parse(mSelectedDateText);
            mEstimatedDate = dateFormat.parse(mEstimatedDateText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void formatDateIntoTextInfoAndShow() {
        mCalendarSelectedDate.setTime(mSelectedDate);
        mCalendarEstimatedtDate.setTime(mEstimatedDate);
        mDiffInDays = mCalendarCurrentDate.get(Calendar.DAY_OF_YEAR) - mCalendarEstimatedtDate.get(Calendar.DAY_OF_YEAR);
        String oldString = String.valueOf(mDiffInDays);
        String newString = oldString.replaceAll("-", "");

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mTextViewDateInfo.setText("Last period was: " + sdf.format(mSelectedDate) + " Next one should be: " + sdf.format(mEstimatedDate));
        mTextViewInfo.setText(newString + " days until the next period!");
        if (mDiffInDays == -1) {
            mTextViewInfo.setText(newString + " day until the next period!");
        } else if (mDiffInDays == 0) {
            mTextViewDateInfo.setText("Last period was: " + sdf.format(mSelectedDate) + " Next one should be today: " + sdf.format(mEstimatedDate));
            mTextViewInfo.setText("The period began today!");
        } else if (mDiffInDays > 0) {
            mTextViewDateInfo.setText("Last period was: " + sdf.format(mSelectedDate) + " Next one has passed and should have been: " + sdf.format(mEstimatedDate));
            mTextViewInfo.setText("Go to the previous screen and select the day that the period began!");
        }
    }

}
