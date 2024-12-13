package com.migu3lone.prueba.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;
import com.migu3lone.prueba.R;

public class PerrosAdapter extends ArrayAdapter<String> {

    private int resourceLayout;
    private Context context;
    //private final Context context;
    //private final List<String> objects;
    //private final int resource;

    public PerrosAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.context = context;
        //this.objects = objects;
        //this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resourceLayout, parent, false);
        }

        String perroNombre = getItem(position);

        TextView textViewItem = convertView.findViewById(R.id.textViewItem);
        if (perroNombre != null) {
            textViewItem.setText(perroNombre);
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resourceLayout, parent, false);
        }

        String perroNombre = getItem(position);

        TextView textViewItem = convertView.findViewById(R.id.textViewItem);
        if (perroNombre != null) {
            textViewItem.setText(perroNombre);
        }

        return convertView;
    }
}
