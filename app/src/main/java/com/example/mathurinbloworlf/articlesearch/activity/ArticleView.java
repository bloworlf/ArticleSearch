package com.example.mathurinbloworlf.articlesearch.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mathurinbloworlf.articlesearch.R;
import com.example.mathurinbloworlf.articlesearch.model.Article;

public class ArticleView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        Toolbar toolbar = findViewById(R.id.toolbar_article_view);
        setSupportActionBar(toolbar);

        final Article article = (Article) getIntent().getSerializableExtra("article");

        WebView webView = findViewById(R.id.article_view);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(article.getWeb_url());
                return true;
            }
        });

        webView.loadUrl(article.getWeb_url());
    }
}
