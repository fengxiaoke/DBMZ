package com.anniemal.dbmz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anniemal.adapter.JokeListAdapter;
import com.anniemal.jsoup.JsoupTool;
import com.anniemal.model.ImageInfo;
import com.anniemal.model.JokeInfo;
import com.anniemal.utils.ImageLoader;
import com.anniemal.utils.RefreshUtil;
import com.anniemal.utils.Validator;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Andy on 2015/6/11
 */
public class PageSectionFragment extends Fragment {
    private Context mContext;
    private String mCategoryId;
    private PullToRefreshListView mPullRefreshListView;
    private ListViewAdapter mAdapter;
    private int curLoadedPage = 1;
    private List<ImageInfo> imgList;
    private List<ImageInfo> tempImgList;
    private boolean isPullUp = false;
    private boolean isPullDown = false;
    private MaterialDialog mMaterialDialog;
    ProgressDialog dialog=null;
    private Bitmap bitmap=null;
    private ImageLoader imageLoader;



    private static final int REQUEST_FINISHED = 100;
    private static final int DOWNLOAD_FINISHED1 = 200;
    private static final String KEY_CONTENT = "PageSectionFragment:CategoryId";
    private static final String gURL = "http://www.dbmeinv.com/dbgroup/show.htm?cid=";


    public static PageSectionFragment newInstance(String categoryId) {
        PageSectionFragment fragment = new PageSectionFragment();
        fragment.mCategoryId = categoryId;
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
            mCategoryId = savedInstanceState.getString(KEY_CONTENT);
        }

        if (imgList == null) {
            imgList = new ArrayList<ImageInfo>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e("AN", "onCreateView:" + this.hashCode()+"size====="+imgList.size());
        View view = inflater.inflate(R.layout.fragment_section, container, false);

        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        mAdapter = new ListViewAdapter();
        mPullRefreshListView.setAdapter(mAdapter);

        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(RefreshUtil.getRefreshTime(mContext));
                // Do work to refresh the list here.
                isPullUp = true;
                getImageData(curLoadedPage + 1);
            }

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(RefreshUtil.getRefreshTime(mContext));
                // Do work to refresh the list here.
                isPullDown = true;
                getImageData(1);
            }
        });

        getImageData(1);

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
        outState.putString(KEY_CONTENT, mCategoryId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getImageData(int pageNum) {
        if (Validator.isEffective(mCategoryId)) {
            final String pageUrl = gURL + mCategoryId + "&pager_offset=" + pageNum;
            Log.e("pageurl",pageUrl);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    List<ImageInfo> imgList = JsoupTool.getInstance().getAllImages(pageUrl);
                    if (imgList != null) {
                        Message msg = new Message();
                        msg.what = REQUEST_FINISHED;
                        msg.obj = imgList;
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();
        }
    }


    public void showContent(List<ImageInfo> tempList) {
        if (tempList != null && tempList.size() > 0) {

            // first load
            if (!isPullDown && !isPullUp) {
                if (imgList == null) {
                    imgList = new ArrayList<ImageInfo>();

                }
                imgList.addAll(tempList);
                tempList.clear();
            }
            // pull down
            if (isPullDown) {
                curLoadedPage = 1;
                if (imgList != null) {
                    imgList.clear();
                    imgList.addAll(tempList);
                    tempList.clear();
                    isPullDown = false;
                }
            }
            // pull up
            if (isPullUp) {
                if (imgList == null) {
                    imgList = new ArrayList<ImageInfo>();
                }
                imgList.addAll(tempList);
                tempList.clear();
                curLoadedPage++;
                isPullUp = false;
            }

            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(mContext, "已无更多图片", Toast.LENGTH_SHORT).show();
        }

    }

    public class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imgList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return imgList.get(position);
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
                view = LayoutInflater.from(mContext).inflate(R.layout.listview_row_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.iv_img = (ImageView) view.findViewById(R.id.img);
                viewHolder.tv_title = (TextView) view.findViewById(R.id.img_title);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final ImageInfo imgInfo = imgList.get(position);
            String title = imgInfo.getImgTitle();
            String imgUrl = imgInfo.getImgUrl();

            if (Validator.isEffective(title)) {
                viewHolder.tv_title.setText(title);
            }

            if (Validator.isEffective(imgUrl)) {
                Glide.with(mContext).load(imgUrl).into(viewHolder.iv_img);
            }

            viewHolder.iv_img.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //Snackbar.make(view, "Image clicked, there will be a new page!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    mMaterialDialog = new MaterialDialog(mContext)
                            .setTitle("是否下载图片")
                            .setMessage(imgInfo.getImgTitle())
                            .setPositiveButton("下载", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                               DownLoadImageTask dtask = new DownLoadImageTask();
                               dtask.execute(imgInfo.getImgUrl());
                               mMaterialDialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
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
            public ImageView iv_img;
            public TextView tv_title;
        }

    }

    private class MyHandler extends Handler {
        private final WeakReference<PageSectionFragment> mFragment;

        public MyHandler(PageSectionFragment fragment) {
            mFragment = new WeakReference<PageSectionFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PageSectionFragment fragment = mFragment.get();
            if (fragment != null) {
                switch (msg.what) {
                    case REQUEST_FINISHED:
                        if (fragment.mPullRefreshListView != null) {
                            fragment.mPullRefreshListView.onRefreshComplete();
                        }
                        fragment.tempImgList = (List<ImageInfo>) msg.obj;
                        fragment.showContent(fragment.tempImgList);
                        break;
                    case DOWNLOAD_FINISHED1:
                        dialog.dismiss();
                        break;

                    default:
                        break;
                }
            }
        }
    }

    public class DownLoadImageTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(mContext, "",
                    "下载数据，请稍等 …", true, true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if(s!=null){
                Toast.makeText(mContext,"下载失败",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext,"下载成功",Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                Log.d("TAG", "monted sdcard");
            } else {
                Log.d("TAG", "has no sdcard");
            }
            HttpURLConnection con = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            File imageFile = null;
            try {
                URL url = new URL(params[0]);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(10 * 1000);
                con.setReadTimeout(15 * 1000);
                con.setDoInput(true);
                con.setDoOutput(true);
                bis = new BufferedInputStream(con.getInputStream());
                imageFile = new File(getImagePath(params[0]));
                fos = new FileOutputStream(imageFile);
                bos = new BufferedOutputStream(fos);
                byte[] b = new byte[1024];
                int length;
                while ((length = bis.read(b)) != -1) {
                    bos.write(b, 0, length);
                    bos.flush();
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                    if (con != null) {
                        con.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    private String getImagePath(String imageUrl) {
        int lastSlashIndex = imageUrl.lastIndexOf("/");
        String imageName = imageUrl.substring(lastSlashIndex + 1);
        Log.e("imageName",imageName);
        String imageDir = Environment.getExternalStorageDirectory()
                .getPath() + "/DBMZ/";
        File file = new File(imageDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String imagePath = imageDir + imageName;
        Log.e("imagePath",imagePath);
        return imagePath;
    }
}
