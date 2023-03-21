package com.example.quizbulider;

import android.annotation.SuppressLint;
import android.app.GameManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class StartGame  extends AppCompatActivity{

    // File IO
    String str = null;
    BufferedReader br = null;
    // Hash Map and Arraylists
    Map<String,String> map = new HashMap<String, String>();
    ArrayList<String> defList = new ArrayList<String>();
    ArrayList<String> termList = new ArrayList<String>();
    // App
    TextView tvTimer, tvPoints, tvResult, tvDefinition;
    int index, points;
    Button btn1, btn2, btn3, btn4;
    CountDownTimer countDownTimer;
    long millisUntilFin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);

        tvTimer = findViewById(R.id.tvTimer);
        tvResult = findViewById(R.id.tvResult);
        tvDefinition = findViewById(R.id.tvDefinition);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        index = 0;

        //READ INTERNAL using RAW & Buffered Reader - note res/raw is read only in Android
        try {
            InputStream is = getResources().openRawResource(R.raw.def);
            br = new BufferedReader(new InputStreamReader(is));
            while ((str = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(str, "$$");
                while (st.hasMoreTokens()){
                    String def = st.nextToken();
                    String term = st.nextToken();

                    map.put(def, term);
                    defList.add(def);
                    termList.add(term);
                }
            }
            is.close();
            System.out.println("File in RAW is closed");
        }catch(Exception e){
            e.printStackTrace();
        }// End Catch

        Collections.shuffle(defList);
        Collections.shuffle(termList);
//        calls thios
        startGame();

    }

    @SuppressLint("SetTextI18n")
    private void startGame() {
        millisUntilFin = 10000;
        tvTimer.setText("" + (millisUntilFin / 1000) + "s");
        tvPoints.setText(points + "/" + defList.size());
        //
        genQuestions();
        //
        genAnswers(index);

    }

    private void genQuestions() {
        tvDefinition.setText(defList.get(0));
    }

    private void genAnswers(int index) {
        ArrayList<String> termListTemp = (ArrayList<String>) termList.clone();
        String correctAnswer = termList.get(index);
        termListTemp.remove(correctAnswer);
        Collections.shuffle(termListTemp);
        ArrayList<String> newList = new ArrayList<>();
        newList.add(termListTemp.get(0));
        newList.add(termListTemp.get(1));
        newList.add(termListTemp.get(2));
        newList.add(correctAnswer);
        Collections.shuffle(newList);
        btn1.setText(newList.get(0));
        btn1.setText(newList.get(1));
        btn1.setText(newList.get(2));
        btn1.setText(newList.get(3));
//        tvDefinition(map.get(termList.get(index)));

    }
    public void nextQuestion(View view) {
        index++;
        if(index > termList.size() - 1){
            tvDefinition.setVisibility(View.GONE);
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            btn4.setVisibility(View.GONE);
            Intent intent = new Intent(StartGame.this, GameOver.class);
            intent.putExtra("points",points);
            startActivity(intent);
            finish();
        }else{
            startGame();
        }
    }

    @SuppressLint("SetTextI18n")
    public void answSelected(View view) {
        String answer = ((Button)view).getText().toString().trim();
        String correctAnswer = termList.get(index);
        if(answer.equals(correctAnswer)){
            points++;
            tvPoints.setText(points + "/" + termList.size());
            tvResult.setText("Correct");
        }else{
            tvResult.setText("Incorrect");
        }
    }


}
