package game.fifteen.ru.fifteenbeta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Lokki on 01.12.2015.
 */
public class RecordsActivity extends Activity {

    SharedPreferences sharedPref;
    final String bestRecord = "FifteenRecord";
    ListView recordsList;

    ArrayAdapter<String> recordAdapter;

    ArrayList<String> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.records);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        recordsList = (ListView) findViewById(R.id.records_listView);

        loadRecord();


    }


    public void loadRecord() {

        sharedPref = getSharedPreferences(bestRecord, MODE_PRIVATE);

        records = new ArrayList<String>();
        DecimalFormat decimalFormat = new DecimalFormat("00");

        for (int i = 0; i < 10; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            int gameTime = sharedPref.getInt(("gameTime_" + i), 0);
            int moves = sharedPref.getInt(("gameMove_" + i), 0);

            if (moves != 0) {
                stringBuilder.append(i + 1);
                stringBuilder.append(". ");
                if (gameTime < 60) {
                    stringBuilder.append(decimalFormat.format(gameTime % 60) + " sec, ");
                    stringBuilder.append(moves + " moves");
                } else {
                    stringBuilder.append(decimalFormat.format(gameTime / 60) + " min ");
                    stringBuilder.append(decimalFormat.format(gameTime % 60) + " sec, ");
                    stringBuilder.append(moves + " moves");
                }
                records.add(stringBuilder.toString());
            }

        }

        recordAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, records) {
            @Override
            public boolean isEnabled(int position) {
                return false;
            }
        };
        recordsList.setAdapter(recordAdapter);


        /*
        int gameTime = sharedPref.getInt("gameTime", 0);
        int moves = sharedPref.getInt("moves", 0);
        String ss;

        if (moves != 0) {
            if (gameTime < 60) {
                ss = "  " + decimalFormat.format(gameTime % 60) + " sec, " + moves + " moves";
            } else {
                ss = "  " + decimalFormat.format(gameTime / 60) + " min " + decimalFormat.format(gameTime % 60) + " sec, " + moves + " moves";
            }

            //recordTextView.setText(recordTextView.getText() + ss);
            //recordTextView.setVisibility(View.VISIBLE);
        }
        */

    }

    public void startGame(View v) {
        startActivity(new Intent(this, GameActivity.class));
    }

    public void goToMainMenu(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

}
