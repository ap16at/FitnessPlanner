package com.example.fitnessplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements LoginForm.LoginListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        Intent intent = new Intent(this,Reminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);

        SharedPreferences mPref = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean onLogin = mPref.getBoolean("signin", true);
        boolean mealReminders = mPref.getBoolean("reminders", true);
        int breakfastH = mPref.getInt("breakfasthour", 9);
        int breakfastM = mPref.getInt("breakfastminute", 0);
        int lunchH = mPref.getInt("lunchhour", 13);
        int lunchM = mPref.getInt("lunchminute", 0);
        int dinnerH = mPref.getInt("dinnerhour", 17);
        int dinnerM = mPref.getInt("dinnerminute", 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        AlarmManager alarmManager1 = (AlarmManager)getSystemService(ALARM_SERVICE);
        AlarmManager alarmManager2 = (AlarmManager)getSystemService(ALARM_SERVICE);

        Calendar breakfast = Calendar.getInstance();
        Calendar lunch = Calendar.getInstance();
        Calendar dinner = Calendar.getInstance();

        breakfast.set(Calendar.HOUR_OF_DAY, breakfastH);
        breakfast.set(Calendar.MINUTE, breakfastM);
        lunch.set(Calendar.HOUR_OF_DAY, lunchH);
        lunch.set(Calendar.MINUTE, lunchM);
        dinner.set(Calendar.HOUR_OF_DAY, dinnerH);
        dinner.set(Calendar.MINUTE, dinnerM);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,breakfast.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP,lunch.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP,dinner.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


        if(!mealReminders)
        {
            alarmManager.cancel(pendingIntent);
            alarmManager1.cancel(pendingIntent);
            alarmManager2.cancel(pendingIntent);
        }


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

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "MealReminderChannel";
            String description = "Channel to remind users to add meals";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyPlanner", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
