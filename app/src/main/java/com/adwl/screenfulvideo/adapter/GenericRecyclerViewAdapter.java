package com.adwl.screenfulvideo.adapter;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView Adapter
 */
public abstract class GenericRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> models;


    public GenericRecyclerViewAdapter() {
        this(null);
    }

    public GenericRecyclerViewAdapter(List<T> models) {
        this.models = models;
    }

    public void setModels(List<T> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    public List<T> getModels() {
        return models;
    }


    @Override
    public int getItemCount() {
        return models != null ? models.size() : 0;
    }
}
