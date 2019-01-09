package com.example.mathieumorel.mobiussample.search;

import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

@DataEnum
public interface SearchEffect_dataenum {
    dataenum_case SearchRequest(String query);
    dataenum_case ShowErrorMessage(String message);
}
