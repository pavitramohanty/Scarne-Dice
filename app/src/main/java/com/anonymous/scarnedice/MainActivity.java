package com.anonymous.scarnedice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView tvGameStatus;
    ImageView diceImage;
    Button bRoll;
    Button bHold;
    Button bReset;

    int currentDiceValue = 1;
    int playerScore = 0, computerScore = 0, turnScore = 0;
    boolean isPlayerTurn = true;
    int images[] = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvGameStatus = (TextView) findViewById(R.id.gameStatus);
        diceImage = (ImageView) findViewById(R.id.ivDice);
        bRoll = (Button) findViewById(R.id.bRoll);
        bHold = (Button) findViewById(R.id.bHold);
        bReset = (Button) findViewById(R.id.bReset);

        updateUI();

        bRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlayerTurn) {
                    roll();
                }
            }
        });

        bHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlayerTurn) {
                    hold();
                }
            }
        });

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
    }

    public void roll() {
        currentDiceValue = new Random().nextInt(6) + 1;

        if (currentDiceValue == 1) {
            turnScore = 0;
            hold();
        } else {
            turnScore += currentDiceValue;
        }
        updateUI();
    }

    public void hold() {
        if (isPlayerTurn) {
            playerScore += turnScore;
        } else {
            computerScore += turnScore;
        }
        turnScore = 0;
        currentDiceValue = 1;
        updateUI();
        isPlayerTurn = !isPlayerTurn;
        if (computerScore >= 100 || playerScore >= 100) {
            Toast.makeText(this, (computerScore >= 100 ? "Computer" : "Player") + " wins!", Toast.LENGTH_SHORT).show();
            reset();
        }
        if (!isPlayerTurn) {
            bReset.setEnabled(false);
            bHold.setEnabled(false);
            bRoll.setEnabled(false);
            bReset.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            bHold.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            bRoll.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            }, 1000);
        } else {
            bReset.setEnabled(true);
            bHold.setEnabled(true);
            bRoll.setEnabled(true);
            bReset.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            bHold.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            bRoll.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }

    public void reset() {
        turnScore = computerScore = playerScore = 0;
        currentDiceValue = 1;
        isPlayerTurn = true;
        updateUI();

        Toast.makeText(this, "PLAY AGAIN!!!", Toast.LENGTH_SHORT).show();
    }

    public void computerTurn() {
        if (!isPlayerTurn) {
            if (turnScore < 15) {
                roll();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        computerTurn();
                    }
                }, 1000);
            } else {
                hold();
            }
        }
    }

    public void updateUI() {
        tvGameStatus.setText(" PLAYER SCORE: " + playerScore
                + "\n COMPUTER SCORE: " + computerScore
                + "\n TURN SCORE: " + turnScore);
        diceImage.setImageResource(images[currentDiceValue - 1]);
    }
}
