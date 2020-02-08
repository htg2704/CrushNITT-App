package com.example.crushnitt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    Button register;
    EditText email,password, name;
    RadioGroup mrg;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent= new Intent(register.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        register = findViewById(R.id.submit);
        email = findViewById(R.id.email);
        password= findViewById(R.id.password);
        mrg = findViewById(R.id.radio);
        name = findViewById(R.id.name);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int select = mrg.getCheckedRadioButtonId();
                final RadioButton r = findViewById(select);
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                if(r.getText()==null){
                    Toast.makeText(getApplicationContext(),"Please select a gender",Toast.LENGTH_SHORT).show();
                }
                String e = email.getText().toString();
                String p = password.getText().toString();
                final String n = name.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(register.this , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
                        } else {
                            String userid = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference current = FirebaseDatabase.getInstance().
                                    getReference().child("Users").child(userid);

                            Map userInfo = new HashMap<>();
                            userInfo.put("name", n);
                            userInfo.put("sex", r.getText().toString());
                            userInfo.put("profileImageUrl", "default");
                            current.updateChildren(userInfo);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
