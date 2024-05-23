package com.example.ruralhealthadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class HomeSuperAdmin extends AppCompatActivity {

    TextView username,address,Number,Role;

    ImageView UserProfile;

    ImageButton navBtn;

    NavigationView nav;

    DrawerLayout drawer;

    FirebaseDatabase  firebaseDatabase;

    RecyclerView DoctorList,AppointmentList;

    ArrayList<UserRole> DoctorListItem;

    EmployeeAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_super_admin);

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

        DoctorList = findViewById(R.id.DoctorList);
        DoctorListItem = new ArrayList<>();

        DoctorList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapter = new EmployeeAdapter(HomeSuperAdmin.this,DoctorListItem);

        DoctorList.setAdapter(adapter);

        firebaseDatabase.getReference("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    UserRole userRole = ds.getValue(UserRole.class);
                    DoctorListItem.add(userRole);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        navBtn.setOnClickListener(v -> {

            drawer.open();
        });


        firebaseDatabase.getReference("Employee").child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserRole userRole = snapshot.getValue(UserRole.class);
                username.setText(userRole.getUsername());
                address.setText(userRole.getAddress());
                Number.setText(userRole.getContact());
                Role.setText(userRole.getRole());

               Glide.with(HomeSuperAdmin.this).load(userRole.getProfile()).error(R.drawable.logo).into(UserProfile);

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
                    Toast.makeText(HomeSuperAdmin.this, "This is Home", Toast.LENGTH_SHORT).show();

                }else if(id == R.id.NavManage){
                    Intent intent = new Intent (HomeSuperAdmin.this, UpdateProfile.class);
                    startActivity(intent);

                }else if(id == R.id.AddEmployee){
                    Intent intent = new Intent (HomeSuperAdmin.this,DoctorRegister.class);
                    startActivity(intent);

                }else if(id == R.id.RejectAppointment){
                    Intent intent = new Intent (HomeSuperAdmin.this,RejectAppointment.class);
                    startActivity(intent);


                }else if(id == R.id.PendingAppointment){
                    Intent intent = new Intent (HomeSuperAdmin.this,PendingAppointment.class);
                    startActivity(intent);

                }else if(id == R.id.NavAbout){
                    Intent intent = new Intent (HomeSuperAdmin.this,About.class);
                    startActivity(intent);

                }else if(id == R.id.NavSignOut){
                    Intent intent = new Intent (HomeSuperAdmin.this,DoctorRegister.class);
                    startActivity(intent);

                }

                return false;
            }
        });








    }
}