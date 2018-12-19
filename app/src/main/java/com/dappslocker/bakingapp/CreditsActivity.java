package com.dappslocker.bakingapp;

import android.os.Bundle;
import android.support.v4.text.TextUtilsCompat;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreditsActivity extends BaseActivity {
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.textViewCreditSources)
    TextView textViewCreditSources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = getMasterFragmentContainer();
        getLayoutInflater().inflate(R.layout.credits,frameLayout);
        ButterKnife.bind(this);
        String references = textViewCreditSources.getText().toString();

        references = references + System.getProperty("line.separator") + System.getProperty("line.separator") +
                getResources().getString(R.string.exo_player) +  System.getProperty("line.separator") +  getResources().getString(R.string.exo_player_source) +
                System.getProperty("line.separator") + System.getProperty("line.separator") +
                getResources().getString(R.string.udacity) +  System.getProperty("line.separator") +  getResources().getString(R.string.udacity_source) +
                System.getProperty("line.separator") + System.getProperty("line.separator") +
                getResources().getString(R.string.flat_icon) +  System.getProperty("line.separator") +  getResources().getString(R.string.flat_icon_source) +
                System.getProperty("line.separator") + System.getProperty("line.separator") +
                getResources().getString(R.string.stack_overflow) +  System.getProperty("line.separator") +  getResources().getString(R.string.stack_overflow_source);

        textViewCreditSources.setText(references);       
    }
}
