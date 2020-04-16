package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private final String HISTORY_FILE = "gameHistory.obj";

    public final int totalPlayers_ = 4;
    private int turnGlobal_ = 1;

    private ArrayList<ArrayList<HashMap<String, String>>> gameHistories_ = new ArrayList<>();

    private int activePlayerIndex_ = 0;
    private RadioButton counter_;
    private HistoryListAdapter adapter_;
    private final ArrayList<HashMap<String, String>> history_ = new ArrayList<>();
    private EditText comment_;

    private static MainActivity instance_;

    public static MainActivity getInstance() {
        return instance_;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance_ = this;
        counter_ = findViewById(R.id.life);
        comment_ = findViewById(R.id.comment);

        adapter_ = new HistoryListAdapter(this, history_);
        ListView history = findViewById(R.id.history);
        history.setAdapter(adapter_);

        FileInputStream stream = null;
        try {
            stream = getApplicationContext().openFileInput(HISTORY_FILE);
            ObjectInputStream object = new ObjectInputStream(stream);
            try {
                gameHistories_ = (ArrayList<ArrayList<HashMap<String, String>>>) object.readObject();
                // Avoid Unchecked cast warning.
                for (Object x : (ArrayList) object.readObject()) {
                    gameHistories_.add((ArrayList<HashMap<String, String>>) x);
                }
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
        HashMap<String, String>turn = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.US);
        turn.put("turn_date", dateFormat.format(now));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        turn.put("turn_time", timeFormat.format(now));
        turn.put("turn_global", String.format(Locale.getDefault(), "%02d", turnGlobal_));
        turn.put("comment", comment_.getText().toString());

        String key;
        for (int playerIndex = 0; playerIndex < totalPlayers_; ++playerIndex) {
            key = "player" + playerIndex + "_life";
            TextView life = findViewById(getResourceId(key));
            turn.put("turn_"+key, life.getText().toString());
            key = "player" + playerIndex + "_poison";
            TextView poison = findViewById(getResourceId(key));
            turn.put("turn_"+key, poison.getText().toString());

            for (int commanderIndex = 0; commanderIndex < totalPlayers_; ++commanderIndex) {
                key = "player" + playerIndex + "_commander" + commanderIndex;
                TextView commander = findViewById(getResourceId(key));
                turn.put("turn_"+key, commander.getText().toString());
            }
        }

        history_.add(turn);
        adapter_.notifyDataSetChanged();

        ListView lv = findViewById(R.id.history);
        lv.smoothScrollToPosition(history_.size());

        comment_.setText("");
        ++turnGlobal_;
    }

    public void onPlusButtonClicked(@SuppressWarnings("unused") View view) {
        addCounter(+1);
    }

    public void onMinusButtonClicked(@SuppressWarnings("unused") View view) {
        addCounter(-1);
    }

    public void addCounter(int increment) {
        String text = counter_.getText().toString();
        if (isNumber(text)) {
            counter_.setText(String.format(Locale.getDefault(), "%02d",
                    Integer.parseInt(counter_.getText().toString())+1));
        } else {
            TextView tv;
            boolean IS_COMMANDER = text.charAt(0) == 'C';
            if (IS_COMMANDER) {
                int commanderIndex = Integer.parseInt(text.substring(1)) - 1;
                tv = findViewById(getResourceId("player"+activePlayerIndex_+"_commander"+commanderIndex));
            } else {
                tv = findViewById(getResourceId("player"+activePlayerIndex_+"_"+text.toLowerCase()));
            }
            tv.setText(String.format(Locale.getDefault(), "%02d",
                    Integer.parseInt(tv.getText().toString()) + increment));
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
            gameHistories_.add(history_);
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
    public int getPlayerResourceId(int player, String key) {
        return getPlayerResourceId(player, key, "");
    }

    public int getPlayerResourceId(int player, String key, String prefix) {
        prefix = prefix.equals("") ? prefix : prefix + "_";
        return getResourceId(prefix + "player" + player + "_" + key);
    }
    public int getResourceId(String key) {
        return getResources().getIdentifier(key, "id", getPackageName());
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
