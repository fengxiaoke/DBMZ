package com.anniemal.dbmz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anniemal.dbmz.R;
import com.anniemal.jsoup.JsoupTool;

public class JokeDetailActivity extends Activity {

    private TextView titleView;
    private WebView mywebView;
    private myTask mt;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_detail);

        init();

    }

    private void init() {

        titleView = (TextView) this.findViewById(R.id.data_derailed_title);
        mywebView = (WebView) this.findViewById(R.id.data_derailed_webView);
        pb = (ProgressBar)findViewById(R.id.progress);
        pb.setMax(100);
        mt = new myTask();

        mywebView.getSettings().setDefaultTextEncodingName("UTF-8");

        mywebView.setBackgroundColor(Color.parseColor("#FFFFCC"));
        mywebView.getSettings().setBuiltInZoomControls(true);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("dataText");
        titleView.setText(title);
        mt.execute(url);
        //mywebView.loadData(info, "text/html; charset=UTF-8", "UTF-8");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_joke_detail, menu);
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

                break;
            case R.id.main_menu_report:

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class myTask extends AsyncTask<String, Integer, String>{

        @Override
        protected void onPreExecute() {
           // pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                mywebView.loadData(s, "text/html; charset=UTF-8", "UTF-8");
            }
            pb.setVisibility(View.GONE);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {

            String jt = JsoupTool.getInstance().getText(params[0]);
            return jt;
        }
    }
}
