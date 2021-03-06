package com.example.fitnessplanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.DialogFragment;

import com.example.fitnessplanner.models.WeightLog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class AddWeight extends DialogFragment{

    ImageFilterButton datePicker;
    EditText weight;
    EditText date;
    TextView units;
    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference weightRef;

    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date.setText(month+1 + "-" + dayOfMonth + "-" + year);
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        SharedPreferences pref = getActivity().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.weight_add, null);

        datePicker = view.findViewById(R.id.imageFilterButton);
        weight = view.findViewById(R.id.weightInput);
        date = view.findViewById(R.id.dateInput);
        units = view.findViewById(R.id.units);

        String unit = pref.getString("units", "LBS");

        units.setText(unit);

        SharedPreferences mPref = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        String userName = mPref.getString("user", "fluffy");

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(userName);
        weightRef = userRef.child("WeightLog");

        builder.setView(view)
                .setTitle("Add Weight")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //TODO: FIREBASE LOGIC FOR WEIGHT_LOGS
                        String weightStr = weight.getText().toString();
                        String dateStr = date.getText().toString();
                        String units = "lbs";

                        if(weightStr.isEmpty() || dateStr.isEmpty()){
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Weight was not added.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            weightRef.orderByChild("WeightLog").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    WeightLog newWeightLog = new WeightLog(dateStr, Double.parseDouble(weightStr), units);
                                    weightRef.child(dateStr).setValue(newWeightLog);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });



        datePicker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        dateListener,
                        year, month, day);
                dialog.show();
            }
        });

        return builder.create();
    }

}
