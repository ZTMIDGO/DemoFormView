package com.litesnap.open.form;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private FormView mFromView;
    private FormView.Row[] mRows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFromView = findViewById(R.id.from);

        mRows = new FormView.Row[]{
                new FormView.Row(12, "✯", String.valueOf(12), Color.parseColor("#568996")),
                new FormView.Row(18, "✯✯", String.valueOf(18), Color.parseColor("#796338")),
                new FormView.Row(35, "✯✯✯", String.valueOf(35), Color.parseColor("#369525")),
                new FormView.Row(7, "✯✯✯✯", String.valueOf(7), Color.parseColor("#899653")),
                new FormView.Row(12, "✯✯✯✯✯", String.valueOf(12), Color.parseColor("#783265")),
        };
        mFromView.setRows(mRows);
    }
}
