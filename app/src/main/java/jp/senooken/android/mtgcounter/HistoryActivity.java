package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity implements  AdapterView.OnItemClickListener {
    private ArrayList<GameHistory> histories_ = new ArrayList<>();
    private SimpleAdapter adapter_;
    private final ArrayList<HashMap<String, String>> historyItems_ = new ArrayList<>();

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

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd E hh:mm", Locale.US);

        for (int historyIndex = 0; historyIndex < histories_.size(); ++historyIndex) {
            HashMap<String, String> map = new HashMap<>();
            map.put("history_id", String.format(Locale.getDefault(), "%d", historyIndex+1));
            GameHistory gh = histories_.get(historyIndex);
            map.put("history_date", format.format(gh.createdDate));
            map.put("history_title", gh.title);
            historyItems_.add(map);
        }

        adapter_ = new SimpleAdapter(this, historyItems_, R.layout.history,
                new String[]{"history_id", "history_date", "history_title"},
                new int[]{R.id.history_id, R.id.history_date, R.id.history_title});

        ListView lv = findViewById(R.id.game_history);
        lv.setAdapter(adapter_);
        lv.setOnItemClickListener(this);

        registerForContextMenu(lv);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, GameHistoryActivity.class);
        intent.putExtra("game_history", histories_.get(position));
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.history_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        int itemId = item.getItemId();
        if (itemId == R.id.menu_history_delete) {
            ArrayList<Integer> positions = new ArrayList<>();
            positions.add(position);
            deleteHistory(positions);
        }

        return super.onContextItemSelected(item);
    }

    public void onClickedDeleteAll(@SuppressWarnings("unused") View view) {
        ArrayList<Integer> positions = new ArrayList<>();
        for (int index = 0; index < histories_.size(); ++index) {
            positions.add(index);
        }

        deleteHistory(positions);
    }

    private void deleteHistory(ArrayList<Integer> positions) {
        Collections.sort(positions);
        for (int position = positions.size()-1; position >= 0; --position) {
            Log.i(this.getLocalClassName(), "index=" + position + ", pos=" + positions.get(position));
            historyItems_.remove(positions.get(position).intValue());
            histories_.remove(positions.get(position).intValue());
        }

        adapter_.notifyDataSetChanged();

        Intent intent = new Intent();
        intent.putExtra("game_histories", histories_);
        setResult(Activity.RESULT_OK, intent);

        FileOutputStream stream = null;
        try {
            stream = getApplicationContext().openFileOutput(MainActivity.HISTORY_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream object = new ObjectOutputStream(stream);
            object.writeObject(histories_);
            object.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
