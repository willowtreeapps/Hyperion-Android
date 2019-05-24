package com.willowtreeapps.hyperion.attr;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

abstract class DataViewHolder<T> extends RecyclerView.ViewHolder {

    private T data;

    public DataViewHolder(View itemView) {
        super(itemView);
    }

    protected T getData() {
        return this.data;
    }

    void bind(T data) {
        this.data = data;
        onDataChanged(data);
    }

    void onDataChanged(T data) {

    }
}