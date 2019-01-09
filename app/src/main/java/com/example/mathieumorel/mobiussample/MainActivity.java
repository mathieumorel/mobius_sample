package com.example.mathieumorel.mobiussample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.mathieumorel.mobiussample.counter.CounterLoop;
import com.example.mathieumorel.mobiussample.counter.Effect;
import com.example.mathieumorel.mobiussample.counter.Event;
import com.example.mathieumorel.mobiussample.counter.Model;
import com.example.mathieumorel.mobiussample.search.SearchApi;
import com.example.mathieumorel.mobiussample.search.SearchEffect;
import com.example.mathieumorel.mobiussample.search.SearchEvent;
import com.example.mathieumorel.mobiussample.search.SearchLoop;
import com.example.mathieumorel.mobiussample.search.SearchModel;
import com.example.mathieumorel.mobiussample.search.SearchResult;
import com.spotify.mobius.Mobius;
import com.spotify.mobius.MobiusLoop;
import com.spotify.mobius.rx2.RxMobius;

import static com.example.mathieumorel.mobiussample.counter.Event.down;
import static com.example.mathieumorel.mobiussample.counter.Event.up;

public class MainActivity extends AppCompatActivity {

    private MobiusLoop<Model, Event, Effect> mMobiusCounterLoop;
    private TextView mCounterTextView;

    private SearchLoop mSearchLoop = new SearchLoop(new SearchApi());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startCounterLoop();

    }

    private void startCounterLoop() {
        //mCounterTextView = findViewById(R.id.counter_txt);

        mMobiusCounterLoop = Mobius.loop(CounterLoop::update, CounterLoop::effectHandler)
                .startFrom(Model.create(2));

        mMobiusCounterLoop.observe(counter -> System.out.println("Counter " + counter.counter()));

        mMobiusCounterLoop.dispatchEvent(down());    // prints "1"
        mMobiusCounterLoop.dispatchEvent(down());    // prints "0"
        mMobiusCounterLoop.dispatchEvent(down());    // prints "error!"
        mMobiusCounterLoop.dispatchEvent(up());      // prints "1"
        mMobiusCounterLoop.dispatchEvent(up());      // prints "2"
        mMobiusCounterLoop.dispatchEvent(down());    // prints "1"

        //findViewById(R.id.up_btn).setOnClickListener(view -> mMobiusCounterLoop.dispatchEvent(up()));
        //findViewById(R.id.down_btn).setOnClickListener(view -> mMobiusCounterLoop.dispatchEvent(down()));
    }

    private void startSearchLoop() {
        MobiusLoop<SearchModel, SearchEvent, SearchEffect> loop =
                RxMobius.loop(SearchLoop::update, mSearchLoop::effectHandler)
                        .startFrom(SearchModel.create("", null, false));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMobiusCounterLoop.dispose();
    }
}
