package com.upgrad.quizing;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button login, signup;
    RadioGroup radioGroup;
    String loginType;
    private FirebaseAuth mAuth;
    EditText emailET,passwordET;
    String email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.button3);
        signup = findViewById(R.id.button4);
        radioGroup = findViewById(R.id.rg);
        mAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.editText2);
        passwordET = findViewById(R.id.editText3);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButton){
                    //Teacher Interface
                    emailET.setHint("Email with @upgrad.com");
                    loginType="teacher";

                }else if(checkedId==R.id.radioButton2){
                    //Student Interface
                    emailET.setHint("Email");
                    loginType="student";

                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailET.getText().toString();
                password = passwordET.getText().toString();

                if (email.isEmpty()) {
                    emailET.setError("Field is Empty !");
                } else if (password.isEmpty()) {
                    passwordET.setError("Field is Empty !");
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("test", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("test", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });

                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailET.getText().toString();
                password = passwordET.getText().toString();

                if (email.isEmpty()) {
                    emailET.setError("Field is Empty !");
                } else if (password.isEmpty()) {
                    passwordET.setError("Field is Empty !");
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("test", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (loginType.equals("teacher")) {
                                            if (password.equals("teacher")) {
                                                loginType = "teacher";
                                            } else {
                                                loginType = "student";
                                            }

                                        } else {
                                            loginType = "student";
                                        }
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("test", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });

                }
            }
        });


    }

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            //user has logged in
            if(user.getEmail().contains("@upgrad.com")){
                loginType = "teacher";
            }else{
                loginType ="student";
            }

            if(loginType.equals("teacher")){
                //teacher has logged in
                Intent intent = new Intent(LoginActivity.this,WelcomeActivity.class);
                ScoreClass scoreClass = new ScoreClass();
                scoreClass.setLoginType("teacher");
                scoreClass.setUserName(user.getEmail());
                startActivity(intent);

            }else if(loginType.equals("student")){
                //student has logged in
                Intent intent = new Intent(LoginActivity.this,WelcomeActivity.class);
                ScoreClass scoreClass = new ScoreClass();
                scoreClass.setLoginType("student");
                scoreClass.setUserName(user.getEmail());
                startActivity(intent);
            }

        }else{
            //user hasn't logged in/ new user

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);
    }
}
