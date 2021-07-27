package com.example.fitnessplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.fitnessplanner.adapters.MealsAdapter;
import com.example.fitnessplanner.adapters.WorkoutAdapter;
import com.example.fitnessplanner.models.Meal;
import com.example.fitnessplanner.models.WorkoutItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    //TODO: Progress bar stuff
    int water_progress = 0;
    int calorie_progress = 0;
    private ProgressBar water_progress_bar;
    private ProgressBar calorie_progress_bar;
    private Button button_increase;
    private Button button_decrease;
    private RecyclerView workouts_display;
    private ArrayList<WorkoutItem> workoutItems;

    FirebaseDatabase database;
    DatabaseReference workoutRef;
    DatabaseReference userRef;
    DatabaseReference mealRef;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        water_progress_bar = view.findViewById(R.id.water_progress_bar);
        calorie_progress_bar = view.findViewById(R.id.calorie_progress_bar);

        button_decrease = view.findViewById(R.id.water_decrease_button);
        button_increase = view.findViewById(R.id.water_increase_button);

        workouts_display = view.findViewById(R.id.workouts_display);

        water_bar_update();

        button_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(water_progress >= 10){
                    water_progress -= 10;
                    water_bar_update();
                }
            }
        });

        button_increase.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(water_progress <= 90){
                    water_progress += 10;
                    water_bar_update();
                }

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDateTime now = LocalDateTime.now();
                System.out.println(dtf.format(now));
                String date = dtf.format(now);
                String TAG = "DATE";
                Log.i(TAG, date);

            }
        });

        SharedPreferences mPref = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        String userName = mPref.getString("user", "fluffy");

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(userName);
        mealRef = userRef.child("Meals");
        workoutRef = database.getReference("Workouts");
        workoutItems = new ArrayList<>();

        workoutRef.orderByChild("Workouts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object type = map.get("WorkoutType");
                    Object info = map.get("WorkoutInfo");
                    Object id = map.get("YoutubeID");

                    String tValue = String.valueOf(type);
                    String iValue = String.valueOf(info);
                    String idString = String.valueOf(id);
                    String idValue = "<iframe width=\"100%\" height=\"100%\" src=\"" + String.format("https://www.youtube.com/embed/%s", idString + "\" frameborder=\"0\" allowfullscreen><iframe>");

                    workoutItems.add(new WorkoutItem(tValue, iValue, idValue));
                }

                setRecyclerView(view);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        calorie_bar_update();

        return view;

    }

    private void setRecyclerView(View v){

        workouts_display = v.findViewById(R.id.workouts_display);
        workouts_display.setLayoutManager(new LinearLayoutManager(getActivity()));
        WorkoutAdapter workoutAdapter = new WorkoutAdapter(getActivity(), workoutItems);
        workouts_display.setAdapter(workoutAdapter);
        workouts_display.setItemAnimator(new DefaultItemAnimator());

    }

    void water_bar_update(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                water_progress_bar.setProgress(water_progress);
            }
        }, 50);
    }

    private void calorie_bar_update(){

        //TODO: Allow user to input calorie goals to replace these temporary ones:
        int totCalTemp = 2200;

        mealRef.orderByChild("Meals").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int total_sum = 0;
                for(DataSnapshot ds : snapshot.getChildren()){

                    Map<String, Object> map = (Map<String,Object>) ds.getValue();

                    Object date = map.get("date");
                    String dateStr = String.valueOf(date);

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    String dateCurr = dtf.format(now);

                    if(dateStr.equals(dateCurr)){
                        Object calories = map.get("totalCalories");
                        int tValue = Integer.parseInt(String.valueOf(calories));
                        total_sum += tValue;
                    }

                }
                calorie_progress = (total_sum*100)/totCalTemp;

                if(calorie_progress > 100)
                    calorie_progress = 100;

                calorie_progress_bar.setProgress(calorie_progress);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}