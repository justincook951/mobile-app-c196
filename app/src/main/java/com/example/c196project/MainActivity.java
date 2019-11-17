package com.example.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.c196project.utilities.WGUNotificationMgr;
import com.example.c196project.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{

    private MainViewModel mainViewModel;
    private WGUNotificationMgr notifymgr;
    // Assessment nav is re-used elsewhere; manually add the onClick event
    private FloatingActionButton assessmentNav;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assessmentNav = findViewById(R.id.nav_to_assessment);
        assessmentNav.setOnClickListener(v -> send_nav_request(findViewById(R.id.nav_to_assessment)));

        ButterKnife.bind(this);
        initViewModel();
        initNotifications();
    }

    private void initNotifications()
    {
        notifymgr = new WGUNotificationMgr();
        notifymgr.initNotifications(this.getApplicationContext());
    }

    private void initViewModel()
    {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_add_all:
                addAllSampleData();
                break;
            case R.id.action_add_sample_terms:
                addSampleTerms();
                break;
            case R.id.action_delete_sample_terms:
                deleteAllTerms();
                break;
            case R.id.action_add_sample_courses:
                addSampleCourses();
                break;
            case R.id.action_delete_sample_courses:
                deleteAllCourses();
                break;
            case R.id.action_add_sample_assessments:
                addSampleAssessments();
                break;
            case R.id.action_delete_sample_assessments:
                deleteAllAssessments();
                break;
            case R.id.action_add_sample_mentors:
                addSampleMentors();
                break;
            case R.id.action_delete_sample_mentors:
                deleteAllMentors();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addAllSampleData()
    {
        addSampleAssessments();
        addSampleCourses();
        addSampleTerms();
        addSampleMentors();
    }

    private void deleteAllAssessments()
    {
        mainViewModel.deleteAllAssessments();
    }

    private void addSampleAssessments()
    {
        mainViewModel.addSampleAssessments();
    }

    private void deleteAllCourses()
    {
        mainViewModel.deleteAllCourses();
    }

    private void addSampleCourses()
    {
        mainViewModel.addSampleCourses();
    }

    private void deleteAllTerms()
    {
        mainViewModel.deleteAllTerms();
    }

    private void addSampleTerms()
    {
        mainViewModel.addSampleTerms();
    }

    private void deleteAllMentors()
    {
        mainViewModel.deleteAllMentors();
    }

    private void addSampleMentors()
    {
        mainViewModel.addSampleMentors();
    }

    public void send_nav_request(View view)
    {
        // Used to determine where to navigate
        int selectedId = view.getId();
        switch (selectedId) {
            case (R.id.nav_to_term):
                Intent termIntent = new Intent(this, TermsListActivity.class);
                startActivity(termIntent);
                break;
            case (R.id.nav_to_course):
                Intent courseIntent = new Intent(this, CourseListActivity.class);
                startActivity(courseIntent);
                break;
            case (R.id.nav_to_assessment):
                Intent assessmentIntent = new Intent(this, AssessmentsListActivity.class);
                startActivity(assessmentIntent);
                break;
            case (R.id.nav_to_mentor):
                Intent mentorIntent = new Intent(this, MentorsListActivity.class);
                startActivity(mentorIntent);
                break;
            case (R.id.send_test_notify):
                WGUNotificationMgr notifymgr = new WGUNotificationMgr();

            default:
                break;
        }
    }
}
