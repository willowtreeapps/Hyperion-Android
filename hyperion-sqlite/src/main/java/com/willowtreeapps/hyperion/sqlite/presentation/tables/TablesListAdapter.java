package com.willowtreeapps.hyperion.sqlite.presentation.tables;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.willowtreeapps.hyperion.sqlite.R;
import com.willowtreeapps.hyperion.sqlite.presentation.records.DbRecordViewerActivity;

import java.util.Collections;
import java.util.List;

class TablesListAdapter extends RecyclerView.Adapter<TablesListAdapter.TableViewHolder> {

    private List<TableItem> tables;
    private final String dbName;

    TablesListAdapter(String dbName) {
        this.tables = Collections.emptyList();
        this.dbName = dbName;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hsql_db_table_viewholder, parent, false);
        return new TableViewHolder(itemView);
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

    static class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tableName;
        private String databaseName;

        TableViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tableName = itemView.findViewById(R.id.hsql_table_name);
        }

        void bind(String databaseName,
                  TableItem table) {
            tableName.setText(table.tableName);
            this.databaseName = databaseName;
        }

        @Override
        public void onClick(View v) {
            DbRecordViewerActivity.startActivity(v.getContext(), databaseName,
                    tableName.getText().toString());
        }
    }
}
