package com.example.fitnessplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessplanner.R;
import com.example.fitnessplanner.models.WeightLog;

import java.util.ArrayList;

public class WeightLogAdapter extends RecyclerView.Adapter<WeightLogAdapter.ViewHolder>{

    Context context;
    ArrayList<WeightLog> logList;

    public WeightLogAdapter(Context context, ArrayList<WeightLog> logList) {
        this.context = context;
        this.logList = logList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView text;
        public ImageButton menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.log);
            menu = itemView.findViewById(R.id.imageButton);
            menu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
            popupMenu.inflate(R.menu.menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.editLog:
                            Toast.makeText(v.getContext(), "Edit Clicked", Toast.LENGTH_LONG).show();
                            return true;

                        case R.id.deleteLog:
                            Toast.makeText(v.getContext(), "Delete Clicked", Toast.LENGTH_LONG).show();
                            return false;

                        default:
                            return false;
                    }

                }
            });
            popupMenu.show();
        }
    }

    @NonNull
    @Override
    public WeightLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weight_log, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightLogAdapter.ViewHolder holder, int position) {
        WeightLog weightLog = logList.get(position);
        holder.text.setText(weightLog.getLog());
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }
}
