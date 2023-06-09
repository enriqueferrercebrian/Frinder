package com.example.frinder.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.frinder.R;

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
        TextView hobby = (TextView) convertView.findViewById(R.id.hobby);


        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);

        name.setText(cardItem.getName());
       age.setText(cardItem.getAge());
        city.setText(cardItem.getCity());
        hobby.setText(cardItem.getHobby());



        Glide.with(getContext())
                .load(cardItem.getProfileImageUrl())
                .override(18, 18)  //just set override like this
                .into(image);


            return convertView;

    }
}
