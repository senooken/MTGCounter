package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity implements  AdapterView.OnItemClickListener {
//    private ArrayList<GameHistory> histories_ = new ArrayList<GameHistory>();
    private ArrayList<ArrayList<ArrayList<HashMap<String, String>>>> histories_ = new ArrayList<ArrayList<ArrayList<HashMap<String, String>>>>();

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
//            histories_ = (ArrayList<GameHistory>) intent.getSerializableExtra("history");
            histories_ = (ArrayList<ArrayList<ArrayList<HashMap<String, String>>>>) intent.getSerializableExtra("history");
        }

        // 履歴に存在する最初のコミット

        ArrayList<String> histItems = new ArrayList<String>();
        for (int hist_i = 0; hist_i < histories_.size(); ++hist_i) {
            histItems.add(String.valueOf(hist_i));
        }

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, histItems);
//        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.about,
//                new String[]{"about_key", "about_value"}, new int[]{R.id.about_key, R.id.about_value});

        ListView lv = findViewById(R.id.history);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

//    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Intent intent = new Intent(this, GameHistoryActivity.class);
            Intent intent = new Intent(this, GameHistoryActivity.class);
            intent.putExtra("history", histories_.get(position));
            startActivity(intent);
        }

//    }
}
