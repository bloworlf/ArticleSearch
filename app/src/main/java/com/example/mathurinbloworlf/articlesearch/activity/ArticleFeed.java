package com.example.mathurinbloworlf.articlesearch.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.mathurinbloworlf.articlesearch.R;
import com.example.mathurinbloworlf.articlesearch.adapter.ArticleAdapter;
import com.example.mathurinbloworlf.articlesearch.adapter.ArticleArrayAdapter;
import com.example.mathurinbloworlf.articlesearch.fragment.Filter;
import com.example.mathurinbloworlf.articlesearch.model.Article;
import com.example.mathurinbloworlf.articlesearch.other.ItemClickSupport;
import com.example.mathurinbloworlf.articlesearch.other.SpacesItemDecoration;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class ArticleFeed extends AppCompatActivity {

    //String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=android&begin_date=20160112&sort=oldest&fq=news_desk:(%22Education%22%20%22Health%22)&api-key=303d1dbb703549768a457037e703b577";

    GridView gridViewFeed;
    MenuItem filterAction, searchAction;
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<Article> articles;
    ArticleArrayAdapter articleArrayAdapter;
    ArticleAdapter articleAdapter;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    RequestParams requestParams = new RequestParams();
    private boolean emptySearchTriggered = false;

    public RequestParams getRequestParams() {
        return this.requestParams;
    }

    int page = 0;
    String searchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isOnline()){
            setContentView(R.layout.activity_article_feed);

            Toolbar toolbar = findViewById(R.id.toolbar_article_feed);
            setSupportActionBar(toolbar);

            articles = new ArrayList<>();
            //articleArrayAdapter = new ArticleArrayAdapter(this, articles);

            swipeRefreshLayout = findViewById(R.id.feed_refresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    page = 0;
                    if(TextUtils.isEmpty(searchText)){
                        loadFeed(page);
                    }
                    else {
                        searchArticle(searchText, page);
                    }

                }
            });

            recyclerView = findViewById(R.id.recyclerView_feed);
            articleAdapter = new ArticleAdapter(articles);
            recyclerView.setAdapter(articleAdapter);
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);

            SpacesItemDecoration decoration = new SpacesItemDecoration(10);
            recyclerView.addItemDecoration(decoration);
            recyclerView.setItemAnimator(new SlideInUpAnimator());

            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1)) {
                        page++;
                        if(TextUtils.isEmpty(searchText) && emptySearchTriggered){
                            loadFeed(page);
                        }
                        else {
                            searchArticle(searchText, page);
                        }
                    }
                }
            });

            //recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            //    @Override
            //    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            //        return false;
            //    }
            //    @Override
            //    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            //    }
            //    @Override
            //    public void onRequestDisallowInterceptTouchEvent(boolean b) {
            //    }
            //});

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Intent intent = new Intent(getApplicationContext(), ArticleView.class);
                    Article article = articles.get(position);
                    intent.putExtra("article", Parcels.wrap(article));
                    startActivity(intent);
                }
            });



            /*
            gridViewFeed = findViewById(R.id.gridView_feed);
            gridViewFeed.setNumColumns(2);
            //gridViewFeed.setAdapter(articleArrayAdapter);
            gridViewFeed.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if(firstVisibleItem + visibleItemCount >= totalItemCount){
                        page++;
                        if(TextUtils.isEmpty(searchText) && emptySearchTriggered){
                            loadFeed(page);
                        }
                        else {
                            searchArticle(searchText, page);
                        }

                    }
                }
            });

            gridViewFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), ArticleView.class);
                    Article article = articles.get(i);
                    intent.putExtra("article", Parcels.wrap(article));
                    startActivity(intent);
                }
            });
            */

            //loadFeed(page);
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Connection Error");
            builder.setMessage("You should check your connection and try again");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.show();
        }

    }

    //@Override
    //public boolean onPrepareOptionsMenu(Menu menu){
    //    return super.onPrepareOptionsMenu(menu);
    //}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_feed_menu, menu);
        filterAction = menu.findItem(R.id.action_filter);
        searchAction = menu.findItem(R.id.action_search);

        searchView = (SearchView)MenuItemCompat.getActionView(searchAction);
        searchView.setQueryHint("Type to search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(TextUtils.isEmpty(s)){
                    Toast.makeText(ArticleFeed.this, "No term to search \n Looking for any recent news", Toast.LENGTH_SHORT).show();
                    emptySearchTriggered = true;
                    loadFeed(page);
                }
                else {
                    searchText = s;
                    emptySearchTriggered = false;
                    searchView.clearFocus();

                    articles.clear();
                    page = 0;
                    searchArticle(s, page);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        //searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
        //    @Override
        //    public boolean onSuggestionSelect(int i) {
        //        return false;
        //    }
        //
        //    @Override
        //    public boolean onSuggestionClick(int i) {
        //        Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(i);
        //        searchView.setQuery(cursor.getString(4), true);
        //        searchView.clearFocus();
        //        return true;
        //    }
        //});
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                Filter filter = Filter.newInstance();
                filter.show(getSupportFragmentManager(), "FilterFragment");
                return true;
            case R.id.action_search:
                searchAction.expandActionView();
                searchView.requestFocus();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFeed(int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        requestParams = new RequestParams();
        requestParams.put("api-key", "303d1dbb703549768a457037e703b577");
        requestParams.put("page", page);
        requestParams.put("order", "newest");

        client.get(url, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                //Log.d("DEBUG", response.toString());

                JSONArray jsonArray;

                try {
                    jsonArray = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(jsonArray));
                    //articleArrayAdapter.notifyDataSetChanged();
                    if(articleAdapter.getItemCount() == 0){
                        articleAdapter.notifyItemInserted(0);
                    }
                    else {
                        articleAdapter.notifyItemInserted(articleAdapter.getItemCount()-1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                swipeRefreshLayout.setRefreshing(false);
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

        if (requestParams.toString().isEmpty()){
            requestParams = new RequestParams();
            requestParams.put("api-key", "303d1dbb703549768a457037e703b577");
            requestParams.put("page", page);
            requestParams.put("q", string);
        }
        else {
            requestParams.put("api-key", "303d1dbb703549768a457037e703b577");
            requestParams.put("page", page);
            requestParams.put("q", string);
        }
        if (!requestParams.toString().contains("order")){
            SharedPreferences sharedPreferences = getSharedPreferences("FilterPref", Context.MODE_PRIVATE);
            if (sharedPreferences.getInt("order", 0) == 0){
                requestParams.put("order", "newest");
            }
            else {
                requestParams.put("order", "oldest");
            }
        }
        Log.d("REQUESTPARAMS", requestParams.toString());

        client.get(url, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                //Log.d("DEBUG", response.toString());

                JSONArray jsonArray;

                try {
                    jsonArray = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(jsonArray));
                    //articleArrayAdapter.notifyDataSetChanged();
                    //articleAdapter.notifyDataSetChanged();
                    if(articleAdapter.getItemCount() == 0){
                        articleAdapter.notifyItemInserted(0);
                    }
                    else {
                        articleAdapter.notifyItemInserted(articleAdapter.getItemCount()-1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                //Toast.makeText(ArticleFeed.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    protected void onResume() {
        if(!TextUtils.isEmpty(searchText)){
            searchArticle(searchText, page);
        }

        super.onResume();
    }
}
