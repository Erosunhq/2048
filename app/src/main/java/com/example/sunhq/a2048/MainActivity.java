package com.example.sunhq.a2048;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvScore;
    private static MainActivity mainActivity = null;
    public int score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvScore = (TextView) findViewById(R.id.tvScore);

    }
    public MainActivity(){
        mainActivity = this;
    }
    public void clearScore(){
        score = 0;
    }
    public void showScore(){
        tvScore.setText(score+"");
    }
    public void addScore(int s){
        score+=s;
        showScore();
    }


    public static MainActivity getMainActivity(){
        return mainActivity;
    }

}
