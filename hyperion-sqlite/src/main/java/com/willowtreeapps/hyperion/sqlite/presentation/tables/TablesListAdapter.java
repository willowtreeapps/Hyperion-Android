package com.willowtreeapps.hyperion.sqlite.presentation.tables;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.willowtreeapps.hyperion.sqlite.R;

import java.util.Collections;
import java.util.List;

class TablesListAdapter extends RecyclerView.Adapter<TablesListAdapter.TableViewHolder> {

    private List<TableItem> tables;
    private final String dbName;

    private OnTableSelectedListener clickListener;

    TablesListAdapter(String dbName) {
        this.tables = Collections.emptyList();
        this.dbName = dbName;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hsql_db_table_viewholder, parent, false);
        return new TableViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        holder.bind(dbName, tables.get(position));
    }

    public void setData(List<TableItem> items) {
        this.tables = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public void setClickListener(OnTableSelectedListener clickListener) {
        this.clickListener = clickListener;
    }

    static class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tableName;
        private String databaseName;

        private final OnTableSelectedListener l;

        TableViewHolder(View itemView, OnTableSelectedListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            tableName = itemView.findViewById(R.id.hsql_table_name);
            this.l = listener;
        }

        void bind(String databaseName,
                  TableItem table) {
            tableName.setText(table.tableName);
            this.databaseName = databaseName;
        }

        @Override
        public void onClick(View v) {
            if (l != null) {
                l.onClick(databaseName, tableName.getText().toString());
            }
        }
    }

    public interface OnTableSelectedListener {
        void onClick(String databaseName, String tableName);
    }
}
