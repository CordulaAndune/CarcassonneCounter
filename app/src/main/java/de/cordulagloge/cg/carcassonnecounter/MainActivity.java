package de.cordulagloge.cg.carcassonnecounter;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView p1ScoreTextView, p2ScoreTextView;
    private Button p1RoadButton, p1CityButton, p1CloisterButton, p1FarmerButton;
    private Button p2RoadButton, p2CityButton, p2CloisterButton, p2FarmerButton;
    private Button finalScoringButton;
    private int p1Score, p2Score;
    private int[] beforeFinalScore;
    private int[] formerScores;
    private Boolean isFinalScoring;
    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get intent and data from StartActivity
        Intent startIntent = getIntent();
        int[] playerColors = startIntent.getIntArrayExtra("playerColors");

        // set player pawns
        ImageView p1_color = findViewById(R.id.p1_color);
        p1_color.setImageResource(playerColors[0]);
        ImageView p2_color = findViewById(R.id.p2_color);
        p2_color.setImageResource(playerColors[1]);

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
        beforeFinalScore = new int[2];
        formerScores = new int[2];

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
        finalScoringButton = findViewById(R.id.final_scoring);
        finalScoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFinalScoring){
                    cancelFinalScoring(false);
                }
                else {
                    makeFinalScoring();
                }
            }
        });

        Button resetButton = findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetScore();
            }
        });

        Button undoButton = findViewById(R.id.undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoScore();
            }
        });
    }

    /**
     * OnClick method for the score buttons
     *
     * @param view
     */
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
                    p1Score = setScore(p1Score, 1, valueCloister);
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
                    p2Score = setScore(p2Score, 1, valueCloister);
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
        popupTilesWindow.setOutsideTouchable(true);
        final EditText userInput = popupTilesLayout.findViewById(R.id.popup_number_of_tiles);

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

    /**
     * Convert String (from user input) to int
     *
     * @param tilesString: String which should be converted to int
     * @return integer from the string (number of tiles)
     */
    private int convertStringToInt(String tilesString) {
        if (tilesString.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(tilesString);
        }
    }

    /**
     * Add points to current Score
     *
     * @param currentScore: old score of the current player
     * @param tiles: quantity of tiles
     * @param value: value of each tile, depends on which type was finished
     * @return new score
     */
    private int setScore(int currentScore, int tiles, int value) {
        formerScores[0] = p1Score;
        formerScores[1] = p2Score;
        if (tiles >= 1) {
            return currentScore + (tiles * value);
        } else return currentScore;
    }

    /**
     * Display player's score
     *
     * @param score: current score which should be displayed
     * @param scoreView: Score TextView of the current player
     */
    private void displayScore(int score, TextView scoreView) {
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Reset score, restart new game
     * reset scores to 0 and if finalScoring = true, make it false and go to normal screen
     */
    private void resetScore() {
        p1Score = 0;
        p2Score = 0;
        displayScore(p1Score, p1ScoreTextView);
        displayScore(p2Score, p2ScoreTextView);
        if (isFinalScoring) {
            cancelFinalScoring(true);
        }
    }

    /**
     * Undo last scoring step
     */
    private void undoScore() {
        p1Score = formerScores[0];
        p2Score = formerScores[1];
        displayScore(p1Score, p1ScoreTextView);
        displayScore(p2Score, p2ScoreTextView);
    }

    /**
     * Set final scoring to true and show final scoring buttons
     */
    private void makeFinalScoring() {
        isFinalScoring = true;
        beforeFinalScore[0] = p1Score;
        beforeFinalScore[1] = p2Score;
        // change finalScoringButton to cancel final scoring
        finalScoringButton.setText(R.string.cancel_final_scoring);
        Drawable buttonBackground = finalScoringButton.getBackground();
        changeBackgroundColor(buttonBackground, R.color.colorButtonAccent);
        // make farmer button visible
        p1FarmerButton.setVisibility(View.VISIBLE);
        p2FarmerButton.setVisibility(View.VISIBLE);
        // change text of city and cloister buttons
        p1CityButton.setText(R.string.city_and_shields_final);
        p2CityButton.setText(R.string.city_and_shields_final);
        p1CloisterButton.setText(R.string.cloister_final);
        p2CloisterButton.setText(R.string.cloister_final);
    }

    /**
     * Cancel final scoring and return to normal scoring
     * @param isReset: are teh scores reseted (true) or only the final scoring cancelled (false)
     */
    private void cancelFinalScoring(Boolean isReset){
        isFinalScoring = false;
        // change finalScoring button to normal
        finalScoringButton.setText(R.string.final_scoring);
        Drawable buttonBackground = finalScoringButton.getBackground();
        changeBackgroundColor(buttonBackground, R.color.colorButton);
        // make farmer button visible
        p1FarmerButton.setVisibility(View.INVISIBLE);
        p2FarmerButton.setVisibility(View.INVISIBLE);
        // change text of city and cloister buttons
        p1CityButton.setText(R.string.city_and_shields);
        p2CityButton.setText(R.string.city_and_shields);
        p1CloisterButton.setText(R.string.cloister);
        p2CloisterButton.setText(R.string.cloister);
        if (!isReset){
            p1Score = beforeFinalScore[0];
            p2Score = beforeFinalScore[1];
            displayScore(p1Score, p1ScoreTextView);
            displayScore(p2Score, p2ScoreTextView);
        }
    }

    private void changeBackgroundColor(Drawable background, int idColor){
        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(ContextCompat.getColor(this,idColor));
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(ContextCompat.getColor(this,idColor));
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(ContextCompat.getColor(this,idColor));
        }
    }
}
