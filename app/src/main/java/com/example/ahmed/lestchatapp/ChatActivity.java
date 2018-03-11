package com.example.ahmed.lestchatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    EditText editTextMessageInput;
    Button buttonSend;
    TextView textView;
    DatabaseReference reference;
    String userName, chatName = "";
    String temp_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editTextMessageInput = findViewById(R.id.msg_input);
        buttonSend = findViewById(R.id.btn_send);
        textView = findViewById(R.id.textView);

        userName = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        chatName = getIntent().getExtras().get("chat_name").toString();
        setTitle("GroupChat" + chatName);
        reference = FirebaseDatabase.getInstance().getReference().child(chatName);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                temp_key = reference.push().getKey();
                reference.updateChildren(map);
                DatabaseReference databaseReference = reference.child(temp_key);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("name", userName);
                map2.put("msg", editTextMessageInput.getText().toString());
                databaseReference.updateChildren(map2);
                editTextMessageInput.setText("");

            }
        });
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    String chat_msg, chat_user_name;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            chat_msg = ((DataSnapshot) iterator.next()).getValue().toString();
            chat_user_name = ((DataSnapshot) iterator.next()).getValue().toString();

        }
        textView.append(chat_user_name + ":" + chat_msg + "\n");
    }
}
