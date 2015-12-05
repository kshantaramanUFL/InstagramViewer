package com.ebay.kshantaraman.instagramviewer;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ListView contentListView;
    private ArrayList<ImageDisplay> imageDisplayArrayList;
    private ArrayAdapter<ImageDisplay> imageDisplayArrayAdapter;
    private SwipeRefreshLayout swipeRefreshLayoutContainer;
    private static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayoutContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayoutContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPopularPhotosFeed();
            }
        });
        getPopularPhotosFeed();

    }



    private void getPopularPhotosFeed() {
        contentListView = (ListView) findViewById(R.id.contentListView);
        imageDisplayArrayList = new ArrayList<ImageDisplay>();
        //imageDisplayArrayList.add(new ImageDisplay(R.drawable.dp,"karthikvs","#WalkOfFame"));
        //imageDisplayArrayList.add(new ImageDisplay(R.drawable.logo,"eBay","#WeAreeBay"));
        imageDisplayArrayAdapter = new ImageArrayAdapter(this, imageDisplayArrayList);
        contentListView.setAdapter(imageDisplayArrayAdapter);
        String requestURL = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(requestURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                //url, height, username, caption
                JSONArray photosJSON = null;
                try {
                    imageDisplayArrayList.clear();
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photo = photosJSON.getJSONObject(i);
                        JSONArray comments = null;
                        if (photo.optJSONObject("images") != null) {
                            ImageDisplay iPhoto = new ImageDisplay();

                            // ALso check for standard_resolution
                            if (photo.getJSONObject("images").optJSONObject("standard_resolution") != null) {
                                iPhoto.setImageURL(photo.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
                            }

                            if (photo.optJSONObject("user") != null) {
                                iPhoto.setUsername(photo.getJSONObject("user").getString("username"));
                            }

                            if (photo.optJSONObject("caption") != null) {
                                iPhoto.setCaption(photo.getJSONObject("caption").getString("text"));
                            }

                            // add photo to the array
                            imageDisplayArrayList.add(iPhoto);
                        }
                    }
                    imageDisplayArrayAdapter.notifyDataSetChanged();
                    swipeRefreshLayoutContainer.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}