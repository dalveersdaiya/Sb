package in.ajm.sb.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.ajm.sb.R;

public class WebViewActivity extends BaseActivity
{
	public static void initActivity(Context context, String title, String url)
	{
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.putExtra("TITLE", title);
		intent.putExtra("URL", url);
		context.startActivity(intent);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		getDeviceTypes();

		String title = getIntent().getStringExtra("TITLE");
		String url = getIntent().getStringExtra("URL");

		setupToolBar(title);
		WebView webView = findViewById(R.id.webview);

		webView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
		webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true); //A
		webView.getSettings().setUseWideViewPort(true); //B
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // load online by default

		if (!isNetworkAvailable())
		{
			webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}
		webView.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
				showLoader();
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
				hideLoader();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
			{
				super.onReceivedError(view, errorCode, description, failingUrl);
				hideLoader();
			}

			@Override
			public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
			{
				super.onReceivedError(view, request, error);
				hideLoader();
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
			{
				super.onReceivedSslError(view, handler, error);
				hideLoader();
			}

			@Override
			public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse)
			{
				super.onReceivedHttpError(view, request, errorResponse);
				hideLoader();
			}
		});
//		LogHelper.systemOutPrint(url);
		webView.loadUrl(url);
	}


	public void onbackPressed(View view)
	{
		onBackPressed();
	}


}
