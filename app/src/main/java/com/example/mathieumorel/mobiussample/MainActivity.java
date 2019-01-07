package com.example.mathieumorel.mobiussample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.spotify.mobius.MobiusLoop;
import com.spotify.mobius.extras.MobiusExtras;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MobiusLoop<Integer, Event, ?> mMobiusLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView counterTextView = findViewById(R.id.counter_txt);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMobiusLoop = MobiusExtras.beginnerLoop(MainActivity::update)
                .startFrom(2);

        findViewById(R.id.up_btn).setOnClickListener(view -> mMobiusLoop.dispatchEvent(Event.UP));
        findViewById(R.id.down_btn).setOnClickListener(view -> mMobiusLoop.dispatchEvent(Event.DOWN));

        mMobiusLoop.observe(counter -> {
            System.out.println("The counter value is " + counter);
            counterTextView.setText(String.format(Locale.getDefault(), "The counter is %d", counter));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMobiusLoop.dispose();
    }

    static int update(int counter, Event event) {
        switch (event) {
            case UP:
                return counter + 1;

            case DOWN:
                if (counter > 0) {
                    return counter - 1;
                }
                return counter;
        }
        // this should not be required here
        return 0;
    }
}
