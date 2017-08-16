package com.examples.photogallery;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.support.v7.widget.LinearLayoutManager.*;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private Context context;
    private int screenWidth,screenHeight;

    private RecyclerView recyclerView;
    private int recyclerWidth,recyclerHeight;
    private ViewGroup.LayoutParams recyclerViewParams;

    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ContentResolver contentResolver;
    private Uri uri;
    private Cursor cursorHoldsMediaImagesFolderName;
    List<MediaImageDetails> mediaImageDetailsList;

    public static GridView gridView;
    private int gridViewWidth,gridViewHeight;
    private ViewGroup.LayoutParams gridViewParams;

    private GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerWidth = screenWidth;
        recyclerHeight = ((screenHeight*20)/100);
        recyclerViewParams = (ViewGroup.LayoutParams) recyclerView.getLayoutParams();
        recyclerViewParams.width = recyclerWidth;
        recyclerViewParams.height = recyclerHeight;

        gridView = (GridView) findViewById(R.id.gridViewID);
        gridViewWidth = screenWidth;
        gridViewHeight = ((screenHeight*80)/100);
        gridViewParams = (ViewGroup.LayoutParams) gridView.getLayoutParams();
        gridViewParams.width = gridViewWidth;
        gridViewParams.height = gridViewHeight;


//        Debug.startMethodTracing("MyTestCase");

        mediaImageDetailsList = new ArrayList<>();

        contentResolver = getContentResolver();
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] columns = {MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        cursorHoldsMediaImagesFolderName = contentResolver.query(uri, columns, null, null, null);
        readAllImageFolderName(cursorHoldsMediaImagesFolderName);
        cursorHoldsMediaImagesFolderName.close();

//        Debug.stopMethodTracing();

        recyclerViewAdapter = new RecyclerViewAdapter(context,screenWidth,screenHeight,mediaImageDetailsList);
        linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        gridViewAdapter = new GridViewAdapter(context,screenWidth,screenHeight,mediaImageDetailsList,0);
        gridView.setAdapter(gridViewAdapter);
    }

    private void readAllImageFolderName(Cursor cursor)
    {
        Set<String> mediadImagesFolderNameSet = new HashSet<>();

        if( cursor != null && cursor.moveToFirst() )
        {
            do
            {
                String mediaImageFolderName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                mediadImagesFolderNameSet.add(mediaImageFolderName);

            }while(cursor.moveToNext());
        }


        Iterator<String> iterator = mediadImagesFolderNameSet.iterator();
        while (iterator.hasNext())
        {
            String mediaImageFolderName = iterator.next();
            String[]    columns = {MediaStore.Images.Media.DATA,MediaStore.Images.Media.TITLE};
            String      whereClause = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =? ";
            String[]    whereArgs = {mediaImageFolderName};
            String      sortingOrder = MediaStore.Images.Media.DATE_ADDED +" DESC";
            Cursor      cursorHoldsMediaImagesOfSpecifiedFolder = contentResolver.query(uri, columns, whereClause, whereArgs, sortingOrder);
            readAllMediaImages(cursorHoldsMediaImagesOfSpecifiedFolder,whereArgs[0]);
            cursorHoldsMediaImagesOfSpecifiedFolder.close();
        }
    }

    private void readAllMediaImages(Cursor cursor,String folderName)
    {
        List<String> mediaImagesPathList = new ArrayList<>();
        if( cursor != null && cursor.moveToFirst() )
        {
            do
            {
                String mediaImagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                mediaImagesPathList.add(mediaImagePath);

                String mediaImageName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));


            }while(cursor.moveToNext());
        }

        MediaImageDetails mediaImageDetails = new MediaImageDetails();
        mediaImageDetails.mediaImageFolderName                      = folderName;
        mediaImageDetails.mediaImagePathsWithRespectToFolderName    = mediaImagesPathList;
        mediaImageDetails.mediaImageCountWithRespectToFolderName    = mediaImagesPathList.size();
        mediaImageDetails.mediaThumbnailPathWithRespectToFolderName = mediaImagesPathList.get(0);

        mediaImageDetailsList.add(mediaImageDetails);
    }
}
