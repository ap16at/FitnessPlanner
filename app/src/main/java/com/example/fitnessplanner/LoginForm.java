package com.example.fitnessplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    Button login;

    private String user_name;
    private String pass_word;

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

        SharedPreferences mPref = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.loginform, null);

        builder.setView(view)
                .setTitle("Login")
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
        login = view.findViewById(R.id.Login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                setUser(user);
                setPassword(pass);


                boolean found = false;

                if (!user.isEmpty() && !pass.isEmpty()){
                    //check database, confirm user and password
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String dbPassword;
                            if (snapshot.hasChild(getUser())) {
                                DataSnapshot user = snapshot.child(getUser());
                                Map<String, Object> values = (HashMap<String, Object>) user.getValue();
                                dbPassword = values.get("password").toString();
                                if (dbPassword.equals(getPassword().trim())) {
                                    Toast.makeText(getContext(), "Successful Login", Toast.LENGTH_LONG).show();
                                    SharedPreferences pref = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("user", getUser());
                                    editor.putBoolean("signin", false);
                                    editor.putString("fullname", values.get("fullName").toString());
                                    editor.commit();
                                    Intent intent = new Intent(getActivity(),MainActivity.class);
                                    getActivity().finish();
                                    startActivity(intent);
                                    dismiss();
                                } else
                                    Toast.makeText(getContext(), "Login Attemp Failed", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            }
                else
                    Toast.makeText(getContext(),"Fields Are Empty", Toast.LENGTH_LONG).show();



                listener.getUser(user,pass);
            }
        });

        return builder.create();
    }

    public interface LoginListener{
        void getUser(String user,String pass);
    }


    private void setUser(String user_name)
    {
        this.user_name = user_name;
    }

    private String getUser()
    {
        return user_name;
    }

    private void setPassword(String pass_word)
    {
        this.pass_word = pass_word;
    }

    private String getPassword()
    {
        return pass_word;
    }

}
