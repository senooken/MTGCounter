package jp.senooken.android.mtgcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String[] FROM = {"turnGlobal", "turnLocal", "turnLife", "turnComment"};
    private final int[] TO = {R.id.turnGlobal, R.id.turnLocal, R.id.turnLife, R.id.turnComment};

    private int turnGlobal_ = 1;

    private int life1_ = 20;
    private SimpleAdapter adapter1_;
    private List<Map<String, String>> history1_ = new ArrayList<>();
    private String comment1_ = "";

    private int life2_ = 20;
    private SimpleAdapter adapter2_;
    private List<Map<String, String>> history2_ = new ArrayList<>();
    private String comment2_ = "";

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


    public void onCommitButtonClick(View view) {
        HashMap<String, String>turn1 = new HashMap<>();
        turn1.put("turnGlobal", String.valueOf(turnGlobal_));
        turn1.put("turnLocal", String.valueOf(turnGlobal_/2+turnGlobal_%2));
        turn1.put("turnLife", String.valueOf(life1_));
        turn1.put("turnComment", comment1_);
        history1_.add(turn1);
        adapter1_.notifyDataSetChanged();
        // Reset turn comment.
        comment1_ = "";
        EditText comment1 = findViewById(R.id.comment1);
        comment1.setText(comment1_);

        HashMap<String, String>turn2 = new HashMap<>();
        turn2.put("turnGlobal", String.valueOf(turnGlobal_));
        turn2.put("turnLocal", String.valueOf(turnGlobal_/2+turnGlobal_%2));
        turn2.put("turnLife", String.valueOf(life2_));
        turn2.put("turnComment", comment2_);
        history2_.add(turn2);
        adapter2_.notifyDataSetChanged();
        // Reset turn comment.
        comment2_ = "";
        EditText comment2 = findViewById(R.id.comment2);
        comment2.setText(comment2_);

        ++turnGlobal_;
    }

    public void onPendingButton1Click(View view) {
        StringBuilder buffer = new StringBuilder(comment1_);
        if (!comment1_.isEmpty()) {
            buffer.append(", ");
        }
        buffer.append(life1_);
        comment1_ = buffer.toString();
        EditText comment = findViewById(R.id.comment1);
        comment.setText(comment1_);
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

    public void onPendingButton2Click(View view) {
        StringBuilder buffer = new StringBuilder(comment2_);
        if (!comment2_.isEmpty()) {
            buffer.append(", ");
        }
        buffer.append(life2_);
        comment2_ = buffer.toString();
        EditText comment = findViewById(R.id.comment2);
        comment.setText(comment2_);
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
