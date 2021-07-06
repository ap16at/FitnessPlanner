package com.example.fitnessplanner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessplanner.R;
import com.example.fitnessplanner.models.Meal;

import java.util.ArrayList;
import java.util.Arrays;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHolder>{
    Context context;
    private ArrayList<Meal> mealItems;

    public MealsAdapter(Context context, ArrayList<Meal> mealItems){
        this.context = context;
        this.mealItems = mealItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String des = mealItems.get(position).getDescription();
        holder.meal_name.setText(des);
        int cal = mealItems.get(position).getTotalCalories();
        String calories = cal + "calories";
        holder.total_cal.setText(calories);
        int pro = mealItems.get(position).getProtein();
        String protein = "Protein: " + pro + "g";
        holder.protein.setText(protein);
        int car = mealItems.get(position).getCarbs();
        String carbs = "Carbs: " + car + "g";
        holder.carb.setText(carbs);
        int fat = mealItems.get(position).getFat();
        String fats = "Fats: " + fat + "g";
        holder.fat.setText(fats);

        System.out.println("des: " + des + "cal: " + cal);

    }

    @Override
    public int getItemCount() {
        return mealItems.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView meal_name;
        TextView total_cal;
        TextView protein;
        TextView carb;
        TextView fat;
        ConstraintLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            meal_name = itemView.findViewById(R.id.meal_name);
            total_cal = itemView.findViewById(R.id.total_cal);
            protein = itemView.findViewById(R.id.protein);
            carb = itemView.findViewById(R.id.carb);
            fat = itemView.findViewById(R.id.fat);
            item = itemView.findViewById(R.id.meal_item);
        }
    }
}

