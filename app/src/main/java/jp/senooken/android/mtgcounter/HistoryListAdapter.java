package jp.senooken.android.mtgcounter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryListAdapter extends SimpleAdapter {
    private static final String[] FROM = {"turn_global", "turn_date", "turn_time",
            "turn_player0_life", "turn_player0_poison", "turn_player0_commander0", "turn_player0_commander1", "turn_player0_commander2", "turn_player0_commander3",
            "turn_player1_life", "turn_player1_poison", "turn_player1_commander0", "turn_player1_commander1", "turn_player1_commander2", "turn_player1_commander3",
            "turn_player2_life", "turn_player2_poison", "turn_player2_commander0", "turn_player2_commander1", "turn_player2_commander2", "turn_player2_commander3",
            "turn_player3_life", "turn_player3_poison", "turn_player3_commander0", "turn_player3_commander1", "turn_player3_commander2", "turn_player3_commander3",
            "turn_comment",
    };
    private static final int[] TO = {R.id.turn_global, R.id.turn_date, R.id.turn_time,
            R.id.turn_player0_life, R.id.turn_player0_commander0, R.id.turn_player0_poison, R.id.turn_player0_commander0, R.id.turn_player0_commander1, R.id.turn_player0_commander2, R.id.turn_player0_commander3,
            R.id.turn_player1_life, R.id.turn_player1_commander0, R.id.turn_player1_poison, R.id.turn_player1_commander0, R.id.turn_player1_commander1, R.id.turn_player1_commander2, R.id.turn_player1_commander3,
            R.id.turn_player2_life, R.id.turn_player2_commander0, R.id.turn_player2_poison, R.id.turn_player2_commander0, R.id.turn_player2_commander1, R.id.turn_player2_commander2, R.id.turn_player2_commander3,
            R.id.turn_player3_life, R.id.turn_player3_commander0, R.id.turn_player3_poison, R.id.turn_player3_commander0, R.id.turn_player3_commander1, R.id.turn_player3_commander2, R.id.turn_player3_commander3,
    };

    private final ArrayList<? extends HashMap<String, String>> data_;

    HistoryListAdapter(Context context, ArrayList<? extends HashMap<String, String>> data) {
        super(context, data, R.layout.turn, FROM, TO);
        data_ = data;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = MainActivity.getInstance().getLayoutInflater().inflate(R.layout.turn, parent, false);
        }

        TextView turnGlobal = convertView.findViewById(R.id.turn_global);
        turnGlobal.setText(data_.get(position).get("turn_global"));
        TextView turnDate = convertView.findViewById(R.id.turn_date);
        turnDate.setText(data_.get(position).get("turn_date"));
        TextView turnTime = convertView.findViewById(R.id.turn_time);
        turnTime.setText(data_.get(position).get("turn_time"));

        String key;

        for (int playerIndex = 0; playerIndex < MainActivity.getInstance().totalPlayers_; ++playerIndex) {
            final EditText turnLife = convertView.findViewById(
                    MainActivity.getInstance().getPlayerResourceId(playerIndex, "life", "turn"));
            key = "turn_player" + playerIndex + "_life";
            turnLife.setText(data_.get(position).get(key));
            turnLife.addTextChangedListener(new HistoryWatcher(data_.get(position), key));

            final EditText turnPoison = convertView.findViewById(
                    MainActivity.getInstance().getPlayerResourceId(playerIndex, "poison", "turn"));
            key = "turn_player" + playerIndex + "_poison";
            turnPoison.setText(data_.get(position).get(key));
            turnPoison.addTextChangedListener(new HistoryWatcher(data_.get(position), key));

            for (int commanderIndex = 0; commanderIndex < MainActivity.getInstance().totalPlayers_; ++commanderIndex) {
                final EditText turnCommander = convertView.findViewById(
                        MainActivity.getInstance().getPlayerResourceId(playerIndex, "commander" + commanderIndex, "turn"));
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