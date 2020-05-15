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
    public static final String HISTORY_FILE = "gameHistory.obj";
    public static final int TOTAL_PLAYERS = 4;

    private final int REQUEST_MENU_HISTORY = 200;
    private int turnCount_ = 1;

    private ArrayList<GameHistory> gameHistories_ = new ArrayList<>();

    private int activePlayerIndex_ = 0;
    private RadioButton counter_;
    private HistoryListAdapter adapter_;
    private final GameHistory gameHistory_ = new GameHistory();
    private EditText comment_;
    private EditText title_;

    public static int getResourceId(Context context, String key) {
        return context.getResources().getIdentifier(key, "id", context.getPackageName());
    }
    private int getResourceId(String key) {
        return getResourceId(getApplicationContext(), key);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter_ = new HistoryListAdapter(this, getLayoutInflater(), gameHistory_.history);
        ((ListView) findViewById(R.id.game_history)).setAdapter(adapter_);

        counter_ = findViewById(R.id.life);
        comment_ = findViewById(R.id.comment);

        title_ = findViewById(R.id.game_title);
        title_.addTextChangedListener(new TitleWatcher(gameHistory_));

        FileInputStream stream = null;
        try {
            stream = getApplicationContext().openFileInput(HISTORY_FILE);
            ObjectInputStream object = new ObjectInputStream(stream);
            try {
                //noinspection unchecked
                gameHistories_ = (ArrayList<GameHistory>) object.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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

    public void onPlayerSelected(View view) {
        RadioButton rb = (RadioButton) view;
        if (rb.isChecked()) {
            activePlayerIndex_ = Integer.parseInt(rb.getText().toString()) - 1;
            Log.i(this.getLocalClassName(), "activePlayerIndex_=" + activePlayerIndex_);
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
        HashMap<String, String> turn = new HashMap<>();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        turn.put("turn_time", timeFormat.format(now));
        turn.put("turn_count", String.format(Locale.getDefault(), "%02d", turnCount_));
        turn.put("turn_comment", comment_.getText().toString());

        String key;
        for (int playerIndex = 0; playerIndex < TOTAL_PLAYERS; ++playerIndex) {
            for (String type : new String[]{"life", "poison", "experience", "energy"}) {
                key = "player" + playerIndex + "_" + type;
                EditText et = findViewById(getResourceId(key));
                turn.put("turn_"+key, et.getText().toString());
            }

            for (int commanderIndex = 0; commanderIndex < TOTAL_PLAYERS; ++commanderIndex) {
                key = "player" + playerIndex + "_commander" + commanderIndex;
                EditText commander = findViewById(getResourceId(key));
                turn.put("turn_"+key, commander.getText().toString());
            }
        }

        gameHistory_.history.add(turn);
        adapter_.notifyDataSetChanged();

        ListView lv = findViewById(R.id.game_history);
        lv.smoothScrollToPosition(gameHistory_.history.size());

        comment_.setText("");
        ++turnCount_;
    }

    public void onPlusButtonClicked(@SuppressWarnings("unused") View view) {
        addCounter(+1);
    }

    public void onMinusButtonClicked(@SuppressWarnings("unused") View view) {
        addCounter(-1);
    }

    private void addCounter(int increment) {
        String text = counter_.getText().toString();
        if (isNumber(text)) {
            counter_.setText(String.format(Locale.getDefault(), "%02d",
                    Integer.parseInt(counter_.getText().toString()) + increment));
        } else {
            EditText counter;
            boolean IS_COMMANDER = text.charAt(0) == 'C';
            if (IS_COMMANDER) {
                int commanderIndex = Integer.parseInt(text.substring(1)) - 1;
                counter = findViewById(getResourceId("player"+activePlayerIndex_+"_commander"+commanderIndex));
            } else {
                counter = findViewById(getResourceId("player"+activePlayerIndex_+"_"+text.toLowerCase()));
            }
            counter.setText(String.format(Locale.getDefault(), "%02d",
                    Integer.parseInt(counter.getText().toString()) + increment));
        }
    }

    public void onPendingButtonClicked(@SuppressWarnings("unused") View view) {
        StringBuilder buffer = new StringBuilder(comment_.getText().toString());
        if (!comment_.getText().toString().isEmpty()) {
            buffer.append(", ");
        }

        EditText life = findViewById(getResourceId("player"+activePlayerIndex_+"_life"));
        buffer.append(life.getText().toString()).append(" (P").append(activePlayerIndex_+1).append(")");
        comment_.setText(buffer.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_reset) {
            reset();
        } else if (itemId == R.id.menu_new) {
            reset();
            recreate();
        } else if (itemId == R.id.menu_commander) {
            setCommanderLife();
        } else if (itemId == R.id.menu_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("game_histories", gameHistories_);
            startActivityForResult(intent, REQUEST_MENU_HISTORY);

        } else if (itemId == R.id.menu_save) {
            gameHistory_.createdDate = new Date();
            gameHistories_.add(0, gameHistory_);

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void reset() {
        turnCount_ = 1;
        activePlayerIndex_ = 0;
        gameHistory_.clear();

        title_.setText("");
        comment_.setText("");

        EditText et;
        et = findViewById(R.id.comment);
        et.setText("");
        et = findViewById(R.id.other);
        et.setText(R.string.label_other);

        RadioButton rb;
        rb = findViewById(R.id.player0);
        rb.setChecked(true);
        counter_ = findViewById(R.id.life);
        clearRadioButtonCheck((ViewGroup) counter_.getParent().getParent());
        counter_.setChecked(true);

        for (String key : new String[]{
                "mana_colorless", "mana_white", "mana_blue", "mana_black", "mana_red", "mana_green",
                "counter_storm", "counter_draw", "counter_other",
        }) {
            Log.i(this.getLocalClassName(), key);
            rb = findViewById(getResourceId(key));
            rb.setText(R.string.zero);
        }

        EditText counter;
        for (int playerIndex = 0; playerIndex < TOTAL_PLAYERS; ++playerIndex) {
            String prefix = "player" + playerIndex;
            counter = findViewById(getResourceId(prefix+"_life"));
            counter.setText(R.string.life);

            for (String type : new String[]{"poison", "experience", "energy"}) {
                counter = findViewById(getResourceId(prefix+"_"+type));
                counter.setText(R.string.zero);
            }

            for (int commanderIndex = 0; commanderIndex < TOTAL_PLAYERS; ++commanderIndex) {
                counter = findViewById(getResourceId(prefix+"_commander"+commanderIndex));
                counter.setText(R.string.zero);
            }
        }
        adapter_.notifyDataSetChanged();
    }

    private void setCommanderLife() {
        EditText counter;
        for (int playerIndex = 0; playerIndex < TOTAL_PLAYERS; ++playerIndex) {
            String prefix = "player" + playerIndex;
            counter = findViewById(getResourceId(prefix+"_life"));
            counter.setText(R.string.commander);
        }
    }

    private boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_MENU_HISTORY) && (data != null)) {
            //noinspection unchecked
            gameHistories_ = (ArrayList<GameHistory>) data.getSerializableExtra("game_histories");
        }
    }

    private class TitleWatcher implements TextWatcher {
        private final GameHistory gameHistory_;
        TitleWatcher(GameHistory gameHistory) {
            gameHistory_ = gameHistory;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            gameHistory_.title = s.toString();
        }
    }

}
