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

        ArrayList<ArrayList<HashMap<String, String>>> playersHistory = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
//            if (intent.getSerializableExtra("history") instanceof ArrayList) {
//                playersHistory = (ArrayList<ArrayList<HashMap<String, String>>>) intent.getSerializableExtra("history");
//            }
//            playersHistory = ((GameHistory) intent.getSerializableExtra("history")).history;
            playersHistory = (ArrayList<ArrayList<HashMap<String, String>>>) intent.getSerializableExtra("history");

            if (playersHistory == null) {
                playersHistory = new ArrayList<>();
            }
        }

        ArrayList<HashMap<String, String>> history = new ArrayList<>();

        HashMap<String, String> row = new HashMap<>();

        for (int playerIndex = 0; playerIndex < playersHistory.size(); ++playerIndex) {
            String prefix = String.format(Locale.getDefault(), "history_player%d", playerIndex);
            for (HashMap<String, String> play : playersHistory.get(playerIndex)) {
                for (Map.Entry<String, String> entry : play.entrySet()) {
                    row.put(String.format(Locale.getDefault(), "%s_%s", prefix, entry.getKey()), entry.getValue());
                }
                row.put("history_time", row.get(String.format(Locale.getDefault(), "%s_%s", prefix, "time")));
                row.put("history_turn", row.get(String.format(Locale.getDefault(), "%s_%s", prefix, "global_turn")));
                history.add(row);
            }
        }

        Log.i("GameHistoryActivity", String.valueOf(history.size()));

        SimpleAdapter adapter = new SimpleAdapter(this, history, R.layout.game_turn,
            new String[]{"history_time", "history_turn",
                "history_player0_life", "history_player0_commander", "history_player0_poison",
                "history_player1_life", "history_player1_commander", "history_player1_poison",
            },
            new int[]{R.id.history_time, R.id.history_turn,
                R.id.history_player0_life, R.id.history_player0_commander, R.id.history_player0_poison,
                R.id.history_player1_life, R.id.history_player1_commander, R.id.history_player1_poison,
            }
        );
        ListView gameHistory = findViewById(R.id.game_history);
        gameHistory.setAdapter(adapter);
    }
}
