package de.cordulagloge.cg.carcassonnecounter;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static int score;
    private EditText p1Input, p2Input;
    private TextView p1ScoreTextView, p2ScoreTextView;
    private int p1Score, p2Score;
    private Boolean isFinalScoring;
    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get global variables
        p1Input = findViewById(R.id.p1_input);
        p2Input = findViewById(R.id.p2_input);
        p1ScoreTextView = findViewById(R.id.p1_score);
        p2ScoreTextView = findViewById(R.id.p2_score);
        rootLayout = findViewById(R.id.root_layout);

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
    public void onClick(View view) {
        String stringTiles;
        int valueRoad, valueCity, valueCloister, valueFarmer;
        if (!isFinalScoring) {
            valueRoad = 1;
            valueCity = 2;
            valueCloister = 9;
            valueFarmer = 0;
        } else {
            valueRoad = 1;
            valueCity = 1;
            valueCloister = 1;
            valueFarmer = 3;
        }
        // switch between the different buttons
        switch (view.getId()) {
            // Player 1 scores
            case R.id.p1_road_button:
                p1Score = setPopUpWindow(p1ScoreTextView, p1Score, valueRoad);
                Log.i("in p1 button", String.valueOf(p1Score));
                break;
            case R.id.p1_city_button:
                setPopUpWindow(p1ScoreTextView, p1Score, valueCity);
                p1Score += score;
                break;
            case R.id.p1_cloister_button:
                if (isFinalScoring) {
                    setPopUpWindow(p1ScoreTextView, p1Score, valueCloister);
                    p1Score += score;
                } else {
                    p1Score += valueCloister;
                    displayScore(p1Score, p1ScoreTextView);
                }
                break;
            case R.id.p1_farmer_button:
                setPopUpWindow(p1ScoreTextView, p1Score, valueFarmer);
                p1Score += score;
                break;
            // Player 2 scores
            case R.id.p2_road_button:
                setPopUpWindow(p2ScoreTextView, p2Score, valueRoad);
                p2Score += score;
                break;
            case R.id.p2_city_button:
                setPopUpWindow(p2ScoreTextView, p2Score, valueCity);
                p2Score += score;
                break;
            case R.id.p2_cloister_button:
                if (isFinalScoring) {
                    setPopUpWindow(p2ScoreTextView, p2Score, valueCloister);
                    p2Score += score;
                } else {
                    p2Score += valueCloister;
                    displayScore(p2Score, p2ScoreTextView);
                }
                break;
            case R.id.p2_farmer_button:
                setPopUpWindow(p2ScoreTextView, p2Score, valueFarmer);
                p2Score += score;
                break;
        }
    }

    /**
     *
     */
    private int setPopUpWindow(final TextView currentPlayer, final int currentScore, final int currentValue) {
        MainActivity.score = 0;
        LayoutInflater popupTilesInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupTilesLayout = popupTilesInflater.inflate(R.layout.popup_window, null);
        final PopupWindow popupTilesWindow = new PopupWindow(popupTilesLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        final EditText userInput = (EditText) popupTilesLayout.findViewById(R.id.popup_number_of_tiles);

        // set ok Button onClickListener
        Button okButton = popupTilesLayout.findViewById(R.id.popup_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get Input = number of tiles
                String tiles = userInput.getText().toString();
                if (!tiles.isEmpty()) {
                    int numberOfTiles = Integer.parseInt(tiles);
                    MainActivity.score = setScore(currentScore, numberOfTiles, currentValue);
                    String temp = String.valueOf(MainActivity.score);
                    Log.i("in ok", temp);
                    displayScore(MainActivity.score, currentPlayer);
                }
                popupTilesWindow.dismiss();
            }
        });

        // Set cancel button onCLickListener
        Button cancelButton = popupTilesLayout.findViewById(R.id.popup_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get Input = number of tiles
                popupTilesWindow.dismiss();
            }
        });
        popupTilesWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
        return MainActivity.score;
    }

    private int setScore(int currentScore, int tiles, int value) {
        if (tiles >= 1) {
            return currentScore + (tiles * value);
        } else return currentScore;
    }

    private void displayScore(int score, TextView scoreView) {
        scoreView.setText(String.valueOf(score));
    }
}
