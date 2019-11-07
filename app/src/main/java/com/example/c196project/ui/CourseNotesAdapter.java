package com.example.c196project.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.R;
import com.example.c196project.CourseNotesEditorActivity;
import com.example.c196project.database.coursenote.CourseNoteEntity;
import com.example.c196project.utilities.Standardizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.COURSENOTE_ID;

public class CourseNotesAdapter extends RecyclerView.Adapter<CourseNotesAdapter.ViewHolder>
{

    private final List<CourseNoteEntity> courseNotes;
    private final Context context;

    public CourseNotesAdapter(List<CourseNoteEntity> courseNotes, Context context)
    {
        this.courseNotes = courseNotes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_course_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final CourseNoteEntity courseNote = courseNotes.get(position);
        holder.courseNoteTextView.setText(courseNote.getCourseNoteText());
        holder.editCourseNote.setOnClickListener(view -> {
            Intent intent = new Intent(context, CourseNotesEditorActivity.class);
            // Pass extra value in the style of key => value pairing
            intent.putExtra(COURSENOTE_ID, courseNote.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courseNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.course_note_text)
        TextView courseNoteTextView;

        @BindView(R.id.edit_course_note)
        FloatingActionButton editCourseNote;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
