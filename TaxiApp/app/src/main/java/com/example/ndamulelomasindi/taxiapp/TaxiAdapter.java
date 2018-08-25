package com.example.ndamulelomasindi.taxiapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TaxiAdapter extends ArrayAdapter<Drivers>
{
    private Context context;
    private List<Drivers> drivers;

    public TaxiAdapter(Context context, List<Drivers> list)
    {
        super(context, R.layout.row_layout, list);
        this.context = context;
        this.drivers = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.row_layout, parent, false);

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);

        tvName.setText(drivers.get(position).getName());
        tvAmount.setText("R" + drivers.get(position).getAmount());

        return convertView;
    }
}
