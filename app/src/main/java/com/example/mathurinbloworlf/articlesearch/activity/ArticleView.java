package com.example.mathurinbloworlf.articlesearch.activity;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mathurinbloworlf.articlesearch.R;
import com.example.mathurinbloworlf.articlesearch.model.Article;

import org.parceler.Parcels;

public class ArticleView extends AppCompatActivity {

    private ShareActionProvider shareActionProvider;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        Toolbar toolbar = findViewById(R.id.toolbar_article_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ArticleFeed.class));
                finish();
            }
        });

        //article = (Article) getIntent().getSerializableExtra("article");

        article = Parcels.unwrap(getIntent().getParcelableExtra("article"));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_view_menu, menu);
        MenuItem share = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(share);
        //create the sharing intent
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        //String sharing = "You\'ll like this article:\n" + article.getHeadline() + "\n" + article.getWeb_url();
        String sharing = "You\'ll like this article:\n" + article.getWeb_url();
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, article.getHeadline());
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sharing);

        //then set the sharing intent
        shareActionProvider.setShareIntent(sharingIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                //
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
