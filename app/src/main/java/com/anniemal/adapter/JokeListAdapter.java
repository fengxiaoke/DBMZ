package com.anniemal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import com.anniemal.dbmz.R;

import com.anniemal.model.JokeInfo;
import com.anniemal.utils.Validator;


import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by dell on 2015/8/17.
 */
public class JokeListAdapter extends BaseAdapter {

    private List<JokeInfo> jokes;
    private Context mContext;
    private MaterialDialog mMaterialDialog;

    public JokeListAdapter(List<JokeInfo> js,Context ctx){
        this.jokes = js;
        this.mContext = ctx;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return jokes.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return jokes.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        final ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_joke_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.joke_title = (TextView) view.findViewById(R.id.joke_title);
            viewHolder.joke_time = (TextView) view.findViewById(R.id.joke_time);
            viewHolder.joke_num = (TextView) view.findViewById(R.id.joke_num);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final JokeInfo jokeInfo = jokes.get(position);
        String title = jokeInfo.getTitle();
        String time = jokeInfo.getTime();
        String num = jokeInfo.getNumber();

        if (Validator.isEffective(title)) {
            viewHolder.joke_title.setText(title);
        }

        if (Validator.isEffective(time)) {
            viewHolder.joke_time.setText(time);
        }

        if (Validator.isEffective(num)) {
            viewHolder.joke_num.setText(num);
        }

        viewHolder.joke_title.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Image clicked, there will be a new page!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                mMaterialDialog = new MaterialDialog(mContext)
                        .setTitle(jokeInfo.getTitle())
                        .setMessage(jokeInfo.getText())
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();

                            }
                        })
                        .setNegativeButton("CANCEL", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();

                            }
                        })
                        .setCanceledOnTouchOutside(true);

                mMaterialDialog.show();

            }
        });

        return view;
    }

    class ViewHolder {
        public TextView joke_title;
        public TextView joke_time;
        public TextView joke_num;
    }
}
