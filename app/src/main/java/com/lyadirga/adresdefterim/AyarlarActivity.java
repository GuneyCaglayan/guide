package com.lyadirga.adresdefterim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AyarlarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(AyarlarActivity.this);
        String s=sharedPreferences.getString("tema","0");
        int tema=Integer.parseInt(s);
        switch (tema){
            case 0:setTheme(R.style.AppTheme); break;
            case 1:setTheme(R.style.AppThemeMavi); break;
            case 2:setTheme(R.style.AppThemeYesil); break;
            case 3:setTheme(R.style.AppThemePink); break;
            case 4:setTheme(R.style.AppThemeKahverengi); break;
            case 5:setTheme(R.style.AppThemeGri); break;

        }
        setContentView(R.layout.activity_ayarlar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(AyarlarActivity.this,MainActivity.class));
    }
}
