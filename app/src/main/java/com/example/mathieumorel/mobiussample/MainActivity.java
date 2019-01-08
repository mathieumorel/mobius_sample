package com.example.mathieumorel.mobiussample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.spotify.mobius.Connection;
import com.spotify.mobius.Mobius;
import com.spotify.mobius.MobiusLoop;
import com.spotify.mobius.Next;
import com.spotify.mobius.functions.Consumer;

import java.util.Locale;

import static com.example.mathieumorel.mobiussample.Effect.REPORT_ERROR_NEGATIVE;
import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.dispatch;
import static com.spotify.mobius.Next.next;

public class MainActivity extends AppCompatActivity {

    private MobiusLoop<Integer, Event, ?> mMobiusLoop;
    private TextView mCounterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCounterTextView = findViewById(R.id.counter_txt);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMobiusLoop = Mobius.loop(MainActivity::update, MainActivity::effectHandler)
                .startFrom(2);

        findViewById(R.id.up_btn).setOnClickListener(view -> mMobiusLoop.dispatchEvent(Event.UP));
        findViewById(R.id.down_btn).setOnClickListener(view -> mMobiusLoop.dispatchEvent(Event.DOWN));

        mMobiusLoop.observe(counter -> {
            System.out.println("The counter value is " + counter);
            mCounterTextView.setText(String.format(Locale.getDefault(), "The counter is %d", counter));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMobiusLoop.dispose();
    }

    static Next<Integer, Effect> update(int model, Event event) {
        switch (event) {
            case UP:
                return next(model + 1);

            case DOWN:
                if (model > 0) {
                    return next(model - 1);
                }
                return dispatch(effects(Effect.REPORT_ERROR_NEGATIVE));
        }
        // this should not be required here
        return Next.next(0);
    }

    static Connection<Effect> effectHandler(Consumer<Event> eventConsumer) {
        return new Connection<Effect>() {
            @Override
            public void accept(Effect effect) {
                if (effect == REPORT_ERROR_NEGATIVE) {
                    System.out.println("The counter is error!");
                }
            }

            @Override
            public void dispose() {
                // ...
            }
        };
    }
}
