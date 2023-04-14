package com.example.minigames;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {


    TextView btn;
    EditText inputEmail, inputPassword;
    Button btnLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;
    private Button btnGoogle;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btn = findViewById(R.id.textViewSignUp);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                checkCrededentials();
            }

            private void checkCrededentials() {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                mAuth = FirebaseAuth.getInstance();
                mLoadingBar = new ProgressDialog(LoginActivity.this);
                btnGoogle = findViewById(R.id.btnGoogle);


                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    }
                });




                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                gsc = GoogleSignIn.getClient(LoginActivity.this, gso);

                btnGoogle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SignIn();
                    }
                });


                if (email.isEmpty() || !email.contains("@"))
                {
                    showError(inputEmail, "Email is not valid");
                }
                else if (password.isEmpty() || password.length()<7)
                {
                    showError(inputPassword, "Password must be 7 character");
                }
                else
                {
                    mLoadingBar.setTitle("Login");
                    mLoadingBar.setMessage("Please wait, while check your credentials");
                    mLoadingBar.setCanceledOnTouchOutside(false);
                    mLoadingBar.show();

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                mLoadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }

            private void showError(EditText input, String s) {
                input.setError(s);
                input.requestFocus();
            }
        });
        TextView btn = findViewById(R.id.textViewSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    private void SignIn() {

        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                MainActivity();
            } catch (ApiException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void MainActivity() {

        finish();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}