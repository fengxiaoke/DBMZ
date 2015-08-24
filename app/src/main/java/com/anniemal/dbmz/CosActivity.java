package com.anniemal.dbmz;

import android.app.Activity;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.anniemal.model.CosImageInfo;
import com.anniemal.model.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class CosActivity extends Activity {

        public static List<String> picList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cos);

        picList.clear();
         if(hasSDCard()){
                 String imagePath = Environment.getExternalStorageDirectory().toString()
                         + "/DBMZ";
                 File mfile = new File(imagePath);
                 File[] files = mfile.listFiles();
                 for (int i = 0; i < files.length; i++) {
                         File file = files[i];
                         if (checkIsImageFile(file.getPath())) {
                                 picList.add(file.getPath());
                         }

                 }
             Log.e("sizessssss",picList.size()+"ppppppppp");
         }else{
                 Toast.makeText(CosActivity.this,"SD卡不存在",Toast.LENGTH_SHORT).show();
         }


}

        private boolean checkIsImageFile(String fName) {
                boolean isImageFile = false;

                // 获取扩展名
                String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                        fName.length()).toLowerCase();
                if (FileEnd.equals("jpg") || FileEnd.equals("gif")
                        || FileEnd.equals("png") || FileEnd.equals("jpeg")
                        || FileEnd.equals("bmp")) {
                        isImageFile = true;
                } else {
                        isImageFile = false;
                }

                return isImageFile;

        }

        private boolean hasSDCard() {
                return Environment.MEDIA_MOUNTED.equals(Environment
                        .getExternalStorageState());
        }

        public static String[] imageUrls = new String[] {
            "http://ww4.sinaimg.cn/bmiddle/0060lm7Tgw1ev75ymam2xj30dw0ijwh6.jpg",
            "http://www.cosplay8.com/uploads/allimg/150225/1-150225140610.jpg",
            "http://www.cosplay8.com/uploads/allimg/141107/1-14110G43203.jpg",
            "http://ww2.sinaimg.cn/bmiddle/0060lm7Tgw1ev8tarceqcj30dw0jpq78.jpg",
            "http://ww1.sinaimg.cn/bmiddle/0060lm7Tgw1ev8tawndiej30dw0iitai.jpg",
            "http://image.tianjimedia.com/uploadImages/2012/299/H4O84O52N974_113.jpg",
            "http://img6.douban.com/view/photo/thumb/public/p2247322652.jpg",
            "http://c.hiphotos.baidu.com/image/pic/item/09fa513d269759eebfb8fac6b0fb43166c22dfd7.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037192_8379.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037178_9374.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037177_1254.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037177_6203.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037152_6352.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037151_9565.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037151_7904.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037148_7104.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037129_8825.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037128_5291.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037128_3531.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037127_1085.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037095_7515.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037094_8001.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037093_7168.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037091_4950.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949643_6410.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949642_6939.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949630_4505.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949630_4593.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949629_7309.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949629_8247.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949615_1986.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949614_8482.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949614_3743.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949614_4199.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949599_3416.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949599_5269.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949598_7858.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949598_9982.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949578_2770.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949578_8744.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949577_5210.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949577_1998.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949482_8813.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949481_6577.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949480_4490.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949455_6792.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949455_6345.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949442_4553.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949441_8987.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949441_5454.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949454_6367.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949442_4562.jpg" };
}


