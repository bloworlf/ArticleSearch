package com.example.mathurinbloworlf.articlesearch.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Article {
    /*
    {
    "web_url": "https://www.nytimes.com/interactive/2016/01/13/us/document-Harvard-Slate-Bios.html",
    "snippet": "The five candidates have a mix of political, academic, business and legal experience. One is a household name, and all attended Harvard in some capacity.",
    "print_page": "0",
    "blog": { },
    "source": "The New York Times",
    "multimedia": [
        {
            "rank": 0,
            "subtype": "thumbnail",
            "caption": null,
            "credit": null,
            "type": "image",
            "url": "images/2016/01/13/us/image-Harvard-Slate-Bios/image-Harvard-Slate-Bios-thumbStandard.gif",
            "height": 75,
            "width": 75,
            "legacy": {
                "thumbnailheight": 75,
                "thumbnail": "images/2016/01/13/us/image-Harvard-Slate-Bios/image-Harvard-Slate-Bios-thumbStandard.gif",
                "thumbnailwidth": 75
            },
            "subType": "thumbnail",
            "crop_name": null
        },
        {
            "rank": 0,
            "subtype": "xlarge",
            "caption": null,
            "credit": null,
            "type": "image",
            "url": "images/2016/01/13/us/image-Harvard-Slate-Bios/image-Harvard-Slate-Bios-articleLarge.gif",
            "height": 776,
            "width": 600,
            "legacy": {
                "xlargewidth": 600,
                "xlarge": "images/2016/01/13/us/image-Harvard-Slate-Bios/image-Harvard-Slate-Bios-articleLarge.gif",
                "xlargeheight": 776
            },
            "subType": "xlarge",
            "crop_name": null
        },
        {
            "rank": 0,
            "subtype": "wide",
            "caption": null,
            "credit": null,
            "type": "image",
            "url": "images/2016/01/13/us/image-Harvard-Slate-Bios/image-Harvard-Slate-Bios-thumbWide.gif",
            "height": 126,
            "width": 190,
            "legacy": {
                "wide": "images/2016/01/13/us/image-Harvard-Slate-Bios/image-Harvard-Slate-Bios-thumbWide.gif",
                "widewidth": 190,
                "wideheight": 126
            },
            "subType": "wide",
            "crop_name": null
        }
    ],
    "headline": {
        "main": "Candidates for the Harvard Board of Overseers",
        "kicker": null,
        "content_kicker": null,
        "print_headline": null,
        "name": null,
        "seo": null,
        "sub": null
    },
    "keywords": [ ],
    "pub_date": "2016-01-14T15:20:55+0000",
    "document_type": "multimedia",
    "news_desk": "Education",
    "byline": {
        "original": null,
        "person": null,
        "organization": null
    },
    "type_of_material": "Interactive Feature",
    "_id": "5697bce238f0d865e6c34bbc",
    "word_count": 0,
    "score": 1.0
    }
    */

    String web_url;
    String headline;
    String thumbnail;
    String snippet;

    public String getWeb_url() {
        return web_url;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSnippet() {
        return snippet;
    }

    public Article(){

    }

    public Article(JSONObject jsonObject){
        try{
            this.web_url = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.snippet = jsonObject.getString("snippet");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0){
                JSONObject multimediaObject = multimedia.getJSONObject(0);
                this.thumbnail = "https://www.nytimes.com/" + multimediaObject.getString("url");
            }
            else {
                this.thumbnail = "";
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    public static ArrayList<Article> fromJSONArray(JSONArray jsonArray){
        ArrayList<Article> articles = new ArrayList<>();

        for (int i=0;i<jsonArray.length();i++){
            try {
                articles.add(new Article(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return articles;
    }
}
