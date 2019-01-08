package com.example.mathieumorel.mobiussample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.spotify.mobius.Connection;
import com.spotify.mobius.Mobius;
import com.spotify.mobius.MobiusLoop;
import com.spotify.mobius.Next;
import com.spotify.mobius.functions.Consumer;

import static com.example.mathieumorel.mobiussample.Event.down;
import static com.example.mathieumorel.mobiussample.Event.up;
import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.dispatch;
import static com.spotify.mobius.Next.next;

public class MainActivity extends AppCompatActivity {

    private MobiusLoop<Model, Event, Effect> mMobiusLoop;
    private TextView mCounterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCounterTextView = findViewById(R.id.counter_txt);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMobiusLoop = Mobius.loop(MainActivity::update, MainActivity::effectHandler)
                .startFrom(Model.create(2));

        mMobiusLoop.observe(counter -> System.out.println("Counter " + counter.counter()));

        mMobiusLoop.dispatchEvent(down());    // prints "1"
        mMobiusLoop.dispatchEvent(down());    // prints "0"
        mMobiusLoop.dispatchEvent(down());    // prints "error!"
        mMobiusLoop.dispatchEvent(up());      // prints "1"
        mMobiusLoop.dispatchEvent(up());      // prints "2"
        mMobiusLoop.dispatchEvent(down());    // prints "1"

        //findViewById(R.id.up_btn).setOnClickListener(view -> mMobiusLoop.dispatchEvent(up()));
        //findViewById(R.id.down_btn).setOnClickListener(view -> mMobiusLoop.dispatchEvent(down()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMobiusLoop.dispose();
    }

    @NonNull
    static Next<Model, Effect> update(Model model, Event event) {
        return event.map(
                up -> next(model.increase()),

                down -> {
                    if (model.counter() > 0) {
                        return next(model.decrease());
                    }
                    return dispatch(effects(Effect.reportErrorNegative()));
                }
        );
    }

    static Connection<Effect> effectHandler(Consumer<Event> eventConsumer) {
        return new Connection<Effect>() {
            @Override
            public void accept(Effect effect) {
                // effect.match() is like event.map() but has no return value
                effect.match(
                        reportErrorNegative -> System.out.println("Counter error!")
                );
            }

            @Override
            public void dispose() {
                // ...
            }
        };
    }
}
