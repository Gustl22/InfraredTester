package com.oberhauser.infraredtester;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class InfraredActivity extends AppCompatActivity {

    TextView mFreqsText;
    ConsumerIrManager mCIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infrared);

        // Get a reference to the ConsumerIrManager
        mCIR = (ConsumerIrManager)getSystemService(Context.CONSUMER_IR_SERVICE);
        // See assets/res/any/layout/consumer_ir.xml for this
        // view layout definition, which is being set here as
        // the content of our screen.
        setContentView(R.layout.activity_infrared);
        // Set the OnClickListener for the button so we see when it's pressed.
        findViewById(R.id.sendShort).setOnClickListener(mSendClickListener);
        findViewById(R.id.sendLong).setOnClickListener(mSendClickListener);
        findViewById(R.id.getFrequency).setOnClickListener(mGetFreqsClickListener);
        mFreqsText = (TextView) findViewById(R.id.tv_frequency);
    }

    View.OnClickListener mSendClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!mCIR.hasIrEmitter()) {
                Log.e("Warning", "No IR Emitter found\n");
                return;
            }

            String input;

            // A pattern of alternating series of carrier on and off periods measured in
            // microseconds.

            if(v.equals(findViewById(R.id.sendShort))){
                input = "0000 006D 0022 0002";

            } else {
                input = "0000 006D 0022 0022 0159 00AD 0016 0015 0016 0040 0016 0016 0015 0016 0016 0040 0016 0040 0016 0015 0016 0015 0016 0041 0015 0016 0016 0040 0016 0040 0016 0015 0016 0015 0016 0040 0016 0041 0016 0040 0016 0040 0016 0040 0016 0040 0016 0041 0015 0016 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0016 0015 0041 0016 0040 0016 0040 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6 0159 0057 0016 05E6";
            }

            input = input.replace("0000 ", "");
            String[] hex = input.split(" ");
            int[] pattern = new int[hex.length];
            for(int i=0; i < pattern.length; i++){
                pattern[i] = Integer.parseInt(hex[i], 16);
            }

            // transmit the pattern at 38.4KHz
            mCIR.transmit(38400, pattern);
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 50 milliseconds
            vibrator.vibrate(50);
        }
    };
    View.OnClickListener mGetFreqsClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            StringBuilder b = new StringBuilder();
            if (!mCIR.hasIrEmitter()) {
                mFreqsText.setText("No IR Emitter found!");
                Log.e("Warning", "No IR Emitter found!\n");
                return;
            }
            // Get the available carrier frequency ranges
            ConsumerIrManager.CarrierFrequencyRange[] freqs = mCIR.getCarrierFrequencies();
            b.append("IR Carrier Frequencies:\n");
            for (ConsumerIrManager.CarrierFrequencyRange range : freqs) {
                b.append(String.format("    %d - %d\n", range.getMinFrequency(),
                        range.getMaxFrequency()));
            }
            mFreqsText.setText(b.toString());
        }
    };
}
