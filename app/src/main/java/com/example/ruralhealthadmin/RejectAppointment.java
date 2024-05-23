package com.example.ruralhealthadmin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

public class RejectAppointment extends AppCompatActivity {

    ArrayList<AppointmentStatus> AppointmentStatusList;

    AppointmentAdapter AppointAdapter;

    RecyclerView AppointmentListPending;

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reject_appointment);

        String Uid = getIntent().getStringExtra("Uid");

        firebaseDatabase = FirebaseDatabase.getInstance();

        AppointmentListPending = findViewById(R.id.AppointmentListReject);


        AppointmentListPending.setLayoutManager(new LinearLayoutManager(this));
        AppointmentStatusList = new ArrayList<>();
        AppointAdapter = new AppointmentAdapter(this,AppointmentStatusList);

        AppointmentListPending.setAdapter(AppointAdapter);



        firebaseDatabase.getReference("Employee").child(Uid).child("Appointment").orderByChild("Status").equalTo("Reject").addValueEventListener(new ValueEventListener() {
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
}