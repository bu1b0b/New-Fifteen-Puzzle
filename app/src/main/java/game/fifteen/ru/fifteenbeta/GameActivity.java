package game.fifteen.ru.fifteenbeta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import game.fifteen.ru.matrix.Matrix;

public class GameActivity extends Activity {

    final String bestRecord = "FifteenRecord";
    final Handler uiHandler = new Handler();
    String countMoves = "Moves: ";
    String timeText = "Time: ";
    SharedPreferences sPref;
    Timer myTimer;
    GameTimerTask gameTimerTask;
    boolean bb = false;
    int gameTime = 0;
    private Matrix matrix;
    private TextView playerMovesTextView;
    private TextView gameTimer;
    private GridLayout grid;
    private Button[][] buttons;
    private int[][] currentGameMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        matrix = new Matrix();
        grid = (GridLayout) findViewById(R.id.gridLayout);
        playerMovesTextView = (TextView) findViewById(R.id.playerMovesText);
        gameTimer = (TextView) findViewById(R.id.gameTimer);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        startNewGame(new View(getApplicationContext()));
        startTimer();
    }

    @Override
    protected void onStop() {
        gameTime = gameTimerTask.getGameTime();
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        gameTimerTask.setGameTime(gameTime);
    }

    public void startTimer() {
        myTimer = new Timer();
        gameTimerTask = new GameTimerTask();
        myTimer.schedule(gameTimerTask, 0L, 1000L * 1);
        gameTimerTask.setGameTime(0);
    }

    public void showWinAlertDialog(View v) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(GameActivity.this);

        DecimalFormat alertDecimalFormat = new DecimalFormat("00");

        int winTime = gameTimerTask.getGameTime();

        alertBuilder.setTitle("Congratulations!")
                .setMessage(countMoves + matrix.getPlayerMoves() + "\n" + timeText + alertDecimalFormat.format(winTime / 60)
                        + ":" + alertDecimalFormat.format(winTime % 60))
                .setCancelable(false)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        initGame();
                    }
                });

        AlertDialog alert = alertBuilder.create();
        alert.show();

        sPref = getSharedPreferences(bestRecord, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        loadRecord(winTime, matrix.getPlayerMoves());
    }

    public void loadRecord(int gameTime, int gameMove) {
        SharedPreferences.Editor ed = sPref.edit();
        boolean hasVisited = sPref.getBoolean("hasVisited", false);
        ArrayList<Integer> gamesTime = new ArrayList<Integer>();
        ArrayList<Integer> gamesMove = new ArrayList<Integer>();

        for (int i = 0; i < 10; i++) {
            int time = sPref.getInt(("gameTime_" + i), 0);
            int move = sPref.getInt(("gameMove_" + i), 0);
            if (move != 0 && time != 0) {
                gamesTime.add(time);
                gamesMove.add(move);
            }
        }

        //String TAG = "MyActivity";
        //Log.v(TAG, "Количество рекордов - " + gamesTime.size());
        int sss = gamesTime.size();

        if (!hasVisited) {          //первая победа
            ed.putBoolean("hasVisited", true);
            gamesTime.add(gameTime);
            gamesMove.add(gameMove);
        } else {
            for (int i = 0; i < sss; i++) {
                if (gameTime <= gamesTime.get(i) && gameMove <= gamesMove.get(i)) {
                    gamesTime.add(i, gameTime);
                    gamesMove.add(i, gameMove);
                    break;
                } else if (gameTime > gamesTime.get(i) && gameMove > gamesMove.get(i)) {
                    gamesTime.add(gameTime);
                    gamesMove.add(gameMove);
                }
            }
        }

        for (int i = 0; i < gamesTime.size(); i++) {
            if (i < 10) {
                ed.putInt(("gameTime_" + i), gamesTime.get(i));
                ed.putInt(("gameMove_" + i), gamesMove.get(i));
                ed.commit();
            }
        }


        ed = null;
        gamesTime = null;
        gamesMove = null;
    }

    public void showPlayerMoves() {
        playerMovesTextView.setText(countMoves + matrix.getPlayerMoves());
    }

    public void initGame() {
        bb = true;
        if (gameTimerTask != null) {
            gameTimerTask.setGameTime(-1);
        }
        matrix.initMatrix();
        currentGameMatrix = matrix.getMatrix();

        initButtons();
        addTextToButtons();

    }

    public void startNewGame(View v) {
        initGame();
    }

    public void initButtons() {
        buttons = new Button[4][4];
        int count = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = (Button) grid.getChildAt(count);
                buttons[i][j].setOnClickListener(new MyListener(i, j));
                count++;
            }
        }
    }

    public void addTextToButtons() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String buttonText = "" + currentGameMatrix[i][j];
                buttons[i][j].setText(buttonText);
                buttons[i][j].setTextSize(30);
                buttons[i][j].setVisibility(View.VISIBLE);
                if (buttons[i][j].getText().toString().equals("0")) {
                    buttons[i][j].setVisibility(View.INVISIBLE);
                }
            }
        }
        showPlayerMoves();
    }

    private void checkWin() {
        int count = 1;
        int err = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (currentGameMatrix[i][j] != count) {
                    err++;
                }
                count++;
            }
        }
        if (err == 1) {
            bb = false;
            showWinAlertDialog(new View(getApplicationContext()));
        }
    }

    public void goToMainMenu(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

    class GameTimerTask extends TimerTask {
        private DecimalFormat decimalFormat = new DecimalFormat("00");
        private volatile int time = 0;

        public int getGameTime() {
            return time;
        }

        public void setGameTime(int t) {
            this.time = t;
        }

        @Override
        public void run() {

            if (bb == true) {
                time++;
            }

            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    int t = time;
                    gameTimer.setText(timeText + decimalFormat.format(t / 60)
                            + ":" + decimalFormat.format(t % 60));
                }

            });
        }

    }

    class MyListener implements View.OnClickListener {

        int posI;
        int posJ;

        public MyListener(int x, int y) {
            this.posI = x;
            this.posJ = y;
        }

        @Override
        public void onClick(View view) {
            matrix.setUserMove(this.posI, this.posJ);
            currentGameMatrix = matrix.getMatrix();
            addTextToButtons();
            checkWin();
        }
    }

}



