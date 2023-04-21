package com.example.minigames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    TextView btn;

    private EditText inputUsername,inputPassword,inputEmail,inputConformPassword;
    Button btnRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://minigames-c3d69-default-rtdb.firebaseio.com/");
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        // check is user already logged in
        if (MemoryData.getData(this).isEmpty()){

            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("mobile", MemoryData.getData(this));
            intent.putExtra("name", MemoryData.getName(this));
            intent.putExtra("email", "");
            startActivity(intent);
            finish();
        }


        btn = findViewById(R.id.AlreadyHaveAccount);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConformPassword = findViewById(R.id.inputConformPassword);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(RegisterActivity.this);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                checkCrededentials();
            }
        }));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

        private void checkCrededentials() {
            String username = inputUsername.getText().toString();
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String conformPassword = inputConformPassword.getText().toString();

            if (username.isEmpty() || username.length()<7)
            {
                showError(inputUsername, "Your username is not valid!");
                progressDialog.dismiss();
            }
            else if (email.isEmpty() || !email.contains("@"))
            {
                showError(inputEmail, "Email is not valid");
                progressDialog.dismiss();
            }
            else if (password.isEmpty() || password.length()<7)
            {
                showError(inputPassword, "Password must be 7 character");
                progressDialog.dismiss();
            }
            else if (conformPassword.isEmpty() || !conformPassword.equals(password))
            {
                showError(inputConformPassword, "Password not match");
                progressDialog.dismiss();
            }
            else
            {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        progressDialog.dismiss();

                        if (snapshot.child("users").hasChild(mobileTxt)){
                            Toast.makeText(RegisterActivity.this, "Mobile already exists", Toast.LENGTH_LONG).show();
                        }
                        else{
                            databaseReference.child("users").child(mobileTxt).child("email").setValue(inputEmail);
                            databaseReference.child("users").child(mobileTxt).child("name").setValue(username);

                            // save mobile to memory
                            MemoryData.saveData(mobileTxt, RegisterActivity.this);

                            // save name to memory
                            MemoryData.saveName(inputUsername, RegisterActivity.this);
                            Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        progressDialog.dismiss();
                    }
                });

                mLoadingBar.setTitle("Registration");
                mLoadingBar.setMessage("Please wait, while check your credentials");
                mLoadingBar.setCanceledOnTouchOutside(false);
                mLoadingBar.show();


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this, "Successfully Registration", Toast.LENGTH_LONG).show();

                            mLoadingBar.dismiss();
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("mobile", mobileTxt);
                            intent.putExtra("name", (CharSequence) inputUsername);
                            intent.putExtra("email", (CharSequence) inputEmail);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}