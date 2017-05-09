package com.wanderer.tictactoe;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TicTacToeActivity extends AppCompatActivity implements View.OnClickListener {

    //define sharedPreferences value
    private SharedPreferences savedValues;

    //declare variables for widgets
    private Button square1a;
    private Button square1b;
    private Button square1c;
    private Button square2a;
    private Button square2b;
    private Button square2c;
    private Button square3a;
    private Button square3b;
    private Button square3c;
    private TextView gameStatusTextView;
    private Button newGameButton;

    private int turn;
    private boolean gameOver;
    private Button gameBoard[][];
    private String winner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        //get references to widgets
        square1a = (Button) findViewById(R.id.square1a);
        square1b = (Button) findViewById(R.id.square1b);
        square1c = (Button) findViewById(R.id.square1c);
        square2a = (Button) findViewById(R.id.square2a);
        square2b = (Button) findViewById(R.id.square2b);
        square2c = (Button) findViewById(R.id.square2c);
        square3a = (Button) findViewById(R.id.square3a);
        square3b = (Button) findViewById(R.id.square3b);
        square3c = (Button) findViewById(R.id.square3c);
        newGameButton = (Button) findViewById(R.id.newGameButton);
        gameStatusTextView = (TextView) findViewById(R.id.gameStatusTextView);

        //initialize instance variables
        turn = 0;
        gameOver = false;

        //set listeners
        square1a.setOnClickListener(this);
        square1b.setOnClickListener(this);
        square1c.setOnClickListener(this);
        square2a.setOnClickListener(this);
        square2b.setOnClickListener(this);
        square2c.setOnClickListener(this);
        square3a.setOnClickListener(this);
        square3b.setOnClickListener(this);
        square3c.setOnClickListener(this);
        newGameButton.setOnClickListener(this);

        //make the game board using a 2d array
        gameBoard = new Button[][]{
                {square1a,square1b,square1c},
                {square2a,square2b,square2c},
                {square3a,square3b,square3c}
        };

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause(){
        SharedPreferences.Editor editor = savedValues.edit();

        editor.putString("gameStatusText",gameStatusTextView.getText().toString());
        editor.putInt("turn", turn);
        editor.putBoolean("gameOver",gameOver);

        editor.putString("1a",square1a.getText().toString());
        editor.putString("1b",square1b.getText().toString());
        editor.putString("1c",square1c.getText().toString());
        editor.putString("2a",square2a.getText().toString());
        editor.putString("2b",square2b.getText().toString());
        editor.putString("2c",square2c.getText().toString());
        editor.putString("3a",square3a.getText().toString());
        editor.putString("3b",square3b.getText().toString());
        editor.putString("3c",square3c.getText().toString());

        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();

        //Recall and reset the game state
        gameStatusTextView.setText(savedValues.getString("gameStatusText",""));
        turn = savedValues.getInt("turn",0);
        gameOver = savedValues.getBoolean("gameOver",false);

        square1a.setText(savedValues.getString("1a",""));
        square1b.setText(savedValues.getString("1b",""));
        square1c.setText(savedValues.getString("1c",""));
        square2a.setText(savedValues.getString("2a",""));
        square2b.setText(savedValues.getString("2b",""));
        square2c.setText(savedValues.getString("2c",""));
        square3a.setText(savedValues.getString("3a",""));
        square3b.setText(savedValues.getString("3b",""));
        square3c.setText(savedValues.getString("3c",""));

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newGameButton:
                startNewGame();
                break;
            default:
                if(!gameOver){
                    //checkGame() will return 0 for an ongoing game, 1 for X winning, 2 for O winning, 3 for a draw
                    int gameState = 0;
                    Button b = (Button) v;
                    if (turn % 2 == 0 && b.getText().toString().equals("")){
                        b.setText("X");

                        gameState = checkGame();
                        if (gameState == 1 || gameState == 3)
                        {
                            break;
                        }
                        turn ++;
                        gameStatusTextView.setText("O's Turn");
                    }
                    else if (turn % 2 == 1 && b.getText().toString().equals("")){
                        b.setText("O");
                        gameState = checkGame();
                        if (gameState == 2 || gameState == 3)
                        {
                            break;
                        }
                        turn ++;
                        gameStatusTextView.setText("X's Turn");
                    }
                }

                break;
        }

    }

    // return 0 for ongoing game, 1 for X winning, 2 for O winning, 3 for a draw
    private int checkGame(){

        // if there's an empty square and nobody has won then canPlay is set to true
        boolean canPlay = false;

        //check diagonals
        if ((
                (getSquareState(gameBoard[0][0]) == getSquareState(gameBoard[1][1]) && getSquareState(gameBoard[1][1]) == getSquareState(gameBoard[2][2]))
                        ||
                        (getSquareState(gameBoard[0][2]) == getSquareState(gameBoard[1][1]) && getSquareState(gameBoard[1][1]) == getSquareState(gameBoard[2][0]))
                ) && getSquareState(gameBoard[1][1]) != 0)
        {
            gameOver = true;
            if (getSquareState(gameBoard[1][1]) == 1){
                gameStatusTextView.setText("X Wins!");
                return 1;
            }else {
                gameStatusTextView.setText("O Wins!");
                return 2;
            }
        }

        //check rows and columns
        for(int i = 0; i < 3; i++){

            //check rows
            if (
                    getSquareState(gameBoard[i][0]) == getSquareState(gameBoard[i][1])
                    && getSquareState(gameBoard[i][1]) == getSquareState(gameBoard[i][2])
                    && getSquareState(gameBoard[i][0]) != 0)
            {
                gameOver = true;
                if (getSquareState(gameBoard[i][0]) == 1)
                {
                    gameStatusTextView.setText("X Wins!");
                    return 1;
                }else {
                    gameStatusTextView.setText("O Wins!");
                    return 2;
                }
            }

            //check columns
            if (
                    getSquareState(gameBoard[0][i]) == getSquareState(gameBoard[1][i])
                    && getSquareState(gameBoard[1][i]) == getSquareState(gameBoard[2][i])
                    && getSquareState(gameBoard[0][i]) != 0)
            {
                gameOver = true;
                if (getSquareState(gameBoard[0][i]) == 1){
                    gameStatusTextView.setText("X Wins!");
                    return 1;
                }else {
                    gameStatusTextView.setText("O Wins!");
                    return 2;
                }
            }

            //check for empty squares to see if play can continue
            for (int j = 0; j < 3; j++){
                if(getSquareState(gameBoard[i][j]) == 0){
                    canPlay = true;
                };
            }
        }
        if(canPlay){
            //There is at least one empty square to play
            return 0;
        } else{
            //Nobody has won and no one can play so it's a draw
            gameOver = true;
            gameStatusTextView.setText("It's a Draw!");
            return 3;
        }

    }

    // return 1 for X, 2 for O and, 0 (zero) for empty
    private int getSquareState(Button b){
        if (b.getText().toString().equals("X")){
            return 1;
        }
        else if (b.getText().toString().equals("O")){
            return 2;
        }
        else {
            return 0;
        }
    }

    private void startNewGame(){
        turn = 0;
        gameOver = false;
        //X always goes first
        gameStatusTextView.setText("X's Turn");

        //clear the board
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                gameBoard[i][j].setText("");
            }
        }
    }

}
