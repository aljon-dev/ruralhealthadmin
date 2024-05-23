package com.example.ruralhealthadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ItemHolder> {


    private ArrayList<AppointmentStatus> AppointmentList;

    private Context context;


    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public  interface OnItemClickListener {
        void onClick(AppointmentStatus appointmentStatus);
    }

    public AppointmentAdapter(Context context, ArrayList<AppointmentStatus> AppointmentList){
        this.context = context;
        this.AppointmentList = AppointmentList;
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_list,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        AppointmentStatus appointmentStatus = AppointmentList.get(position);
        holder.onBind(appointmentStatus);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onClick(AppointmentList.get(position)));

    }

    @Override
    public int getItemCount() {
        return AppointmentList.size();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder{

        TextView    name,Position,Status,Date,Time;

        String  AdminUid,PatientId, DoctorName;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.PatientName);
            Status = itemView.findViewById(R.id.status);
            Date = itemView.findViewById(R.id.timeframe);
            Time = itemView.findViewById(R.id.date);

        }
        public void onBind(AppointmentStatus appointmentstatus){
            name.setText(appointmentstatus.getPatientName());

            Status.setText(appointmentstatus.getStatus());
            Date.setText(appointmentstatus.getDate());
            Time.setText(appointmentstatus.getTime());

            AdminUid = appointmentstatus.getAdminUid();
            PatientId = appointmentstatus.getPatientId();
            DoctorName = appointmentstatus.getName();


        }
    }


}