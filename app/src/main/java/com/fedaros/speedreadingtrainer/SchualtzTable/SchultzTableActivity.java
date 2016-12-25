package com.fedaros.speedreadingtrainer.SchualtzTable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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

public class SchultzTableActivity extends AppCompatActivity {

    @InjectView(R.id.random_numbers_grid)
    GridView numbersGridView;
    @InjectView(R.id.current_number_txt)
    TextView currentNumberTxt;
    @InjectView(R.id.time_tracker_txt)
    TextView timeTrackerTxt;

    //starter array the holds the numbers to start the game with.
    private ArrayList<Integer> numbersArrayList;
    //array to keep track of the numbers order.
    private ArrayList<Integer> trackingArrayList;

    private NumberAdapter numberAdapter;
    private Context mContext;

    private Timer timer;
    private LoggingHelper loggingHelper = new LoggingHelper(true);

    private int chosenGridSize = 9;
    private int gridColumnSize = 3;

    private String LOG_TAG = SchultzTableActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schultz_table);

        ButterKnife.inject(this);
        mContext = this;

        numbersArrayList = new ArrayList<>();
        trackingArrayList = new ArrayList<>();
        trackingArrayList = removeAllElements(trackingArrayList);

        currentNumberTxt.setText("0");

        //fill the array with numbers.
        numbersArrayList = fillArrayWithNumbers(numbersArrayList, chosenGridSize);
        //shuffle the numbers inside the array.
        shuffleNumbers();

        Log.d(LOG_TAG, "ArrayList Size" + numbersArrayList.size());

        numberAdapter = new NumberAdapter(this,
                R.layout.number_view_layout, numbersArrayList);

        numbersGridView.setAdapter(numberAdapter);

        numbersGridView.setNumColumns(gridColumnSize);

        Log.d(LOG_TAG, "AdapterCount" + numbersGridView.getAdapter().getCount());

        numbersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.number_txt);

                final boolean isInserted = checkNumberOrder(Integer.parseInt(textView.getText().toString().trim()));

                final ImageView imageView = (ImageView) view.findViewById(R.id.number_holder_img);
                if (isInserted) {
                    currentNumberTxt.setText(String.valueOf(
                            trackingArrayList.get(trackingArrayList.size() - 1)));
                }

                if (trackingArrayList.size() == numbersArrayList.size()) {
                    showEndDialog();
                    timer.cancel();
                    timer.purge();
                }
            }
        });
        startTimer();

    }

    //shuffle the numbers array
    private void shuffleNumbers() {
        Collections.shuffle(numbersArrayList);
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
            arrayList = new ArrayList<Integer>();
        }

        return arrayList;
    }

    private ArrayList removeAllElements(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.remove(i);
        }
        return arrayList;
    }

    private boolean checkNumberOrder(int number) {

        int elementSize = trackingArrayList.size();

        if (((number - elementSize) == 1) && !trackingArrayList.contains(number)) {
            trackingArrayList.add(number);
            showArrayContent(trackingArrayList);
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
                restartGame(chosenGridSize);
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
        numberAdapter.clear();
        numbersArrayList = fillArrayWithNumbers(numbersArrayList, gridSize);
        shuffleNumbers();
        trackingArrayList = new ArrayList<>();

        currentNumberTxt.setText("0");
        timeTrackerTxt.setText(R.string.timer_text_format);
        numberAdapter.notifyDataSetChanged();

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
                startActivity(new Intent(mContext, SchultzTabkeSettingsActivity.class));
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
                        chosenGridSize = 9;
                        restartGame(chosenGridSize);
                        gridColumnSize = 3;
                        numbersGridView.setColumnWidth(3);
                        refreshActivity();
                        break;
                    case 1:
                        chosenGridSize = 16;
                        restartGame(chosenGridSize);
                        gridColumnSize = 4;
                        numbersGridView.setColumnWidth(4);
                        refreshActivity();
                        break;
                    case 2:
                        chosenGridSize = 25;
                        restartGame(chosenGridSize);
                        gridColumnSize = 5;
                        numbersGridView.setColumnWidth(5);
                        refreshActivity();
                        break;
                    default:
                        chosenGridSize = 9;
                        restartGame(chosenGridSize);
                        gridColumnSize = 3;
                        numbersGridView.setColumnWidth(3);
                        refreshActivity();

                }
                gridDialog.dismiss();
                gridDialog = null;

            }
        });


        gridDialog = builder.create();
        if (gridDialog != null) gridDialog.show();

    }

    private void refreshActivity(){
//        recreate();
    }
}
