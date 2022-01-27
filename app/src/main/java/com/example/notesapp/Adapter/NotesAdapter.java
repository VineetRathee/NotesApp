package com.example.notesapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.View.MainActivity;
import com.example.notesapp.Model.Notes;
import com.example.notesapp.R;
import com.example.notesapp.utils.AdapterUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Vineet
 * Date: 27th January, 2022
 * An Adapter object acts as a bridge between an AdapterView and the underlying data for that view. The Adapter provides access to the data items. The Adapter is also responsible for making a View for each item in the data set.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.notesViewHolder> {

    MainActivity mainActivity;
    List<Notes> notes;
    List<Notes> notesList;

    public NotesAdapter(MainActivity mainActivity, List<Notes> notes) {
        this.mainActivity = mainActivity;
        this.notes = notes;
        this.notesList= new ArrayList<>(notes);
    }
    /**
     * @param searchedNotes
     * It notify that data set is changed
     */
    public void searchNotes(List<Notes> searchedNotes){
        this.notes = searchedNotes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public notesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new notesViewHolder(LayoutInflater.from(mainActivity).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull notesViewHolder holder, int position) {
        Notes note = notes.get(position);
        holder.title.setText(note.notesTitle);
        holder.notes.setText(note.notes);


        if(note.video!=null) {
            holder.videoView.setVisibility(View.VISIBLE);
            Uri mUri = Uri.parse(note.video);
            holder.videoView.setVideoURI(mUri);
            holder.videoView.setMediaController(new MediaController(mainActivity));
            holder.videoView.setZOrderOnTop(true);
            holder.videoView.start();
        }
        if (note.image != null)
            Picasso.get().load(Uri.parse(note.image)).error(R.drawable.placeholder).centerCrop().fit().into(holder.image);
        else
            Picasso.get().load(R.drawable.placeholder).into(holder.image);

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (note.image != null)
                    AdapterUtils.onShareItem(note.notesTitle, holder.image,mainActivity);
                else {
                    AdapterUtils.onShareItem(note.notesTitle, null,mainActivity);
                }
            }

        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note.image != null) {
                    if (Uri.parse(note.image) != null)
                        showImage(Uri.parse(note.image));
                }
            }
        });


    }



    @Override
    public int getItemCount() {
        return notes.size();
    }


    static class notesViewHolder extends RecyclerView.ViewHolder {
        TextView title, notes;
        ImageView image, shareButton;
        VideoView videoView;

        public notesViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            notes = view.findViewById(R.id.notes);
            image = view.findViewById(R.id.image);
            shareButton = view.findViewById(R.id.share_notes);
            videoView = view.findViewById(R.id.video);
        }
    }


    /**
     * @param uri
     * This method is used to show the dialogue box to show image
     */
    private void showImage(Uri uri) {
        final View imageDialogView = LayoutInflater.from(mainActivity).inflate(R.layout.image_dialog, null);
        final AlertDialog imageDialog = new AlertDialog.Builder(mainActivity).create();
        ImageView preview_image = imageDialogView.findViewById(R.id.iv_preview);
        preview_image.setImageURI(uri);
        Button close_button = imageDialogView.findViewById(R.id.btnClose);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDialog.dismiss();
            }

        });
        imageDialog.setView(imageDialogView);
        imageDialog.show();
    }
}
