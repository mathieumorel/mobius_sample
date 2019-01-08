package com.example.mathieumorel.mobiussample;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Model {
    public abstract int counter();

    public Model increase() {
        return create(counter() + 1);
    }

    public Model decrease() {
        return create(counter() - 1);
    }

    public static Model create(int counter) {
        return new AutoValue_Model(counter);
    }
}
