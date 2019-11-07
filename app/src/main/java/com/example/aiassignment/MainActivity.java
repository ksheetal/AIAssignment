package com.example.aiassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;


    private String mCurrentUserId;
    private List<ChatData> mMessagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private Adapter mAdapter;
    private EditText mInputMessageView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String chatuserid;
    private ImageButton sendchatbtn;
   // private EditText userName;

    private DatabaseReference mRootRef;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<String> mchat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new Adapter(mMessagesList);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mInputMessageView = findViewById(R.id.chatText);
       // userName = findViewById(R.id.textView8);


        mAdapter = new Adapter(mMessagesList);
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);


        loadmessages();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mCurrentUserId = mAuth.getCurrentUser().getUid();


        sendchatbtn = findViewById(R.id.imageButton2);
        sendchatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String chatMessage = mInputMessageView.getText().toString();
                // mchat.add(chatMessage);
                sendMessage();
                mInputMessageView.setText(" ");
                // hiding keyboard after message is send.
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


            }
        });
    }


    private void loadmessages() {

        mRootRef.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mMessagesList.add(dataSnapshot.getValue(ChatData.class));
                mAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mMessagesList.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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


    private void sendMessage() {
        String message = mInputMessageView.getText().toString();
        if (!TextUtils.isEmpty(message)) {

            DatabaseReference user_message_push = mRootRef.child("messages").push();
            String push_id = user_message_push.getKey();

            //   DatabaseReference current_user_emailid = mRootRef.child("Users").child(mCurrentUserId);
            //  DatabaseReference u

            ChatData chatData = new ChatData("chatMessage", "userId", "userName", "timestamp");

            DatabaseReference newPost = user_message_push;

            Map<String, String> DataToSave = new HashMap<>();
            DataToSave.put("chatMessage", message);
            DataToSave.put("userId", mUser.getUid());
           // DataToSave.put("userName", userName.getText().toString());
            DataToSave.put("timestamp", String.valueOf(java.lang.System.currentTimeMillis()));

            user_message_push.setValue(DataToSave);
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();

        }
    }
}
