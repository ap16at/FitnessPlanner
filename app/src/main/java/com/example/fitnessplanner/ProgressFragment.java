package com.example.fitnessplanner;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
    ArrayList<WeightLog> allData;
    SharedPreferences mPref;

    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference weightRef;
    boolean week = false;

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
        graphRange.post(new Runnable() {
            @Override
            public void run() {
                graphRange.setOnItemSelectedListener(graphListener);
            }
        });

        allData = new ArrayList<WeightLog>();

        //graphRange.setOnItemClickListener(rangeListener);
        addButton.setOnClickListener(fabListener);

        weightLog.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPref = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        String userName = mPref.getString("user", "fluffy");

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(userName);
        weightRef = userRef.child("WeightLog");

        weights = new ArrayList<>();

        weightRef.orderByChild("WeightLog").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object date = map.get("date");
                    Object weight = map.get("weight");
                    Object units = map.get("units");

                    String dValue = String.valueOf(date);
                    double wValue = Double.parseDouble(String.valueOf(weight));
                    String uValue = String.valueOf(units);

                    addToArray(new WeightLog(dValue, wValue, uValue));

                    System.out.println(dValue + " " + wValue);

                }

                series = new LineGraphSeries<>();

                System.out.println("ArraySizeOriginal: " + weights.size());

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

                System.out.println("Original Size: " + weights.size());

                if (graphRange.getSelectedItemPosition() == 0){
                    int count = 7;

                    if(weights.size() < 7)
                        count = weights.size();
                    for(int i=weights.size()-count;i<weights.size();i++) {
                        WeightLog weight = weights.get(i);
                        try {
                            Date date = new SimpleDateFormat("MM-dd-yyyy").parse(weight.getDate());
                            double theWeight = weight.getWeight();
                            if (mPref.getString("units", "LBS").equalsIgnoreCase("KG"))
                                theWeight = theWeight / 2.205;
                            series.appendData(new DataPoint(date, theWeight), true, 7);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                }

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

                setRecyclerView(fragment);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return fragment;
    }

    private void addToArray(WeightLog weight)
    {
        weights.add(weight);
    }


    View.OnClickListener fabListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            AddWeight addWeight = new AddWeight();
            addWeight.show(getActivity().getSupportFragmentManager(), "add weight");

        }
    };

    AdapterView.OnItemSelectedListener graphListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position == 1 || position == 2)
            {
                progressGraph.removeAllSeries();
                weights.clear();

                //data
                weightRef.orderByChild("WeightLog").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object date = map.get("date");
                            Object weight = map.get("weight");
                            Object units = map.get("units");

                            String dValue = String.valueOf(date);
                            double wValue = Double.parseDouble(String.valueOf(weight));
                            String uValue = String.valueOf(units);

                            addToArray(new WeightLog(dValue, wValue, uValue));

                            System.out.println(dValue + " " + wValue);

                        }

                        series = new LineGraphSeries<>();

                        System.out.println("ArraySizeOriginal: " + weights.size());

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


                        //Graph
                        series = new LineGraphSeries<DataPoint>();

                        switch(position)
                        {
                            case 1:
                                int count = 31;

                                if(weights.size() < 31)
                                    count = weights.size();
                                for(int i=weights.size()-count;i<weights.size();i++) {
                                    WeightLog weight = weights.get(i);
                                    try {
                                        Date date = new SimpleDateFormat("MM-dd-yyyy").parse(weight.getDate());
                                        double theWeight = weight.getWeight();
                                        if (mPref.getString("units", "LBS").equalsIgnoreCase("KG"))
                                            theWeight = theWeight / 2.205;
                                        System.out.println("My Date: " + date);
                                        series.appendData(new DataPoint(date, theWeight), true, 100);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case 2:
                                count = 365;

                                if(weights.size() < 365)
                                    count = weights.size();
                                //for(int i=weights.size()-count;i<weights.size();i++) {
                                for(int i=0;i<weights.size();i++){
                                    WeightLog weight = weights.get(i);
                                    try {
                                        Date date = new SimpleDateFormat("MM-dd-yyyy").parse(weight.getDate());
                                        double theWeight = weight.getWeight();
                                        if (mPref.getString("units", "LBS").equalsIgnoreCase("KG"))
                                            theWeight = theWeight / 2.205;
                                        series.appendData(new DataPoint(date, theWeight), true, 366);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                        }


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
                        //end graph



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //end data



            }
            else
            {

                    weights.clear();
                    progressGraph.removeAllSeries();
                    weightRef.orderByChild("WeightLog").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {

                                Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                Object date = map.get("date");
                                Object weight = map.get("weight");
                                Object units = map.get("units");

                                String dValue = String.valueOf(date);
                                double wValue = Double.parseDouble(String.valueOf(weight));
                                String uValue = String.valueOf(units);

                                addToArray(new WeightLog(dValue, wValue, uValue));

                                System.out.println(dValue + " " + wValue);

                            }

                            series = new LineGraphSeries<>();

                            System.out.println("ArraySizeOriginal: " + weights.size());

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

                            System.out.println("Original Size: " + weights.size());

                            if (graphRange.getSelectedItemPosition() == 0){
                                int count = 7;

                                if(weights.size() < 7)
                                    count = weights.size();
                                for(int i=weights.size()-count;i<weights.size();i++) {
                                    WeightLog weight = weights.get(i);
                                    try {
                                        Date date = new SimpleDateFormat("MM-dd-yyyy").parse(weight.getDate());
                                        double theWeight = weight.getWeight();
                                        if (mPref.getString("units", "LBS").equalsIgnoreCase("KG"))
                                            theWeight = theWeight / 2.205;
                                        series.appendData(new DataPoint(date, theWeight), true, 7);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

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



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    week = true;

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private void setRecyclerView(View v){

        ArrayList<WeightLog> weightsRev = new ArrayList<>();


        for(int i = weights.size() - 1; i >= weights.size()-5; i--){
            weightsRev.add(weights.get(i));
        }

        weightLog = v.findViewById(R.id.recents);
        weightLog.setLayoutManager(new LinearLayoutManager(getActivity()));
        WeightLogAdapter weightLogAdapter = new WeightLogAdapter(getActivity(), weightsRev);
        weightLog.setAdapter(weightLogAdapter);
        weightLog.setItemAnimator(new DefaultItemAnimator());
    }

}