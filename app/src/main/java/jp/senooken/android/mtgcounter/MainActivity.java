package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int life1_ = 20;
    private int life2_ = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
