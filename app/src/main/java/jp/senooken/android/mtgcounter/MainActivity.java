package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final String[] FROM = {"turn_time", "turn_global", "turn_life", "turn_commander", "turn_poison", "turn_comment"};
    private final int[] TO = {R.id.turn_time, R.id.turn_global, R.id.turn_player0_life, R.id.turn_player0_commander, R.id.turn_player0_poison, R.id.turn_player0_comment};
    private final String HISTORY_FILE = "gameHistory.obj";

    private final int totalPlayers_ = 2;
    private int turnGlobal_ = 1;

//    private GameHistory gameHistory_;
//    private final ArrayList<GameHistory> gameHistories_ = new ArrayList<>();

    private ArrayList<ArrayList<HashMap<String, String>>> gameHistory_;
    private ArrayList<ArrayList<ArrayList<HashMap<String, String>>>> gameHistories_ = new ArrayList<>();

    private class Player {
        private RadioButton life;
        private SimpleAdapter adapter;
        private final ArrayList<HashMap<String, String>> history = new ArrayList<>();
        private EditText comment;
        private RadioButton counter;
    }

    private int activePlayerIndex_ = 0;
    private RadioButton counter_;

    private final Player[] player_ = {new Player(), new Player()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter_ = findViewById(R.id.player_life);

//        for (int i = 0; i < totalPlayers_; ++i) {
//            Player player = player_[i];
//            player.adapter = new HistoryListAdapter(this, player.history);
//            ListView history = findViewById(getPlayerResourceId(i, "history"));
//            history.setAdapter(player.adapter);
////            player.life = findViewById(getPlayerResourceId(i, "life"));
////            player.counter = player.life;
////            player.comment = findViewById(getPlayerResourceId(i, "comment"));
//        }
//
//        gameHistories_.add(0, gameHistory_);
//        FileInputStream stream = null;
//        try {
//            stream = getApplicationContext().openFileInput(HISTORY_FILE);
//            ObjectInputStream object = new ObjectInputStream(stream);
//            try {
//                gameHistories_ = (ArrayList<ArrayList<ArrayList<HashMap<String, String>>>>) object.readObject();
//                // Avoid Unchecked cast warning.
////                for (Object x : (ArrayList) object.readObject()) {
//////                    gameHistories_.add((GameHistory) x);
////                    gameHistories_.add((ArrayList<ArrayList<HashMap<String, String>>>) x);
////                }
//
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (stream != null) {
//                try {
//                    stream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

    }
//
//
//
////    @Override
////    protected void onStart() {
////        super.onStart();
//////        gameHistory_ = new GameHistory(new ArrayList<ArrayList<HashMap<String, String>>>());
////        gameHistory_ = new ArrayList<ArrayList<HashMap<String, String>>>();
////    }

    public void onPlayerSelected(View view) {
        RadioButton rb = (RadioButton) view;
        if (rb.isChecked()) {
            activePlayerIndex_ = Integer.parseInt(rb.getText().toString()) - 1;
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            clearRadioButtonCheck((ViewGroup)view.getParent().getParent());
            counter_ = (RadioButton) view;
            counter_.setChecked(true);
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

//    public void onCommitButtonClicked(@SuppressWarnings("unused") View view) {
//        for (int player_i = 0; player_i < totalPlayers_; ++player_i) {
//            Player player = player_[player_i];
//            Date now = new Date();
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
//            HashMap<String, String>turn = new HashMap<>();
//            turn.put("turn_time", sdf.format(now));
//            turn.put("turn_global", String.format(Locale.getDefault(), "%02d", turnGlobal_));
//
//            turn.put("turn_life", player.life.getText().toString());
//            RadioButton commander = findViewById(getPlayerResourceId(player_i, "life_commander"));
//            turn.put("turn_commander", commander.getText().toString());
//            RadioButton poison = findViewById(getPlayerResourceId(player_i, "life_poison"));
//            turn.put("turn_poison", poison.getText().toString());
//
//            turn.put("turn_comment", player.comment.getText().toString());
//            player.history.add(turn);
//            player.adapter.notifyDataSetChanged();
//
//            // Reset turn comment.
//            player.comment.setText("");
//
//            ListView lv = findViewById(getPlayerResourceId(player_i, "history"));
//            lv.smoothScrollToPosition(player.history.size());
//
////            if (gameHistory_.history.size() < totalPlayers_) {
////                gameHistory_.history.add(new ArrayList<HashMap<String, String>>());
////            }
////            gameHistory_.history.set(player_i, player.history);
//            if (gameHistory_.size() < totalPlayers_) {
//                gameHistory_.add(new ArrayList<HashMap<String, String>>());
//            }
//            gameHistory_.set(player_i, player.history);
//        }
//        ++turnGlobal_;
//    }

    public void onPlusButtonClicked(@SuppressWarnings("unused") View view) {
        if (isNumber(counter_.getText().toString())) {
            counter_.setText(String.format(Locale.getDefault(), "%02d",
                    Integer.parseInt(counter_.getText().toString())+1));
        } else {
            TextView tv = findViewById(getPlayerResourceId(
                    activePlayerIndex_, counter_.getText().toString().toLowerCase()));
            tv.setText(String.format(Locale.getDefault(), "%02d",
                    Integer.parseInt(tv.getText().toString())+1));
        }
    }

    public void onMinusButtonClicked(@SuppressWarnings("unused") View view) {
        if (isNumber(counter_.getText().toString())) {
            counter_.setText(String.format(Locale.getDefault(), "%02d",
                    Integer.parseInt(counter_.getText().toString())-1));
        } else {
            TextView tv = findViewById(getPlayerResourceId(
                    activePlayerIndex_, counter_.getText().toString().toLowerCase()));
            tv.setText(String.format(Locale.getDefault(), "%02d",
                    Integer.parseInt(tv.getText().toString())-1));
        }
    }

//    public void onPendingButtonClicked(@SuppressWarnings("unused") View view) {
//        int player_id = Integer.parseInt(getResources().getResourceEntryName(view.getId()).replaceAll("[^0-9]", ""));
//        Player player = player_[player_id];
//        StringBuilder buffer = new StringBuilder(player.comment.getText().toString());
//        if (!player.comment.getText().toString().isEmpty()) {
//            buffer.append(", ");
//        }
//        buffer.append(player.life.getText().toString());
//        player.comment.setText(buffer.toString());
//    }
//
//    private class HistoryListAdapter extends SimpleAdapter {
//        private final ArrayList<? extends HashMap<String, String>> data_;
//
//        HistoryListAdapter(Context context, ArrayList<? extends HashMap<String, String>> data) {
//            super(context, data, R.layout.turn, FROM, TO);
//            data_ = data;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = getLayoutInflater().inflate(R.layout.turn, parent, false);
//            }
//
//            TextView turnTime = convertView.findViewById(R.id.turn_time);
//            turnTime.setText(data_.get(position).get("turn_time"));
//            TextView turnGlobal = convertView.findViewById(R.id.turn_global);
//            turnGlobal.setText(data_.get(position).get("turn_global"));
//
//            final EditText turnLife = convertView.findViewById(R.id.turn_player0_life);
//            turnLife.setText(data_.get(position).get("turn_life"));
//            turnLife.addTextChangedListener(new HistoryWatcher(data_.get(position), "turn_life"));
//
//            final EditText turnCommander = convertView.findViewById(R.id.turn_player0_commander);
//            turnCommander.setText(data_.get(position).get("turn_commander"));
//            turnCommander.addTextChangedListener(new HistoryWatcher(data_.get(position), "turn_commander"));
//
//            final EditText turnPoison = convertView.findViewById(R.id.turn_player0_poison);
//            turnPoison.setText(data_.get(position).get("turn_poison"));
//            turnPoison.addTextChangedListener(new HistoryWatcher(data_.get(position), "turn_poison"));
//
//            EditText turnComment = convertView.findViewById(R.id.turn_player0_comment);
//            turnComment.setText(data_.get(position).get("turn_comment"));
//            turnComment.addTextChangedListener(new HistoryWatcher(data_.get(position), "turn_comment"));
//
//            return convertView;
//        }
//
//        private class HistoryWatcher implements TextWatcher {
//            private final HashMap<String, String> map_;
//            private final String key_;
//            HistoryWatcher(HashMap<String, String> map, String key) {
//                map_ = map;
//                key_ = key;
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                map_.put(key_, s.toString());
//            }
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_options_menu_list, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == R.id.menu_about) {
//            Intent intent = new Intent(this, AboutActivity.class);
//            startActivity(intent);
//        } else if (itemId == R.id.menu_history) {
//            Intent intent = new Intent(this, HistoryActivity.class);
//            intent.putExtra("history", gameHistories_);
//            startActivity(intent);
//        } else if (itemId == R.id.menu_save) {
//            gameHistories_.add(gameHistory_);
//            FileOutputStream stream = null;
//            try {
//                stream = getApplicationContext().openFileOutput(HISTORY_FILE, Context.MODE_PRIVATE);
//                ObjectOutputStream object = new ObjectOutputStream(stream);
//                object.writeObject(gameHistories_);
//                object.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (stream != null) {
//                    try {
//                        stream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } else if (itemId == R.id.menu_reset) {
//            turnGlobal_ = 1;
//            for (int i = 0; i < totalPlayers_; ++i) {
//                Player player = player_[i];
//                player.life.setText(R.string.life);
//                player.history.clear();
//                player.adapter.notifyDataSetChanged();
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
    private int getPlayerResourceId(int player, String key) {
        return getResources().getIdentifier(
                String.format(Locale.getDefault(), "player%d_%s", player, key), "id", getPackageName());
    }

    private boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
