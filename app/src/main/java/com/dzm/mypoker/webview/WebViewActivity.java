package com.dzm.mypoker.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dzm.mypoker.R;
import com.dzm.mypoker.databinding.ActivityWebViewBinding;

public class WebViewActivity extends AppCompatActivity {
    public static final String WEB_VIEW_TITLE = "web_view_title";
    public static final String WEB_VIEW_URL = "web_view_url";
    public static final String WEB_VIEW_FULL_SCREEN = "web_view_full_screen";

    private static final int REQ_UPLOAD = 111;

    private ActivityWebViewBinding mActivityWebViewBinding;

    private String mTitle;
    private String mUrl;
    private boolean mIsFullScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        initWebView();

        mActivityWebViewBinding.webview.loadUrl(mUrl);
    }

    private void initData() {
        mTitle = getIntent().getStringExtra(WEB_VIEW_TITLE);
        mUrl = getIntent().getStringExtra(WEB_VIEW_URL);
        mIsFullScreen = getIntent().getBooleanExtra(WEB_VIEW_FULL_SCREEN, false);
    }

    private void initView() {
        mActivityWebViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);

        if (!TextUtils.isEmpty(mTitle) || !mIsFullScreen) {
            setTitle(mTitle);
        }
    }

    private void initWebView() {
        mActivityWebViewBinding.webview.setWebViewClient(webViewClient);
        mActivityWebViewBinding.webview.setWebChromeClient(webChromeClient);
        mActivityWebViewBinding.webview.getSettings().setJavaScriptEnabled(true);
        mActivityWebViewBinding.webview.getSettings().setTextZoom(100);
        WebView.setWebContentsDebuggingEnabled(true);
    }

    public void setTitle(String title) {
        if (mActivityWebViewBinding != null) {
            mActivityWebViewBinding.tvTitle.setVisibility(View.VISIBLE);
            mActivityWebViewBinding.ivBack.setVisibility(View.VISIBLE);
            mActivityWebViewBinding.viewTitleLine.setVisibility(View.VISIBLE);

            mActivityWebViewBinding.tvTitle.setText(title);
            mActivityWebViewBinding.ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.this.finish();
                }
            });
        }
    }

    private WebViewClient webViewClient = new WebViewClient();

    private WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            mUploadCallback = filePathCallback;
//            dispatchOpenMatisse(filePathCallback);
            return true;

        }
    };

    /**
     * 选择本地相册
     */
//    private void dispatchOpenMatisse(ValueCallback<Uri[]> filePathCallback) {
//        Disposable disposable = new RxPermissions(this)
//                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .subscribe(new Consumer<Permission>() {
//                    @Override
//                    public void accept(Permission permission) throws Exception {
//                        if (permission.granted) {
//                            LogUtils.d("request permission: Granted");
//                            dispatchOpenMatisseWithPermission();
//                        } else if (permission.shouldShowRequestPermissionRationale) {
//                            LogUtils.d("request permission: Denied permission without ask never again");
//                            Utils.showShortToast(R.string.permission_need_write_external_storage);
//                            releaseUploadCallback(null);
//                        } else {
//                            // Need to go to the settings
//                            LogUtils.d("request permission: Need to go to the settings");
//                            Utils.showShortToast(R.string.permission_need_write_external_storage);
//                            releaseUploadCallback(null);
//                        }
//                    }
//                });
//    }

//    private void dispatchOpenMatisseWithPermission() {
//        Matisse.from(WebViewActivity.this)
//                .choose(MimeType.ofImage())
//                .countable(false)
//                .maxSelectable(1)
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                .thumbnailScale(0.85f)
//                .imageEngine(new Glide4Engine())
//                .theme(R.style.Matisse_Dracula)
//                .forResult(REQ_UPLOAD);
//    }

    /**
     * 上传文件
     * 1.onShowFileChooser中接收
     * 2.选择文件
     * 3.onActivityResult中回调
     */
    private ValueCallback<Uri[]> mUploadCallback;

    /**
     * 释放UploadCallback
     *
     * @param results 回调结果，可为null
     */
    private void releaseUploadCallback(Uri[] results) {
        if (mUploadCallback != null) {
            mUploadCallback.onReceiveValue(results);
        }
        mUploadCallback = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 上传图片：将获取到的图片uri[]上传
//        if(requestCode == REQ_UPLOAD) {
//            if (null == mUploadCallback) {
//                return;
//            }
//
//            Uri[] results = null;
//
//            if (resultCode == Activity.RESULT_OK && data != null) {
//                List<Uri> list = Matisse.obtainResult(data);
//                if (list != null && list.size() != 0) {
//                    results = new Uri[list.size()];
//                    for (int i = 0; i < list.size(); i ++) {
//                        results[i] = list.get(i);
//                    }
//                }
//            }
//
//            releaseUploadCallback(results);
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && mActivityWebViewBinding != null
                && mActivityWebViewBinding.webview.canGoBack()) {
            mActivityWebViewBinding.webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 打开H5
     * @param context      context
     * @param url           页面地址
     * @param isFullScreen  页面是否需要全屏
     * @param title         页面标题
     */
    public static void launch(Context context, String url, boolean isFullScreen, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WEB_VIEW_URL, url);
        intent.putExtra(WEB_VIEW_FULL_SCREEN, isFullScreen);
        intent.putExtra(WEB_VIEW_TITLE, title);
        context.startActivity(intent);
    }
}
