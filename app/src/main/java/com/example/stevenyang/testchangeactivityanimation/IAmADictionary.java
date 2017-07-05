package com.example.stevenyang.testchangeactivityanimation;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

public class IAmADictionary extends AppCompatActivity {
    View secretView;
    CountDownTimer countDownTimer;
    RelativeLayout activity_iam_adictionary;
    Animation shake;
    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iam_adictionary);




        secretView = this.findViewById(R.id.secretView);

        activity_iam_adictionary = (RelativeLayout)this.findViewById(R.id.activity_iam_adictionary);
        secretView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                countDownTimer = new CountDownTimer(5000,1000) {
                    @Override
                    public void onTick(long l) {
                        if(l<4000&& !flag){
                            flag = true;
                            for(int i=0 ; i<activity_iam_adictionary.getChildCount();i++){
                                shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                                shake.setStartOffset((int)(Math.random()* 1000)+200);
                                activity_iam_adictionary.getChildAt(i).startAnimation(shake);
                            }
                        }
                     }

                    @Override
                    public void onFinish() {
                    Intent intent = new Intent(IAmADictionary.this,new MainActivity().getClass());
                        startActivity(intent);
                        IAmADictionary.this.finish();
                    }
                }.start();
                return false;
            }
        });
    }
}
