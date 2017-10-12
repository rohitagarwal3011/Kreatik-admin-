package com.app.rbc.admin.adapters;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.rbc.admin.R;
import com.app.rbc.admin.models.db.models.Video;
import com.facebook.drawee.view.SimpleDraweeView;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;

/**
 * Created by jeet on 10/9/17.
 */

public class CustomVideoListAdapter extends RecyclerView.Adapter<CustomVideoListAdapter.MyViewHolder> {

    private List<Video> videos;
    private Context context;
    public CustomVideoListAdapter(Context context, List<Video> videos) {
        this.videos = videos;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_video_item,
                parent,
                false
        );

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.video_title.setText(videos.get(position).getTitle());
        try {
            DateTime date = ISODateTimeFormat.dateTime().parseDateTime(videos.get(position).getPublishedat());
            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMM, yyyy");
            holder.video_date.setText(fmt.print(date));
        } catch (Exception e) {
            Log.e("Video Date Error",e.toString());
        }

        Uri imageUri = Uri.parse(videos.get(position).getThumbnail());
        PointF focusPoint = new PointF(0.5f, 0.5f);
        holder.video_icon
                .getHierarchy()
                .setActualImageFocusPoint(focusPoint);
        holder.video_icon.setImageURI(imageUri);

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView video_title;
        public SimpleDraweeView video_icon;
        public TextView video_date;

        public MyViewHolder(View itemView) {
            super(itemView);
            video_title = (TextView)itemView.findViewById(R.id.video_title);
            video_icon =  (SimpleDraweeView) itemView.findViewById(R.id.video_icon);
            video_date = (TextView)itemView.findViewById(R.id.video_date);

        }
    }

    public void refreshAdapter(List<Video> data) {
        this.videos.clear();
        this.videos.addAll(data);
        notifyDataSetChanged();
    }
}