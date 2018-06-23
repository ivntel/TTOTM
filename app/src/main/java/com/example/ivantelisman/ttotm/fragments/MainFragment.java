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
    private TextView dateTextView;
    private Button submitButton;
    private Button continueButton;
    private EditText cycleLength;
    private ImageView imageViewCalender;
    private String age;
    private User user;
    private Calendar myCalendar = Calendar.getInstance();
    private Calendar currentDay = Calendar.getInstance();
    CalanderFragment calanderFragment;
    private MainActivityViewModel mainActivityViewModel;
    DatePickerDialog.OnDateSetListener date;
    Date dateSelected = new Date();
    Date d = new Date();
    Date next = new Date();
    View view;
    private boolean hasBeenClicked = false;
    private int selectedDayOfTheYear;
    private String estimatedCycleStartDate;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        // Inflate the layout for this fragment
        dateTextView = view.findViewById(R.id.textViewSelectADate);
        imageViewCalender = view.findViewById(R.id.imageViewCalender);
        continueButton = view.findViewById(R.id.continueButton);
        cycleLength = view.findViewById(R.id.cycleLength);
        submitButton = view.findViewById(R.id.submit);

        calanderFragment = new CalanderFragment();
        // Note: Db references should not be in an activity.
        mDb = AppDatabase.getInMemoryDatabase(getContext());

        // Get a reference to the ViewModel for this screen.
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        subscribeUiUserDate();

        imageViewCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasBeenClicked = true;
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cycleLength.getText().toString().trim().isEmpty() || (Integer.valueOf(cycleLength.getText().toString().trim()) < 24 || Integer.valueOf(cycleLength.getText().toString().trim()) > 34)) {
                    cycleLength.setError("Enter a valid number between 24 and 34");
                }
                else if(!hasBeenClicked){
                    Toast.makeText(getContext(), "Select A Date From The Calender", Toast.LENGTH_LONG).show();
                }
                else if(dateSelected.getTime()>System.currentTimeMillis()){
                    Toast.makeText(getContext(), "Please Select A Past Date From The Calender", Toast.LENGTH_LONG).show();
                } else if ((myCalendar.get(Calendar.DAY_OF_YEAR) - (currentDay.get(Calendar.DAY_OF_YEAR))) < -23) {
                    Toast.makeText(getContext(), "That Date Is Out Of Possible Ranges!", Toast.LENGTH_LONG).show();
                } else{
                    myCalendar.setTime(dateSelected);
                    selectedDayOfTheYear = myCalendar.get(Calendar.DAY_OF_YEAR);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_YEAR, selectedDayOfTheYear + Integer.valueOf(cycleLength.getText().toString().trim()));
                    estimatedCycleStartDate = calendar.getTime().toString();
                    Log.d("onClick: ", estimatedCycleStartDate + " Selected: " + dateSelected.toString());

                    mainActivityViewModel.mDb.userModel().deleteAll();
                    user = new User();
                    user.id = "5";
                    user.cycleDuration = Integer.valueOf(cycleLength.getText().toString().trim());
                    user.name = "jo";
                    user.estimatedStartDate = estimatedCycleStartDate;
                    user.date = dateSelected.toString();
                    mDb.userModel().insertUser(user);
                    Log.d("submitDate: ", String.valueOf(myCalendar.get(Calendar.DAY_OF_YEAR) - (currentDay.get(Calendar.DAY_OF_YEAR))));
                    getFragmentManager().beginTransaction().replace(R.id.content, new CalanderFragment(), "calender").commit();
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content, new CalanderFragment(), "calender").commit();
            }
        });

        return view;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateSelected = myCalendar.getTime();

        submitButton.setVisibility(View.VISIBLE);
        cycleLength.setVisibility(View.VISIBLE);
        dateTextView.setText("DATE SELECTED:\n" + sdf.format(dateSelected));
    }

    private void subscribeUiUserDate() {
        mainActivityViewModel.getUsers().observe(this, new Observer<List<User>>() {
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            @Override
            public void onChanged(@NonNull final List<User> users) {
                try{
                    d = dateFormat.parse(users.get(0).date);
                    next = dateFormat.parse(users.get(0).estimatedStartDate);
                    dateTextView.setText("DATE THE LAST PERIOD BEGAN:\n" + /*users.get(0).date*/sdf.format(d) +"\n\nESTIMATED START DATE OF THE NEXT PERIOUD:\n" + sdf.format(next) + "\n\nIF THE LATEST PERIOD HAS COME CLICK THE CALENDER AND SELECT THE DATE IT BEGAN");

                    submitButton.setVisibility(View.GONE);
                    cycleLength.setVisibility(View.GONE);
                }
                catch (Exception e){
                    dateTextView.setText("CLICK THE CALENDER AND SELECT THE DATE THE LAST PERIOD BEGAN");
                    continueButton.setVisibility(View.GONE);
                }
            }
        });
    }

}

