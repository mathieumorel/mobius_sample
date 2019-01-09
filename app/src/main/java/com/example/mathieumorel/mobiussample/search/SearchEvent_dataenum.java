package com.example.mathieumorel.mobiussample.search;

import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

@DataEnum
public interface SearchEvent_dataenum {
    dataenum_case TextChanged(String text);
    dataenum_case SearchResult(String query, Result result);
    dataenum_case SearchError(String query);
}
