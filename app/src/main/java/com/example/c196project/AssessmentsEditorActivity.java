package com.example.c196project;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.utilities.Standardizer;
import com.example.c196project.viewmodel.assessment.AssessmentEditorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.ASSESSMENT_ID;
import static com.example.c196project.utilities.Const.KEY_EDIT;
import static com.example.c196project.utilities.Const.SAVE_EDITING;

public class AssessmentsEditorActivity extends AppCompatActivity
{

    @BindView(R.id.assessment_name)
    TextView assessmentNameField;
    @BindView(R.id.end_date_replaceable)
    TextView endDateText;
    @BindView(R.id.related_course_dropdown)
    Spinner relatedCourseDropdown;
    @BindView(R.id.assessment_type_dropdown)
    Spinner assessmentTypeSpinner;

    Button startBtn, endBtn;
    DatePickerDialog datePicker;
    Date endDate;
    Calendar cal;
    CheckBox setAlarm;
    private boolean isInEdit;
    private AssessmentEditorViewModel assessmentEditorViewModel;
    private static int existingCourseId;
    private String assessmentType;

    @Override
    protected void onCreate(Bundle prevState)
    {
        super.onCreate(prevState);
        setContentView(R.layout.activity_assessments_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startBtn = findViewById(R.id.start_date_button);
        endBtn = findViewById(R.id.end_date_button);
        setAlarm = findViewById(R.id.set_alarm);

        endBtn.setOnClickListener(v -> showCalendar(endDateText, "end"));

        Spinner assessmentTypeSpinner = findViewById(R.id.assessment_type_dropdown);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterAssessmentType = ArrayAdapter.createFromResource(this,
                R.array.assessment_type_dropdown_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterAssessmentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        assessmentTypeSpinner.setAdapter(adapterAssessmentType);

        Spinner relatedCourseDropdown = findViewById(R.id.related_course_dropdown);
        ArrayAdapter<CharSequence> adapterCourses = ArrayAdapter.createFromResource(this,
                R.array.courses_dropdown_options, android.R.layout.simple_spinner_item);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relatedCourseDropdown.setAdapter(adapterCourses);

        if (prevState != null) {
            isInEdit = prevState.getBoolean(SAVE_EDITING);
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

        datePicker = new DatePickerDialog(AssessmentsEditorActivity.this,
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
        assessmentEditorViewModel = ViewModelProviders.of(this).get(AssessmentEditorViewModel.class);
        assessmentEditorViewModel.mutableAssessment.observe(this, assessmentEntity -> {
            if (assessmentEntity != null && !isInEdit) {
                // Do not overwrite any existing changes
                assessmentNameField.setText(assessmentEntity.getTitle());
                endDate = assessmentEntity.getEndDate();
                endDateText.setText(Standardizer.standardizeSingleDateString(endDate));
                existingCourseId = assessmentEntity.getCourseId();
                assessmentType = assessmentEntity.getAssessmentType();
            }
        });

        assessmentEditorViewModel.liveDataCourses.observe(this, courseData -> {
            if (courseData != null && !courseData.isEmpty()) {
                ArrayAdapter coursesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courseData);
                relatedCourseDropdown.setAdapter(coursesAdapter);
                existingCourseId = assessmentEditorViewModel.courseId;
                if (existingCourseId != 0) {
                    for (int dropdownIndex = 0; dropdownIndex < courseData.size(); dropdownIndex++) {
                        if (courseData.get(dropdownIndex).getId() == existingCourseId) {
                            relatedCourseDropdown.setSelection(dropdownIndex);
                            break;
                        }
                    }
                }
                if (assessmentType != null) {
                    ArrayAdapter<CharSequence> adapterAssessmentType = ArrayAdapter.createFromResource(this,
                            R.array.assessment_type_dropdown_options, android.R.layout.simple_spinner_item);
                    int typePosition = adapterAssessmentType.getPosition(assessmentType);
                    assessmentTypeSpinner.setSelection(typePosition);
                }
            }
            else {
                List<String> blankList = new ArrayList<>();
                blankList.add("None");
                ArrayAdapter coursesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, blankList);
                relatedCourseDropdown.setAdapter(coursesAdapter);
            }
        });

        Bundle kvExtras = getIntent().getExtras();
        if (kvExtras == null) {
            // This is a brand new assessment
        }
        else {
            // This is a assessment that's being edited
            int assessmentId = kvExtras.getInt(ASSESSMENT_ID);
            assessmentEditorViewModel.loadById(assessmentId);
            existingCourseId = assessmentEditorViewModel.courseId;
            final FloatingActionButton delButton = findViewById(R.id.delete_assessment);
            delButton.setOnClickListener(v -> deleteAssessment());
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
        String assessmentName = assessmentNameField.getText().toString();
        if (endDate == null) {
            // Do not even try to reference saveAssessment
            finish();
        }
        int courseId = 0;
        if (relatedCourseDropdown.getSelectedItem() instanceof CourseEntity) {
            courseId = ((CourseEntity)relatedCourseDropdown.getSelectedItem()).getId();
        }
        else {
            courseId = -1;
        }
        String assessmentType = assessmentTypeSpinner.getSelectedItem().toString();
        assessmentEditorViewModel.saveAssessment(assessmentName, endDate, courseId, assessmentType, setAlarm.isChecked());
        finish();
    }

    public void deleteAssessment() {
        assessmentEditorViewModel.deleteAssessment();
        finish();
    }

    private void setDate(Date date, String dateLocation)
    {
        if (dateLocation == "end"){
            this.endDate = date;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putBoolean(SAVE_EDITING, true);
        super.onSaveInstanceState(outState);
    }
}
