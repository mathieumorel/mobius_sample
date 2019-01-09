package com.example.mathieumorel.mobiussample.counter;

import android.support.annotation.NonNull;

import com.spotify.mobius.Connection;
import com.spotify.mobius.Next;
import com.spotify.mobius.functions.Consumer;

import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.dispatch;
import static com.spotify.mobius.Next.next;

public class CounterLoop {

    @NonNull
    public static Next<Model, Effect> update(Model model, Event event) {
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

    public static Connection<Effect> effectHandler(Consumer<Event> eventConsumer) {
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
