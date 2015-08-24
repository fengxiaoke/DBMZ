package com.anniemal.dbmz;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.anniemal.adapter.PagerAdapter;
import com.anniemal.jsoup.JokeTool;
import com.anniemal.jsoup.JsoupTool;
import com.anniemal.model.CosImageInfo;
import com.anniemal.model.Data;

import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Andy on 2015/6/11
 */
public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle drawerToggle;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Button mEdit;
    private MaterialDialog mMaterialDialog;
    private String[] jokeTitle;
    private String[] jokeTitleIds;
    private Map<Integer,String> cmap;
    private Data data;



    private static final String[] tabTitles = new String[]{"小清新", "文艺范",
            "大长腿", "黑丝袜", "小翘臀", "大胸妹"};
    private static final String[] tabIds = new String[]{"4", "5", "3", "7",
            "6", "2"};

    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_content);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        mEdit = (Button)findViewById(R.id.edit_info);
        data = (Data)getApplication();

        //mTab = (SlidingTabLayout)findViewById(R.id.tabs);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
            mToolBar.setNavigationIcon(R.mipmap.ic_ab_drawer);
        }
       // setSupportActionBar(mToolBar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        if (mViewPager != null) {
            setupViewPager(mViewPager,tabTitles,tabIds);
            mTabLayout.setupWithViewPager(mViewPager);
        }

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        String[] values = new String[]{
                 "已下载图片", "爆笑段子", "小游戏","绿色背景"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 3:
                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        mToolBar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        mTabLayout.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        mEdit.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 0:
//                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.red));
//                        mToolBar.setBackgroundColor(getResources().getColor(R.color.red));
//                        mTabLayout.setBackgroundColor(getResources().getColor(R.color.red));
//                        mEdit.setBackgroundColor(getResources().getColor(R.color.red));
//                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        mDrawerLayout.closeDrawer(GravityCompat.START);
//                        AsyncTask atcos = new AsyncTask() {
//                            @Override
//                            protected Object doInBackground(Object[] params) {
//                                List<CosImageInfo> cosImages = JsoupTool.getInstance().getCoses("http://tu.duowan.com/tag/41.html");
//                                data.setCs(cosImages);
//                                Log.e("datacos", data.getCs().size() + "...");
//                                return null;
//                            }
//
//                            @Override
//                            protected void onPreExecute() {
//                                mMaterialDialog = new MaterialDialog(MainActivity.this);
//                                mMaterialDialog.setTitle("正在加载……").setMessage("快乐正在路上，请耐心等待哦！");
//                                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.progressbar_item, null);
//                                mMaterialDialog.setCanceledOnTouchOutside(false);
//                                mMaterialDialog.setView(view).show();
//                            }
//
//                            @Override
//                            protected void onPostExecute(Object o) {
//                                mMaterialDialog.dismiss();
//                                Intent cosit = new Intent(MainActivity.this,CosActivity.class);
//                                startActivity(cosit);
//                            }
//                        };
//                        atcos.execute();

                        Intent cosit = new Intent(MainActivity.this, CosActivity.class);
                        startActivity(cosit);

                        break;
                    case 1:

                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        AsyncTask at = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] params) {
                                cmap = new JokeTool().GetCatagrey(JokeTool.PATH_BASE);
                                jokeTitle = new String[cmap.size()];
                                jokeTitleIds = new String[cmap.size()];
                                for (int i = 0; i < cmap.size(); i++) {
                                    int index = cmap.get(i).indexOf("$");
                                    jokeTitle[i] = cmap.get(i).substring(0, index);
                                    jokeTitleIds[i] = cmap.get(i).substring(index + 1);
                                }

                                data.setS(cmap.size());
                                data.setJokeTitle(jokeTitle);
                                data.setJokeId(jokeTitleIds);
                                return null;
                            }

                            @Override
                            protected void onPreExecute() {
//                                mMaterialDialog = new MaterialDialog(MainActivity.this);
//                                mMaterialDialog.setTitle("正在加载……").setMessage("快乐正在路上，请耐心等待哦！");
//                                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.progressbar_item, null);
//                                mMaterialDialog.setCanceledOnTouchOutside(false);
//                                mMaterialDialog.setView(view).show();
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                //mMaterialDialog.dismiss();
                                Intent jit = new Intent(MainActivity.this, JokeActivity.class);
                                startActivity(jit);
                            }
                        };
                        at.execute();

                        break;
                    case 2:

                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        Intent it = new Intent(MainActivity.this, CatActivity.class);
                        startActivity(it);
                        break;
                }

            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog = new MaterialDialog(MainActivity.this);
                mMaterialDialog.setMessage("您确定要离开了吗？")
                .setPositiveButton("离开",new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {


                        System.exit(0);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                }).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_statement:
                Snackbar.make(mToolBar, "本APP仅为学习开发使用,所有图片抓取自http://www.dbmeinv.com/,版权归原作者所有", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;

            case R.id.action_about:
                Snackbar.make(mToolBar, "Email: 949769811@qq.com", Snackbar.LENGTH_LONG).setAction("Action",null).show();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupViewPager(ViewPager viewPager,String[] title,String[] titleid) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        int mCount = title.length;
        for (int i = 0; i < mCount; i++) {

                    adapter.addFragment(PageSectionFragment.newInstance(titleid[i]), title[i]);
            }


        viewPager.setAdapter(adapter);
    }

}
