package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        List<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> map;
        map = new HashMap<>();
        map.put("about_key", "Application");
        map.put("about_value", "MTGCounter");
        list.add(map);
        map = new HashMap<>();
        map.put("about_key", "Version");
        map.put("about_value", "0.x.x");
        list.add(map);

        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.about,
                new String[]{"about_key", "about_value"}, new int[]{R.id.about_key, R.id.about_value});
        ListView lv = findViewById(R.id.about);
        lv.setAdapter(adapter);
    }
}