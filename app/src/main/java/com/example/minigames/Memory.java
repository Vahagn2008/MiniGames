package com.example.minigames;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Memory extends AppCompatActivity {

    Button memory43, memory54;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

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