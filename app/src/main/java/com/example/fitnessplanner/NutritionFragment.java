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
import android.widget.ProgressBar;
import android.widget.TextView;

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

    ProgressBar protein_bar;
    int protein_progress;
    TextView protein_bar_text;
    ProgressBar carb_bar;
    int carb_progress;
    TextView carb_bar_text;
    ProgressBar fat_bar;
    int fat_progress;
    TextView fat_bar_text;
    ProgressBar total_cal_bar;
    int total_progress;
    TextView total_bar_text;

    private Button add_a_meal;

    private RecyclerView meals_display;
    private ArrayList<Meal> mealItems;

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
//                setWorkoutList();

            }
        });

        protein_bar = v.findViewById(R.id.protein_progress_bar);
        carb_bar = v.findViewById(R.id.carb_progress_bar);
        fat_bar = v.findViewById(R.id.fat_progress_bar);
        total_cal_bar = v.findViewById(R.id.full_calorie_progress_bar);

        protein_bar_text = v.findViewById(R.id.protein_progress_text);
        carb_bar_text = v.findViewById(R.id.carb_progress_text);
        fat_bar_text = v.findViewById(R.id.fat_progress_text);
        total_bar_text = v.findViewById(R.id.full_calorie_progress_text);

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
        updateProgressBars();

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

    private void updateProgressBars(){

        //TODO: Allow user to input calorie goals to replace these temporary ones:

        int totCalTemp = 2200;
        int proTemp = 220;
        int carbTemp = 193;
        int fatTemp = 61;

        mealRef.orderByChild("Meals").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int total_sum = 0;
                int protein_sum = 0;
                int carb_sum = 0;
                int fat_sum = 0;
                for(DataSnapshot ds : snapshot.getChildren()){

                    Map<String, Object>map = (Map<String,Object>) ds.getValue();

                    Object date = map.get("date");
                    String dateStr = String.valueOf(date);

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    String dateCurr = dtf.format(now);

                    if(dateStr.equals(dateCurr)){

                        Object calories = map.get("totalCalories");
                        Object protein = map.get("protein");
                        Object carbs = map.get("carbs");
                        Object fat = map.get("fat");
                        int tValue = Integer.parseInt(String.valueOf(calories));
                        int pValue = Integer.parseInt(String.valueOf(protein));
                        int cValue = Integer.parseInt(String.valueOf(carbs));
                        int fValue = Integer.parseInt(String.valueOf(fat));
                        total_sum += tValue;
                        protein_sum += pValue;
                        carb_sum += cValue;
                        fat_sum += fValue;

                    }

                }
                total_progress = (total_sum*100)/totCalTemp;
                protein_progress = (protein_sum*100)/proTemp;
                carb_progress = (carb_sum*100)/carbTemp;
                fat_progress = (fat_sum*100)/fatTemp;

                if(total_progress > 100)
                    total_progress = 100;
                if(protein_progress > 100)
                    protein_progress = 100;
                if(carb_progress > 100)
                    carb_progress = 100;
                if(fat_progress > 100)
                    fat_progress = 100;

                total_bar_text.setText(String.valueOf(total_sum));
                protein_bar_text.setText(String.valueOf(protein_sum));
                carb_bar_text.setText(String.valueOf(carb_sum));
                fat_bar_text.setText(String.valueOf(fat_sum));

                protein_bar.setProgress(protein_progress);
                carb_bar.setProgress(carb_progress);
                fat_bar.setProgress(fat_progress);
                total_cal_bar.setProgress(total_progress);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}