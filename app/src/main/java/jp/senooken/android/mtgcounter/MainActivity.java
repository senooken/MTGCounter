package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final String[] FROM = {"turnTime", "turnGlobal", "turnLocal", "turnLife", "turnCommander", "turnPoison", "turnComment"};
    private final int[] TO = {R.id.turnTime, R.id.turnGlobal, R.id.turnLocal, R.id.turnLife, R.id.turnComment};

    private final int totalPlayers_ = 2;
    private int turnGlobal_ = 1;

    private class GameHistory {
        private final ArrayList<ArrayList<HashMap<String, String>>> history;
        GameHistory(ArrayList<ArrayList<HashMap<String, String>>> hist) {
            history = hist;
        }
    }

    private GameHistory gameHistory_;
    private final ArrayList<GameHistory> gameHistories_ = new ArrayList<>();

    private class Player {
        private RadioButton life;
        private SimpleAdapter adapter;
        private final ArrayList<HashMap<String, String>> history = new ArrayList<>();
        private EditText comment;
        private RadioButton counter;
    }

    private final Player[] player_ = {new Player(), new Player()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < totalPlayers_; ++i) {
            Player player = player_[i];
            player.adapter = new HistoryListAdapter(this, player.history);
            ListView history = findViewById(getPlayerResourceId(i, "history"));
            history.setAdapter(player.adapter);
            player.life = findViewById(getPlayerResourceId(i, "life_player"));
            player.counter = player.life;
            player.comment = findViewById(getPlayerResourceId(i, "comment"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameHistory_ = new GameHistory(new ArrayList<ArrayList<HashMap<String, String>>>());
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            clearRadioButtonCheck((ViewGroup)view.getParent().getParent());
            int player_id = Integer.parseInt(getResources().getResourceEntryName(view.getId()).replaceAll("[^0-9]", ""));
            Player player = player_[player_id];
            player.counter = (RadioButton) view;
            player.counter.setChecked(true);
        }
    }

    private void clearRadioButtonCheck(ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); ++i) {
            if (group.getChildAt(i) instanceof ViewGroup ) {
                clearRadioButtonCheck((ViewGroup)group.getChildAt(i));
            }
            if (group.getChildAt(i) instanceof RadioButton) {
                RadioButton button = (RadioButton) group.getChildAt(i);
                button.setChecked(false);
            }
        }
    }

    public void onCommitButtonClicked(@SuppressWarnings("unused") View view) {
        for (int player_i = 0; player_i < totalPlayers_; ++player_i) {
            Player player = player_[player_i];
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
            HashMap<String, String>turn = new HashMap<>();
            turn.put("turnTime", sdf.format(now));
            turn.put("turnGlobal", String.format(Locale.getDefault(), "%02d", turnGlobal_));
            turn.put("turnLocal", String.format(Locale.getDefault(), "%02d", turnGlobal_/2+turnGlobal_%2));

            turn.put("turnLife", player.life.getText().toString());
            RadioButton commander = findViewById(getPlayerResourceId(player_i, "life_commander"));
            turn.put("turnCommander", commander.getText().toString());
            RadioButton poison = findViewById(getPlayerResourceId(player_i, "life_poison"));
            turn.put("turnPoison", poison.getText().toString());

            turn.put("turnComment", player.comment.getText().toString());
            player.history.add(turn);
            player.adapter.notifyDataSetChanged();

            // Reset turn comment.
            player.comment.setText("");

            ListView lv = findViewById(getPlayerResourceId(player_i, "history"));
            lv.smoothScrollToPosition(player.history.size());

            if (gameHistory_.history.size() < totalPlayers_) {
                gameHistory_.history.add(new ArrayList<HashMap<String, String>>());
            }
            gameHistory_.history.set(player_i, player.history);
        }
        ++turnGlobal_;
    }

    public void onPlusButtonClicked(@SuppressWarnings("unused") View view) {
        int player_id = Integer.parseInt(getResources().getResourceEntryName(view.getId()).replaceAll("[^0-9]", ""));
        Player player = player_[player_id];
        player.counter.setText(String.format(Locale.getDefault(), "%02d",
                Integer.parseInt(player.counter.getText().toString())+1));
    }

    public void onMinusButtonClicked(@SuppressWarnings("unused") View view) {
        int player_id = Integer.parseInt(getResources().getResourceEntryName(view.getId()).replaceAll("[^0-9]", ""));
        Player player = player_[player_id];
        player.counter.setText(String.format(Locale.getDefault(), "%02d",
                Integer.parseInt(player.counter.getText().toString())-1));
    }

    public void onPendingButtonClicked(@SuppressWarnings("unused") View view) {
        int player_id = Integer.parseInt(getResources().getResourceEntryName(view.getId()).replaceAll("[^0-9]", ""));
        Player player = player_[player_id];
        StringBuilder buffer = new StringBuilder(player.comment.getText().toString());
        if (!player.comment.getText().toString().isEmpty()) {
            buffer.append(", ");
        }
        buffer.append(player.life.getText().toString());
        player.comment.setText(buffer.toString());
    }

    private class HistoryListAdapter extends SimpleAdapter {
        private final ArrayList<? extends HashMap<String, String>> data_;

        HistoryListAdapter(Context context, ArrayList<? extends HashMap<String, String>> data) {
            super(context, data, R.layout.turn, FROM, TO);
            data_ = data;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.turn, parent, false);
            }

            TextView turnTime = convertView.findViewById(R.id.turnTime);
            turnTime.setText(data_.get(position).get("turnTime"));
            TextView turnGlobal = convertView.findViewById(R.id.turnGlobal);
            turnGlobal.setText(data_.get(position).get("turnGlobal"));
            TextView turnLocal = convertView.findViewById(R.id.turnLocal);
            turnLocal.setText(data_.get(position).get("turnLocal"));

            final EditText turnLife = convertView.findViewById(R.id.turnLife);
            turnLife.setText(data_.get(position).get("turnLife"));
            turnLife.addTextChangedListener(new HistoryWatcher(data_.get(position), "turnLife"));

            final EditText turnCommander = convertView.findViewById(R.id.turnCommander);
            turnCommander.setText(data_.get(position).get("turnCommander"));
            turnCommander.addTextChangedListener(new HistoryWatcher(data_.get(position), "turnCommander"));

            final EditText turnPoison = convertView.findViewById(R.id.turnPoison);
            turnPoison.setText(data_.get(position).get("turnPoison"));
            turnPoison.addTextChangedListener(new HistoryWatcher(data_.get(position), "turnPoison"));

            EditText turnComment = convertView.findViewById(R.id.turnComment);
            turnComment.setText(data_.get(position).get("turnComment"));
            turnComment.addTextChangedListener(new HistoryWatcher(data_.get(position), "turnComment"));

            return convertView;
        }

        private class HistoryWatcher implements TextWatcher {
            private final HashMap<String, String> map_;
            private final String key_;
            HistoryWatcher(HashMap<String, String> map, String key) {
                map_ = map;
                key_ = key;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                map_.put(key_, s.toString());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_save) {
            gameHistories_.add(0, gameHistory_);
        } else if (itemId == R.id.menu_reset) {
            turnGlobal_ = 1;
            for (int i = 0; i < totalPlayers_; ++i) {
                Player player = player_[i];
                player.life.setText(R.string.life);
                player.history.clear();
                player.adapter.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private int getPlayerResourceId(int player, String key) {
        return getResources().getIdentifier(
                String.format(Locale.getDefault(), "player%d_%s", player, key), "id", getPackageName());
    }
}
