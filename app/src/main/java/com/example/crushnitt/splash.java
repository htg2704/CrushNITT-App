package com.example.crushnitt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {
    Handler mHandler;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();

        // Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

        final Typewriter ty=findViewById(R.id.type);
        ty.setText("");
        ty.setCharacterDelay(200);
        ty.animateText("CrushNITT");
        mHandler=new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    intent= new Intent(getApplicationContext(), MainActivity.class);
                }
                else{
                    intent=new Intent(getApplicationContext(), Choose.class);
                }
                startActivity(intent);
            }
        },2200);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
