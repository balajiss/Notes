package com.balaji.notehomelane.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balaji.notehomelane.Constants;
import com.balaji.notehomelane.NoteApplication;
import com.balaji.notehomelane.Pojo.Note;
import com.balaji.notehomelane.R;
import com.balaji.notehomelane.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ListNote extends NoteAbstractActivity {

    private RecyclerView notesList;
    private NotesListAdapter notesListAdapter;
    private Context mContext;
    private List<Note> noteArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);

        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CreateNote.class);
                startActivity(intent);
            }
        });

        initListView();
    }

    private void initListView() {
        notesList = (RecyclerView) findViewById(R.id.notes_list);
        notesList.setLayoutManager(new LinearLayoutManager(mContext));
        notesListAdapter = new NotesListAdapter();
        notesList.setAdapter(notesListAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration
                .VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.divider));
        notesList.addItemDecoration(divider);
    }

    private void getAllNotes() {
        noteArrayList = NoteApplication.getDatabaseHandler().getAllNotes();
        notesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllNotes();
    }

    private class NotesListAdapter extends RecyclerView.Adapter<NotesListViewHolder> {

        @Override
        public NotesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_note, parent,
                    false);
            return new NotesListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NotesListViewHolder holder, final int position) {
            holder.noteTitle.setText(noteArrayList.get(position).getNoteTitle());
            holder.noteText.setText(noteArrayList.get(position).getNoteText());
            holder.createdDate.setText(Utils.getReadableDateTime(noteArrayList.get(position)
                    .getDateCreated()));

            holder.itemNoteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ViewNote.class);
                    intent.putExtra(Constants.NOTE, noteArrayList.get(position));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return noteArrayList.size();
        }
    }

    private class NotesListViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout itemNoteLayout;
        TextView noteTitle;
        TextView noteText;
        TextView createdDate;

        public NotesListViewHolder(View itemView) {
            super(itemView);

            itemNoteLayout = itemView.findViewById(R.id.item_note_layout);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteText = itemView.findViewById(R.id.note_text_truncated);
            createdDate = itemView.findViewById(R.id.note_created_on);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_list_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
