package com.example.fitnessplanner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessplanner.R;
import com.example.fitnessplanner.models.WorkoutItem;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder>{

    Context context;
    ArrayList<WorkoutItem> workoutItems;

    public WorkoutAdapter(Context context, ArrayList<WorkoutItem> workoutItems){
        this.context = context;
        this.workoutItems = workoutItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapter.ViewHolder holder, int position) {
        String workout = workoutItems.get(position).getWorkoutType();
        String workoutInfo = workoutItems.get(position).getWorkoutInfo();

        holder.tvWorkoutType.setText(workout);
        holder.tvWorkoutInfo.setText(workoutInfo);
        holder.webView.loadData(workoutItems.get(position).getVideoID(), "text/html", "utf-8");
    }

    @Override
    public int getItemCount() {
        return workoutItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout workoutItem;
        TextView tvWorkoutType;
        TextView tvWorkoutInfo;
        WebView webView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutItem = itemView.findViewById(R.id.workoutItem);
            tvWorkoutType = itemView.findViewById(R.id.tvWorkoutType);
            tvWorkoutInfo = itemView.findViewById(R.id.tvWorkoutInfo);
            webView = itemView.findViewById(R.id.webView);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());



        }
    }

}
