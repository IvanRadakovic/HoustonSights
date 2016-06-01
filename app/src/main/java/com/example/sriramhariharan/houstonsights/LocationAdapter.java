package com.example.sriramhariharan.houstonsights;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SriramHariharan on 5/15/16.
 */
public class LocationAdapter extends BaseAdapter {

        Context context;
        private ArrayList<Place> places;
        private static LayoutInflater inflater = null;
        public LocationAdapter (Context contxt,ArrayList<Place> tim) {
        places = tim;
        context = contxt;
        inflater = (LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return places.size();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public class Holder
    {

        TextView place;
        TextView info;
        ImageView im;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.rowlayout, null);
        holder.place= (TextView) rowView.findViewById(R.id.Destinationtitle);
        holder.info = (TextView)rowView.findViewById(R.id.moreinfo);
        holder.im = (ImageView) rowView.findViewById(R.id.imaged);
        holder.place.setText(places.get(position).getName());
        //holder.info.setText(places.get(position).getInfo());
        holder.info.setText(places.get(position).getDescription());
        String tag = "";
        if(places.get(position).getTags().contains(",")) {
            tag = places.get(position).getTags().substring(0, places.get(position).getTags().indexOf(","));
        }
        else{
           tag = places.get(position).getTags();
        }
        if(tag.equals("history")){
            holder.im.setImageResource(R.drawable.museum_war);
        }
        if(tag.equals("culture")){
            holder.im.setImageResource(R.drawable.jazzclub);
        }
        if(tag.equals("art")){
            holder.im.setImageResource(R.drawable.music_live);
        }
        if(tag.equals("science")){
            holder.im.setImageResource(R.drawable.chemistry2);
        }
        if(tag.equals("food")){
            holder.im.setImageResource(R.drawable.burger);
        }
        if(tag.equals("museum")){
            holder.im.setImageResource(R.drawable.historical_museum);
        }
        if(tag.equals("shopping")){
            holder.im.setImageResource(R.drawable.supermarket);
        }
        if(tag.equals("park")){
            holder.im.setImageResource(R.drawable.tree);
        }
        if(tag.equals("landmark")){
            holder.im.setImageResource(R.drawable.junction);
        }
        if(tag.equals("spiritual")){
            holder.im.setImageResource(R.drawable.prayer);
        }
        if(tag.equals("entertainment")){
            holder.im.setImageResource(R.drawable.theater);
        }
        return rowView;
    }
}
