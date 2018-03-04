package de.cordulagloge.cg.carcassonnecounter;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    int currentPlayer;
    TextView titleText;
    int p1_color;
    SparseIntArray colorDictionary;
    int[] selectedColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        selectedColors = new int[2];

        // set sparseIntArray to refer button id to selected color
        colorDictionary = new SparseIntArray();
        colorDictionary.put(R.id.black, R.drawable.ic_pawn_black_24dp);
        colorDictionary.put(R.id.blue, R.drawable.ic_pawn_blue_24dp);
        colorDictionary.put(R.id.red, R.drawable.ic_pawn_red_24dp);
        colorDictionary.put(R.id.green, R.drawable.ic_pawn_green_24dp);
        colorDictionary.put(R.id.yellow, R.drawable.ic_pawn_yellow_24dp);

        titleText = findViewById(R.id.start_title);


        // set onclicklistener for buttons
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

    private void setPlayer2Selection(View selectedColor) {
        titleText.setText("Player 2 select your color");
        selectedColor.setVisibility(View.GONE);
    }

    private void goToMainActivity(){
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.putExtra("playerColors", selectedColors);
        startActivity(mainActivityIntent);
    }
}
