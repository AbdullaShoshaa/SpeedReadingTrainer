package com.fedaros.speedreadingtrainer.SchualtzTable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fedaros.speedreadingtrainer.R;
import com.fedaros.speedreadingtrainer.Util.LoggingHelper;

import java.util.ArrayList;

/**
 * Created by Shoshaa on 1912//16.
 */

public class NumberAdapter extends ArrayAdapter {

    private String LOG_TAG = this.getClass().getSimpleName();
    private Context mContext;
    private ArrayList numArrayList;
    private int layoutResourceId;

    //Logging Helper
    private LoggingHelper loggingHelper;


    public NumberAdapter(Context context, int resource, ArrayList<Integer> numArrayList){
        super(context, resource, numArrayList);
        this.mContext = context;
        this.numArrayList = numArrayList;
        this.layoutResourceId = resource;
        loggingHelper = new LoggingHelper(false);//turn on/off logs.
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(layoutResourceId, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.numberTxt = (TextView) convertView.findViewById(R.id.number_txt);
            viewHolder.numberImg = (ImageView) convertView.findViewById(R.id.number_holder_img);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (numArrayList != null){
            loggingHelper.LogDebug(LOG_TAG, "numArrayList is not Null");

            viewHolder.numberTxt.setText(String.valueOf(numArrayList.get(position)));
        }

        return convertView;
    }

    private static class ViewHolder{
        TextView numberTxt;
        ImageView numberImg;
    }

    @Override
    public int getCount() {
        return numArrayList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return numArrayList.get(position);
    }
}
