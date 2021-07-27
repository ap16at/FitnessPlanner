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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import java.util.Calendar;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class NutritionFragment extends Fragment {

    HorizontalCalendar horizontalCalendar;
    private RecyclerView meals_display;
    private ArrayList<Meal> mealItems;

    private Button add_a_meal;

    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference mealRef;

    public NutritionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nutrition, container, false);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, -1);

        horizontalCalendar = new HorizontalCalendar.Builder(v, R.id.calendarView).range(startDate, endDate).datesNumberOnScreen(5).build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

//                System.out.println("hello world");
                setWorkoutList();

            }
        });

        add_a_meal = v.findViewById(R.id.button_add_meal);

        add_a_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddMealActivity.class));
            }
        });

        meals_display = v.findViewById(R.id.meals_display);

        SharedPreferences mPref = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        String userName = mPref.getString("user", "fluffy");

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(userName);
        mealRef = userRef.child("Meals");

        mealItems = new ArrayList<>();

        mealRef.orderByChild("Meals").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                    Object date = map.get("date");

                    String dValue = String.valueOf(descrip);
                    int sValue = Integer.parseInt(String.valueOf(size));
                    int tValue = Integer.parseInt(String.valueOf(calories));
                    int pValue = Integer.parseInt(String.valueOf(protein));
                    int cValue = Integer.parseInt(String.valueOf(carbs));
                    int fValue = Integer.parseInt(String.valueOf(fat));
                    String ddValue = String.valueOf(date);

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    String dateCurr = dtf.format(now);

                    if(dateCurr.equals(ddValue)){
                        mealItems.add(new Meal(dValue, sValue, tValue, pValue, cValue, fValue, ddValue));
                    }

                }

                setRecyclerView(v);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }

    private void setRecyclerView(View v){
        meals_display = v.findViewById(R.id.meals_display);
        meals_display.setLayoutManager(new LinearLayoutManager(getActivity()));
        MealsAdapter mealsAdapter = new MealsAdapter(getActivity(), mealItems);
        meals_display.setAdapter(mealsAdapter);
        meals_display.setItemAnimator(new DefaultItemAnimator());
    }

    private void  setWorkoutList(){

    }

}