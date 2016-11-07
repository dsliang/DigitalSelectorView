package com.dsliang.digitalselectorview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dsliang.digitalselector.DigitalSelector;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    DigitalSelector mDigitalSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDigitalSelector = (DigitalSelector) findViewById(R.id.digitalSelector);

        ((SeekBar) findViewById(R.id.seekBarSelectedColor)).setOnSeekBarChangeListener(this);

        ((SeekBar) findViewById(R.id.seekBarNumber)).setOnSeekBarChangeListener(this);
        ((SeekBar) findViewById(R.id.seekBarNumber)).setProgress(mDigitalSelector.getNumber());

        ((SeekBar) findViewById(R.id.seekBarTextSize)).setOnSeekBarChangeListener(this);
        ((SeekBar) findViewById(R.id.seekBarTextSize)).setProgress( mDigitalSelector.getTextSize());

        ((SeekBar) findViewById(R.id.seekBarTextColorNotSelected)).setOnSeekBarChangeListener(this);

        ((SeekBar) findViewById(R.id.seekBarTextColorSelected)).setOnSeekBarChangeListener(this);

        ((SeekBar) findViewById(R.id.seekBarInsideMargin)).setOnSeekBarChangeListener(this);
        ((SeekBar) findViewById(R.id.seekBarInsideMargin)).setProgress(mDigitalSelector.getInsideMargin());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        int color = Color.rgb(Color.red(progress), Color.green(progress), Color.blue(progress));

        switch (seekBar.getId()) {
            case R.id.seekBarSelectedColor:
                mDigitalSelector.setSelectedColor(color);
                ((TextView) findViewById(R.id.txtSelectedColor)).setText(Integer.toHexString(color));
                break;
            case R.id.seekBarNumber:
                mDigitalSelector.setNumber(progress);
                ((TextView) findViewById(R.id.txtNumber)).setText(Integer.toString(progress));
                break;
            case R.id.seekBarTextSize:
                mDigitalSelector.setTextSize(progress);
                ((TextView) findViewById(R.id.txtTextSize)).setText(Integer.toString(progress));
                break;
            case R.id.seekBarTextColorNotSelected:
                mDigitalSelector.setTextColorNotSelected(color);
                ((TextView) findViewById(R.id.txtTextColorNotSelected)).setText(Integer.toHexString(color));
                break;
            case R.id.seekBarTextColorSelected:
                mDigitalSelector.setTextColorSelected(color);
                ((TextView) findViewById(R.id.txtTextColorSelected)).setText(Integer.toHexString(color));
                break;
            case R.id.seekBarInsideMargin:
                mDigitalSelector.settInsideMargin(progress);
                ((TextView) findViewById(R.id.txtInsideMargin)).setText(Integer.toString(progress));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
