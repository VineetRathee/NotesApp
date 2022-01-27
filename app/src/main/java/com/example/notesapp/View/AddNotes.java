package com.example.notesapp.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.notesapp.Adapter.NotesAdapter;
import com.example.notesapp.Model.Notes;
import com.example.notesapp.R;
import com.example.notesapp.ViewModel.NotesViewModel;
import com.example.notesapp.databinding.ActivityAddNotesBinding;
import com.example.notesapp.utils.AdapterUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * @author Vineet
 * Date: 1st November, 2021
 * AddNotes Create notes which will be stored in database
 */
public class AddNotes extends AppCompatActivity {
    ActivityAddNotesBinding binding;
    String title, note , stringUri,imageUri;
    NotesViewModel notesViewModel;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int  MY_VIDEO_CAMERA_PERMISSION_CODE = 200;
    public static final int GET_FROM_GALLERY = 3;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int GET_VIDEO_FROM_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        binding.scroll.smoothScrollTo(0,0);
       /* on click listener to click image from camera */
        binding.cameraConstraintLayout.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
        /* on click listener to choose image from gallery*/
        binding.galleryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GET_FROM_GALLERY);
                }
                else
                {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

                }
            }
        });
        /* on click listener to shoot video from camera*/
        binding.videoConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_VIDEO_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                    }
                }
            }
        });
        /* on click listener to choose video from gallery*/
        binding.videoGalleryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GET_VIDEO_FROM_GALLERY);
                }
                else
                {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI), GET_VIDEO_FROM_GALLERY);

                }
            }
        });

        /* on click listener to add new notes*/
        binding.addNotes.setOnClickListener(v -> {
           title= binding.title.getText().toString();
           note = binding.writeNotes.getText().toString();
           if(title.equalsIgnoreCase("")&& note.equalsIgnoreCase("")){
               Toast.makeText(this, "Please Enter Title And Notes", Toast.LENGTH_SHORT).show();
           }
           else if(note.equalsIgnoreCase("")){
               Toast.makeText(this, "Please Enter Notes", Toast.LENGTH_SHORT).show();
           }
           else if(title.equalsIgnoreCase("")){
               Toast.makeText(this, "Please Enter Title", Toast.LENGTH_SHORT).show();
           }
           else
           CreateNotes(title,note,imageUri,stringUri);
        });
    }

    private void CreateNotes(String title, String notes,String imageUri,String stringUri) {
        Notes newNote = new Notes();
        newNote.notesTitle=title;
        newNote.notes=notes;
        newNote.image=imageUri;
        newNote.video= stringUri;
        notesViewModel.insertNote(newNote);
        finish();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == GET_FROM_GALLERY){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "storage permission granted", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
            else
            {
                Toast.makeText(this, "storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode== MY_VIDEO_CAMERA_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
        else if(requestCode == GET_VIDEO_FROM_GALLERY){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "storage permission granted", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI), GET_VIDEO_FROM_GALLERY);
            }
            else
            {
                Toast.makeText(this, "storage permission denied", Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageUri= AdapterUtils.getImageUri(this,photo).toString();
            Picasso.get().load(AdapterUtils.getImageUri(this,photo)).error(R.drawable.placeholder).centerCrop().fit().into(binding.uploadedImageView);
           // binding.uploadedImageView.setImageBitmap(photo);
        }
        else if(requestCode==GET_FROM_GALLERY&& resultCode == Activity.RESULT_OK){
            Uri selectedImage = data.getData();
            try {
                imageUri=selectedImage.toString();
                Picasso.get().load(selectedImage).error(R.drawable.placeholder).centerCrop().fit().into(binding.uploadedImageView);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            try {
                Uri videoUri = data.getData();
                stringUri=videoUri.toString();
                binding.uploadedVideoView.setVideoURI(videoUri);
                binding.uploadedVideoView.setMediaController(new MediaController(this));
                binding.uploadedVideoView.setZOrderOnTop(true);
                binding.uploadedVideoView.start();
           }
            catch (Exception e){

            }

        }
        else if(requestCode==GET_VIDEO_FROM_GALLERY&&resultCode == RESULT_OK){
            try {
                Uri videoUri = data.getData();
                stringUri=videoUri.toString();
                binding.uploadedVideoView.setVideoURI(videoUri);
                binding.uploadedVideoView.setMediaController(new MediaController(this));
                binding.uploadedVideoView.setZOrderOnTop(true);
                binding.uploadedVideoView.start();
            }
            catch (Exception e){

            }
        }

    }
}

