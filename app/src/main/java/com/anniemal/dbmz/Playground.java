package com.anniemal.dbmz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

import java.util.HashMap;
import java.util.Vector;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by dell on 2015/8/16.
 */
public class Playground extends SurfaceView implements View.OnTouchListener{


    private static int WIDTH = 70;
    private static final int Row = 10;
    private static final int COL = 10;
    private static final int BLOCKS = 10;
    private Dot matrix[][];
    private Dot cat;
    private MaterialDialog mMaterialDialog;
    private Context mContext;
    private Bitmap catpic,redpic;

    public Playground(Context context) {
        super(context);
        mContext = context;
        getHolder().addCallback(cb);
        setOnTouchListener(this);
        matrix = new Dot[Row][COL];
        for (int i=0;i<Row;i++){
            for(int j=0;j<COL;j++){
                matrix[i][j] = new Dot(j,i);
            }
        }


        init();
    }

    private void redraw(){
        Canvas c = getHolder().lockCanvas();
        c.drawColor(Color.LTGRAY);

        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        for (int i=0;i<Row;i++){
            int offset = 0;
            if(i%2!=0){
                offset = WIDTH/2;
            }

            for(int j=0;j<COL;j++){
               Dot one = getDot(j,i);
                switch (one.getStatus()){
                    case Dot.STATUS_IN:
                        paint.setColor(0xFFFF0000);
                        c.drawBitmap(catpic,one.getX() * WIDTH + offset,one.getY() * WIDTH,paint);
                        break;
                    case Dot.STATUS_OFF:
                        paint.setColor(0xFFEEEEEE);
                        c.drawOval(new RectF(one.getX() * WIDTH + offset, one.getY() * WIDTH,
                                (one.getX() + 1) * WIDTH + offset, (one.getY() + 1) * WIDTH), paint);
                        break;
                    case Dot.STATUS_ON:
                        paint.setColor(0xFFFFAA00);
                        c.drawBitmap(redpic, one.getX() * WIDTH + offset, one.getY() * WIDTH, paint);
                        break;
                    default:
                        break;
                }
               // c.drawOval(new RectF(one.getX() * WIDTH + offset, one.getY() * WIDTH,
                //        (one.getX() + 1) * WIDTH + offset, (one.getY() + 1) * WIDTH), paint);
                //c.drawBitmap(catpic,one.getX() * WIDTH + offset+WIDTH/2,one.getY() * WIDTH+WIDTH/2,paint);
            }
        }
        getHolder().unlockCanvasAndPost(c);

    }

    private Dot getDot(int x,int y){
        return matrix[y][x];
    }

    //判断是否边界
    private boolean isAtEdge(Dot d){
        if(d.getX()*d.getY()==0||d.getX()+1==COL||d.getY()+1==Row){
            return true;
        }
        return false;
    }

    //获取相邻点
    private Dot getNeighbour(Dot d,int dir){
        switch (dir){
            case 1:
                return getDot(d.getX()-1,d.getY());
            case 2:
                if(d.getY()%2==0){
                    return  getDot(d.getX()-1, d.getY()-1);
                }else{
                    return  getDot(d.getX(),d.getY()-1);
                }
            case 3:
                if(d.getY()%2==0){
                    return  getDot(d.getX(),d.getY()-1);
                }else{
                    return  getDot(d.getX()+1,d.getY()-1);
                }
            case 4:
                return getDot(d.getX()+1,d.getY());
            case 5:
                if(d.getY()%2==0){
                    return getDot(d.getX(),d.getY()+1);
                }else{
                    return  getDot(d.getX()+1,d.getY()+1);
                }
            case 6:
                if(d.getY()%2==0){
                    return  getDot(d.getX()-1,d.getY()+1);
                }else{
                    return  getDot(d.getX(),d.getY()+1);
                }
            default:
                break;
        }

        return null;
    }

    //获取周围可移动距离
    private int getDistance(Dot d,int dir){
        int distance = 0;
        if(isAtEdge(d)){
            return 1;
        }
        Dot ori = d;
        Dot next;
        while (true){
            next = getNeighbour(ori,dir);
            if(next.getStatus() == Dot.STATUS_ON){
                return distance*-1;
            }
            if(isAtEdge(next)){
                distance++;
                return distance;
            }
            distance++;
            ori = next;
        }
    }

