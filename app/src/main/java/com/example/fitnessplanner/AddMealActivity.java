package com.example.fitnessplanner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessplanner.adapters.MealsAdapter;
import com.example.fitnessplanner.models.Meal;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static java.security.AccessController.getContext;

public class AddMealActivity extends AppCompatActivity {

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

    TextView description_label;
    TextView size_label;
    TextView calorie_label;
    TextView protein_label;
    TextView carbs_label;
    TextView fat_label;

    EditText description_field;
    EditText size_field;
    EditText calorie_field;
    EditText protein_field;
    EditText carbs_field;
    EditText fat_field;

    Button cancel_button;
    Button add_button;

    RecyclerView meals;
    private ArrayList<Meal> mealItems;
    ColorStateList defaultColor;

    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference mealRef;

    // sets textView colors to black
    private void setColorDefault(){
        description_label.setTextColor(defaultColor);
        size_label.setTextColor(defaultColor);
        calorie_label.setTextColor(defaultColor);
        protein_label.setTextColor(defaultColor);
        carbs_label.setTextColor(defaultColor);
        fat_label.setTextColor(defaultColor);
    }

    // sets textView color to red
    private void setColorRed(TextView tv){
        tv.setTextColor(Color.parseColor("#FF0000"));
    }

    private void reset(){
        description_field.setText("");
        size_field.setText("");
        calorie_field.setText("");
        protein_field.setText("");
        carbs_field.setText("");
        fat_field.setText("");
    }

    private final View.OnClickListener cancelListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            startActivity(new Intent(AddMealActivity.this, MainActivity.class));
        }
    };

    private final View.OnClickListener addListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            String des = description_field.getText().toString();
            int desLength = description_field.getText().length();
            int sizeLength = size_field.getText().length();
            int calLength = calorie_field.getText().length();
            int proteinLength = protein_field.getText().length();
            int carbsLength = carbs_field.getText().length();
            int fatLength = fat_field.getText().length();

            boolean addWorthy = true;

            int size = 0;
            int cal = 0;
            int protein = 0;
            int carbs = 0;
            int fat = 0;

            try{
                size = Integer.parseInt(String.valueOf(size_field.getText()));
            }catch(NumberFormatException ex){
                setColorRed(size_label);
            }

            try{
                cal = Integer.parseInt(String.valueOf(calorie_field.getText()));
            }catch(NumberFormatException ex){
                setColorRed(calorie_label);
            }

            try{
                protein = Integer.parseInt(String.valueOf(protein_field.getText()));
            }catch(NumberFormatException ex){
                setColorRed(protein_label);
            }

            try{
                carbs = Integer.parseInt(String.valueOf(carbs_field.getText()));
            }catch(NumberFormatException ex){
                setColorRed(carbs_label);
            }

            try{
                fat = Integer.parseInt(String.valueOf(fat_field.getText()));
            }catch(NumberFormatException ex){
                setColorRed(fat_label);
            }

            if(desLength == 0){
                setColorRed(description_label);
                addWorthy = false;
            }
            if(sizeLength <= 0){
                setColorRed(size_label);
                addWorthy = false;
            }
            if(calLength <= 0){
                setColorRed(calorie_label);
                addWorthy = false;
            }
            if(proteinLength <= 0){
                setColorRed(protein_label);
                addWorthy = false;
            }
            if(carbsLength <= 0){
                setColorRed(carbs_label);
                addWorthy = false;
            }
            if(fatLength <= 0){
                setColorRed(fat_label);
                addWorthy = false;
            }

            if(addWorthy){
                setColorDefault();
                int finalSize = size;
                int finalCal = cal;
                int finalProtein = protein;
                int finalCarbs = carbs;
                int finalFat = fat;
                mealRef.orderByChild("Meals").addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long count = snapshot.getChildrenCount();
                        String meal_num = String.valueOf(++count);

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        System.out.println(dtf.format(now));
                        String date = dtf.format(now);
                        String TAG = "DATE";
                        Log.i(TAG, date);

                        setColorDefault();
                        Meal newMeal = new Meal(des, finalSize, finalCal, finalProtein, finalCarbs, finalFat, date);
                        mealRef.child(meal_num).setValue(newMeal);

                        View.OnClickListener snackActionListener = new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                reset();
                            }
                        };

                        update_progress_bars();
                        Toast.makeText(AddMealActivity.this, "Meal Successfully Added!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else
            {
                Toast.makeText(AddMealActivity.this, "Error: Some fields were empty.", Toast.LENGTH_LONG).show();
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        protein_bar = findViewById(R.id.protein_progress_bar);
        carb_bar = findViewById(R.id.carb_progress_bar);
        fat_bar = findViewById(R.id.fat_progress_bar);
        total_cal_bar = findViewById(R.id.full_calorie_progress_bar);

        protein_bar_text = findViewById(R.id.protein_progress_text);
        carb_bar_text = findViewById(R.id.carb_progress_text);
        fat_bar_text = findViewById(R.id.fat_progress_text);
        total_bar_text = findViewById(R.id.full_calorie_progress_text);

        description_label = findViewById(R.id.descriptiontv);
        size_label = findViewById(R.id.sizetv);
        calorie_label = findViewById(R.id.calorietv);
        protein_label = findViewById(R.id.proteintv);
        carbs_label = findViewById(R.id.carbtv);
        fat_label = findViewById(R.id.fattv);
        description_field = findViewById(R.id.descriptionet);
        size_field = findViewById(R.id.sizeet);
        calorie_field = findViewById(R.id.calorieet);
        protein_field = findViewById(R.id.proteinet);
        carbs_field = findViewById(R.id.carbet);
        fat_field = findViewById(R.id.fatet);
        cancel_button = findViewById(R.id.meal_cancel);
        add_button = findViewById(R.id.meal_add);
        meals = findViewById(R.id.rv_meals);

        defaultColor = description_label.getTextColors();

        cancel_button.setOnClickListener(cancelListener);
        add_button.setOnClickListener(addListener);

        SharedPreferences mPref = getSharedPreferences("prefs", MODE_PRIVATE);
        String userName = mPref.getString("user", "pabloH");

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(userName);
        mealRef = userRef.child("Meals");

        update_progress_bars();

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

                    System.out.println("des: " + dValue + " CALORIES: " + cValue);

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    String dateCurr = dtf.format(now);

                    if(ddValue.equals(dateCurr)){
                        mealItems.add(new Meal(dValue, sValue, tValue, pValue, cValue, fValue, ddValue));
                    }

                }

                setRecyclerView(findViewById(android.R.id.content));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setRecyclerView(View v){

        meals = v.findViewById(R.id.rv_meals);
        meals.setLayoutManager(new LinearLayoutManager(this));
        MealsAdapter mealsAdapter = new MealsAdapter(this, mealItems);
        meals.setAdapter(mealsAdapter);
        meals.setItemAnimator(new DefaultItemAnimator());

    }

    private void update_progress_bars(){

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