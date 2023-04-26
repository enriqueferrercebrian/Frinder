package com.example.frinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {

    Context context;

    public arrayAdapter ( Context context , int resourceId, List<cards> items){
        super(context,resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        cards cardItem = getItem(position);

        if (convertView ==  null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }


        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView age = (TextView) convertView.findViewById(R.id.age);
        TextView city = (TextView) convertView.findViewById(R.id.city);


        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);

        name.setText(cardItem.getName());
        age.setText(cardItem.getAge());
        city.setText(cardItem.getCity());


        image.setImageResource(R.mipmap.ic_launcher);


            return convertView;

    }
}
