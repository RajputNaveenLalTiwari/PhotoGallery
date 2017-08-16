package com.examples.photogallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

/**
 * Created by 2114 on 24-02-2017.
 */

public class GridViewAdapter extends BaseAdapter
{
    private Context context;
    private int screenWidth,screenHeight;
    private List<MediaImageDetails> mediaImageDetailsList;
    private int pos;
    public GridViewAdapter(Context context,int screenWidth,int screenHeight,List<MediaImageDetails> mediaImageDetailsList,int pos)
    {
        this.context                = context;
        this.screenWidth            = screenWidth;
        this.screenHeight           = screenHeight;
        this.mediaImageDetailsList  = mediaImageDetailsList;
        this.pos = pos;
    }

    @Override
    public int getCount()
    {
        return mediaImageDetailsList.get(pos).mediaImagePathsWithRespectToFolderName.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mediaImageDetailsList.get(pos).mediaImagePathsWithRespectToFolderName.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        ViewHolder viewHolder = null;

        if(view == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_item,parent,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(parent.getContext())
                .load(new File(mediaImageDetailsList.get(pos).mediaImagePathsWithRespectToFolderName.get(position)))
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(viewHolder.getGridViewImage());


        return view;
    }

    public class ViewHolder
    {
        private ImageView gridViewImage;
        private int gridViewImageWidth,gridViewImageHeight;
        private ViewGroup.LayoutParams gridViewImageParams;

        public ViewHolder(View view)
        {
            gridViewImage = (ImageView) view.findViewById(R.id.gridViewImageID);
            gridViewImageWidth = (Math.min(screenWidth,screenHeight)*20)/100;
            gridViewImageHeight = gridViewImageWidth;
            gridViewImageParams = (ViewGroup.LayoutParams) gridViewImage.getLayoutParams();
            gridViewImageParams.width = gridViewImageWidth;
            gridViewImageParams.height = gridViewImageHeight;
        }

        public ImageView getGridViewImage()
        {
            return gridViewImage;
        }
    }
}
