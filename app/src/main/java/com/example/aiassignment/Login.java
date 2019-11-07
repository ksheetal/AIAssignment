package com.example.aiassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {


    private LottieAnimationView lottieAnimationView;
    private Button loginBtn;


    private Button btn;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private TextInputLayout emailtext, passwordtext;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lottieAnimationView = findViewById(R.id.lottie_layer_name);
        loginBtn = findViewById(R.id.button);

        emailtext = findViewById(R.id.textInputLayout);
        passwordtext = findViewById(R.id.textInputLayout2);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this, "Please wait..", Toast.LENGTH_SHORT).show();

                String email = emailtext.getEditText().getText().toString().trim();
                String password = passwordtext.getEditText().getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Intent mainIntent = new Intent(Login.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();

                            } else {
                                String erroMessage = task.getException().getMessage();
                                Toast.makeText(Login.this, "Error : " + erroMessage, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);
        //  lottieAnimationView.setDuplicateParentStateEnabled(true);


    }
}
