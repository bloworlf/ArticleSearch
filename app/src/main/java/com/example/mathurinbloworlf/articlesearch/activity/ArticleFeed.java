package com.example.mathurinbloworlf.articlesearch.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.mathurinbloworlf.articlesearch.R;
import com.example.mathurinbloworlf.articlesearch.adapter.ArticleArrayAdapter;
import com.example.mathurinbloworlf.articlesearch.fragment.Filter;
import com.example.mathurinbloworlf.articlesearch.model.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ArticleFeed extends AppCompatActivity {

    //String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=android&begin_date=20160112&sort=oldest&fq=news_desk:(%22Education%22%20%22Health%22)&api-key=303d1dbb703549768a457037e703b577";

    Button buttonSearch;
    GridView gridViewFeed;
    EditText editTextSearch;
    MenuItem settings, filter;

    ArrayList<Article> articles;
    ArticleArrayAdapter articleArrayAdapter;

    int page = 0;
    String searchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_feed);

        Toolbar toolbar = findViewById(R.id.toolbar_article_feed);
        setSupportActionBar(toolbar);

        articles = new ArrayList<>();
        articleArrayAdapter = new ArticleArrayAdapter(this, articles);

        gridViewFeed = findViewById(R.id.gridView_feed);
        gridViewFeed.setNumColumns(2);
        gridViewFeed.setAdapter(articleArrayAdapter);
        gridViewFeed.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount >= totalItemCount){
                    if(!TextUtils.isEmpty(searchText)){
                        page++;
                        searchArticle(searchText, page);
                    }

                }
            }
        });

        editTextSearch = findViewById(R.id.editText_search);

        buttonSearch = findViewById(R.id.search_button);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = editTextSearch.getText().toString().trim();
                articles.clear();
                page = 0;
                searchArticle(searchText, page);
            }
        });

        gridViewFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ArticleView.class);
                Article article = articles.get(i);
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });

        //loadFeed(page);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        settings = menu.findItem(R.id.action_settings);
        filter = menu.findItem(R.id.action_filter);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //
                return true;
            case R.id.action_filter:
                Filter filter = Filter.newInstance();
                filter.show(getSupportFragmentManager(), "FilterFragment");
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFeed(int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams requestParams = new RequestParams();
        requestParams.put("api-key", "303d1dbb703549768a457037e703b577");
        requestParams.put("page", page);

        client.get(url, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                //Log.d("DEBUG", response.toString());

                JSONArray jsonArray;

                try {
                    jsonArray = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(jsonArray));
                    articleArrayAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                //Toast.makeText(ArticleFeed.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchArticle(String string, int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams requestParams = new RequestParams();
        requestParams.put("api-key", "303d1dbb703549768a457037e703b577");
        requestParams.put("page", page);
        requestParams.put("q", string);

        client.get(url, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                //Log.d("DEBUG", response.toString());

                JSONArray jsonArray;

                try {
                    jsonArray = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(jsonArray));
                    articleArrayAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                //Toast.makeText(ArticleFeed.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
