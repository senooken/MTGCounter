package jp.senooken.android.mtgcounter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

class GameHistory implements Serializable {
    final ArrayList<ArrayList<HashMap<String, String>>> history;
    GameHistory(ArrayList<ArrayList<HashMap<String, String>>> hist) {
        history = hist;
    }
}
