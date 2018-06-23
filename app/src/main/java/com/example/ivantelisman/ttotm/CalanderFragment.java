package com.example.ivantelisman.ttotm;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivantelisman.ttotm.db.AppDatabase;
import com.example.ivantelisman.ttotm.db.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.ivantelisman.ttotm.MainActivity.diffInDays;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalanderFragment extends Fragment {
    public CalendarView calendarView;
    public boolean inFragment = false;
    TextView textViewInfo;
    Date selectedDate = new Date();
    Date estimatedDate = new Date();
    String estimatedDateText, selectedDateText;
    Calendar calendarEstimatedtDate = Calendar.getInstance();
    Calendar calendarSelectedDate = Calendar.getInstance();
    Calendar calendarCurrentDate = Calendar.getInstance();
    private MainActivityViewModel mainActivityViewModel;

    public CalanderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calander, container, false);
        calendarView = (CalendarView) view.findViewById(R.id.calender);
        textViewInfo = (TextView) view.findViewById(R.id.info);
        // Get a reference to the ViewModel for this screen.
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        subscribeUiUsersDates();
        inFragment = true;
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                getFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                calendarView.setDate(selectedDate.getTime());
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

    private void showDateInUi(final @NonNull List<User> users){
        selectedDateText = users.get(0).date;
        estimatedDateText = users.get(0).estimatedStartDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            selectedDate = dateFormat.parse(selectedDateText);
            estimatedDate = dateFormat.parse(estimatedDateText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        calendarView.setDate(estimatedDate.getTime());
        calendarSelectedDate.setTime(selectedDate);
        
        calendarEstimatedtDate.setTime(estimatedDate);
        diffInDays = calendarSelectedDate.get(Calendar.DAY_OF_YEAR) - calendarEstimatedtDate.get(Calendar.DAY_OF_YEAR);
        Log.d("showDateInUiD: ", selectedDate.toString() + " - " + estimatedDate.toString() + " = " + String.valueOf(diffInDays));
        String oldString = String.valueOf(diffInDays);
        String newString = oldString.replaceAll("-", "");
        textViewInfo.setText(newString + " days until the next period");
        if(diffInDays == -1){
            textViewInfo.setText(newString + " day until the next period");
        }
        else if(diffInDays==0){
            textViewInfo.setText("The period began today");
        }
        else if(diffInDays>0){
            textViewInfo.setText("Go to the previous screen and select the day that the period began");
        }
    }

}
