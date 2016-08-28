package game.fifteen.ru.fifteenbeta;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public void startGame(View v) {
        startActivity(new Intent(this, GameActivity.class));
    }

    public void showHelpPage(View view) {
        startActivity(new Intent(this, HelpActivity.class));
    }

    public void showRecordsPage(View view) {
        startActivity(new Intent(this, RecordsActivity.class));
    }

}
