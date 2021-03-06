package com.purefaithstudio.gurbani;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shephertz.app42.paas.sdk.android.upload.Upload;

import java.util.ArrayList;

/**
 * Created by MY System on 10/20/2015.
 */
public class UpDownAdapter extends RecyclerView.Adapter<UpDownAdapter.ViewHolder> {
    public Context context;
    private String fontUrl = "fonts/AnmolLipi2.ttf";

    public ClickListener clickListener;
    int currImage = 0;
    private ArrayList<Upload.File> items;


    public UpDownAdapter(Context context, ArrayList<Upload.File> items) {
        Log.i("harjas", "names");
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.udcard_layout, null);
        Log.i("harjas", "oncreate");
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            String name=items.get(position).getName();
            if(name.length()>17) {
                name=name.substring(0,17)+"..";
            }
            name.replaceAll("_"," ");
            holder.name.setText(name);
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.getitemTextSize());
            holder.ragi.setText(items.get(position).getUserName());
            holder.ragi.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.getitemTextSize());
            holder.size.setText("" + items.get(position).getDescription());
            holder.size.setTextSize(TypedValue.COMPLEX_UNIT_PX, MainActivity.getitemTextSize());
            Log.d("harsim", "name:" + items.get(position).getName());//nice
            holder.imageView.setImageResource(R.drawable.khanda);
            //pass viewholder to async task-DownloadImage(viewholder,url)
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("harjas", "onbind");
    }

    @Override
    public int getItemCount() {
        // Log.d("harsim","recycler items size:"+items.size());
        if(items==null)
            return 0;
        return items.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void updateList(ArrayList<Upload.File> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        public void itemClicked(View v, int position, String name);


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout upperView;
        TextView name, ragi, size;
        ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.shabad_name);
            ragi = (TextView) itemView.findViewById(R.id.ragi);
            size = (TextView) itemView.findViewById(R.id.size);
            imageView = (ImageView) itemView.findViewById(R.id.shabad_image);


          /*  if (MainActivity.font == "hindi") fontUrl = "fonts/GurbaniHindi.ttf";
            else if (MainActivity.font == "punjabi") fontUrl = "fonts/AnmolLipi2.ttf";
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontUrl);
            //typeface.isBold();

            name.setTypeface(typeface, Typeface.BOLD);
            ragi.setTypeface(typeface, Typeface.BOLD);
            size.setTypeface(typeface, Typeface.BOLD);*/

            itemView.setOnClickListener(this);
            Log.i("harjas", "textview set");
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();//dec by 1 if header on plus add header logic
            if (clickListener != null) {
                clickListener.itemClicked(v, position, items.get(position).getUrl());
            }
        }

        private int convert(int n) {
            return Integer.valueOf(String.valueOf(n), 16);
        }
    }
}
