package com.example.c196project;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.c196project.utilities.Standardizer;
import com.example.c196project.viewmodel.mentor.MentorEditorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.MENTOR_ID;
import static com.example.c196project.utilities.Const.KEY_EDIT;

public class MentorsEditorActivity extends AppCompatActivity
{

    @BindView(R.id.mentor_name)
    TextView mentorNameField;
    @BindView(R.id.mentor_email)
    TextView mentorEmailField;
    @BindView(R.id.mentor_phone)
    TextView mentorPhoneField;

    private boolean isInEdit;
    private MentorEditorViewModel mentorEditorViewModel;

    @Override
    protected void onCreate(Bundle prevState)
    {
        super.onCreate(prevState);
        setContentView(R.layout.activity_mentors_editor);
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
        mentorEditorViewModel = ViewModelProviders.of(this).get(MentorEditorViewModel.class);
        mentorEditorViewModel.mutableMentor.observe(this, mentorEntity -> {
            if (mentorEntity != null && !isInEdit) {
                // Do not overwrite any existing changes
                mentorNameField.setText(mentorEntity.getName());
                mentorPhoneField.setText(mentorEntity.getPhone());
                mentorEmailField.setText(mentorEntity.getEmail());
            }
        });

        Bundle kvExtras = getIntent().getExtras();
        if (kvExtras == null) {
            // This is a brand new mentor
        }
        else {
            // This is a mentor that's being edited
            int mentorId = kvExtras.getInt(MENTOR_ID);
            mentorEditorViewModel.loadById(mentorId);
            final FloatingActionButton delButton = findViewById(R.id.delete_mentor);
            delButton.setOnClickListener(v -> deleteMentor());
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
        String mentorName = mentorNameField.getText().toString();
        String mentorPhone = mentorPhoneField.getText().toString();
        String mentorEmail = mentorEmailField.getText().toString();

        mentorEditorViewModel.saveMentor(mentorName, mentorPhone, mentorEmail);
        finish();
    }

    public void deleteMentor() {
        mentorEditorViewModel.deleteMentor();
        finish();
    }
}
