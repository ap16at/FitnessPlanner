package com.example.fitnessplanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {


    TextView nameField;
    ListView settingList;
    ImageView image;

    AdapterView.OnItemClickListener listListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position)
            {

                case 0:     //profile
                    //change name, change profile pic
                    break;
                case 1:     //notification
                    //change notification settings
                    break;
                case 2:     //goals
                    //change goal
                    break;
                case 3:     //units
                    String[] str_array = getResources().getStringArray(R.array.units);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Units")
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Confirm",null)
                            .setCancelable(false)
                            .setSingleChoiceItems(R.array.units, 0, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Toast.makeText(getContext(),  str_array[which] + " units selected", Toast.LENGTH_LONG).show();
                                            //store in the app
                                        }
                                    }).create().show();
                                    //change unit
                    break;
                case 4:     //help
                    //help section to help users navigate through the app
                    break;
            }
        }
    };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment =  inflater.inflate(R.layout.fragment_settings, container, false);
        SharedPreferences mPref = getContext().getSharedPreferences("prefs",Context.MODE_PRIVATE);

        nameField = fragment.findViewById(R.id.profileName);
        settingList = fragment.findViewById(R.id.SettingsList);
        image = fragment.findViewById(R.id.imageView);

        int imageResource = getResources().getIdentifier("@drawable/avatar", null, getActivity().getPackageName());
        image.setImageResource(imageResource);
        image.getLayoutParams().width = 250;
        image.getLayoutParams().height = 250;


        nameField.setText(mPref.getString("name", "N/A"));
        settingList.setOnItemClickListener(listListener);
        return fragment;
    }
}