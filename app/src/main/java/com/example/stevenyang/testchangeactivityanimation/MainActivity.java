package com.example.stevenyang.testchangeactivityanimation;


import android.content.DialogInterface;

import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AlertDialog;

import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
//引入相关类
import android.app.Activity;
//引入相关类

//引入相关类
import android.hardware.SensorManager;
//引入相关类

//引入相关类
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;

//引入相关类

public class MainActivity extends Activity implements SensorEventListener,View.OnClickListener,CallBackFromDialog ,PapaInterface{
    private SensorManager aSensorManager;
    private Sensor aSensor;
    private float gravity[] = new float[3];
    float beforeX;
    ListView lv;
    float afterX;
    ArrayList<Button> buttonArray = new ArrayList<Button>();
    ArrayList<WebView> webViewArray = new ArrayList<WebView>();
    RelativeLayout activity_main ;
    Gson gson;
    //    Button button1,button2,button3,button4;
//    WebView myWebView,myWebView4,myWebView2,myWebView3;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();
        aSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        aSensor = aSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        aSensorManager.registerListener(this, aSensor, aSensorManager.SENSOR_DELAY_NORMAL);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        activity_main = (RelativeLayout)findViewById(R.id.activity_main);
        SharedPreferencesUtils.setContext(getApplicationContext());
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
            Button tv=new Button(this);
            tv.setLayoutParams(lparams);
            tv.setText("+ 頻道");
            tv.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    new MyDialog(MainActivity.this,MainActivity.this,MainActivity.this).show();
                }
            });
           linearLayout.addView(tv);

        initData();

    }

    private void initData() {
        for(int i=0 ; i<buttonArray.size();i++){
            linearLayout.removeView(buttonArray.get(i));
        }
        buttonArray.clear();
        for(int i=0 ; i<webViewArray.size();i++){
            activity_main.removeView(webViewArray.get(i));
        }
        webViewArray.clear();
        if(((String)SharedPreferencesUtils.getParam("myVideoStrings",""))!=null&&((String)SharedPreferencesUtils.getParam("myVideoStrings","")).length()!=0){
            String str = (String) SharedPreferencesUtils.getParam("myVideoStrings","");
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> arrayList = gson.fromJson(str, type);
            if(arrayList==null)
                arrayList = new ArrayList<>();
            int dataSize = arrayList.size();
             for (int i = 0; i < dataSize; i++) {
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                Button tv=new Button(this);
                tv.setLayoutParams(lparams);
                tv.setText("頻道"+i);
                 final int finalI = i;
                 tv.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        for (int i1 = 0; i1 < webViewArray.size(); i1++) {
                            if(i1== finalI)
                                webViewArray.get(i1).setVisibility(View.VISIBLE);
                            else
                                webViewArray.get(i1).setVisibility(View.INVISIBLE);

                        }
                    }
                });
                 final ArrayList<String> finalArrayList = arrayList;
                 tv.setOnLongClickListener(new View.OnLongClickListener() {
                     @Override
                     public boolean onLongClick(View view) {
                         normalDialogEvent(finalI, finalArrayList);
                         return false;
                     }
                 });


                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                lp.addRule(RelativeLayout.ABOVE,linearLayout.getId());
                WebView wv = new WebView(this);
                wv.setLayoutParams(lp);

                wv.getSettings().setJavaScriptEnabled(true);
                wv.requestFocus();
                wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
                wv.loadUrl(arrayList.get(i));

//                activity_main
                linearLayout.addView(tv);
                buttonArray.add(tv);
                 activity_main.addView(wv);
                 webViewArray.add(wv);
            }
        }
    }

    private void normalDialogEvent(final int index, final ArrayList<String> urlList) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("確定要刪除?")
                .setMessage("刪除此頻道")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), R.string.gogo, Toast.LENGTH_SHORT).show();
                        linearLayout.removeView(buttonArray.get(index));
                        buttonArray.remove(index);
                        activity_main.removeView(webViewArray.get(index));
                        webViewArray.remove(index);
                        urlList.remove(index);
                        Gson gson = new Gson();
                        String json = gson.toJson(urlList);
                        SharedPreferencesUtils.setParam("myVideoStrings", json);
                        initData();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .show();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

// TODO Auto-generated method stub
        gravity[0] = event.values[0];
        gravity[1] = event.values[1];
        gravity[2] = event.values[2];
        afterX = gravity[0];
        if (beforeX - afterX > 8 || beforeX - afterX <= -8) {
            aSensorManager.unregisterListener(this);
//            List<ApplicationInfo> packages;
//            PackageManager pm;
//            pm = getPackageManager();
//            //get a list of installed apps.
//            packages = pm.getInstalledApplications(0);
//
//            ActivityManager mActivityManager = (ActivityManager)getApplication().getSystemService(Context.ACTIVITY_SERVICE);
//
//            for (ApplicationInfo packageInfo : packages) {
//                if((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1)continue;
//                if(packageInfo.packageName.equals("com.example.stevenyang.testchangeactivityanimation")) continue;
//                mActivityManager.killBackgroundProcesses(packageInfo.packageName);
//            }
//            finishAndRemoveTask ();
            finish();
        }
        beforeX = afterX;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean) SharedPreferencesUtils.getParam("myPapa", false)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            aSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            aSensor = aSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            aSensorManager.registerListener(this, aSensor, aSensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
/* 取消註冊SensorEventListener */
        if(aSensorManager!=null) {
            aSensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    @Override
    public void onClick(View view) {
//        if (view == button1) {
//            myWebView.setVisibility(View.VISIBLE);
//            myWebView2.setVisibility(View.INVISIBLE);
//            myWebView3.setVisibility(View.INVISIBLE);
//            myWebView4.setVisibility(View.INVISIBLE);
//
//        }
//        if(view==button2){
//            myWebView.setVisibility(View.INVISIBLE);
//            myWebView2.setVisibility(View.VISIBLE);
//            myWebView3.setVisibility(View.INVISIBLE);
//            myWebView4.setVisibility(View.INVISIBLE);
//        }
//        if(view==button3){
//            myWebView.setVisibility(View.INVISIBLE);
//            myWebView2.setVisibility(View.INVISIBLE);
//            myWebView3.setVisibility(View.VISIBLE);
//            myWebView4.setVisibility(View.INVISIBLE);
//        }
//        if(view==button4){
//            myWebView.setVisibility(View.INVISIBLE);
//            myWebView2.setVisibility(View.INVISIBLE);
//            myWebView3.setVisibility(View.INVISIBLE);
//            myWebView4.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void dialogCallBack() {
        initData();
    }

    @Override
    public void papaCallBack(boolean isPapa) {
        if ((boolean) SharedPreferencesUtils.getParam("myPapa", false)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            aSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            aSensor = aSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            aSensorManager.registerListener(this, aSensor, aSensorManager.SENSOR_DELAY_NORMAL);
        }else{
            if(aSensorManager!=null) {
                aSensorManager.unregisterListener(this);
            }
        }
    }
}