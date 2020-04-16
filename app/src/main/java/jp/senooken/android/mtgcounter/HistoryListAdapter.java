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
    private static final String[] FROM = {"turn_global", "turn_date", "turn_time", "turn_comment",
            "turn_player0_life", "turn_player0_poison", "turn_player0_commander0", "turn_player0_commander1", "turn_player0_commander2", "turn_player0_commander3",
            "turn_player1_life", "turn_player1_poison", "turn_player1_commander0", "turn_player1_commander1", "turn_player1_commander2", "turn_player1_commander3",
            "turn_player2_life", "turn_player2_poison", "turn_player2_commander0", "turn_player2_commander1", "turn_player2_commander2", "turn_player2_commander3",
            "turn_player3_life", "turn_player3_poison", "turn_player3_commander0", "turn_player3_commander1", "turn_player3_commander2", "turn_player3_commander3",
    };
    private static final int[] TO = {R.id.turn_global, R.id.turn_date, R.id.turn_time, R.id.turn_comment,
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

        TextView tv;
        for (String key : new String[]{"turn_global", "turn_date", "turn_time", "turn_comment"}) {
            tv = convertView.findViewById(MainActivity.getInstance().getResourceId(key));
            tv.setText(data_.get(position).get(key));
        }

        String key;
        EditText et;

        for (int playerIndex = 0; playerIndex < MainActivity.getInstance().totalPlayers_; ++playerIndex) {
            key = "turn_player" + playerIndex + "_life";
            et = convertView.findViewById(MainActivity.getInstance().getResourceId(key));
            et.setText(data_.get(position).get(key));
            et.addTextChangedListener(new HistoryWatcher(data_.get(position), key));

            key = "turn_player" + playerIndex + "_poison";
            et = convertView.findViewById(MainActivity.getInstance().getResourceId(key));
            et.setText(data_.get(position).get(key));
            et.addTextChangedListener(new HistoryWatcher(data_.get(position), key));

            for (int commanderIndex = 0; commanderIndex < MainActivity.getInstance().totalPlayers_; ++commanderIndex) {
                key = "turn_player" + playerIndex + "_commander" + commanderIndex;
                et = convertView.findViewById(MainActivity.getInstance().getResourceId(key));
                et.setText(data_.get(position).get(key));
                et.addTextChangedListener(new HistoryWatcher(data_.get(position), key));
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