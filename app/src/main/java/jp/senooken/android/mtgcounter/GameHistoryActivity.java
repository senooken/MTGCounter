package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GameHistoryActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);
        // Show back menu.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayList<HashMap<String, String>> history = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
//            if (intent.getSerializableExtra("history") instanceof ArrayList) {
//                history = (ArrayList<ArrayList<HashMap<String, String>>>) intent.getSerializableExtra("history");
//            }
//            history = ((GameHistory) intent.getSerializableExtra("history")).history;
            history = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("history");

            if (history == null) {
                history = new ArrayList<>();
            }
        }

        HashMap<String, String> row = new HashMap<>();
        HistoryListAdapter adapter = new HistoryListAdapter(this, history);

        ListView gameHistory = findViewById(R.id.game_history);
        gameHistory.setAdapter(adapter);
    }
}
