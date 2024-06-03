package ru.mirea.khrebtovsky.mireaproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FileListAdapter extends ArrayAdapter<FileItem> {

    public FileListAdapter(Context context, List<FileItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView fileNameTextView = convertView.findViewById(android.R.id.text1);
        TextView fileHashTextView = convertView.findViewById(android.R.id.text2);

        fileNameTextView.setText(item.getFileName());
        fileHashTextView.setText(item.getFileHash());

        return convertView;
    }

    @Override
    public FileItem getItem(int position) {
        return super.getItem(position);
    }
}

