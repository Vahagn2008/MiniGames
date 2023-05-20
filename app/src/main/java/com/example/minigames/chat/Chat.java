package com.example.minigames.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minigames.MemoryData;
import com.example.minigames.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private final List<ChatList> chatLists = new ArrayList<>();
    private String getUserEmail = "";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://minigames-c3d69-default-rtdb.firebaseio.com/");
    private String chatKey;
    private RecyclerView chattingRecyclerView;
    private ChatAdapter chatAdapter;
    private boolean loadingFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final ImageView backbtn = findViewById(R.id.backbtn);
        final TextView nameTV = findViewById(R.id.name);
        final EditText messageEditText = findViewById(R.id.messageEditTxt);
        final CircleImageView profilepic = findViewById(R.id.profilePic);
        final ImageView sendBtn = findViewById(R.id.sendBtn);

        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);

        // get data from message andapter class
        final String getName = getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getEmail = getIntent().getStringExtra("email");


        nameTV.setText(getName);
        Picasso.get().load(getProfilePic).into(profilepic);

        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));

        chatAdapter = new ChatAdapter(chatLists, Chat.this);
        chattingRecyclerView.setAdapter(chatAdapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (chatKey.isEmpty()) {
                    // generate chat key. by defalut chatKey is 1
                    chatKey = "1";

                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }

                if (snapshot.child("chat").child(chatKey).hasChild("messages")) {

                    chatLists.clear();

                    for (DataSnapshot messagesSnapshot : snapshot.child("chat").child(chatKey).child("messages").getChildren()) {

                        if (messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("email")) {

                            final String messageTimesTamps = messagesSnapshot.getKey();
                            final String getEmail = messagesSnapshot.child("email").getValue(String.class);
                            final String getMsg = messagesSnapshot.child("msg").getValue(String.class);

                            Timestamp timestamp = new Timestamp(Long.parseLong(messageTimesTamps));
                            Date date = new Date(timestamp.getTime());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

                            ChatList chatList = new ChatList(getEmail, getName, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                            chatLists.add(chatList);

                            if (loadingFirstTime || Long.parseLong(messageTimesTamps) > Long.parseLong(MemoryData.getLastMsgTS(Chat.this, chatKey))) {

                                loadingFirstTime = false;

                                MemoryData.saveLastMsgTs(messageTimesTamps, chatKey, Chat.this);
                                chatAdapter.updateChatList(chatLists);

                                chattingRecyclerView.scrollToPosition(chatLists.size() - 1);

                            }

                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String getTxtMessage = messageEditText.getText().toString();

                // get current tinestamps
                final String currentTimesTamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
                

                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserEmail);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getEmail);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimesTamp).child("msg").setValue(getTxtMessage);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimesTamp).child("email").setValue(getUserEmail);

                // clear edit text
                messageEditText.setText("");

            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}