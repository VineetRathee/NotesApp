package com.example.notesapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class AdapterUtils {
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title"+"-"+System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    /**
     * @param title,ivImage
     * This method is used to share the image and the title to any other app
     */
    public static void onShareItem(String title, ImageView ivImage,Context mainActivity) {
        // Get access to bitmap image from view
        // Get access to the URI for the bitmap
        if (ivImage != null) {
            BitmapDrawable drawable = (BitmapDrawable) ivImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            Uri bmpUri = getImageUri(mainActivity,bitmap);
            if (bmpUri != null) {
                // Construct a ShareIntent with link to image
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.putExtra(Intent.EXTRA_TEXT, title);
                shareIntent.setType("image/*");
                // Launch sharing dialog for image
                mainActivity.startActivity(Intent.createChooser(shareIntent, "Share Notes"));

            } else {

            }
        } else {
            //in case if image is not available
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_TEXT, title);
            shareIntent.setType("text/plain");
            // Launch sharing dialog for image
            mainActivity.startActivity(Intent.createChooser(shareIntent, "Share Notes"));

        }

    }
}
