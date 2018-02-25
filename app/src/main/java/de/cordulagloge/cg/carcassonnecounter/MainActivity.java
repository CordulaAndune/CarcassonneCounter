package de.cordulagloge.cg.carcassonnecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText p1Input, p2Input;
    private TextView p1ScoreTextView, p2ScoreTextView;
    private int p1Score, p2Score;
    private Boolean isFinalScoring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get global variables
        p1Input = findViewById(R.id.p1_input);
        p2Input = findViewById(R.id.p2_input);
        p1ScoreTextView = findViewById(R.id.p1_score);
        p2ScoreTextView = findViewById(R.id.p2_score);

        //set Values
        p1Score = 0;
        p2Score = 0;
        displayScore(p1Score, p1ScoreTextView);
        displayScore(p2Score, p2ScoreTextView);

        isFinalScoring = false;

        // Buttons Player 1
        Button p1RoadButton = findViewById(R.id.p1_road_button);
        p1RoadButton.setOnClickListener(this);

        Button p1CityButton = findViewById(R.id.p1_city_button);
        p1CityButton.setOnClickListener(this);

        Button p1CloisterButton = findViewById(R.id.p1_cloister_button);
        p1CloisterButton.setOnClickListener(this);

        // Buttons P2ayer 2
        Button p2RoadButton = findViewById(R.id.p2_road_button);
        p2RoadButton.setOnClickListener(this);

        Button p2CityButton = findViewById(R.id.p2_city_button);
        p2CityButton.setOnClickListener(this);

        Button p2CloisterButton = findViewById(R.id.p2_cloister_button);
        p2CloisterButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        int valueRoad, valueCity, valueCloister, valueGrass;
        if (!isFinalScoring) {
            valueRoad = 1;
            valueCity = 2;
            valueCloister = 9;
        }
        else {
            valueRoad = 1;
            valueCity = 1;
            valueCloister = 1;
            valueGrass = 3;
        }
        switch (view.getId()){
            // Player 1 scores
            case R.id.p1_road_button:
                p1Score += setScore(p1Input, valueRoad);
                displayScore(p1Score, p1ScoreTextView);
                break;
            case R.id.p1_city_button:
                p1Score += setScore(p1Input,valueCity);
                displayScore(p1Score, p1ScoreTextView);
                break;
            case R.id.p1_cloister_button:
                if (isFinalScoring){
                    p1Score += setScore(p1Input, valueCloister);
                }
                else {
                    p1Score += valueCloister;
                }
                displayScore(p1Score, p1ScoreTextView);
                break;
            // Player 2 scores
            case R.id.p2_road_button:
                p2Score += setScore(p2Input, valueRoad);
                displayScore(p2Score, p2ScoreTextView);
                break;
            case R.id.p2_city_button:
                p2Score += setScore(p2Input,valueCity);
                displayScore(p2Score, p2ScoreTextView);
                break;
            case R.id.p2_cloister_button:
                if (isFinalScoring){
                    p2Score += setScore(p2Input, valueCloister);
                }
                else {
                    p2Score += valueCloister;
                }
                displayScore(p2Score, p2ScoreTextView);
                break;
        }
    }

    private int setScore(EditText playerInput, int value){
        String stringTiles = playerInput.getText().toString();
        if (!stringTiles.isEmpty()){
        int numberTiles = Integer.parseInt(stringTiles);
        return numberTiles * value;
        }
        else return 0;
    }

    private void displayScore(int score, TextView scoreView){
        scoreView.setText(String.valueOf(score));
    }
}
