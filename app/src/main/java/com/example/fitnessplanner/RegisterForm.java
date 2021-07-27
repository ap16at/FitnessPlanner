package com.example.fitnessplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fitnessplanner.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterForm extends DialogFragment {

    EditText username;
    EditText fullName;
    EditText password;
    EditText confirmPassword;
    Button register;

    FirebaseDatabase database;
    DatabaseReference ref;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.registerform, null);

        username = view.findViewById(R.id.userNameET);
        fullName = view.findViewById(R.id.fullNameET);
        password = view.findViewById(R.id.passwordET);
        confirmPassword = view.findViewById(R.id.confirmPasswordET);
        register = view.findViewById(R.id.register);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        builder.setView(view)
                .setTitle("Register")
                .setCancelable(false)
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        LoginForm login = new LoginForm();
                        login.setCancelable(false);
                        login.show(getActivity().getSupportFragmentManager(),"login dialog");
                    }
                });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameStr = username.getText().toString();
                String fullNameStr = fullName.getText().toString();
                String passwordStr = password.getText().toString();
                String confirmPasswordStr = confirmPassword.getText().toString();

                if(passwordStr.equals(confirmPasswordStr)){
                    User user = new User(fullNameStr, passwordStr, "Lose Weight");
                    ref.child(userNameStr).setValue(user);

                    SharedPreferences pref = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("user", userNameStr);
                    editor.putString("fullname", fullNameStr);
                    editor.putBoolean("signin", false);
                    editor.commit();
                    dismiss();
                    Toast.makeText(getContext(), "User: " + userNameStr + " successfully registered", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    password.setText("");
                    confirmPassword.setText("");
                    Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return builder.create();
    }
}
