package com.example.ruralhealthadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeDoctor extends AppCompatActivity {


    TextView username,address,Number,Role;

    ImageView UserProfile;

    ImageButton navBtn;

    NavigationView nav;

    DrawerLayout drawer;

    FirebaseDatabase firebaseDatabase;

    RecyclerView DoctorList,AppointmentList;

    ArrayList<AppointmentStatus> AppointmentStatusList;

    AppointmentAdapter AppointAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_doctor);

        String Uid = getIntent().getStringExtra("Uid");


        firebaseDatabase = FirebaseDatabase.getInstance();

        username = findViewById(R.id.username);
        address = findViewById(R.id.userAddress);
        Number = findViewById(R.id.userNumber);
        Role = findViewById(R.id.userRole);

        UserProfile = findViewById(R.id.userProfile);

        nav = findViewById(R.id.navLayOut);
        drawer = findViewById(R.id.NavDrawer);
        navBtn = findViewById(R.id.ShowNavigation);

        AppointmentList = findViewById(R.id.AppointmentProveList);

        AppointmentList.setLayoutManager(new LinearLayoutManager(this));
        AppointmentStatusList = new ArrayList<>();
        AppointAdapter = new AppointmentAdapter(this,AppointmentStatusList);

        AppointmentList.setAdapter(AppointAdapter);




        navBtn.setOnClickListener(v -> {

            drawer.open();
        });

        firebaseDatabase.getReference("Employee").child(Uid).child("Appointment").orderByChild("Status").equalTo("Approved").addValueEventListener(new ValueEventListener() {
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

        firebaseDatabase.getReference("Employee").child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserRole userRole = snapshot.getValue(UserRole.class);
                username.setText(userRole.getUsername());
                address.setText(userRole.getAddress());
                Number.setText(userRole.getContact());


                Glide.with(HomeDoctor.this).load(userRole.getProfile()).error(R.drawable.logo).into(UserProfile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.NavHome){
                    Toast.makeText(HomeDoctor.this, "This is Home", Toast.LENGTH_SHORT).show();

                }else if(id == R.id.NavManage){
                    Intent intent = new Intent (HomeDoctor.this, UpdateProfile.class);
                    intent.putExtra("Uid",Uid);
                    startActivity(intent);

                }else if(id == R.id.NavReject){
                    Intent intent = new Intent (HomeDoctor.this,RejectAppointment.class);
                    intent.putExtra("Uid",Uid);
                    startActivity(intent);


                }else if(id == R.id.NavPending){
                    Intent intent = new Intent (HomeDoctor.this,PendingAppointment.class);
                    intent.putExtra("Uid",Uid);
                    startActivity(intent);

                }else if(id == R.id.NavAbout){
                    Intent intent = new Intent (HomeDoctor.this,About.class);
                    startActivity(intent);

                }else if(id == R.id.NavSignOut){
                    Intent intent = new Intent (HomeDoctor.this,DoctorRegister.class);
                    startActivity(intent);
                }

                return false;
            }
        });




    }
}