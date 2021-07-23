package com.example.fitnessplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fitnessplanner.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginForm extends DialogFragment {
    @NonNull

    EditText username;
    EditText password;

    boolean success;

    LoginListener listener;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;


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
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.loginform, null);

        builder.setView(view)
                .setTitle("Login")
                .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user = username.getText().toString().trim();
                        String pass = password.getText().toString().trim();


                        boolean found = false;

                        //check database, confirm user and password
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String dbPassword;
                                if(snapshot.hasChild("pabloH"))
                                {
                                    DataSnapshot user = snapshot.child("pabloH");
                                    Map<String,Object> values = (HashMap<String,Object>) user.getValue();
                                    dbPassword = values.get("password").toString();
                                    if(dbPassword.equals(pass))
                                        setLoginSuccess(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        if(getLoginSuccess())
                        {
                            dialog.dismiss();
                        }
                        else
                        {

                            Toast.makeText(getContext(),"Username and Password does not match", Toast.LENGTH_LONG).show();
                        }

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

    private void setLoginSuccess(boolean success)
    {
        this.success = success;
    }

    private boolean getLoginSuccess()
    {
        return success;
    }
}
