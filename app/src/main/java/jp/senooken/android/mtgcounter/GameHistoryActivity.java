package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

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

        Intent intent = getIntent();
        GameHistory history = (GameHistory) intent.getSerializableExtra("game_history");

        assert history != null;
        HistoryListAdapter adapter = new HistoryListAdapter(this, getLayoutInflater(), history.history);

        ListView gameHistory = findViewById(R.id.game_history);
        gameHistory.setAdapter(adapter);

        EditText et = findViewById(R.id.game_title);
        et.setText(history.title);
    }
}
