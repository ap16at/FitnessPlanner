package com.example.fitnessplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NotificationDialog extends DialogFragment {

    Switch notification;
    TextView notificationChanged;
    Button breakfast;
    Button lunch;
    Button dinner;
    Button weight;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_dialog, null);
        builder.setView(view);

        notification = view.findViewById(R.id.notificationSwitch);
        notificationChanged = view.findViewById(R.id.notificationText);
        breakfast = view.findViewById(R.id.breakfast);
        lunch= view.findViewById(R.id.lunch);
        dinner = view.findViewById(R.id.dinner);
        weight = view.findViewById(R.id.weight);



        return builder.create();
    }
}
