package com.example.mathurinbloworlf.articlesearch.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mathurinbloworlf.articlesearch.R;
import com.example.mathurinbloworlf.articlesearch.model.Article;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{

    public Context context;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView article_thumbnail;
        public TextView article_headline;
        public TextView article_snippet;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View view){
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(view);

            article_thumbnail = view.findViewById(R.id.article_thumbnail);
            article_headline = view.findViewById(R.id.article_headline);
            article_snippet = view.findViewById(R.id.article_snippet);
        }
    }

    // Store a member variable for the articles
    private List<Article> articles;

    // Pass in the article array into the constructor
    public ArticleAdapter(List<Article> articles){
        this.articles = articles;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.article_model, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // Get the data model based on position
        Article article = articles.get(i);

        // Set item views based on your views and data model
        TextView headline = viewHolder.article_headline;
        headline.setText(article.getHeadline());
        TextView snippet = viewHolder.article_snippet;
        snippet.setText(article.getSnippet());
        ImageView thumbnail = viewHolder.article_thumbnail;
        String thumb = article.getThumbnail();
        if(!TextUtils.isEmpty(thumb)){
            viewHolder.article_thumbnail.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Uri.parse(thumb))
                    .thumbnail(Glide.with(context).load(R.drawable.gif_loading))
                    .error(Glide.with(context).load(R.drawable.ic_broken_image))
                    .into(thumbnail);
        }
        else{
            viewHolder.article_thumbnail.setVisibility(View.GONE);
            Glide.with(context)
                    .load(R.drawable.ic_broken_image)
                    .into(thumbnail);
        }

    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return articles.size();
    }
}
