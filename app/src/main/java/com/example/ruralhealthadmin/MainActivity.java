package com.example.ruralhealthadmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase;



    TextView SignUp;

    EditText Email,Password;

    Button LoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.password);

        LoginBtn = findViewById(R.id.loginBtn);





        LoginBtn.setOnClickListener( v -> {
            String email = Email.getText().toString();
            String password = Password.getText().toString();

            SignIn(email,password);


        });


    }
    private void SignIn (String email, String password ){

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                FirebaseUser user = auth.getCurrentUser();

            firebaseDatabase.getReference("Employee").child(user.getUid())
                    .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                           String  Role = snapshot.child("role").getValue().toString();
                            if(Role.equals("Doctor")){
                                Intent intent = new Intent(MainActivity.this, HomeDoctor.class);
                                intent.putExtra("Uid",user.getUid());
                                Toast.makeText(MainActivity.this, "Hi Welcome " + Role, Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }else if(Role.equals("Admin")){
                                Intent intent = new Intent(MainActivity.this, HomeSuperAdmin.class);
                                intent.putExtra("Uid",user.getUid());
                                Toast.makeText(MainActivity.this, "Hi Welcome " + Role, Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }

                    }else{
                            Toast.makeText(MainActivity.this, "This account is not Exist ", Toast.LENGTH_SHORT).show();
                        }
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });









                }else{
                    Toast.makeText(MainActivity.this, "Check Email and Password", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}