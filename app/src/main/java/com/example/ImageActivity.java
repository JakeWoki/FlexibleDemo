package com.example;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 *
 */

public class ImageActivity extends AppCompatActivity {

    public static final String IS_ACTIVITY_OPTIONS = "isActivityOptions";
    public final static String PHOTO_KEY = "photo";

    ImageView mIcon;
    TextView mTitle;

    boolean isActivityOptions;
    Photo photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mIcon = (ImageView) findViewById(android.R.id.icon);
        mTitle = (TextView) findViewById(android.R.id.title);
        if (getIntent().hasExtra(IS_ACTIVITY_OPTIONS)) {
            isActivityOptions = getIntent().getBooleanExtra(IS_ACTIVITY_OPTIONS, false);
        }
        photo = getIntent().getParcelableExtra(PHOTO_KEY);

        final int requestedPhotoWidth = getResources().getDisplayMetrics().widthPixels;
        mTitle.setText(photo.author);
        Picasso.with(this)
                .load(photo.getPhotoUrl(requestedPhotoWidth))
                .into(mIcon);

        if (isActivityOptions && Build.VERSION.SDK_INT >= 21) {
            postponeEnterTransition();
            mIcon.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public boolean onPreDraw() {
                            mIcon.getViewTreeObserver().removeOnPreDrawListener(this);
                            startPostponedEnterTransition();
                            return true;
                        }
                    });
        }
    }

    public static void start(@NonNull Activity activity, @NonNull ImageView mIcon, @NonNull TextView mTitle, @NonNull Photo photo) {
        final Intent intent = new Intent(activity, ImageActivity.class);

        intent.putExtra(PHOTO_KEY, photo);

        if (Build.VERSION.SDK_INT >= 21) {
            intent.putExtra(IS_ACTIVITY_OPTIONS, true);
            Pair[] sharedElements = new Pair[2];
            sharedElements[0] = Pair.create(mIcon, activity.getString(R.string.transition_designer_imageView));
            sharedElements[1] = Pair.create(mTitle, activity.getString(R.string.transition_designer_title));

            final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, sharedElements);
            activity.startActivity(intent, options.toBundle());
        } else {
            activity.startActivity(intent);
        }
    }
}
