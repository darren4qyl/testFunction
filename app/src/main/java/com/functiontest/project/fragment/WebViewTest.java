package com.functiontest.project.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xwalk.core.XWalkGetBitmapCallback;
import org.xwalk.core.XWalkView;

import com.functiontest.project.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebViewTest extends BaseFragment implements View.OnClickListener {
    private final String TAG = "MutilateThreadArrayMapTestFragment";
    private View view;
    private Button mBtnNormalStart = null;
    private XWalkView mWeb = null;
    private ArrayMap<String,Integer> mLockArray = new ArrayMap<>();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    //https://www.vip3070.com/User_Login/index.aspx?__VIEWSTATE=%2FwEPDwUJMzY5NzExMjg0ZGTqAm02OI5%2FEQZLA0AOKn6D07ilijhNpAyZe6JFZxP9DA%3D%3D&__VIEWSTATEGENERATOR=407A9F86&__EVENTVALIDATION=%2FwEdAAb4QAlj4Nxtrj2hAhybEhbxD43FB8qy1A20R2ySuZF09O53ynl7T%2BobIx%2Bzewg2ZToa57zRAWFq9Re%2FsC1o8PqfWOqFFB84rEDYyJK6oq69g834O%2FGfAV4V4n0wgFZHr3cYFpdJiT3welK6Gg26FtUG4pHW%2BITRNEo87NY78x8LUw%3D%3D&Login_Phone=18618289760&Login_pass=802244&J_codetext=VBMN&J_codes=VBMN&Button1=%E7%AB%8B%E5%8D%B3%E7%99%BB%E9%99%86
    @Override
    public View InitilizedView() {
        if (view == null)
            view = View.inflate(mMainActivity, R.layout.fragment_web_test_func, null);
        mBtnNormalStart = view.findViewById(R.id.web_btn);
        mWeb = view.findViewById(R.id.webtest);
        mBtnNormalStart.setOnClickListener(this);
        return view;
    }
    private OkHttpClient client;
    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";
    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }
    public String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        ActivityCompat.requestPermissions(mMainActivity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.web_btn:
                client=new OkHttpClient();
                mWeb.loadUrl("https://www.vip3070.com/User_Login/index.aspx");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String src;try {
                        //http使用get方式获得返回的html代码
                        Request request = new Request.Builder()
                                .url("https://www.vip3070.com/User_Login/index.aspx")
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();//返回的内容

                        //jsoup解析，从而得到动态验证码的url地址
                        Document parse = Jsoup.parse(responseData);
                        Elements select = parse.select("img[alt=CAPTCHA]");
                        Element element = select.get(0);
                        src = element.attr("src");
                        String imagehash = src.substring(36);
                        System.out.println("imagehash"+src);

                        //get方式请求验证码url地址，得到返回验证码图片文件
                        request = new Request.Builder()
                                .url("http://hdhome.org/"+src)
                                .build();
                            response = client.newCall(request).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }
}
