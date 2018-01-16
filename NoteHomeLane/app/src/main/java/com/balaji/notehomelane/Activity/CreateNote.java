package com.balaji.notehomelane.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.balaji.notehomelane.Constants;
import com.balaji.notehomelane.NoteApplication;
import com.balaji.notehomelane.Pojo.Note;
import com.balaji.notehomelane.R;
import com.balaji.notehomelane.Utils.Utils;
import com.balaji.notehomelane.Utils.Validator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by balaji on 16/01/18.
 */

public class CreateNote extends NoteAbstractActivity implements NoteAbstractActivity.AlertDialogResponse {

    private Context mContext;
    private ImageView addImage;
    private ImageView image;
    private ImageView save;
    private RelativeLayout createNoteLayout;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_create_note);
        enableBackButton();
        file = null;
        createNoteLayout = (RelativeLayout) findViewById(R.id.create_note_layout);
        image = (ImageView) findViewById(R.id.image);
        addImage = (ImageView) findViewById(R.id.add_image);
        save = (ImageView) findViewById(R.id.save);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest
                        .permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(mContext, Manifest
                        .permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startGalleryIntent();
                } else {
                    ActivityCompat.requestPermissions(CreateNote.this, new
                                    String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Constants.STORAGE_PERMISSION);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteTitle = ((EditText) findViewById(R.id.note_title)).getText().toString();
                String noteText = ((EditText) findViewById(R.id.note_text)).getText().toString();
                if (!Validator.validateNoteTitle(noteTitle)) {
                    Snackbar.make(createNoteLayout, getString(R.string.err_title), Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                if (!Validator.validateNoteText(noteText)) {
                    Snackbar.make(createNoteLayout, getString(R.string.err_text), Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                showProgressDialog(getString(R.string.saving));
                String imageUri = null;
                if(file != null) {
                    imageUri = file.getPath();
                }
                Note note = new Note();
                note.setNoteTitle(noteTitle);
                note.setNoteText(noteText);
                note.setImageUri(imageUri);
                note.setActive(1);
                note.setDateCreated(Utils.getCurrentDateTime());
                note.setDateUpdated(Utils.getCurrentDateTime());
                NoteApplication.getDatabaseHandler().addNote(note);
                dismissProgressDialog();
                finish();
            }
        });
    }


    public String saveImage(Bitmap myBitmap) {
        image.setImageBitmap(null);
        image.setImageBitmap(myBitmap);
        image.setVisibility(View.VISIBLE);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File imageDirectory = new File(
                Environment.getExternalStorageDirectory() + Constants.PICTURE_STORAGE_URI);
        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs();
        }

        try {
            File f = new File(imageDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            file = f;
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GALLERY_IMAGE) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == Constants.STORAGE_PERMISSION) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest
                    .permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(mContext, Manifest
                    .permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                startGalleryIntent();
            }
        }
    }

    public void startGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, Constants.GALLERY_IMAGE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showAlert(getString(R.string.discard), getString(R.string.no), getString(R.string
                        .sure_to_discard), CreateNote.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPositive(DialogInterface dialog) {
        dialog.cancel();
        finish();
    }

    @Override
    public void onNegative(DialogInterface dialog) {
        dialog.cancel();
    }
}
