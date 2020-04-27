package jp.senooken.android.mtgcounter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GameHistory implements Serializable {
    public ArrayList<HashMap<String, String>> history = new ArrayList<>();
    public String title;
    public Date createdDate = new Date();

    public void clear() {
        history.clear();
        title = "";
        createdDate = new Date();
    }
}
