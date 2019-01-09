package com.example.mathieumorel.mobiussample.counter;

import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

@DataEnum
public interface Event_dataenum {
    dataenum_case Up();
    dataenum_case Down();
}
