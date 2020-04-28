package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity implements  AdapterView.OnItemClickListener {
    private ArrayList<GameHistory> histories_ = new ArrayList<>();

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
        setContentView(R.layout.activity_history);
        // Show back menu.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            //noinspection unchecked
            histories_ = (ArrayList<GameHistory>) intent.getSerializableExtra("game_histories");
        }

        ArrayList<HashMap<String, String>> historyItems = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd E hh:mm", Locale.US);

        for (int historyIndex = 0; historyIndex < histories_.size(); ++historyIndex) {
            HashMap<String, String> map = new HashMap<>();
            map.put("history_id", String.format(Locale.getDefault(), "%3d", historyIndex+1));
            GameHistory gh = histories_.get(historyIndex);
            map.put("history_date", format.format(gh.createdDate));
            map.put("history_title", gh.title);
            historyItems.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, historyItems, R.layout.history,
                new String[]{"history_id", "history_date", "history_title"},
                new int[]{R.id.history_id, R.id.history_date, R.id.history_title});

        ListView lv = findViewById(R.id.game_history);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, GameHistoryActivity.class);
        intent.putExtra("game_history", histories_.get(position));
        startActivity(intent);
    }
}
