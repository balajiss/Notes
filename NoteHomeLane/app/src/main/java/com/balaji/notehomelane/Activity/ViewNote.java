package com.balaji.notehomelane.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.balaji.notehomelane.Constants;
import com.balaji.notehomelane.NoteApplication;
import com.balaji.notehomelane.Pojo.Note;
import com.balaji.notehomelane.R;

/**
 * Created by balaji on 16/01/18.
 */

public class ViewNote extends NoteAbstractActivity implements NoteAbstractActivity.AlertDialogResponse {

    private Context mContext;
    private Note note;
    private TextView noteTitle;
    private TextView noteText;
    private ImageView image;
    private ImageView deleteNote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_note);
        mContext = this;
        if (!getIntent().hasExtra(Constants.NOTE)) {
            startActivity(new Intent(this, CreateNote.class));
        }
        note = (Note) getIntent().getExtras().getSerializable(Constants.NOTE);

        /*ImageView parent = new ImageView(mContext){
            @Override
            public boolean dispatchTouchEvent(MotionEvent ev) {
                int x = Math.round(ev.getX());
                int y = Math.round(ev.getY());
                if(x > image.getLeft() && x < image.getRight() && y > image.getTop() && y < image
                        .getBottom()){
                    changeToGrayScale(image);
                    if(ev.getAction() == MotionEvent.ACTION_UP){
                        changeToColor(image);
                    }
                }
                return true;
            }
        }*/

        noteTitle = findViewById(R.id.note_title);
        noteText = findViewById(R.id.note_text);
        image = findViewById(R.id.image);
        deleteNote = findViewById(R.id.delete_note);
        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(getString(R.string.discard), getString(R.string.no), getString(R.string
                        .sure_to_delete), ViewNote.this);
            }
        });
        image.setOnTouchListener(new View.OnTouchListener() {

            private long startClickTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() ==
                        MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent
                        .ACTION_HOVER_MOVE || event.getAction() == MotionEvent.ACTION_SCROLL) {
                    changeToGrayScale(image);
                }else {
                    changeToColor(image);
                }
                return true;
            }
        });
        initViewNote();
    }

    public void changeToGrayScale(ImageView image) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        image.setColorFilter(filter);
    }

    public void changeToColor(ImageView image) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(1);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        image.setColorFilter(filter);
    }

    public void initViewNote() {
        noteTitle.setText(note.getNoteTitle());
        noteText.setText(note.getNoteText());
        if (note.getImageUri() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(note.getImageUri(), options);
            image.setImageBitmap(bitmap);
            image.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPositive(DialogInterface dialog) {
        NoteApplication.getDatabaseHandler().deleteNote(note);
        dialog.dismiss();
        finish();
    }

    @Override
    public void onNegative(DialogInterface dialog) {
        dialog.dismiss();
    }
}
