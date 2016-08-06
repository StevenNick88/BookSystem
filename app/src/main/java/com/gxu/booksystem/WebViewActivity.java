package com.gxu.booksystem;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import java.util.Set;


public class WebViewActivity extends BaseActivity {
    private WebView webview;
    private String url=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url=getIntentData();
        if (url != null) {
            //实例化WebView对象
            webview = new WebView(this);
            //设置WebView属性，能够执行Javascript脚本
            webview.getSettings().setJavaScriptEnabled(true);
            //加载需要显示的网页
            webview.loadUrl(url);
            //设置Web视图
            setContentView(webview);
        }else{
            msg(this,"提示","URL有误，请检查！");
        }

    }

    private String getIntentData(){
        Set<String> keySet=getIntent().getExtras().keySet();
        for (String key:keySet){
            if (key.equals("onlineBookItemUrl")){
                String url=getIntent().getStringExtra(key);
                return url;
            }
            if (key.equals("relaxationItemUrl")){
                String url=getIntent().getStringExtra(key);
                return url;
            }

        }
        return null;
    }

//    @Override
//    //设置回退
//    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
//            webview.goBack(); //goBack()表示返回WebView的上一页面
//            return true;
//        }
//        return false;
//    }


}
