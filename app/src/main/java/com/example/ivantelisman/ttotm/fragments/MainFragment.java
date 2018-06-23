package com.example.ivantelisman.ttotm.fragments;


import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivantelisman.ttotm.MainActivityViewModel;
import com.example.ivantelisman.ttotm.R;
import com.example.ivantelisman.ttotm.db.AppDatabase;
import com.example.ivantelisman.ttotm.db.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private AppDatabase mDb;
    private TextView mDateTextView;
    private Button mSubmitButton;
    private Button mContinueButton;
    private EditText mCycleLength;
    private ImageView mImageViewCalender;
    private User user;
    private Calendar mMyCalendar = Calendar.getInstance();
    private Calendar mCurrentDay = Calendar.getInstance();
    CalanderFragment mCalanderFragment;
    private MainActivityViewModel mainActivityViewModel;
    DatePickerDialog.OnDateSetListener mDate;
    Date mDateSelected = new Date();
    Date mEstimatedDate = new Date();
    View mView;
    private boolean mHasBeenClicked = false;
    private int mSelectedDayOfTheYear;
    private String mEstimatedCycleStartDate;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        // Inflate the layout for this fragment
        mDateTextView = mView.findViewById(R.id.textViewSelectADate);
        mImageViewCalender = mView.findViewById(R.id.imageViewCalender);
        mContinueButton = mView.findViewById(R.id.continueButton);
        mCycleLength = mView.findViewById(R.id.cycleLength);
        mSubmitButton = mView.findViewById(R.id.submit);

        mCalanderFragment = new CalanderFragment();
        // Note: Db references should not be in an activity.
        mDb = AppDatabase.getInMemoryDatabase(getContext());

        // Get a reference to the ViewModel for this screen.
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                mMyCalendar.set(Calendar.YEAR, year);
                mMyCalendar.set(Calendar.MONTH, monthOfYear);
                mMyCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        subscribeUiUserDate();

        mImageViewCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHasBeenClicked = true;
                new DatePickerDialog(getContext(), mDate, mMyCalendar
                        .get(Calendar.YEAR), mMyCalendar.get(Calendar.MONTH),
                        mMyCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCycleLength.getText().toString().trim().isEmpty() || (Integer.valueOf(mCycleLength.getText().toString().trim()) < 24 || Integer.valueOf(mCycleLength.getText().toString().trim()) > 34)) {
                    mCycleLength.setError("Enter a valid number between 24 and 34");
                } else if (!mHasBeenClicked) {
                    Toast.makeText(getContext(), "Select A Date From The Calender", Toast.LENGTH_LONG).show();
                } else if (mDateSelected.getTime() > System.currentTimeMillis()) {
                    Toast.makeText(getContext(), "Please Select A Past Date From The Calender", Toast.LENGTH_LONG).show();
                } else if ((mMyCalendar.get(Calendar.DAY_OF_YEAR) - (mCurrentDay.get(Calendar.DAY_OF_YEAR))) < -23) {
                    Toast.makeText(getContext(), "That Date Is Out Of Possible Ranges!", Toast.LENGTH_LONG).show();
                } else{
                    mMyCalendar.setTime(mDateSelected);
                    mSelectedDayOfTheYear = mMyCalendar.get(Calendar.DAY_OF_YEAR);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_YEAR, mSelectedDayOfTheYear + Integer.valueOf(mCycleLength.getText().toString().trim()));
                    mEstimatedCycleStartDate = calendar.getTime().toString();
                    Log.d("onClick: ", mEstimatedCycleStartDate + " Selected: " + mDateSelected.toString());

                    mainActivityViewModel.mDb.userModel().deleteAll();
                    user = new User();
                    user.id = "5";
                    user.cycleDuration = Integer.valueOf(mCycleLength.getText().toString().trim());
                    user.name = "jo";
                    user.estimatedStartDate = mEstimatedCycleStartDate;
                    user.date = mDateSelected.toString();
                    mDb.userModel().insertUser(user);
                    Log.d("submitDate: ", String.valueOf(mMyCalendar.get(Calendar.DAY_OF_YEAR) - (mCurrentDay.get(Calendar.DAY_OF_YEAR))));
                    getFragmentManager().beginTransaction().replace(R.id.content, new CalanderFragment(), "calender").commit();
                }
            }
        });

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content, new CalanderFragment(), "calender").commit();
            }
        });

        return mView;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mDateSelected = mMyCalendar.getTime();
        //ui changes
        mSubmitButton.setVisibility(View.VISIBLE);
        mCycleLength.setVisibility(View.VISIBLE);
        mDateTextView.setText("DATE SELECTED:\n" + sdf.format(mDateSelected));
    }

    private void subscribeUiUserDate() {
        mainActivityViewModel.getUsers().observe(this, new Observer<List<User>>() {
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            @Override
            public void onChanged(@NonNull final List<User> users) {
                try{
                    mDateSelected = dateFormat.parse(users.get(0).date);
                    mEstimatedDate = dateFormat.parse(users.get(0).estimatedStartDate);
                    //ui changes
                    mDateTextView.setText("DATE THE LAST PERIOD BEGAN:\n" + /*users.get(0).date*/sdf.format(mDateSelected) + "\n\nESTIMATED START DATE OF THE NEXT PERIOUD:\n" + sdf.format(mEstimatedDate) + "\n\nIF THE LATEST PERIOD HAS COME CLICK THE CALENDER AND SELECT THE DATE IT BEGAN");
                    mSubmitButton.setVisibility(View.GONE);
                    mCycleLength.setVisibility(View.GONE);
                }
                catch (Exception e){
                    //ui changes
                    mDateTextView.setText("CLICK THE CALENDER AND SELECT THE DATE THE LAST PERIOD BEGAN");
                    mContinueButton.setVisibility(View.GONE);
                }
            }
        });
    }

}

