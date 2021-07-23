package com.example.fitnessplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ProfileSetting extends DialogFragment {

    Button fullname;
    Button password;
    Button profileImg;
    Button logout;
    SharedPreferences pref;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        pref = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.profile_setting, null);
        builder.setView(view);


        fullname = view.findViewById(R.id.fullname);
        password = view.findViewById(R.id.passwordChange);
        profileImg = view.findViewById(R.id.imageChange);
        logout = view.findViewById(R.id.logout);

        fullname.setText(pref.getString("fullname","N/A"));

        return builder.create();
    }
}
