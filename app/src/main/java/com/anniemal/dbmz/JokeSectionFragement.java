package com.anniemal.dbmz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anniemal.jsoup.JsoupTool;

import com.anniemal.model.JokeInfo;
import com.anniemal.utils.RefreshUtil;
import com.anniemal.utils.Validator;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by dell on 2015/8/17.
 */
public class JokeSectionFragement extends Fragment {

    private Context mContext;
    private String mJokeId;
    private PullToRefreshListView mPullRefreshListView;
    private JokeListAdapter mJokeAdapter;
    private int curLoadedPage = 1;
    private List<JokeInfo> jokeList;
    private List<JokeInfo> tempJokeList;
    private boolean isPullUp = false;
    private boolean isPullDown = false;
    private MaterialDialog mMaterialDialog;
    String jokeText = null;

    private static final int REQUEST_FINISHED = 100;
    private static final String KEY_CONTENT = "JokeSectionFragment:CategoryId";
    private static final String JOKEURL = "http://www.jokeji.cn/";

    public static JokeSectionFragement newInstance(String categoryId) {
        JokeSectionFragement fragment = new JokeSectionFragement();
        fragment.mJokeId = categoryId;
        return fragment;
    }

    private final MyHandler mHandler = new MyHandler(this);

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mJokeId = savedInstanceState.getString(KEY_CONTENT);
        }

        if (jokeList == null) {
            jokeList = new ArrayList<JokeInfo>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e("ANJOKE", "JKonCreateView:" + this.hashCode()+mJokeId);
        View view = inflater.inflate(R.layout.fragment_section, container, false);

        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        mJokeAdapter = new JokeListAdapter();
        mPullRefreshListView.setAdapter(mJokeAdapter);

        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(RefreshUtil.getRefreshTime(mContext));
                // Do work to refresh the list here.
                isPullUp = true;
                getJokeData(curLoadedPage + 1);
            }

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(RefreshUtil.getRefreshTime(mContext));
                // Do work to refresh the list here.
                isPullDown = true;
                getJokeData(1);
            }
        });

        getJokeData(1);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPullRefreshListView != null) {
            mPullRefreshListView.onRefreshComplete();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mJokeId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void getJokeData(int pageNum) {
        if (Validator.isEffective(mJokeId)) {

            final String pageUrl = mJokeId.substring(0,mJokeId.indexOf("_")) +"_"+ pageNum+".htm";

            new Thread(new Runnable() {

                @Override
                public void run() {

                    List<JokeInfo> jokeList = JsoupTool.getInstance().getAllJokes(pageUrl);

                    if (jokeList != null) {
                        Message msg = new Message();
                        msg.what = REQUEST_FINISHED;
                        msg.obj = jokeList;
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();

        }
    }

    public void showJokeContent(List<JokeInfo> tempList) {
        if (tempList != null && tempList.size() > 0) {

            // first load
            if (!isPullDown && !isPullUp) {
                if (jokeList == null) {
                    jokeList = new ArrayList<JokeInfo>();
                }
                jokeList.addAll(tempList);
                tempList.clear();
            }
            // pull down
            if (isPullDown) {
                curLoadedPage = 1;
                if (jokeList != null) {
                    jokeList.clear();
                    jokeList.addAll(tempList);
                    tempList.clear();
                    isPullDown = false;
                }
            }
            // pull up
            if (isPullUp) {
                if (jokeList == null) {
                    jokeList = new ArrayList<JokeInfo>();
                }
                jokeList.addAll(tempList);
                tempList.clear();
                curLoadedPage++;
                isPullUp = false;
            }

            mJokeAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(mContext, "已到世界的尽头", Toast.LENGTH_SHORT).show();
        }

    }

    public class JokeListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return jokeList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return jokeList.get(position);
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

            final JokeInfo jokeInfo = jokeList.get(position);
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


            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //Snackbar.make(view, "Image clicked, there will be a new page!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                    Intent intent = new Intent(mContext,
                            JokeDetailActivity.class);
                    intent.putExtra("dataText", jokeInfo.getGetUrl());
                    intent.putExtra("title", jokeInfo.getTitle());
                    startActivity(intent);

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


    private static class MyHandler extends Handler {
        private final WeakReference<JokeSectionFragement> mFragment;

        public MyHandler(JokeSectionFragement fragment) {
            mFragment = new WeakReference<JokeSectionFragement>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JokeSectionFragement fragment = mFragment.get();
            if (fragment != null) {
                switch (msg.what) {
                    case REQUEST_FINISHED:
                        if (fragment.mPullRefreshListView != null) {
                            fragment.mPullRefreshListView.onRefreshComplete();
                        }
                        fragment.tempJokeList = (List<JokeInfo>) msg.obj;
                        fragment.showJokeContent(fragment.tempJokeList);
                        break;

                    default:
                        break;
                }
            }
        }
    }

}
