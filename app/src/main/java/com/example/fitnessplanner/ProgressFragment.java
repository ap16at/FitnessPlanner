package com.example.fitnessplanner;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fitnessplanner.adapters.WeightLogAdapter;
import com.example.fitnessplanner.models.WeightLog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment {

    LineGraphSeries<DataPoint> series;


    FloatingActionButton addButton;
    RecyclerView weightLog;
    GraphView progressGraph;
    WeightLogAdapter weightLogAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressFragment newInstance(String param1, String param2) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        addButton.setOnClickListener(fabListener);

        weightLog.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<WeightLog> weights = new ArrayList<>();

        //Tests
        Date date = new Date(2021, 0,1);
        weights.add(new WeightLog(date, 150.0, "lbs"));
        Date date1 = new Date(2021,0,2);
        weights.add(new WeightLog(date1, 151.0, "lbs"));
        Date date2 = new Date(2021,0,3);
        weights.add(new WeightLog(date2, 152.0, "lbs"));
        Date date3 = new Date(2021,0,5);
        weights.add(new WeightLog(date3, 153.0, "lbs"));
        //Test Dates End

        Toast.makeText(getContext(), date.toString(),Toast.LENGTH_LONG).show();

        weightLogAdapter = new WeightLogAdapter(getContext(),weights);

        weightLog.setAdapter(weightLogAdapter);

        //test graph
        series = new LineGraphSeries<DataPoint>();

        //Test Graph
        for(int i=0;i<4;i++)
        {
            WeightLog weight = weights.get(i);
            System.out.println(weight.getDate());
            series.appendData(new DataPoint(weight.getDate(),weight.getWeight()), true, 4);
        }
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

    private void setProgressGraph(View v)
    {

    }

    private void setLog(View v)
    {
        //retrieve last 5 data
    }

    View.OnClickListener fabListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            AddWeight addWeight = new AddWeight();
            addWeight.show(getActivity().getSupportFragmentManager(), "add weight");
        }
    };

}