package com.example.minigames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minigames.chat.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    TextView btn;

    private ProgressDialog mLoadingBar;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://minigames-c3d69-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn = findViewById(R.id.AlreadyHaveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        final EditText name = findViewById(R.id.inputUsername);
        final EditText mobile = findViewById(R.id.inputNumber);
        final EditText email = findViewById(R.id.inputEmail);
        final EditText password = findViewById(R.id.inputPassword);
        final EditText conformPassword = findViewById(R.id.inputConformPassword);
        final Button registerBtn = findViewById(R.id.btnRegister);


        if (!MemoryData.getData(this).isEmpty()) {

            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.putExtra("mobile", MemoryData.getData(this));
            intent.putExtra("name", MemoryData.getName(this));
            intent.putExtra("email", "");
            intent.putExtra("password", "");
            intent.putExtra("conformPassword", "");
            startActivity(intent);
            finish();
        }
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLoadingBar = new ProgressDialog(RegisterActivity.this);
                mLoadingBar.setMessage("Loading...");
                mLoadingBar.show();


                final String nameTxt = name.getText().toString();
                final String mobileTxt = mobile.getText().toString();
                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String conformPasswordTxt = conformPassword.getText().toString();


                if (nameTxt.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "All fields Required!!!", Toast.LENGTH_SHORT).show();
                    mLoadingBar.dismiss();
                } else if (emailTxt.isEmpty() || !emailTxt.contains("@")) {
                    Toast.makeText(RegisterActivity.this, "All fields Required!!!", Toast.LENGTH_SHORT).show();
                    mLoadingBar.dismiss();
                } else if (mobileTxt.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "All fields Required!!!", Toast.LENGTH_SHORT).show();
                    mLoadingBar.dismiss();
                } else if (passwordTxt.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "All fields Required!!!", Toast.LENGTH_SHORT).show();
                    mLoadingBar.dismiss();
                } else if (conformPasswordTxt.isEmpty() || !conformPasswordTxt.equals(passwordTxt)) {
                    Toast.makeText(RegisterActivity.this, "All fields Required!!!", Toast.LENGTH_SHORT).show();
                    mLoadingBar.dismiss();

                } else {

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            mLoadingBar.dismiss();

                            if (snapshot.child("users").hasChild(mobileTxt)) {
                                Toast.makeText(RegisterActivity.this, "Mobile already exists", Toast.LENGTH_SHORT).show();

                            } else {
                                databaseReference.child("users").child(mobileTxt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(mobileTxt).child("name").setValue(nameTxt);
                                databaseReference.child("users").child(mobileTxt).child("profile_pic").setValue("");

                                MemoryData.saveData(mobileTxt, RegisterActivity.this);

                                MemoryData.saveName(nameTxt, RegisterActivity.this);

                                Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.putExtra("mobile", mobileTxt);
                                intent.putExtra("name", nameTxt);
                                intent.putExtra("email", emailTxt);
                                intent.putExtra("password", passwordTxt);
                                intent.putExtra("conformPassword", conformPasswordTxt);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            mLoadingBar.dismiss();
                        }
                    });
                }
            }
        });
    }
}