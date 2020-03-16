package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int life1_ = 20;
    private ArrayAdapter<String> adapter1_;
    private int life2_ = 20;
    private ArrayAdapter<String> adapter2_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter1_ = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        ListView history1 = findViewById(R.id.history1);
        history1.setAdapter(adapter1_);
        adapter2_ = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
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
        adapter1_.add(String.valueOf(life1_));
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
        adapter2_.add(String.valueOf(life2_));
    }
}
