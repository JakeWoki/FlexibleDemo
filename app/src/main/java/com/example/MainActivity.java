package com.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements FlexibleAdapter.OnItemClickListener {

    private static final String TAG = "MainActivity";

    ProgressBar mProgress;
    RecyclerView mRecyclerView;

    FlexibleAdapter<AbstractFlexibleItem> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgress = (ProgressBar) findViewById(R.id.progress);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new FlexibleAdapter<>(new ArrayList<AbstractFlexibleItem>(), this, true);

        mRecyclerView.setLayoutManager(new SmoothScrollGridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new GridMarginDecoration(
                getResources().getDimensionPixelSize(R.dimen.grid_item_spacing)));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new ImageScrollListener(this));

        UnsplashService unsplashApi = new RestAdapter.Builder()
                .setEndpoint(UnsplashService.ENDPOINT)
                .build()
                .create(UnsplashService.class);
        unsplashApi.getFeed(new Callback<List<Photo>>() {
            @Override
            public void success(List<Photo> photos, Response response) {
                mProgress.setVisibility(View.GONE);
                final List<AbstractFlexibleItem> mItems = new ArrayList<>();
                for (Photo photo : photos) {
                    final PictureItem item = new PictureItem(photo);
                    mItems.add(item);
                }
                 mAdapter.updateDataSet(mItems);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error retrieving Unsplash feed:", error);
            }
        });
    }

    @Override
    public boolean onItemClick(int position) {
        final PictureItem flexible = (PictureItem) mAdapter.getItem(position);
        final Photo photo = flexible.getPhoto();
        final PictureItem.ChildViewHolder viewHolder = (PictureItem.ChildViewHolder)
                mRecyclerView.findViewHolderForAdapterPosition(position);
        final ImageView mIcon = viewHolder.mIcon;
        final TextView mTitle = viewHolder.mTitle;
        ImageActivity.start(this, mIcon, mTitle, photo);
        return true;
    }
}
