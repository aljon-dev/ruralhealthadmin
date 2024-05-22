package com.example.ruralhealthadmin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorRegister extends AppCompatActivity {

    FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase;

    EditText email, password, confirmpassword, contact, address, UserEt, UserRole;

    Button Register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        email = findViewById(R.id.EmailEt);
        password = findViewById(R.id.passwordEt);
        confirmpassword = findViewById(R.id.ConfirmPassword);
        contact = findViewById(R.id.Contact);
        address = findViewById(R.id.Address);
        UserEt = findViewById(R.id.UsernameEt);
        UserRole = findViewById(R.id.Role);
        Register = findViewById(R.id.register);


        UserRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder Choose = new AlertDialog.Builder(DoctorRegister.this);
                Choose.setTitle("Choose Role");
                CharSequence charSequence[] = {"Doctor", "Pediatrician", "Dentist"};

                Choose.setItems(charSequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            UserRole.setText("Doctor");
                        } else if (which == 1) {
                            UserRole.setText("Pediatrician");
                        } else if (which == 2) {
                            UserRole.setText("Dentist");
                        }
                    }
                });
                Choose.show();

            }
        });


        Register.setOnClickListener(v -> {

            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();
            String confirmPassword = password.getText().toString();
            String Contacts = contact.getText().toString();
            String Address = address.getText().toString();
            String username = UserEt.getText().toString();
            String Roles = UserRole.getText().toString();


            if (username.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || confirmPassword.isEmpty() || Contacts.isEmpty() || Address.isEmpty()) {

                Toast.makeText(this, "Fill the fields", Toast.LENGTH_SHORT).show();

            } else if (!userPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Password should be match", Toast.LENGTH_SHORT).show();
            } else {
                Register(userEmail, username, userPassword, Contacts, Address,Roles);
            }
        });


    }

    private void Register(String email, String username, String password, String contact, String address, String Role) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser users = auth.getCurrentUser();
                    UserRole user = new UserRole();

                    user.setUid(users.getUid());
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setContact(contact);
                    user.setAddress(address);
                    user.setRole(Role);
                    user.setProfile("");
                    firebaseDatabase.getReference().child("Employee").child(users.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(DoctorRegister.this, "This Account is Register  ", Toast.LENGTH_SHORT).show();
                            } else {
                                firebaseDatabase.getReference().child("Employee").child(users.getUid()).setValue(user);
                                Toast.makeText(DoctorRegister.this, "Register Successfull", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DoctorRegister.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(DoctorRegister.this, "This Account is Already Exist ", Toast.LENGTH_SHORT).show();

                } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                    Toast.makeText(DoctorRegister.this, "Password Is Weak & Invalid ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}