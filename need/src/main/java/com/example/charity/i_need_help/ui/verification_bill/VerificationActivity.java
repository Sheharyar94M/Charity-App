package com.example.charity.i_need_help.ui.verification_bill;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.charity.R;
import com.example.charity.databinding.ActivityVerificationBinding;
import com.google.android.material.snackbar.Snackbar;

public class VerificationActivity extends AppCompatActivity {

    private ActivityVerificationBinding verificationBinding;
    private String provider_name;
    //    private String LESCO_URL = "https://lesco.com.pk/#:~:text=Just%20browse%20lesco.com.pk,And%20press%20enter%20to%20proceed.";
    //    private String IESCO_URL = "https://iescobill.pk/#:~:text=FAQs,entering%2014%20digit%20reference%20number.";
    //    private String GEPCO_URL = "http://www.gepco.com.pk/GEPCOBill.aspx";
    //    private String HESCO_URL = "https://dbill.pitc.com.pk/hescobill";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verificationBinding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(verificationBinding.getRoot());

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.start_button_color));

        if (getIntent() != null){

            provider_name = getIntent().getStringExtra("provider");

        }

        verificationBinding.heading.setText(provider_name);

        WebView webView = verificationBinding.verificationSite;

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);


        //loading urls

        if (provider_name.equals("LESCO")){

            class WebViewController extends WebViewClient {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);

                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    verificationBinding.billSiteContentLoader.setVisibility(View.GONE);

                }
            }

            webView.setWebViewClient(new WebViewController());

//            webView.setWebViewClient(new WebViewClient(){
//
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//
//                    view.loadUrl("https://lesco.com.pk/#:~:text=Just%20browse%20lesco.com.pk,And%20press%20enter%20to%20proceed.");
//                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);
//                    return true;
//                }
//
//                @Override
//                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                    super.onPageStarted(view, url, favicon);
//
//                }
//
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
//
//                    verificationBinding.billSiteContentLoader.setVisibility(View.GONE);
//
//                }
//
//                @Override
//                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                    super.onReceivedError(view, errorCode, description, failingUrl);
//
//                    //Snack bar
//                    View parentLayout = findViewById(android.R.id.content);
//                    Snackbar snackbar = Snackbar.make(parentLayout, errorCode + description, Snackbar.LENGTH_LONG);
//                    snackbar.setAction("Close", v -> {
//
//                        snackbar.dismiss();
//                    });
//                    snackbar.setActionTextColor(getResources().getColor(R.color.white));
//                    snackbar.show();
//
//                }
//            });

            webView.loadUrl("https://lesco.com.pk/#:~:text=Just%20browse%20lesco.com.pk,And%20press%20enter%20to%20proceed.");

        }
        else if(provider_name.equals("IESCO")){

            class WebViewController extends WebViewClient {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);

                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    verificationBinding.billSiteContentLoader.setVisibility(View.GONE);

                }
            }

            webView.setWebViewClient(new WebViewController());

//            webView.setWebViewClient(new WebViewClient(){
//
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//
//                    view.loadUrl("https://iescobill.pk/#:~:text=FAQs,entering%2014%20digit%20reference%20number.");
//                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);
//                    return true;
//                }
//
//                @Override
//                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                    super.onPageStarted(view, url, favicon);
//
//                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
//
//                    verificationBinding.billSiteContentLoader.setVisibility(View.GONE);
//
//                }
//
//                @Override
//                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                    super.onReceivedError(view, errorCode, description, failingUrl);
//
//                    //Snack bar
//                    View parentLayout = findViewById(android.R.id.content);
//                    Snackbar snackbar = Snackbar.make(parentLayout, errorCode + description, Snackbar.LENGTH_LONG);
//                    snackbar.setAction("Close", v -> {
//
//                        snackbar.dismiss();
//                    });
//                    snackbar.setActionTextColor(getResources().getColor(R.color.white));
//                    snackbar.show();
//
//                }
//            });

            webView.loadUrl("https://iescobill.pk/#:~:text=FAQs,entering%2014%20digit%20reference%20number.");

        }
        else if(provider_name.equals("GEPCO")){

            class WebViewController extends WebViewClient {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);

                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    verificationBinding.billSiteContentLoader.setVisibility(View.GONE);

                }
            }

            webView.setWebViewClient(new WebViewController());

//            webView.setWebViewClient(new WebViewClient(){
//
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//
//                    view.loadUrl("http://www.gepco.com.pk/GEPCOBill.aspx");
//                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);
//                    return true;
//                }
//
//                @Override
//                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                    super.onPageStarted(view, url, favicon);
//
//                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
//
//                    verificationBinding.billSiteContentLoader.setVisibility(View.GONE);
//
//                }
//
//                @Override
//                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                    super.onReceivedError(view, errorCode, description, failingUrl);
//
//                    //Snack bar
//                    View parentLayout = findViewById(android.R.id.content);
//                    Snackbar snackbar = Snackbar.make(parentLayout, errorCode + description, Snackbar.LENGTH_LONG);
//                    snackbar.setAction("Close", v -> {
//
//                        snackbar.dismiss();
//                    });
//                    snackbar.setActionTextColor(getResources().getColor(R.color.white));
//                    snackbar.show();
//
//                }
//            });

            webView.loadUrl("http://www.gepco.com.pk/GEPCOBill.aspx");

        }
        else if(provider_name.equals("HESCO")){

            class WebViewController extends WebViewClient {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);

                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    verificationBinding.billSiteContentLoader.setVisibility(View.GONE);

                }
            }

            webView.setWebViewClient(new WebViewController());

//            webView.setWebViewClient(new WebViewClient(){
//
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//
//                    view.loadUrl("https://dbill.pitc.com.pk/hescobill");
//                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);
//                    return true;
//                }
//
//                @Override
//                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                    super.onPageStarted(view, url, favicon);
//
//                    verificationBinding.billSiteContentLoader.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
//
//                    verificationBinding.billSiteContentLoader.setVisibility(View.GONE);
//
//                }
//
//                @Override
//                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                    super.onReceivedError(view, errorCode, description, failingUrl);
//
//                    //Snack bar
//                    View parentLayout = findViewById(android.R.id.content);
//                    Snackbar snackbar = Snackbar.make(parentLayout, errorCode + description, Snackbar.LENGTH_LONG);
//                    snackbar.setAction("Close", v -> {
//
//                        snackbar.dismiss();
//                    });
//                    snackbar.setActionTextColor(getResources().getColor(R.color.white));
//                    snackbar.show();
//
//                }
//            });

            webView.loadUrl("https://dbill.pitc.com.pk/hescobill");

        }


    }
}