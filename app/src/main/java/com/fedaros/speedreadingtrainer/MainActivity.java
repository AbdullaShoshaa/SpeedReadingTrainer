package com.fedaros.speedreadingtrainer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fedaros.speedreadingtrainer.SchualtzTable.SchultzTableActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.games_lv)
    ListView gamesLv;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mContext = this;

        ArrayList<String> gamesArrayList = new ArrayList();

        gamesArrayList.add("Schultz Table");

        ArrayAdapter<String> gamesArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, gamesArrayList);

        gamesLv.setAdapter(gamesArrayAdapter);

        gamesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                if (textView.getText().equals("Schultz Table")){
                    startActivity(new Intent(mContext, SchultzTableActivity.class));
                }
            }
        });
    }
}
