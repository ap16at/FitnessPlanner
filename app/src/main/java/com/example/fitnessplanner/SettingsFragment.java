package com.example.fitnessplanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessplanner.models.WeightLog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;


public class SettingsFragment extends Fragment {

    TextView nameField;
    ListView settingList;
    ImageView image;
    TextView current;
    TextView goal;


    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference weightRef;

    SharedPreferences mPref;
    SharedPreferences.Editor editor;

    AdapterView.OnItemClickListener listListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position)
            {

                case 0:     //profile
                    //change name, change profile pic
                    ProfileSetting profileSetting = new ProfileSetting();
                    profileSetting.show(getActivity().getSupportFragmentManager(),"profileSetting");
                    break;
                case 1:     //notification
                    NotificationDialog notificationDialog = new NotificationDialog();
                    notificationDialog.show(getActivity().getSupportFragmentManager(),"notifications");
                    break;
                case 2:     //goals
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Change Goal")
                            .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    goal.setText(mPref.getString("goal", "Lose Weight"));
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .setSingleChoiceItems(R.array.goals, 1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] goalAry = getResources().getStringArray(R.array.goals);
                                    editor.putString("goal", goalAry[which]);
                                    editor.commit();

                                }
                            }).create().show();
                    //change goal
                    break;
                case 3:     //units
                    String[] str_array = getResources().getStringArray(R.array.units);
                    int choice;
                    if(mPref.getString("units","LBS").equals("LBS"))
                        choice = 0;
                    else
                        choice =1;
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Units")
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Confirm",null)
                            .setCancelable(false)
                            .setSingleChoiceItems(R.array.units, choice, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(which == 0) {
                                                editor.putString("units", "LBS");
                                                editor.commit();
                                            }
                                            else {
                                                editor.putString("units", "KG");
                                                editor.commit();
                                            }

                                            Toast.makeText(getContext(),  str_array[which] + " units selected", Toast.LENGTH_LONG).show();
                                            //store in the app
                                        }
                                    }).create().show();
                                    //change unit
                    break;
                case 4:     //About Section
                    //information about the app and developer
                    new AlertDialog.Builder(getActivity())
                            .setTitle("About")
                            .setMessage("Fitness Planner is a nutrition and workout planning application catered towards people who are trying to stay fit or trying to get fit. " +
                                    "It is designed to help users reach their fitness goals by providing structure in their routine. " +
                                    "Fitness Planner will keep users on track to reach their greatest potential.")
                            .setNegativeButton("Return", null)
                            .setCancelable(false)
                            .create().show();
                    break;
            }
        }
    };

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment =  inflater.inflate(R.layout.fragment_settings, container, false);
        mPref = getContext().getSharedPreferences("prefs",Context.MODE_PRIVATE);
        editor = mPref.edit();

        String userName = mPref.getString("user", "fluffy");

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(userName);
        weightRef = userRef.child("WeightLog");



        weightRef.orderByChild("WeightLog").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<WeightLog> weights = new ArrayList<WeightLog>();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object date = map.get("date");
                    Object weight = map.get("weight");
                    Object units = map.get("units");

                    String dValue = String.valueOf(date);
                    double wValue = Double.parseDouble(String.valueOf(weight));
                    String uValue = String.valueOf(units);

                    weights.add(new WeightLog(dValue, wValue, uValue));


                }

                Collections.sort(weights, new Comparator<WeightLog>() {
                    public int compare(WeightLog w1, WeightLog w2){
                        Date date1 = new Date();
                        Date date2 = new Date();
                        try {
                            date1 = new SimpleDateFormat("MM-dd-yyyy").parse(w1.getDate());
                            date2 = new SimpleDateFormat("MM-dd-yyyy").parse(w2.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return date1.compareTo(date2);
                    }
                });

                current.setText(weights.get(weights.size()-1).getWeight() + " " + weights.get(weights.size()-1).getUnits());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        nameField = fragment.findViewById(R.id.profileName);
        settingList = fragment.findViewById(R.id.SettingsList);
        image = fragment.findViewById(R.id.imageView);
        current = fragment.findViewById(R.id.currentField);
        goal = fragment.findViewById(R.id.goalsField);

        int imageResource = getResources().getIdentifier("@drawable/avatar", null, getActivity().getPackageName());
        switch(mPref.getInt("avatar",0)) {
            case 0:
                break;
            case 1:
                imageResource = getResources().getIdentifier("@drawable/male", null, getActivity().getPackageName());
                break;
            case 2:
                imageResource = getResources().getIdentifier("@drawable/female", null, getActivity().getPackageName());
                break;

        }
        image.setImageResource(imageResource);
        image.getLayoutParams().width = 200;
        image.getLayoutParams().height = 200;


        nameField.setText(mPref.getString("fullname", "N/A"));
        goal.setText(mPref.getString("goal", "Lose Weight"));


        settingList.setOnItemClickListener(listListener);
        return fragment;
    }
}