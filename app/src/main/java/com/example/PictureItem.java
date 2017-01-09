package com.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 *
 * Created by owp on 2017/1/9.
 */

public class PictureItem extends AbstractFlexibleItem<PictureItem.ChildViewHolder> {

    private Photo photo;

    public PictureItem(Photo photo) {
        this.photo = photo;
    }

    public Photo getPhoto() {
        return photo;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.picture_item;
    }

    @Override
    public ChildViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ChildViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ChildViewHolder holder, int position, List payloads) {
        final Context context = holder.itemView.getContext();
        final int requestedPhotoWidth = context.getResources().getDisplayMetrics().widthPixels;
        holder.mTitle.setText(photo.author);
        Picasso.with(context)
                .load(photo.getPhotoUrl(requestedPhotoWidth))
                .into(holder.mIcon);
    }

    public static final class ChildViewHolder extends FlexibleViewHolder {

        public ImageView mIcon;
        public TextView mTitle;

        public ChildViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            this.mIcon = (ImageView) view.findViewById(android.R.id.icon);
            this.mTitle = (TextView) view.findViewById(android.R.id.title);
        }
    }

}
