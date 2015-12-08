package com.ebay.kshantaraman.instagramviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kshantaraman on 12/1/15.
 */
public class ImageArrayAdapter extends ArrayAdapter<ImageDisplay> {


    Context ctxt;
    public ImageArrayAdapter(Context context, ArrayList<ImageDisplay> images) {
        super(context, 0, images);
        ctxt=context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        final ImageDisplay currentImage=getItem(position);

        if (view == null) {
            LayoutInflater inflater=LayoutInflater.from(ctxt);
            view = inflater.inflate(R.layout.activity_image_display, null, true);
        }

        final ImageView imgView= (ImageView) view.findViewById(R.id.imageView);
        TextView captionTextView = (TextView) view.findViewById(R.id.captionTextView);
        TextView userNameTextView = (TextView) view.findViewById(R.id.userNameTextView);

        captionTextView.setText(currentImage.getCaption());
        userNameTextView.setText(currentImage.getUsername());
        String imageUri = "https://i.imgur.com/tGbaZCY.jpg";
        imgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ImageView imgView = (ImageView) v.findViewById(R.id.imageView);
                Uri bmpUri = getLocalBitmapUri(imgView);
                if (bmpUri != null) {
                    // Construct a ShareIntent with link to image
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");
                    // Launch sharing dialog for image
                    v.getContext().startActivity(Intent.createChooser(shareIntent,"Share Via"));
                    return true;
                } else {
                    return false;
                    // ...sharing failed, handle error
                }
            }
        });
        Picasso.with(ctxt).load(currentImage.getImageUrl()).placeholder(R.drawable.loading).into(imgView);
        //Picasso.with(context).load(imageUri).into(ivBasicImage);
        //imgView.setImageResource(currentImage.getResourceID());
        return view;

    }




    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

}
