package com.example.fitnessplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.fitnessplanner.adapters.MealsAdapter;
import com.example.fitnessplanner.models.Meal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    //TODO: Progress bar stuff
    int water_progress = 0;
    int calorie_progress = 0;
    int workout_progress = 0;
    private ProgressBar water_progress_bar;
    private ProgressBar calorie_progress_bar;
    private ProgressBar workout_progress_bar;
    private Button button_increase;
    private Button button_decrease;
    private Button add_a_meal;
    private RecyclerView meals_display;
    private ArrayList<Meal> mealItems;

    FirebaseDatabase database;
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
        workout_progress_bar = view.findViewById(R.id.workout_progress_bar);

        button_decrease = view.findViewById(R.id.water_decrease_button);
        button_increase = view.findViewById(R.id.water_increase_button);

        add_a_meal = view.findViewById(R.id.button_add_meal);
        meals_display = view.findViewById(R.id.meals_display);

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
            @Override
            public void onClick(View v) {
                if(water_progress <= 90){
                    water_progress += 10;
                    water_bar_update();
                }
            }
        });

        add_a_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddMealActivity.class));
            }
        });

        SharedPreferences mPref = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        String userName = mPref.getString("user", "pabloH");

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(userName);
        mealRef = userRef.child("Meals");
        calorie_bar_update();

        mealItems = new ArrayList<>();

        mealRef.orderByChild("Meals").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()){

                    Map<String, Object>map = (Map<String,Object>) ds.getValue();
                    Object descrip = map.get("description");
                    Object size = map.get("servingSize");
                    Object calories = map.get("totalCalories");
                    Object protein = map.get("protein");
                    Object carbs = map.get("carbs");
                    Object fat = map.get("fat");

                    String dValue = String.valueOf(descrip);
                    int sValue = Integer.parseInt(String.valueOf(size));
                    int tValue = Integer.parseInt(String.valueOf(calories));
                    int pValue = Integer.parseInt(String.valueOf(protein));
                    int cValue = Integer.parseInt(String.valueOf(carbs));
                    int fValue = Integer.parseInt(String.valueOf(fat));

                    System.out.println("des: " + dValue + " calories: " + cValue);

                    mealItems.add(new Meal(dValue, sValue, tValue, pValue, cValue, fValue));
                }

                setRecyclerView(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }

    private void setRecyclerView(View v){

        meals_display = v.findViewById(R.id.meals_display);
        meals_display.setLayoutManager(new LinearLayoutManager(getActivity()));
        MealsAdapter mealsAdapter = new MealsAdapter(getActivity(), mealItems);
        meals_display.setAdapter(mealsAdapter);
        meals_display.setItemAnimator(new DefaultItemAnimator());

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
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int total_sum = 0;
                for(DataSnapshot ds : snapshot.getChildren()){

                    Map<String, Object> map = (Map<String,Object>) ds.getValue();
                    Object calories = map.get("totalCalories");
                    int tValue = Integer.parseInt(String.valueOf(calories));
                    total_sum += tValue;

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