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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final String[] FROM = {"turn_global", "turn_date", "turn_time",
            "turn_player0_life", "turn_player0_poison", "turn_player0_commander0", "turn_player0_commander1", "turn_player0_commander2", "turn_player0_commander3",
            "turn_player1_life", "turn_player1_poison", "turn_player1_commander0", "turn_player1_commander1", "turn_player1_commander2", "turn_player1_commander3",
            "turn_player2_life", "turn_player2_poison", "turn_player2_commander0", "turn_player2_commander1", "turn_player2_commander2", "turn_player2_commander3",
            "turn_player3_life", "turn_player3_poison", "turn_player3_commander0", "turn_player3_commander1", "turn_player3_commander2", "turn_player3_commander3",
            "turn_comment",
    };
    private final int[] TO = {R.id.turn_global, R.id.turn_date, R.id.turn_time,
            R.id.turn_player0_life, R.id.turn_player0_commander0, R.id.turn_player0_poison, R.id.turn_player0_commander0, R.id.turn_player0_commander1, R.id.turn_player0_commander2, R.id.turn_player0_commander3,
            R.id.turn_player1_life, R.id.turn_player1_commander0, R.id.turn_player1_poison, R.id.turn_player1_commander0, R.id.turn_player1_commander1, R.id.turn_player1_commander2, R.id.turn_player1_commander3,
            R.id.turn_player2_life, R.id.turn_player2_commander0, R.id.turn_player2_poison, R.id.turn_player2_commander0, R.id.turn_player2_commander1, R.id.turn_player2_commander2, R.id.turn_player2_commander3,
            R.id.turn_player3_life, R.id.turn_player3_commander0, R.id.turn_player3_poison, R.id.turn_player3_commander0, R.id.turn_player3_commander1, R.id.turn_player3_commander2, R.id.turn_player3_commander3,
    };
    private final String HISTORY_FILE = "gameHistory.obj";

    private final int totalPlayers_ = 4;
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
    }

    private int activePlayerIndex_ = 0;
    private RadioButton counter_;
    private HistoryListAdapter adapter_;
    private final ArrayList<HashMap<String, String>> history_ = new ArrayList<>();
    private EditText comment_;

    private final Player[] player_ = {new Player(), new Player()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter_ = findViewById(R.id.life);
        comment_ = findViewById(R.id.comment);

        adapter_ = new HistoryListAdapter(this, history_);
        ListView history = findViewById(R.id.history);
        history.setAdapter(adapter_);

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

    public void onCommitButtonClicked(@SuppressWarnings("unused") View view) {
        Date now = new Date();
        HashMap<String, String>turn = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.US);
        turn.put("turn_date", dateFormat.format(now));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        turn.put("turn_time", timeFormat.format(now));
        turn.put("turn_global", String.format(Locale.getDefault(), "%02d", turnGlobal_));

        for (int playerIndex = 0; playerIndex < totalPlayers_; ++playerIndex) {
            TextView life = findViewById(getPlayerResourceId(playerIndex, "life"));
            turn.put(String.format(Locale.getDefault(), "turn_player%d_life", playerIndex), life.getText().toString());
            TextView poison = findViewById(getPlayerResourceId(playerIndex, "poison"));
            turn.put(String.format(Locale.getDefault(), "turn_player%d_poison", playerIndex), poison.getText().toString());

            for (int commanderIndex = 0; commanderIndex < totalPlayers_; ++commanderIndex) {
                TextView commander = findViewById(getPlayerResourceId(playerIndex,
                        "commander" + commanderIndex));
                turn.put("turn_player" + playerIndex + "_commander" + commanderIndex,
                        commander.getText().toString());
            }

//            if (gameHistory_.history.size() < totalPlayers_) {
//                gameHistory_.history.add(new ArrayList<HashMap<String, String>>());
//            }
////            gameHistory_.history.set(playerIndex, player.history);
//            if (gameHistory_.size() < totalPlayers_) {
//                gameHistory_.add(new ArrayList<HashMap<String, String>>());
//            }
//            gameHistory_.set(playerIndex, player.history);
        }

//        turn.put("comment", comment_.getText().toString());

        history_.add(turn);
        adapter_.notifyDataSetChanged();

        comment_.setText("");

        ListView lv = findViewById(R.id.history);
        lv.smoothScrollToPosition(history_.size());

        ++turnGlobal_;
    }

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

    public void onPendingButtonClicked(@SuppressWarnings("unused") View view) {
        StringBuilder buffer = new StringBuilder(comment_.getText().toString());
        if (!comment_.getText().toString().isEmpty()) {
            buffer.append(", ");
        }

        TextView life = findViewById(getPlayerResourceId(activePlayerIndex_, "life"));
        buffer.append(life.getText().toString());
        comment_.setText(buffer.toString());
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

            TextView turnGlobal = convertView.findViewById(R.id.turn_global);
            turnGlobal.setText(data_.get(position).get("turn_global"));
            TextView turnDate = convertView.findViewById(R.id.turn_date);
            turnDate.setText(data_.get(position).get("turn_date"));
            TextView turnTime = convertView.findViewById(R.id.turn_time);
            turnTime.setText(data_.get(position).get("turn_time"));

            String key;

            for (int playerIndex = 0; playerIndex < totalPlayers_; ++playerIndex) {
                final EditText turnLife = convertView.findViewById(getPlayerResourceId(playerIndex, "life", "turn"));
                key = "turn_player" + playerIndex + "_life";
                turnLife.setText(data_.get(position).get(key));
                turnLife.addTextChangedListener(new HistoryWatcher(data_.get(position), key));

                final EditText turnPoison = convertView.findViewById(getPlayerResourceId(playerIndex, "poison", "turn"));
                key = "turn_player" + playerIndex + "_poison";
                turnPoison.setText(data_.get(position).get(key));
                turnPoison.addTextChangedListener(new HistoryWatcher(data_.get(position), key));

                for (int commanderIndex = 0; commanderIndex < totalPlayers_; ++commanderIndex) {
                    final EditText turnCommander = convertView.findViewById(
                            getPlayerResourceId(playerIndex, "commander" + commanderIndex, "turn"));
                    key = "turn_player" + playerIndex + "_commander" + commanderIndex;
                    turnCommander.setText(data_.get(position).get(key));
                    turnCommander.addTextChangedListener(new HistoryWatcher(data_.get(position), key));
                }
            }

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
        } else if (itemId == R.id.menu_history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("history", gameHistories_);
            startActivity(intent);
        } else if (itemId == R.id.menu_save) {
            gameHistories_.add(gameHistory_);
            FileOutputStream stream = null;
            try {
                stream = getApplicationContext().openFileOutput(HISTORY_FILE, Context.MODE_PRIVATE);
                ObjectOutputStream object = new ObjectOutputStream(stream);
                object.writeObject(gameHistories_);
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
        } else if (itemId == R.id.menu_reset) {
            turnGlobal_ = 1;
            for (int i = 0; i < totalPlayers_; ++i) {
                TextView life = findViewById(getPlayerResourceId(i, "life"));
                life.setText(R.string.life);
                history_.clear();
                adapter_.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private int getPlayerResourceId(int player, String key) {
        return getPlayerResourceId(player, key, "");
    }

    private int getPlayerResourceId(int player, String key, String prefix) {
        prefix = prefix.equals("") ? prefix : prefix + "_";
        return getResources().getIdentifier(
                prefix + "player" + player + "_" + key,
//                String.format(Locale.getDefault(), "player%d_%s", player, key),
                "id", getPackageName());
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
