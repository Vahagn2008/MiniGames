package com.example.minigames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.minigames.activities.ChatMainActivity;
import com.example.minigames.activities.LoginActivity;
import com.example.minigames.databinding.ActivityChatMainBinding;
import com.example.minigames.databinding.ActivityMainBinding;
import com.example.minigames.utilities.Constants;
import com.example.minigames.utilities.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button logout, btnChat, btnMemory, TicTacToe;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadUserDetails();

        btnChat = findViewById(R.id.btn_chat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatMainActivity.class);
                startActivity(intent);
            }
        });

        btnMemory = findViewById(R.id.btn_memory);
        btnMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Memory.class);
                startActivity(intent);
            }
        });

        TicTacToe=(Button) findViewById(R.id.TicTacToe);

        TicTacToe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TicTacToeMainActivity.class);
                startActivity(intent);
            }
        });


        logout = findViewById(R.id.logout);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });



    }

    private void signOut() {
        Toast.makeText(this, "Signing out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Unable to sign out", Toast.LENGTH_SHORT).show());
    }

    private void loadUserDetails() {
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }
}