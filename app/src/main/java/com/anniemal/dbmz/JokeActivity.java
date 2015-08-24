package com.anniemal.dbmz;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.anniemal.adapter.PagerAdapter;
import com.anniemal.dbmz.R;
import com.anniemal.model.Data;

import me.drakeet.materialdialog.MaterialDialog;

public class JokeActivity extends ActionBarActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MaterialDialog mMaterialDialog;
    private Data data;


    private String[] JTitles ;
    private String[] JTid ;

    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        mTabLayout = (TabLayout) findViewById(R.id.joketabs);
        data = (Data)getApplication();
        JTitles = new String[data.getS()];
        JTid = new String[data.getS()];
        JTitles = data.getJokeTitle();
        JTid = data.getJokeId();

        mToolBar = (Toolbar) findViewById(R.id.joketoolbar);
        if (mToolBar != null) {
            mToolBar.setTitle("爆笑段子");
            setSupportActionBar(mToolBar);
            mToolBar.setNavigationIcon(R.mipmap.fun);

        }

        mTabLayout = (TabLayout) findViewById(R.id.joketabs);
        mViewPager = (ViewPager) findViewById(R.id.jokeview);
        if (mViewPager != null) {
            setupJokeViewPager(mViewPager, JTitles, JTid);
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_joke, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.main_menu_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "哇塞，这个软件好棒啊，有许多笑话可以看啊，哈哈哈！！！(来自：豆瓣美女)");

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));

                break;

            case R.id.main_menu_report:
                Snackbar.make(mToolBar, "Email: 949769811@qq.com", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupJokeViewPager(ViewPager viewPager,String[] title,String[] titleid) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        int mCount = title.length;
        for (int i = 0; i < mCount; i++) {
                    adapter.addFragment(JokeSectionFragement.newInstance(titleid[i]),title[i]);
        }
        viewPager.setAdapter(adapter);
    }
}
