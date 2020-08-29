package com.lyadirga.adresdefterim;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by kiosk on 21/11/2016.
 */

public class AyarlarFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ayarlar);
    }
}
