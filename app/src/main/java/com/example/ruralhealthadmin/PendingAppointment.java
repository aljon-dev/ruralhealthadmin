package com.example.ruralhealthadmin;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendingAppointment extends AppCompatActivity {

    ArrayList<AppointmentStatus> AppointmentStatusList;

    AppointmentAdapter AppointAdapter;

    RecyclerView AppointmentListPending;

    FirebaseDatabase firebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_appointment);

        String Uid = getIntent().getStringExtra("Uid");

        firebaseDatabase = FirebaseDatabase.getInstance();

        AppointmentListPending = findViewById(R.id.AppointmentListPending);


        AppointmentListPending.setLayoutManager(new LinearLayoutManager(this));
        AppointmentStatusList = new ArrayList<>();
        AppointAdapter = new AppointmentAdapter(this,AppointmentStatusList);

        AppointmentListPending.setAdapter(AppointAdapter);

        AppointAdapter.setOnItemClickListener(new AppointmentAdapter.OnItemClickListener() {
            @Override
            public void onClick(AppointmentStatus appointmentStatus) {
                Action(appointmentStatus);
            }
        });

        firebaseDatabase.getReference("Employee").child(Uid).child("Appointment").orderByChild("Status").equalTo("Waiting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    AppointmentStatus appointmentStatus = ds.getValue(AppointmentStatus.class);
                    AppointmentStatusList.add(appointmentStatus);
                }
                AppointAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void Action(AppointmentStatus appointmentStatus){
        AlertDialog.Builder Choose = new AlertDialog.Builder(PendingAppointment.this);
        Choose.setTitle("Action");

        CharSequence actionpick [] = {"Confirm","Cancelled"};

        Choose.setItems(actionpick, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0 ){
                    Map<String, Object> StatusUpdate = new HashMap<>();
                    StatusUpdate.put("Status","Approved");

                    firebaseDatabase.getReference("Employee").child(appointmentStatus.getAdminUid()).child("Appointment").child(appointmentStatus.getAppointId()).updateChildren(StatusUpdate);

                    firebaseDatabase.getReference("Patients").child(appointmentStatus.getPatientId()).child("Appointment").child(appointmentStatus.getAppointId()).updateChildren(StatusUpdate);

                }else if(which == 1){

                    Map<String, Object> StatusUpdate = new HashMap<>();
                    StatusUpdate.put("Status","Reject");

                    firebaseDatabase.getReference("Employee").child(appointmentStatus.getAdminUid()).child("Appointment").child(appointmentStatus.getAppointId()).updateChildren(StatusUpdate);

                    firebaseDatabase.getReference("Patients").child(appointmentStatus.getPatientId()).child("Appointment").child(appointmentStatus.getAppointId()).updateChildren(StatusUpdate);

                }
            }
        });
        Choose.show();
    }
}