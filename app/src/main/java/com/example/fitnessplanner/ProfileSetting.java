package com.example.fitnessplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;

public class ProfileSetting extends DialogFragment {

    Button fullname;
    Button password;
    Button profileImg;
    Button logout;
    SharedPreferences pref;
    Uri imageUri;


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        pref = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        EditText nameField = new EditText(getContext());
        nameField.setHint("Full Name");

        EditText passField = new EditText(getContext());
        //more work on this
        passField.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD|InputType.TYPE_MASK_VARIATION);
        passField.setHint("New Password");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.profile_setting, null);
        builder.setView(view);


        fullname = view.findViewById(R.id.fullname);
        password = view.findViewById(R.id.passwordChange);
        profileImg = view.findViewById(R.id.imageChange);
        logout = view.findViewById(R.id.logout);


        fullname.setText(pref.getString("fullname","N/A"));

        fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Input New Name")
                        .setView(nameField)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //update database
                                Toast.makeText(getContext(),"Name Change Successful", Toast.LENGTH_LONG).show();
                                editor.putString("fullname", nameField.getText().toString());
                                editor.commit();
                            }
                        })
                        .setCancelable(false)
                        .setNegativeButton("Cancel", null)
                        .create().show();
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Change Password")
                        .setView(passField)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //update database
                                Toast.makeText(getContext(), "Password Change Attempt", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setCancelable(false)
                        .setNegativeButton("Cancel", null)
                        .create().show();
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //image change to gallery
                CropImage.activity().setAspectRatio(1,1).start(getActivity());
                Toast.makeText(getContext(),"Not Yet Implemented", Toast.LENGTH_LONG).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Are You Sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.clear().commit();
                                LoginForm login = new LoginForm();
                                login.setCancelable(false);
                                login.show(getActivity().getSupportFragmentManager(),"loginlogout");
                            }
                        })
                        .setCancelable(false)
                        .setNegativeButton("No", null).create().show();



            }
        });

        return builder.create();
    }
}
