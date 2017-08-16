package com.examples.photogallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;

/**
 * Created by 2114 on 13-02-2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private Context context;
    private static int screenWidth,screenHeight;
    private static List<MediaImageDetails> mediaImageDetailsList;
    /**
     *   Constructor
     */
    public RecyclerViewAdapter(Context context,int screenWidth,int screenHeight,List<MediaImageDetails> mediaImageDetailsList)
    {
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.mediaImageDetailsList = mediaImageDetailsList;
    }

    /**
     *   Inflate our view
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,screenWidth,screenHeight);
        return viewHolder;
    }

    /**
     *   Show content of our view
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        int w = ViewHolder.recyclerViewImageWidth;
        int h = ViewHolder.recyclerViewImageHeight;

//        File file = new File(imageUriList.get(position).toString());
//        holder.getRecyclerViewImage().setImageBitmap(ThumbnailUtils.extractThumbnail(optimizedBitmap(file,w,h),w, h));

       /* Picasso.with(context)
                .load(new File(imageUriList.get(position).toString()))
                .resize(w,h)
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .into(holder.getRecyclerViewImage());*/

        Glide.with(context)
                .load(new File(mediaImageDetailsList.get(position).mediaThumbnailPathWithRespectToFolderName))
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.getRecyclerViewImage());
    }

    /**
     *   Return's total number of items
     */
    @Override
    public int getItemCount()
    {
        return mediaImageDetailsList.size();
    }



    /**
     *   Regiter our view's and return
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView recyclerViewImage;
        private ViewGroup.LayoutParams recyclerViewImageParams;
        private static  int recyclerViewImageWidth,recyclerViewImageHeight;


        public ViewHolder(View view,int screenWidth,int screenHeight)
        {
            super(view);

            recyclerViewImage = (ImageView) view.findViewById(R.id.recyclerViewImageID);
            recyclerViewImageWidth = (Math.min(screenWidth,screenHeight)*20)/100;
            recyclerViewImageHeight = recyclerViewImageWidth;
            recyclerViewImageParams = (ViewGroup.LayoutParams) recyclerViewImage.getLayoutParams();
            recyclerViewImageParams.width = recyclerViewImageWidth;
            recyclerViewImageParams.height = recyclerViewImageHeight;

            recyclerViewImage.setOnClickListener(this);
        }

        public ImageView getRecyclerViewImage()
        {
            return recyclerViewImage;
        }

        @Override
        public void onClick(View v)
        {
            GridViewAdapter gridViewAdapter = new GridViewAdapter(v.getContext(),screenWidth,screenHeight,mediaImageDetailsList,getAdapterPosition());
            MainActivity.gridView.setAdapter(gridViewAdapter);
        }
    }

    private Bitmap optimizedBitmap( File imageFile, int requiredWidth, int requiredHeight )
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
    }

    private int calculateInSampleSize( BitmapFactory.Options options, int requiredWidth, int requiredHeight )
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requiredHeight || width > requiredWidth)
        {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > requiredHeight && (halfWidth / inSampleSize) > requiredWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
