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
        final Button p1RoadButton = findViewById(R.id.p1_road_button);
        p1RoadButton.setOnClickListener(this);

        final Button p1CityButton = findViewById(R.id.p1_city_button);
        p1CityButton.setOnClickListener(this);

        final Button p1CloisterButton = findViewById(R.id.p1_cloister_button);
        p1CloisterButton.setOnClickListener(this);

        final Button p1FarmerButton = findViewById(R.id.p1_farmer_button);
        p1FarmerButton.setOnClickListener(this);

        // Buttons Player 2
        final Button p2RoadButton = findViewById(R.id.p2_road_button);
        p2RoadButton.setOnClickListener(this);

        final Button p2CityButton = findViewById(R.id.p2_city_button);
        p2CityButton.setOnClickListener(this);

        final Button p2CloisterButton = findViewById(R.id.p2_cloister_button);
        p2CloisterButton.setOnClickListener(this);

        final Button p2FarmerButton = findViewById(R.id.p2_farmer_button);
        p2FarmerButton.setOnClickListener(this);

        // Set onclicklistener for final Scoring button
        Button finalScoringButton = findViewById(R.id.final_scoring);
        finalScoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFinalScoring = true;
                // make farmer button visible
                p1FarmerButton.setVisibility(View.VISIBLE);
                p2FarmerButton.setVisibility(View.VISIBLE);
                // change text of city and cloister buttons
                p1CityButton.setText(R.string.city_and_shields_final);
                p2CityButton.setText(R.string.city_and_shields_final);
                p1CloisterButton.setText(R.string.cloister_final);
                p2CloisterButton.setText(R.string.cloister_final);
            }
        });
    }

    @Override
    public void onClick(View view){
        int valueRoad, valueCity, valueCloister, valueFarmer;
        if (!isFinalScoring) {
            valueRoad = 1;
            valueCity = 2;
            valueCloister = 9;
            valueFarmer = 0;
        }
        else {
            valueRoad = 1;
            valueCity = 1;
            valueCloister = 1;
            valueFarmer = 3;
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
            case R.id.p1_farmer_button:
                p1Score += setScore(p1Input,valueFarmer);
                displayScore(p1Score,p1ScoreTextView);
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
            case R.id.p2_farmer_button:
                p2Score += setScore(p2Input,valueFarmer);
                displayScore(p2Score,p2ScoreTextView);
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