    private void moveTo(Dot d){
        d.setStatus(Dot.STATUS_IN);
        getDot(cat.getX(), cat.getY()).setStatus(Dot.STATUS_OFF);
        cat.setXY(d.getX(),d.getY());
    }

    private void move(){
        if(isAtEdge(cat)){
            lose();
            return;
        }
        Vector<Dot> avaliable = new Vector<>();
        Vector<Dot> positive = new Vector<>();
        HashMap<Dot,Integer> allLength = new HashMap<Dot,Integer>();
        for(int i=1;i<7;i++){
            Dot n = getNeighbour(cat,i);
            if(n.getStatus()==Dot.STATUS_OFF){
                avaliable.add(n);
                allLength.put(n, i);
                if(getDistance(n,i)>0){
                    positive.add(n);

                }
            }
        }
        if(avaliable.size()==0){
            win();
        }else if(avaliable.size()==1){
            moveTo(avaliable.get(0));
        }else{
            Dot best = null;
            if(positive.size()>0){//存在可到达屏幕边缘路径
                int min = 999;
                for(int i = 0;i<positive.size();i++){
                   int a = getDistance(positive.get(i),allLength.get(positive.get(i)));
                    if(a<min){
                        min = a;
                        best = positive.get(i);
                    }
                }

            }else{
                int max = 0;
                for(int i = 0;i<avaliable.size();i++){
                    int k = getDistance(avaliable.get(i),allLength.get(avaliable.get(i)));
                    if(k<max){
                        max = k;
                        best = avaliable.get(i);
                    }
                }
            }
            try {
                moveTo(best);
            }catch (Exception e){
                win();
            }

        }
    }

    private void lose(){
        mMaterialDialog = new MaterialDialog(mContext)
                .setTitle("You Lost")
                .setMessage("Try again?")
                .setPositiveButton("Yes", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        init();
                        redraw();
                    }
                }).setNegativeButton("No", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    private void win(){
        mMaterialDialog = new MaterialDialog(mContext)
                .setTitle("You Win")
                .setMessage("Try again?")
                .setPositiveButton("Yes", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        init();
                        redraw();
                    }
                }).setNegativeButton("No", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    Callback cb = new Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            redraw();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            WIDTH = width/(COL+1);
            redraw();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    private void init(){
        for (int i=0;i<Row;i++){
            for(int j=0;j<COL;j++){
                matrix[i][j].setStatus(Dot.STATUS_OFF);
            }
        }
        cat = new Dot(Row/2,COL/2);
        getDot(Row/2,COL/2).setStatus(Dot.STATUS_IN);
        for(int i=0;i<BLOCKS;){
            int x = (int) ((Math.random()*1000)%COL);
            int y = (int) ((Math.random()*1000)%Row);
            if(getDot(x,y).getStatus()==Dot.STATUS_OFF){
                getDot(x,y).setStatus(Dot.STATUS_ON);
                i++;
            }
        }

        catpic = BitmapFactory.decodeResource(getResources(),R.mipmap.ball);
        redpic = BitmapFactory.decodeResource(getResources(),R.mipmap.angry);
        int cwidth = catpic.getWidth();
        int rwidth = redpic.getWidth();
        int cheight = catpic.getHeight();
        int rheight = redpic.getHeight();
        float scaleWidth = ((float) 70) / cwidth;
        float scaleHeight = ((float) 70) / cheight;
        float rscaleWidth = ((float) 70) / rwidth;
        float rscaleHeight = ((float) 70) / rheight;
        Matrix matrix = new Matrix();
        Matrix rmatrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        rmatrix.postScale(rscaleWidth, rscaleHeight);
        catpic = Bitmap.createBitmap(catpic,0,0,cwidth,cheight,matrix,true);
        redpic = Bitmap.createBitmap(redpic,0,0,rwidth,rheight,rmatrix,true);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_UP){
            int x,y;
            y = (int)(event.getY()/WIDTH);
            if(y%2==0){
                x = (int)(event.getX()/WIDTH);
            }else{
                x = (int)((event.getX()-WIDTH/2)/WIDTH);
            }
            if(x+1>COL||y+1>Row){
                //init();
            }else if(getDot(x,y).getStatus() == Dot.STATUS_OFF){
                getDot(x,y).setStatus(Dot.STATUS_ON);
                move();
            }

            redraw();
        }
        return true;
    }
}
