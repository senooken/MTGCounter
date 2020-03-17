package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String[] FROM = {"turnGlobal", "turnLocal", "turnLife"};
    private final int[] TO = {R.id.turnGlobal, R.id.turnLocal, R.id.turnLife};

    private int turnGlobal_ = 1;

    private int life1_ = 20;
    private SimpleAdapter adapter1_;
    private List<Map<String, Integer>> history1_ = new ArrayList<Map<String, Integer>>();
    private int life2_ = 20;
    private SimpleAdapter adapter2_;
    private List<Map<String, Integer>> history2_ = new ArrayList<Map<String, Integer>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter1_ = new SimpleAdapter(this, history1_, R.layout.turn, FROM, TO);
        ListView history1 = findViewById(R.id.history1);
        history1.setAdapter(adapter1_);

        adapter2_ = new SimpleAdapter(this, history2_, R.layout.turn, FROM, TO);
        ListView history2 = findViewById(R.id.history2);
        history2.setAdapter(adapter2_);
    }

    public void onPlusButton1Click(View view) {
        ++life1_;
        TextView life = findViewById(R.id.life1);
        life.setText(String.valueOf(life1_));
    }
    public void onMinusButton1Click(View view) {
        --life1_;
        TextView life = findViewById(R.id.life1);
        life.setText(String.valueOf(life1_));
    }
    public void onCommitButton1Click(View view) {
        HashMap<String, Integer>map = new HashMap<String, Integer>();
        map.put("turnGlobal", turnGlobal_);
        map.put("turnLocal", turnGlobal_/2+turnGlobal_%2);
        map.put("turnLife", life1_);
        history1_.add(map);
        adapter1_.notifyDataSetChanged();
        ++turnGlobal_;
    }

    public void onPlusButton2Click(View view) {
        ++life2_;
        TextView life = findViewById(R.id.life2);
        life.setText(String.valueOf(life2_));
    }
    public void onMinusButton2Click(View view) {
        --life2_;
        TextView life = findViewById(R.id.life2);
        life.setText(String.valueOf(life2_));
    }
    public void onCommitButton2Click(View view) {
        HashMap<String, Integer>map = new HashMap<String, Integer>();
        map.put("turnGlobal", turnGlobal_);
        map.put("turnLocal", turnGlobal_/2+turnGlobal_%2);
        map.put("turnLife", life2_);
        history2_.add(map);
        adapter2_.notifyDataSetChanged();
        ++turnGlobal_;
    }
}
