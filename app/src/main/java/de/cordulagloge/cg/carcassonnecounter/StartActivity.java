package de.cordulagloge.cg.carcassonnecounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    int currentPlayer;
    TextView titleText;
    SparseIntArray colorDictionary;
    int[] selectedColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        selectedColors = new int[2];
        // set sparseIntArray to refer button id to selected color
        colorDictionary = new SparseIntArray();
        colorDictionary.put(R.id.black, R.drawable.meeple_black);
        colorDictionary.put(R.id.blue, R.drawable.meeple_blue);
        colorDictionary.put(R.id.red, R.drawable.meeple_red);
        colorDictionary.put(R.id.green, R.drawable.meeple_green);
        colorDictionary.put(R.id.yellow, R.drawable.meeple);
        titleText = findViewById(R.id.start_title);
        // set onClickListener for buttons
        ImageButton blackButton = findViewById(R.id.black);
        blackButton.setOnClickListener(this);
        ImageButton redButton = findViewById(R.id.red);
        redButton.setOnClickListener(this);
        ImageButton greenButton = findViewById(R.id.green);
        greenButton.setOnClickListener(this);
        ImageButton blueButton = findViewById(R.id.blue);
        blueButton.setOnClickListener(this);
        ImageButton yellowButton = findViewById(R.id.yellow);
        yellowButton.setOnClickListener(this);
        if (savedInstanceState != null){
            currentPlayer = savedInstanceState.getInt("currentPlayer");
            selectedColors = savedInstanceState.getIntArray("selectedColors");
            if (currentPlayer == 2){
                int index = colorDictionary.indexOfValue(selectedColors[0]);
                setPlayer2Selection(findViewById(colorDictionary.keyAt(index)));
            }
        }
        else{
            currentPlayer = 1;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("currentPlayer", currentPlayer);
        savedInstanceState.putIntArray("selectedColors", selectedColors);
    }

    @Override
    public void onClick(View selectedColorView) {
        if (currentPlayer == 1) {
            currentPlayer = 2;
            selectedColors[0] = colorDictionary.get(selectedColorView.getId());
            setPlayer2Selection(selectedColorView);
        } else if (currentPlayer == 2) {
            selectedColors[1] = colorDictionary.get(selectedColorView.getId());
            goToMainActivity();
        }
    }

    /**
     * change to player 2 and hide player 1 color
     * @param selectedColor player 1 color
     */
    private void setPlayer2Selection(View selectedColor) {
        titleText.setText(R.string.player_2_select_color);
        selectedColor.setVisibility(View.GONE);
    }

    /**
     * start main Activity and counter
     */
    private void goToMainActivity(){
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.putExtra("playerColors", selectedColors);
        startActivity(mainActivityIntent);
    }
}
