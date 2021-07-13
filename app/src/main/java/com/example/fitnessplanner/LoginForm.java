package com.example.fitnessplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class LoginForm extends DialogFragment {
    @NonNull

    EditText username;
    EditText password;

    LoginListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (LoginListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "LoginListener not implemented");
        }
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.loginform, null);

        builder.setView(view)
                .setTitle("Login")
                .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user = username.getText().toString();
                        String pass = password.getText().toString();


                        //check database, confirm user and password
                        listener.getUser(user,pass);

                    }
                })
                .setNeutralButton("Sign Up", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        RegisterForm register = new RegisterForm();
                        register.setCancelable(false);
                        register.show(getActivity().getSupportFragmentManager(), "register dialog");
                    }
                })
                .setCancelable(false);

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);

        return builder.create();
    }

    public interface LoginListener{
        void getUser(String user,String pass);
    }
}
