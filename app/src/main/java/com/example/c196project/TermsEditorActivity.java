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

import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.utilities.Standardizer;
import com.example.c196project.viewmodel.term.TermEditorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.COURSE_LIST_KEY;
import static com.example.c196project.utilities.Const.KEY_EDIT;
import static com.example.c196project.utilities.Const.TERM_ID;

public class TermsEditorActivity extends AppCompatActivity
{

    @BindView(R.id.term_name)
    TextView termNameField;
    @BindView(R.id.start_date_replaceable)
    TextView startDateText;
    @BindView(R.id.end_date_replaceable)
    TextView endDateText;

    Button startBtn, endBtn;
    DatePickerDialog datePicker;
    Date startDate, endDate;
    Calendar cal;
    FloatingActionButton navToCourse;
    private boolean isInEdit;
    private TermEditorViewModel termEditorViewModel;
    private TextView courseNavTextview;

    @Override
    protected void onCreate(Bundle prevState)
    {
        super.onCreate(prevState);
        setContentView(R.layout.activity_terms_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startBtn = findViewById(R.id.start_date_button);
        endBtn = findViewById(R.id.end_date_button);

        startBtn.setOnClickListener(v -> showCalendar(startDateText, "start"));
        endBtn.setOnClickListener(v -> showCalendar(endDateText, "end"));

        courseNavTextview = findViewById(R.id.course_nav_textview);
        courseNavTextview.setText(R.string.get_term_courses_tv);

        if (prevState != null) {
            isInEdit = prevState.getBoolean(KEY_EDIT);
        }
        ButterKnife.bind(this);
        initViewModel();
    }

    private void showCalendar(TextView displayLocation, String dateToSet)
    {
        cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(TermsEditorActivity.this,
                    (view, selYear, selMonth, selDay) -> {
                        displayLocation.setText(Standardizer.dateStringFromComponents(selYear, selMonth, selDay));
                        setDate(Standardizer.dateFromComponents(selYear, selMonth, selDay), dateToSet);
                    },
                    year,
                    month,
                    day);
        datePicker.show();
    }

    private void initViewModel()
    {
        termEditorViewModel = ViewModelProviders.of(this).get(TermEditorViewModel.class);
        termEditorViewModel.mutableTerm.observe(this, termEntity -> {
            if (termEntity != null && !isInEdit) {
                // Do not overwrite any existing changes
                termNameField.setText(termEntity.getTitle());
                startDate = termEntity.getStartDate();
                endDate = termEntity.getEndDate();
                startDateText.setText(Standardizer.standardizeSingleDateString(startDate));
                endDateText.setText(Standardizer.standardizeSingleDateString(endDate));
            }
        });

        Bundle kvExtras = getIntent().getExtras();
        if (kvExtras == null) {
            // This is a brand new term

        }
        else {
            // This is a term that's being edited
            int termId = kvExtras.getInt(TERM_ID);
            termEditorViewModel.loadById(termId);
            final FloatingActionButton delButton = findViewById(R.id.delete_term);
            delButton.setOnClickListener(v -> deleteTerm());
        }

        navToCourse = findViewById(R.id.nav_to_course);
        if (kvExtras != null) {
            navToCourse.setOnClickListener(v -> {
                Intent courseListIntent = new Intent(this, CourseListActivity.class);
                List<CourseEntity> relatedCourses = termEditorViewModel.getRelatedCourses();
                ArrayList<String> courseIds = new ArrayList<>();
                for (CourseEntity course : relatedCourses) {
                    courseIds.add(Integer.toString(course.getId()));
                }
                courseListIntent.putStringArrayListExtra(COURSE_LIST_KEY, courseIds);
                courseListIntent.putExtra(TERM_ID, kvExtras.getInt(TERM_ID));
                startActivity(courseListIntent);
            });
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
        String termName = termNameField.getText().toString();
        if (startDate == null || endDate == null) {
            // Do not even try to reference them in saveTerm
            finish();
        }
        termEditorViewModel.saveTerm(termName, startDate, endDate);
        finish();
    }

    public void deleteTerm() {
        termEditorViewModel.deleteTerm();
        finish();
    }

    private void setDate(Date date, String dateLocation)
    {
        if (dateLocation.equals("start")) {
            Log.i("DateSet", "Ran for dateLocation start");
            this.startDate = date;
        }
        else if (dateLocation.equals("end")) {
            this.endDate = date;
        }
    }
}
