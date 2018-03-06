package de.cordulagloge.cg.carcassonnecounter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView p1ScoreTextView, p2ScoreTextView;
    private Button p1FarmerButton;
    private Button p2FarmerButton;
    private Button finalScoringButton;
    private int p1Score, p2Score;
    private int[] beforeFinalScore;
    private int[] formerScores;
    private Boolean isFinalScoring;
    private ScrollView rootLayout;
    private int[] playerColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get intent and data from StartActivity
        Intent startIntent = getIntent();
        playerColors = startIntent.getIntArrayExtra("playerColors");
        // set player pawns
        ImageView p1_color = findViewById(R.id.p1_color);
        p1_color.setImageResource(playerColors[0]);
        ImageView p2_color = findViewById(R.id.p2_color);
        p2_color.setImageResource(playerColors[1]);
        // get global variables
        p1ScoreTextView = findViewById(R.id.p1_score);
        p1ScoreTextView.setTag(playerColors[0]);
        p2ScoreTextView = findViewById(R.id.p2_score);
        p2ScoreTextView.setTag(playerColors[1]);
        rootLayout = findViewById(R.id.root_layout);
        beforeFinalScore = new int[2];
        formerScores = new int[2];
        // Buttons Player 1
        Button p1RoadButton = findViewById(R.id.p1_road_button);
        p1RoadButton.setOnClickListener(this);
        Button p1CityButton = findViewById(R.id.p1_city_button);
        p1CityButton.setOnClickListener(this);
        Button p1CloisterButton = findViewById(R.id.p1_cloister_button);
        p1CloisterButton.setOnClickListener(this);
        p1FarmerButton = findViewById(R.id.p1_farmer_button);
        p1FarmerButton.setOnClickListener(this);
        // Buttons Player 2
        Button p2RoadButton = findViewById(R.id.p2_road_button);
        p2RoadButton.setOnClickListener(this);
        Button p2CityButton = findViewById(R.id.p2_city_button);
        p2CityButton.setOnClickListener(this);
        Button p2CloisterButton = findViewById(R.id.p2_cloister_button);
        p2CloisterButton.setOnClickListener(this);
        p2FarmerButton = findViewById(R.id.p2_farmer_button);
        p2FarmerButton.setOnClickListener(this);
        // Set onclicklistener for final Scoring button
        finalScoringButton = findViewById(R.id.final_scoring);
        finalScoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFinalScoring) {
                    cancelFinalScoring(false);
                } else {
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
        Button endButton = findViewById(R.id.end_game);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endGame();
            }
        });
        if (savedInstanceState != null) {
            p1Score = savedInstanceState.getInt("p1Score");
            p2Score = savedInstanceState.getInt("p2Score");
            isFinalScoring = savedInstanceState.getBoolean("isFinalScoring");
            if (isFinalScoring) {
                makeFinalScoring();
            }
            formerScores = savedInstanceState.getIntArray("formerScores");
            beforeFinalScore = savedInstanceState.getIntArray("beforeFinalScore");
        } else {
            //set Values
            p1Score = 0;
            p2Score = 0;
            isFinalScoring = false;
        }
        displayScore(p1Score, p1ScoreTextView);
        displayScore(p2Score, p2ScoreTextView);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        changeBackgroundColor(getResources().getDrawable(R.drawable.main_buttons_normal), R.color.colorButton);
        savedInstanceState.putInt("p1Score", p1Score);
        savedInstanceState.putInt("p2Score", p2Score);
        savedInstanceState.putBoolean("isFinalScoring", isFinalScoring);
        savedInstanceState.putIntArray("formerScores", formerScores);
        savedInstanceState.putIntArray("beforeFinalScore", beforeFinalScore);
    }

    /**
     * OnClick method for the score buttons
     *
     * @param view clicked button
     */
    @Override
    public void onClick(View view) {
        String popupTitleText;
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
                popupTitleText = setTitleText(getString(R.string.popup_road, valueRoad));
                setPopUpWindow(p1ScoreTextView, p1Score, valueRoad, popupTitleText);
                break;
            case R.id.p1_city_button:
                popupTitleText = setTitleText(getString(R.string.popup_city, valueCity));
                setPopUpWindow(p1ScoreTextView, p1Score, valueCity, popupTitleText);
                break;
            case R.id.p1_cloister_button:
                popupTitleText = setTitleText(getString(R.string.popup_cloister, valueCloister));
                if (isFinalScoring) {
                    setPopUpWindow(p1ScoreTextView, p1Score, valueCloister, popupTitleText);
                } else {
                    p1Score = setScore(p1Score, 1, valueCloister);
                    displayScore(p1Score, p1ScoreTextView);
                }
                break;
            case R.id.p1_farmer_button:
                popupTitleText = setTitleText(getString(R.string.popup_farmer, valueFarmer));
                setPopUpWindow(p1ScoreTextView, p1Score, valueFarmer, popupTitleText);
                break;
            // Player 2 scores
            case R.id.p2_road_button:
                popupTitleText = setTitleText(getString(R.string.popup_road, valueRoad));
                setPopUpWindow(p2ScoreTextView, p2Score, valueRoad, popupTitleText);
                break;
            case R.id.p2_city_button:
                popupTitleText = setTitleText(getString(R.string.popup_city, valueCity));
                setPopUpWindow(p2ScoreTextView, p2Score, valueCity, popupTitleText);
                break;
            case R.id.p2_cloister_button:
                popupTitleText = setTitleText(getString(R.string.popup_cloister, valueCloister));
                if (isFinalScoring) {
                    setPopUpWindow(p2ScoreTextView, p2Score, valueCloister, popupTitleText);
                } else {
                    p2Score = setScore(p2Score, 1, valueCloister);
                    displayScore(p2Score, p2ScoreTextView);
                }
                break;
            case R.id.p2_farmer_button:
                popupTitleText = setTitleText(getString(R.string.popup_farmer, valueFarmer));
                setPopUpWindow(p2ScoreTextView, p2Score, valueFarmer, popupTitleText);
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
    private void setPopUpWindow(final TextView currentPlayer, final int currentScore, final int currentValue, String popupTitleText) {
        LayoutInflater popupTilesInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupTilesLayout = popupTilesInflater.inflate(R.layout.popup_window, null);
        final PopupWindow popupTilesWindow = new PopupWindow(popupTilesLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupTilesWindow.setOutsideTouchable(true);

        final EditText userInput = popupTilesLayout.findViewById(R.id.popup_number_of_tiles);
        // set Description Text in popup window
        TextView popupDescription = popupTilesLayout.findViewById(R.id.popup_description);
        popupDescription.setText(popupTitleText);
        int playerColor = (int) currentPlayer.getTag();
        popupDescription.setCompoundDrawablesWithIntrinsicBounds(playerColor, 0, 0, 0);

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
                if (score > 1) {
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
     * set text for the popup title
     *
     * @param value: finished object and its multiplier
     * @return string for the popup title
     */
    private String setTitleText(String value) {
        StringBuilder titleText = new StringBuilder();
        if (isFinalScoring) {
            titleText.append("Final scoring:\nScores  ");
        } else {
            titleText.append("Scores ");
        }
        titleText.append(value);
        return titleText.toString();
    }

    /**
     * Add points to current Score
     *
     * @param currentScore: old score of the current player
     * @param tiles:        quantity of tiles
     * @param value:        value of each tile, depends on which type was finished
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
     * @param score:     current score which should be displayed
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
    }

    /**
     * Cancel final scoring and return to normal scoring
     *
     * @param isReset: are teh scores reseted (true) or only the final scoring cancelled (false)
     */
    private void cancelFinalScoring(Boolean isReset) {
        isFinalScoring = false;
        // change finalScoring button to normal
        finalScoringButton.setText(R.string.final_scoring);
        Drawable buttonBackground = finalScoringButton.getBackground();
        changeBackgroundColor(buttonBackground, R.color.colorButton);
        // make farmer button visible
        p1FarmerButton.setVisibility(View.INVISIBLE);
        p2FarmerButton.setVisibility(View.INVISIBLE);
        if (!isReset) {
            p1Score = beforeFinalScore[0];
            p2Score = beforeFinalScore[1];
            displayScore(p1Score, p1ScoreTextView);
            displayScore(p2Score, p2ScoreTextView);
        }
    }

    /**
     * change background color of buttons
     *
     * @param background: .xml which color should be changed
     * @param idColor:    new color
     */
    private void changeBackgroundColor(Drawable background, int idColor) {
        LayerDrawable layerDrawable = (LayerDrawable) background;
        Drawable buttonForeground = layerDrawable.findDrawableByLayerId(R.id.button_foreground);
        if (buttonForeground instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) buttonForeground;
            shapeDrawable.getPaint().setColor(ContextCompat.getColor(this, idColor));
        } else if (buttonForeground instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) buttonForeground;
            gradientDrawable.setColor(ContextCompat.getColor(this, idColor));
        } else if (buttonForeground instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) buttonForeground;
            colorDrawable.setColor(ContextCompat.getColor(this, idColor));
        }
    }

    /**
     * End the game and show winner
     */
    private void endGame() {
        int[] winner = findWinner();
        changeBackgroundColor(getResources().getDrawable(R.drawable.main_buttons_normal), R.color.colorButton);
        LayoutInflater popupWinnerInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupWinnerLayout = popupWinnerInflater.inflate(R.layout.popup_winner, null);
        final PopupWindow popupWinner = new PopupWindow(popupWinnerLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWinner.setOutsideTouchable(true);
        ImageView winnerImage = popupWinnerLayout.findViewById(R.id.winner_image);
        TextView winnerText = popupWinnerLayout.findViewById(R.id.popup_description);
        if (winner[0] != 0) {
            winnerImage.setImageResource(winner[0]);
            winnerText.setText(getString(R.string.wins, winner[1]));
        } else {
            winnerText.setText(getString(R.string.tie, winner[1]));
        }
        Button okButton = popupWinnerLayout.findViewById(R.id.popup_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startNewIntent = new Intent(MainActivity.this, StartActivity.class);
                startActivity(startNewIntent);
            }
        });
        Button cancelButton = popupWinnerLayout.findViewById(R.id.popup_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackgroundColor(getResources().getDrawable(R.drawable.main_buttons_normal), R.color.colorButtonAccent);
                popupWinner.dismiss();
            }
        });
        popupWinner.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
    }

    /**
     * Compare scores of players to find winner
     *
     * @return winner and winner score
     */
    private int[] findWinner() {
        if (p1Score > p2Score) {
            return new int[]{playerColors[0], p1Score};
        } else if (p2Score > p1Score) {
            return new int[]{playerColors[1], p2Score};
        } else {
            return new int[]{0, p1Score};
        }
    }
}
