package com.example.fitnessplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class NotificationDialog extends DialogFragment {

    Switch notification;
    TextView notificationChanged;
    Button breakfast;
    Button lunch;
    Button dinner;
    Button weight;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        SharedPreferences mPref = getActivity().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_dialog, null);
        builder.setView(view);

        boolean remind = mPref.getBoolean("reminders",true);



        notification = view.findViewById(R.id.notificationSwitch);
        notificationChanged = view.findViewById(R.id.notificationText);
        breakfast = view.findViewById(R.id.breakfast);
        lunch= view.findViewById(R.id.lunch);
        dinner = view.findViewById(R.id.dinner);
        weight = view.findViewById(R.id.weight);

        if(remind) {
            notification.setChecked(true);
            notificationChanged.setText("Notifications are turned on");
        }
        else {
            notification.setChecked(false);
            notificationChanged.setText("Notifications are turned off");
        }



        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("reminders", isChecked);
                editor.commit();
                if(isChecked)
                    notificationChanged.setText("Notifications are turned on");
                else
                    notificationChanged.setText("Notifications are turned off");
            }
        });

        int mHour = 0;
        int mMinute = 0;

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int mHour = hourOfDay;
                        int mMinute = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,mHour, mMinute);
                        editor.putInt("breakfasthour", mHour);
                        editor.putInt("breakfastminute", mMinute);
                        editor.commit();

                    }
                },12,0,false);
                timePickerDialog.updateTime(mHour,mMinute);
                timePickerDialog.show();

            }
        });



        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int mHour = hourOfDay;
                        int mMinute = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,mHour, mMinute);
                        editor.putInt("lunchhour", mHour);
                        editor.putInt("lunchminute", mMinute);
                        editor.commit();
                    }
                },12,0,false);
                timePickerDialog.updateTime(mHour,mMinute);
                timePickerDialog.show();
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int mHour = hourOfDay;
                        int mMinute = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,mHour, mMinute);
                        editor.putInt("dinnerhour", mHour);
                        editor.putInt("dinnerminute", mMinute);
                        editor.commit();
                    }
                },12,0,false);
                timePickerDialog.updateTime(mHour,mMinute);
                timePickerDialog.show();
            }
        });

        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int mHour = hourOfDay;
                        int mMinute = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,mHour, mMinute);
                        editor.putInt("weighthour", mHour);
                        editor.putInt("weightminute", mMinute);
                        editor.commit();
                    }
                },12,0,false);
                timePickerDialog.updateTime(mHour,mMinute);
                timePickerDialog.show();
            }
        });



        return builder.create();
    }
}
