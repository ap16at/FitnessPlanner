package com.example.fitnessplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnessplanner.adapters.WorkoutAdapter;
import com.example.fitnessplanner.models.WorkoutItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class WorkoutFragment extends Fragment {

    HorizontalCalendar horizontalCalendar;
    private RecyclerView rvWorkout;
    private ArrayList<WorkoutItem> workoutItems;

    FirebaseDatabase database;
    DatabaseReference workoutRef;

    public WorkoutFragment() {
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
        View v = inflater.inflate(R.layout.fragment_workout, container, false);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, -1);

        horizontalCalendar = new HorizontalCalendar.Builder(v, R.id.calendarView).range(startDate, endDate).datesNumberOnScreen(5).build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                System.out.println("hello world");
                setWorkoutList();

            }
        });

        rvWorkout = v.findViewById(R.id.workouts_display);

        database = FirebaseDatabase.getInstance();
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

                setRecyclerView(v);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }

    private void setRecyclerView(View v){
        rvWorkout = v.findViewById(R.id.workouts_display);
        rvWorkout.setLayoutManager(new LinearLayoutManager(getActivity()));
        WorkoutAdapter workoutAdapter = new WorkoutAdapter(getActivity(), workoutItems);
        rvWorkout.setAdapter(workoutAdapter);
        rvWorkout.setItemAnimator(new DefaultItemAnimator());
    }

    private void  setWorkoutList(){

    }
}