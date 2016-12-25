package com.fedaros.speedreadingtrainer.SchualtzTable;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.fedaros.speedreadingtrainer.R;

/**
 * Created by Shoshaa on 2512//16.
 */

public class Blink {
    private Handler handler;
    private int[] farbe = new int[2];
    private int farbIndex = 1;
    private int ticks;
    private final int MAXTICKS = 5;
    private final int DELAYMILLIS = 75;
    private View v;


    private Runnable blinkSchleife = new Runnable() {
        @Override
        public void run() {
            v.setBackgroundColor(farbe[farbIndex]);
            farbIndex = 1 - farbIndex;
            ticks++;
            if (ticks < MAXTICKS) {
                handler.postDelayed(blinkSchleife, DELAYMILLIS);
            }
            else {
                v.setBackgroundResource(R.drawable.textview_border);
            }
        }
    };

    public  Blink(View v) {
        this.v=v;
        ticks = 0;
        farbIndex=0;
        handler = new Handler();
    }

    public void startBlinking(boolean isCorrect){
        if (isCorrect){
            farbe[0] = Color.GREEN;
            farbe[1] = Color.TRANSPARENT;
        }else {
            farbe[0] = Color.RED;
            farbe[1] = Color.TRANSPARENT;
        }

        handler.post(blinkSchleife);
    }
}
