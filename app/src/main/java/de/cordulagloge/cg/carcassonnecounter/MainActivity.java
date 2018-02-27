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

    private TextView p1ScoreTextView, p2ScoreTextView;
    private Button p1RoadButton, p1CityButton, p1CloisterButton, p1FarmerButton;
    private Button p2RoadButton, p2CityButton, p2CloisterButton, p2FarmerButton;
    private int p1Score, p2Score;
    private Boolean isFinalScoring;
    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get global variables
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
        p1RoadButton = findViewById(R.id.p1_road_button);
        p1RoadButton.setOnClickListener(this);

        p1CityButton = findViewById(R.id.p1_city_button);
        p1CityButton.setOnClickListener(this);

        p1CloisterButton = findViewById(R.id.p1_cloister_button);
        p1CloisterButton.setOnClickListener(this);

        p1FarmerButton = findViewById(R.id.p1_farmer_button);
        p1FarmerButton.setOnClickListener(this);

        // Buttons Player 2
        p2RoadButton = findViewById(R.id.p2_road_button);
        p2RoadButton.setOnClickListener(this);

        p2CityButton = findViewById(R.id.p2_city_button);
        p2CityButton.setOnClickListener(this);

        p2CloisterButton = findViewById(R.id.p2_cloister_button);
        p2CloisterButton.setOnClickListener(this);

        p2FarmerButton = findViewById(R.id.p2_farmer_button);
        p2FarmerButton.setOnClickListener(this);

        // Set onclicklistener for final Scoring button
        Button finalScoringButton = findViewById(R.id.final_scoring);
        finalScoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeFinalScoring();
            }
        });

        Button resetButton = findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetScore();
            }
        });
    }

    @Override
    public void onClick(View view) {
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
                setPopUpWindow(p1ScoreTextView, p1Score, valueRoad);
                break;
            case R.id.p1_city_button:
                setPopUpWindow(p1ScoreTextView, p1Score, valueCity);
                break;
            case R.id.p1_cloister_button:
                if (isFinalScoring) {
                    setPopUpWindow(p1ScoreTextView, p1Score, valueCloister);
                } else {
                    p1Score += valueCloister;
                    displayScore(p1Score, p1ScoreTextView);
                }
                break;
            case R.id.p1_farmer_button:
                setPopUpWindow(p1ScoreTextView, p1Score, valueFarmer);
                break;
            // Player 2 scores
            case R.id.p2_road_button:
                setPopUpWindow(p2ScoreTextView, p2Score, valueRoad);
                break;
            case R.id.p2_city_button:
                setPopUpWindow(p2ScoreTextView, p2Score, valueCity);
                break;
            case R.id.p2_cloister_button:
                if (isFinalScoring) {
                    setPopUpWindow(p2ScoreTextView, p2Score, valueCloister);
                } else {
                    p2Score += valueCloister;
                    displayScore(p2Score, p2ScoreTextView);
                }
                break;
            case R.id.p2_farmer_button:
                setPopUpWindow(p2ScoreTextView, p2Score, valueFarmer);
                break;
        }
    }


    /**
     * Set up and show pop up window for user input
     *
     * @param currentPlayer: scoring player
     * @param currentScore:  score of the player
     * @param currentValue:  value of the current finished element (city, road, farmer)
     */
    private void setPopUpWindow(final TextView currentPlayer, final int currentScore, final int currentValue) {
        LayoutInflater popupTilesInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupTilesLayout = popupTilesInflater.inflate(R.layout.popup_window, null);
        final PopupWindow popupTilesWindow = new PopupWindow(popupTilesLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        final EditText userInput = (EditText) popupTilesLayout.findViewById(R.id.popup_number_of_tiles);

        Button incrementButton = popupTilesLayout.findViewById(R.id.popup_increment);
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tiles = userInput.getText().toString();
                int score = convertStringToInt(tiles);
                score += 1;
                String incrementedScore = String.valueOf(score);
                userInput.setText(incrementedScore);
                userInput.setSelection(incrementedScore.length());
            }
        });
        Button decrementButton = popupTilesLayout.findViewById(R.id.popup_decrement);
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tiles = userInput.getText().toString();
                int score = convertStringToInt(tiles);
                if (score > 0) {
                    score -= 1;
                }
                String decrementedScore = String.valueOf(score);
                userInput.setText(decrementedScore);
                userInput.setSelection(decrementedScore.length());
            }
        });
        // set ok Button onClickListener
        Button okButton = popupTilesLayout.findViewById(R.id.popup_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get Input = number of tiles
                String tiles = userInput.getText().toString();
                if (!tiles.isEmpty()) {
                    int numberOfTiles = convertStringToInt(tiles);
                    int score = setScore(currentScore, numberOfTiles, currentValue);
                    if (currentPlayer == p1ScoreTextView) {
                        p1Score = score;
                    } else if (currentPlayer == p2ScoreTextView) {
                        p2Score = score;
                    }
                    displayScore(score, currentPlayer);
                    popupTilesWindow.dismiss();
                }
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
    }

    private int convertStringToInt(String tilesString) {
        if (tilesString.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(tilesString);
        }
    }

    private int setScore(int currentScore, int tiles, int value) {
        if (tiles >= 1) {
            return currentScore + (tiles * value);
        } else return currentScore;
    }

    private void displayScore(int score, TextView scoreView) {
        scoreView.setText(String.valueOf(score));
    }

    private void resetScore() {
        p1Score = 0;
        p2Score = 0;
        displayScore(p1Score, p1ScoreTextView);
        displayScore(p2Score, p2ScoreTextView);
        if (isFinalScoring) {
            isFinalScoring = false;
            // make farmer button visible
            p1FarmerButton.setVisibility(View.INVISIBLE);
            p2FarmerButton.setVisibility(View.INVISIBLE);
            // change text of city and cloister buttons
            p1CityButton.setText(R.string.city_and_shields);
            p2CityButton.setText(R.string.city_and_shields);
            p1CloisterButton.setText(R.string.cloister);
            p2CloisterButton.setText(R.string.cloister);
        }
    }

    private void makeFinalScoring() {
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
}
