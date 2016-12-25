package com.fedaros.speedreadingtrainer.SchualtzTable;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fedaros.speedreadingtrainer.R;
import com.fedaros.speedreadingtrainer.Util.LoggingHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SchultzTableActivity extends AppCompatActivity implements View.OnClickListener{

    @InjectView(R.id.random_numbers_grid)
    GridLayout numbersGridLyout;

    @InjectView(R.id.current_number_txt)
    TextView currentNumberTxt;

    @InjectView(R.id.time_tracker_txt)
    TextView timeTrackerTxt;

    //starter array the holds the numbers to start the game with.
    private ArrayList<Integer> numbersArrayList;
    //array to keep track of the numbers order.
    private ArrayList<Integer> trackingArrayList;
    private Context mContext;

    private Timer timer;
    private LoggingHelper loggingHelper = new LoggingHelper(true);


    private String LOG_TAG = SchultzTableActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schultz_table);

        ButterKnife.inject(this);
        mContext = this;
        numbersArrayList = new ArrayList<>();
        trackingArrayList = new ArrayList<>();

        currentNumberTxt.setText("0");

        buildGrid(4);

        startTimer();
    }

    private void buildGrid(int size){
        numbersArrayList = fillArrayWithNumbers(numbersArrayList, size * size);
        numbersGridLyout.setRowCount(size);
        numbersGridLyout.setColumnCount(size);

        int gridPadding = 1;

        numbersGridLyout.setPadding(dpToPixels(gridPadding), dpToPixels(gridPadding), dpToPixels(gridPadding), dpToPixels(gridPadding));

        int gridLayoutPadding = numbersGridLyout.getPaddingLeft();
        int lengthOfSides = (getResources().getDisplayMetrics().
                widthPixels - (gridLayoutPadding )) / (size + 1);

        float textSize = 20 * (float) lengthOfSides / dpToPixels(50);

        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                lengthOfSides, lengthOfSides);

        int currentIndex = 0;
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                TextView textView = new TextView(this);
                textView.setText(String.valueOf(numbersArrayList.get(currentIndex)));
                textView.setBackgroundResource(R.drawable.textview_border);
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(layoutParams);
                textView.setTextSize(textSize);


                currentIndex++;

                textView.setOnClickListener(this);

                numbersGridLyout.addView(textView);
            }
        }
    }

    private int dpToPixels(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * fills the array with numbers from 1 to range.
     *
     * @param arrayList target array.
     * @param range     the range from 1 to range.
     * @return array populated with numbers.
     */
    private ArrayList<Integer> fillArrayWithNumbers(ArrayList<Integer> arrayList, int range) {
        if (arrayList != null) {
            for (int i = 1; i <= range; i++) {
                arrayList.add(i);
            }
        } else {
            arrayList = new ArrayList<>();
        }
        Collections.shuffle(arrayList);
        return arrayList;
    }

    private boolean checkNumberOrder(int number) {

        int elementSize = trackingArrayList.size();

        if (((number - elementSize) == 1) && !trackingArrayList.contains(number)) {
            trackingArrayList.add(number);
            showArrayContent(trackingArrayList);
            currentNumberTxt.setText(String.valueOf(number));
            return true;
        } else {
            Toast.makeText(mContext, "wrong choice", Toast.LENGTH_SHORT).show();
            showArrayContent(trackingArrayList);
            return false;
        }
    }

    private void showArrayContent(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            loggingHelper.LogDebug(LOG_TAG, "Array Content: " + arrayList.get(i));
        }
    }

    private void showEndDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Good Job, press Ok for another round!");
        builder.setTitle("Great Job");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                restartGame(5);
            }
        });

        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void restartGame(int gridSize) {
        numbersGridLyout.removeAllViews();
        trackingArrayList = new ArrayList<>();
        numbersArrayList = new ArrayList<>();
        buildGrid(gridSize);


        currentNumberTxt.setText("0");
        timeTrackerTxt.setText(R.string.timer_text_format);

        if (timer != null) {
            timer.cancel();
            timer.purge();
            time = 0;
            seconds = 0;
            minutes = 0;
        }
        startTimer();
    }

    private int time = 0;
    private int seconds = 0;
    private int minutes = 0;

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        minutes = (time % 3600) / 60;
                        seconds = time % 60;
                        timeTrackerTxt.setText(String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds));
                    }
                });
            }
        }, 1000, 1000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schultz_table_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:

                return true;
            case R.id.action_grid:
                showGridDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    String[] sizeChoiceArray = {"3 x 3", "4 x 4", "5 x 5"};
    AlertDialog gridDialog = null;
    private void showGridDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("Set Grid Size");
        builder.setSingleChoiceItems(sizeChoiceArray, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int checkedId) {
                switch (checkedId){
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:



                        break;
                    default:


                }
                gridDialog.dismiss();
                gridDialog = null;

            }
        });


        gridDialog = builder.create();
        if (gridDialog != null) gridDialog.show();

    }

    boolean isCorrect = false;

    @Override
    public void onClick(final View view) {
        if (view instanceof TextView){
            Blink x = new Blink(view);
            isCorrect = checkNumberOrder(Integer.parseInt(((TextView) view).getText().toString()));

            if (trackingArrayList.size() == numbersArrayList.size()){
                showEndDialog();
            }

            if (isCorrect){
                x.startBlinking(true);
            }else {
                x.startBlinking(false);
            }

        }
    }

}
