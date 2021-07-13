package com.example.fitnessplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements LoginForm.LoginListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mPref = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean onLogin = mPref.getBoolean("signin", true);

        if(onLogin)
            promptFirstTime();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        NavController navController = Navigation.findNavController(this, R.id.curr_fragment);
        NavigationUI.setupWithNavController(bottomNav, navController);

    }

    //first time prompt
    //needs to polish visuals later
    private void promptFirstTime(){
        LoginForm login = new LoginForm();
        login.setCancelable(false);
        login.show(getSupportFragmentManager(),"login");
    }

    @Override
    public void getUser(String user, String pass) {
        SharedPreferences mPref = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean("signin", false);
        editor.putString("name", user);
        editor.putString("password", pass);
        editor.commit();
    }


}
