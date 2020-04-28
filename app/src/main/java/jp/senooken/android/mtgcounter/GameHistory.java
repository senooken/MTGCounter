package jp.senooken.android.mtgcounter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

class GameHistory implements Serializable {
    final ArrayList<HashMap<String, String>> history = new ArrayList<>();
    String title;
    Date createdDate = new Date();

    void clear() {
        history.clear();
        title = "";
        createdDate = new Date();
    }
}
