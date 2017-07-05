package com.example.stevenyang.testchangeactivityanimation;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by User on 2017/4/27.
 */

public class MyWvAdapter extends BaseAdapter{
    Context context;
    ArrayList<String> urls;
    public MyWvAdapter(Context context ,ArrayList<String> urls){
        this.context = context;
        this.urls = urls;
    }
    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int i) {
        return urls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder _holder;
        if(view == null){
            view= LayoutInflater.from(context).inflate(R.layout.lv_items,null);
            _holder = new ViewHolder();
            view.setTag(_holder);
        }else{
            _holder =(ViewHolder)view.getTag();
        }
        _holder.wv = (WebView)view.findViewById(R.id.wv);
        _holder.wv.getSettings().setJavaScriptEnabled(true);
        _holder.wv.requestFocus();
        _holder.wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }


        });
        _holder.wv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        _holder.wv.loadUrl(urls.get(i));

        return view;
    }
    static class ViewHolder{
        WebView wv;
    }
}
