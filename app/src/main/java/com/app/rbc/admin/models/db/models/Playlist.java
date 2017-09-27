package com.app.rbc.admin.models.db.models;

import com.orm.SugarRecord;

/**
 * Created by jeet on 8/9/17.
 */

public class Playlist extends SugarRecord {
    private String playlistid;
    private String title;
    private String description;
    private String thumbnail;
    private long itemcount;

    public String getPlaylistid() {
        return playlistid;
    }

    public void setPlaylistid(String playlistid) {
        this.playlistid = playlistid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public long getItemcount() {
        return itemcount;
    }

    public void setItemcount(long itemcount) {
        this.itemcount = itemcount;
    }
}
