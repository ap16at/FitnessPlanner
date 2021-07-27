package com.example.fitnessplanner;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fitnessplanner.adapters.MealsAdapter;
import com.example.fitnessplanner.adapters.WeightLogAdapter;
import com.example.fitnessplanner.models.WeightLog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ProgressFragment extends Fragment {

    LineGraphSeries<DataPoint> series;


    FloatingActionButton addButton;
    RecyclerView weightLog;
    GraphView progressGraph;
    WeightLogAdapter weightLogAdapter;
    Spinner graphRange;
    ArrayList<WeightLog> weights;

    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference weightRef;

    public ProgressFragment() {
        // Required empty public constructor
    }

    public static ProgressFragment newInstance() {
        ProgressFragment fragment = new ProgressFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_progress, container, false);
        addButton = fragment.findViewById(R.id.floatingActionButton);
        weightLog = fragment.findViewById(R.id.recents);
        progressGraph = fragment.findViewById(R.id.progressGraph);
        graphRange = fragment.findViewById(R.id.range);

        //graphRange.setOnItemClickListener(rangeListener);
        addButton.setOnClickListener(fabListener);

        weightLog.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences mPref = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        String userName = mPref.getString("user", "fluffy");

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(userName);
        weightRef = userRef.child("WeightLog");

        weights = new ArrayList<>();

        weightRef.orderByChild("WeightLog").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){

                    Map<String, Object> map = (Map<String,Object>) ds.getValue();
                    Object date = map.get("date");
                    Object weight = map.get("weight");
                    Object units = map.get("units");

                    String dValue = String.valueOf(date);
                    double wValue = Double.parseDouble(String.valueOf(weight));
                    String uValue = String.valueOf(units);

                    weights.add(new WeightLog(dValue, wValue, uValue));

                    System.out.println(dValue + " " + wValue);

                }

                setRecyclerView(fragment);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //test graph
        series = new LineGraphSeries<DataPoint>();

        //Test Graph
//        for(int i=0;i<4;i++)
//        {
//            WeightLog weight = weights.get(i);
//            System.out.println(weight.getDate());
////            series.appendData(new DataPoint(weight.getDate(),weight.getWeight()), true, 4);
//        }
        //Test graph ends

        progressGraph.addSeries(series);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        progressGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX)
                {
                    return sdf.format(new Date((long)value));
                }
                else
                    return super.formatLabel(value, isValueX);
            }
        });


        return fragment;
    }

    private void displayGraph(int range)
    {

    }


    View.OnClickListener fabListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            AddWeight addWeight = new AddWeight();
            addWeight.show(getActivity().getSupportFragmentManager(), "add weight");
        }
    };

    AdapterView.OnItemClickListener rangeListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position)
            {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
        }
    };

    private void setRecyclerView(View v){

        ArrayList<WeightLog> weightsRev = new ArrayList<>();

        for(int i = weights.size() - 1; i >= 0; i--){
            weightsRev.add(weights.get(i));
        }

        weightLog = v.findViewById(R.id.recents);
        weightLog.setLayoutManager(new LinearLayoutManager(getActivity()));
        WeightLogAdapter weightLogAdapter = new WeightLogAdapter(getActivity(), weightsRev);
        weightLog.setAdapter(weightLogAdapter);
        weightLog.setItemAnimator(new DefaultItemAnimator());
    }

}