package com.example.minigames;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Memory extends AppCompatActivity {

    ImageButton backk;
    Button memory43, memory54;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        backk = findViewById(R.id.backk);
        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Memory.this, MainActivity.class);
                startActivity(intent);
            }
        });

        memory43 = (Button) findViewById(R.id.memory43);
        memory54 = (Button) findViewById(R.id.memory54);

        memory43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Memory.this, MemoryMainActivity.class);
                startActivity(intent);
            }
        });

        memory54.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Memory.this, MemoryMainActivity1.class);
                startActivity(intent);
            }
        });
    }
}