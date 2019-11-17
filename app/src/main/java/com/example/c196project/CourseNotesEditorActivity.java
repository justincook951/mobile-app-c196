package com.example.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.c196project.viewmodel.coursenote.CourseNoteEditorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.COURSENOTE_ID;
import static com.example.c196project.utilities.Const.COURSE_ID;
import static com.example.c196project.utilities.Const.KEY_EDIT;
import static com.example.c196project.utilities.Const.SAVE_EDITING;

public class CourseNotesEditorActivity extends AppCompatActivity
{

    @BindView(R.id.course_note_text_edit)
    TextView courseNoteTextField;
    
    private boolean isInEdit;
    private int courseId;
    private CourseNoteEditorViewModel courseNoteEditorViewModel;
    private FloatingActionButton sendEmailBttn;

    @Override
    protected void onCreate(Bundle prevState)
    {
        super.onCreate(prevState);
        setContentView(R.layout.activity_course_notes_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendEmailBttn = findViewById(R.id.sendEmailButton);

        sendEmailBttn.setOnClickListener(v -> {
            String to = "";
            String subject = "Course notes!";
            String message = courseNoteTextField.getText().toString();
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ to });
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);

            //need this to prompts email client only
            emailIntent.setType("message/rfc822");

            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        });

        if (prevState != null) {
            isInEdit = prevState.getBoolean(SAVE_EDITING);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putBoolean(SAVE_EDITING, true);
        super.onSaveInstanceState(outState);
    }
}
