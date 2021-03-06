package com.example.ivantelisman.ttotm.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivantelisman.ttotm.MainActivityViewModel;
import com.example.ivantelisman.ttotm.PreferenceUtil;
import com.example.ivantelisman.ttotm.R;
import com.example.ivantelisman.ttotm.db.AppDatabase;
import com.example.ivantelisman.ttotm.db.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private AppDatabase mDb;
    private TextView mDateTextView;
    private Button mSubmitButton;
    private Button mContinueButton;
    private ImageView mImageViewCalender;
    private CardView mCardView;
    private Spinner mSpinnerCycleDays;
    private User user;
    private Calendar mDateSelectedCalendar = Calendar.getInstance();
    private Calendar mCurrentDayCalender = Calendar.getInstance();
    private Calendar mEstimatedDayCalender = Calendar.getInstance();
    private MainActivityViewModel mainActivityViewModel;
    private DatePickerDialog.OnDateSetListener mDate;
    private Date mDateSelected = new Date();
    private Date mEstimatedDate = new Date();
    private View mView;
    private boolean mHasBeenClicked = false;
    private int mSelectedDayOfTheYear, mDifferenceInDays, mCycleDays;
    private String mEstimatedCycleStartDate;
    private String cycleDay;
    private List<String> cycleDaysList = new ArrayList<>();

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_main, container, false);

        setUpUi();
        // Db references
        mDb = AppDatabase.getInMemoryDatabase(getContext());
        // Get a reference to the ViewModel for this screen.
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        setUpCycleDaysSpinner();

        mDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                mDateSelectedCalendar.set(Calendar.YEAR, year);
                mDateSelectedCalendar.set(Calendar.MONTH, monthOfYear);
                mDateSelectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        subscribeUiUserDate();

        return mView;
    }

    private void setUpUi() {
        mDateTextView = mView.findViewById(R.id.textViewSelectADate);
        mImageViewCalender = mView.findViewById(R.id.imageViewCalender);
        mContinueButton = mView.findViewById(R.id.continueButton);
        mSubmitButton = mView.findViewById(R.id.submit);
        mCardView = mView.findViewById(R.id.buttonsAndEditTextCard);
        mSpinnerCycleDays = mView.findViewById(R.id.spinnerCycleLength);

        mImageViewCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                mHasBeenClicked = true;
                                new DatePickerDialog(getContext(), mDate, mDateSelectedCalendar
                                        .get(Calendar.YEAR), mDateSelectedCalendar.get(Calendar.MONTH),
                                        mDateSelectedCalendar.get(Calendar.DAY_OF_MONTH)).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getString(R.string.select_new_date)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.no), dialogClickListener).show();

            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAndSubmitData();
            }
        });

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCalenderFragment();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mDateSelected = mDateSelectedCalendar.getTime();
        //ui changes
        mSubmitButton.setVisibility(View.VISIBLE);
        mSpinnerCycleDays.setVisibility(View.VISIBLE);
        mCardView.setVisibility(View.VISIBLE);
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
                    mDateTextView.setText(getString(R.string.date_last_period) + "\n" + sdf.format(mDateSelected) + "\n\n" + getString(R.string.estimates_start_date) + "\n" + sdf.format(mEstimatedDate) + "\n\n" + getString(R.string.last_has_come));
                    mSubmitButton.setVisibility(View.GONE);
                    mSpinnerCycleDays.setVisibility(View.GONE);
                    mCardView.setVisibility(View.GONE);
                } catch (Exception e){
                    //ui changes
                    mDateTextView.setText(getString(R.string.click_calender));
                    mContinueButton.setVisibility(View.GONE);
                    mCardView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setUpCycleDaysSpinner() {
        cycleDaysList.add("Cycle Length");
        cycleDaysList.add("24");
        cycleDaysList.add("25");
        cycleDaysList.add("26");
        cycleDaysList.add("27");
        cycleDaysList.add("28");
        cycleDaysList.add("29");
        cycleDaysList.add("30");
        cycleDaysList.add("31");
        cycleDaysList.add("32");
        cycleDaysList.add("33");
        cycleDaysList.add("34");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.day_spinner_item, cycleDaysList);
        mSpinnerCycleDays.setAdapter(arrayAdapter);
        mSpinnerCycleDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    cycleDay = "0";
                } else if (i == 1) {
                    cycleDay = "24";
                } else if (i == 2) {
                    cycleDay = "25";
                } else if (i == 3) {
                    cycleDay = "26";
                } else if (i == 4) {
                    cycleDay = "27";
                } else if (i == 5) {
                    cycleDay = "28";
                } else if (i == 6) {
                    cycleDay = "29";
                } else if (i == 7) {
                    cycleDay = "30";
                } else if (i == 8) {
                    cycleDay = "31";
                } else if (i == 9) {
                    cycleDay = "32";
                } else if (i == 10) {
                    cycleDay = "33";
                } else if (i == 11) {
                    cycleDay = "34";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cycleDay = "0";
            }
        });
    }

    private void saveNotificationInfo(int day0, int day1, int day2, int day3, int day4, int day14) {
        PreferenceUtil.getInstance(getContext()).saveNotificationDates(day0, day1, day2, day3, day4, day14);
    }

    private void saveDateDataInDB() {
        mainActivityViewModel.mDb.userModel().deleteAll();
        user = new User();
        user.id = "0";
        user.cycleDuration = Integer.valueOf(cycleDay);
        user.differenceInDays = mDifferenceInDays;
        user.estimatedStartDate = mEstimatedDate.toString();
        user.date = mDateSelected.toString();
        mDb.userModel().insertUser(user);
    }

    private void processAndSubmitData() {
        //set chosen cycle days value
        mCycleDays = Integer.valueOf(cycleDay);
        //set selected date as day of year
        mDateSelectedCalendar.setTime(mDateSelected);
        mSelectedDayOfTheYear = mDateSelectedCalendar.get(Calendar.DAY_OF_YEAR);
        //set estimated date as day of year
        mEstimatedDayCalender.setTime(mDateSelected);
        mEstimatedCycleStartDate = String.valueOf(mDateSelectedCalendar.get(Calendar.DAY_OF_YEAR) + mCycleDays);
        //convert mEstimatedCycleStartDate day of year to date format
        mEstimatedDayCalender.set(Calendar.DAY_OF_YEAR, Integer.valueOf(mEstimatedCycleStartDate));
        mEstimatedDate = mEstimatedDayCalender.getTime();
        //set difference of days as int
        mDifferenceInDays = mCurrentDayCalender.get(Calendar.DAY_OF_YEAR) - Integer.valueOf(mEstimatedCycleStartDate);
        //error cases for choosing cycle length
        if (cycleDay.equals("0")) {
            Toasty.error(getContext(), getString(R.string.choose_between), Toast.LENGTH_LONG, true).show();
        } else if (!mHasBeenClicked) {
            Toasty.error(getContext(), getString(R.string.select_date_from_calender), Toast.LENGTH_LONG, true).show();
        } else if (mDateSelected.getTime() > System.currentTimeMillis()) {
            Toasty.error(getContext(), getString(R.string.select_past_date_from_calender), Toast.LENGTH_LONG, true).show();
        } else if (mDifferenceInDays >= 0) {
            Toasty.error(getContext(), getString(R.string.date_out_of_range), Toast.LENGTH_LONG, true).show();
        } else {
            saveDateDataInDB();
            saveNotificationInfo(Integer.valueOf(mEstimatedCycleStartDate), Integer.valueOf(mEstimatedCycleStartDate) - 1, Integer.valueOf(mEstimatedCycleStartDate) - 2, Integer.valueOf(mEstimatedCycleStartDate) - 3, Integer.valueOf(mEstimatedCycleStartDate) - 4, Integer.valueOf(mEstimatedCycleStartDate) - 14);
            navigateToCalenderFragment();
        }
    }

    private void navigateToCalenderFragment() {
        getFragmentManager().beginTransaction().replace(R.id.content, new CalanderFragment(), "calender").commit();
    }
}
