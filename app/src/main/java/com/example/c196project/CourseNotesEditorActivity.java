package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.c196project.utilities.Standardizer;
import com.example.c196project.viewmodel.coursenote.CourseNoteEditorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.COURSENOTE_ID;
import static com.example.c196project.utilities.Const.COURSE_ID;
import static com.example.c196project.utilities.Const.KEY_EDIT;

public class CourseNotesEditorActivity extends AppCompatActivity
{

    @BindView(R.id.course_note_text_edit)
    TextView courseNoteTextField;
    
    private boolean isInEdit;
    private int courseId;
    private CourseNoteEditorViewModel courseNoteEditorViewModel;

    @Override
    protected void onCreate(Bundle prevState)
    {
        super.onCreate(prevState);
        setContentView(R.layout.activity_course_notes_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (prevState != null) {
            isInEdit = prevState.getBoolean(KEY_EDIT);
        }
        ButterKnife.bind(this);
        initViewModel();
    }


    private void initViewModel()
    {
        courseNoteEditorViewModel = ViewModelProviders.of(this).get(CourseNoteEditorViewModel.class);
        courseNoteEditorViewModel.mutableCourseNote.observe(this, courseNoteEntity -> {
            if (courseNoteEntity != null && !isInEdit) {
                // Do not overwrite any existing changes
                courseNoteTextField.setText(courseNoteEntity.getCourseNoteText());
            }
        });

        Bundle kvExtras = getIntent().getExtras();
        int courseNoteId = kvExtras.getInt(COURSENOTE_ID);
        courseId = kvExtras.getInt(COURSE_ID);
        if (courseNoteId == 0) {
            // This is a brand new courseNote
        }
        else {
            // This is a courseNote that's being edited
            ;
            courseNoteEditorViewModel.loadById(courseNoteId);
            final FloatingActionButton delButton = findViewById(R.id.delete_course_note);
            delButton.setOnClickListener(v -> deleteCourseNote());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            saveAndExit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveAndExit();
    }

    private void saveAndExit() {
        String courseNoteText = courseNoteTextField.getText().toString();
        courseNoteEditorViewModel.saveCourseNote(courseNoteText, courseId);
        finish();
    }

    public void deleteCourseNote() {
        courseNoteEditorViewModel.deleteCourseNote();
        finish();
    }
}
