package com.dappslocker.bakingapp;


import android.os.Bundle;
import android.widget.FrameLayout;

public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = getMasterFragmentContainer();
        getLayoutInflater().inflate(R.layout.about,frameLayout);
    }
}
