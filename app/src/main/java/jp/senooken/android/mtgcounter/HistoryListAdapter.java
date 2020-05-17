package jp.senooken.android.mtgcounter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

class HistoryListAdapter extends SimpleAdapter {
    private static final String[] FROM = {"turn_count", "turn_time",
            "turn_player0_life", "turn_player0_poison", "turn_player0_experience", "turn_player0_energy", "turn_player0_commander0", "turn_player0_commander1", "turn_player0_commander2", "turn_player0_commander3",
            "turn_player1_life", "turn_player1_poison", "turn_player1_experience", "turn_player1_energy", "turn_player1_commander0", "turn_player1_commander1", "turn_player1_commander2", "turn_player1_commander3",
            "turn_player2_life", "turn_player2_poison", "turn_player2_experience", "turn_player2_energy", "turn_player2_commander0", "turn_player2_commander1", "turn_player2_commander2", "turn_player2_commander3",
            "turn_player3_life", "turn_player3_poison", "turn_player3_experience", "turn_player3_energy", "turn_player3_commander0", "turn_player3_commander1", "turn_player3_commander2", "turn_player3_commander3",
    };
    private static final int[] TO = {R.id.turn_count, R.id.turn_time,
            R.id.turn_player0_life, R.id.turn_player0_commander0, R.id.turn_player0_poison, R.id.turn_player0_experience, R.id.turn_player0_energy, R.id.turn_player0_commander0, R.id.turn_player0_commander1, R.id.turn_player0_commander2, R.id.turn_player0_commander3,
            R.id.turn_player1_life, R.id.turn_player1_commander0, R.id.turn_player1_poison, R.id.turn_player1_experience, R.id.turn_player1_energy, R.id.turn_player1_commander0, R.id.turn_player1_commander1, R.id.turn_player1_commander2, R.id.turn_player1_commander3,
            R.id.turn_player2_life, R.id.turn_player2_commander0, R.id.turn_player2_poison, R.id.turn_player2_experience, R.id.turn_player2_energy, R.id.turn_player2_commander0, R.id.turn_player2_commander1, R.id.turn_player2_commander2, R.id.turn_player2_commander3,
            R.id.turn_player3_life, R.id.turn_player3_commander0, R.id.turn_player3_poison, R.id.turn_player3_experience, R.id.turn_player3_energy, R.id.turn_player3_commander0, R.id.turn_player3_commander1, R.id.turn_player3_commander2, R.id.turn_player3_commander3,
    };

    private final ArrayList<? extends HashMap<String, String>> data_;
    private final Context context_;
    private final LayoutInflater inflater_;

    HistoryListAdapter(Context context, LayoutInflater inflater, ArrayList<? extends HashMap<String, String>> data) {
        super(context, data, R.layout.turn, FROM, TO);
        context_ = context;
        data_ = data;
        inflater_ = inflater;
    }

    private int getResourceId(String key) {
        return MainActivity.getResourceId(context_, key);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater_.inflate(R.layout.turn, parent, false);
        }

        TextView tv;
        for (String key : new String[]{"turn_count", "turn_time"}) {
            tv = convertView.findViewById(getResourceId(key));
            tv.setText(data_.get(position).get(key));
        }

        String key;
        EditText et;

        for (int playerIndex = 0; playerIndex < MainActivity.TOTAL_PLAYERS; ++playerIndex) {
            key = "turn_player" + playerIndex + "_life";
            et = convertView.findViewById(getResourceId(key));
            et.setText(data_.get(position).get(key));
            et.addTextChangedListener(new HistoryWatcher(data_.get(position), key));

            key = "turn_player" + playerIndex + "_poison";
            et = convertView.findViewById(getResourceId(key));
            et.setText(data_.get(position).get(key));
            et.addTextChangedListener(new HistoryWatcher(data_.get(position), key));

            for (int commanderIndex = 0; commanderIndex < MainActivity.TOTAL_PLAYERS; ++commanderIndex) {
                key = "turn_player" + playerIndex + "_commander" + commanderIndex;
                et = convertView.findViewById(getResourceId(key));
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