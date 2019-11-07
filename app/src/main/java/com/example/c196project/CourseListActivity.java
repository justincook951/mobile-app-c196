package com.example.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.ui.CoursesAdapter;
import com.example.c196project.viewmodel.course.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseListActivity extends AppCompatActivity
{

    @BindView(R.id.courses_recycler)
    RecyclerView CoursesRecyclerView;

    private List<CourseEntity> CourseList = new ArrayList<>();
    private CoursesAdapter CoursesAdapter;
    private CourseViewModel CourseViewModel;
    private FloatingActionButton addCourseFab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        addCourseFab = findViewById(R.id.add_course_fab);
        addCourseFab.setOnClickListener(view -> {
            Intent intent = new Intent(this.getApplicationContext(), CoursesEditorActivity.class);
            startActivity(intent);
        });
        initRecyclerView();
        initViewModel();
        initNotifications();
    }

    private void initNotifications()
    {
    }

    private void initViewModel()
    {
        final Observer<List<CourseEntity>> CourseObserver = new Observer<List<CourseEntity>>()
        {
            @Override
            public void onChanged(@Nullable List<CourseEntity> courseEntities)
            {
                CourseList.clear();
                CourseList.addAll(courseEntities);
                if (CoursesAdapter == null) {
                    CoursesAdapter = new CoursesAdapter(courseEntities, CourseListActivity.this);
                    CoursesRecyclerView.setAdapter(CoursesAdapter);
                }
                else {
                    CoursesAdapter.notifyDataSetChanged();
                }
            }
        };

        CourseViewModel = ViewModelProviders.of(this)
                .get(CourseViewModel.class);
        CourseViewModel.liveDataCourses.observe(this, CourseObserver);
    }

    private void initRecyclerView()
    {
        CoursesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        CoursesRecyclerView.setLayoutManager(layoutManager);

        CoursesAdapter = new CoursesAdapter(CourseList, this);
        CoursesRecyclerView.setAdapter(CoursesAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                CoursesRecyclerView.getContext(), layoutManager.getOrientation()
        );
        CoursesRecyclerView.addItemDecoration(divider);
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


        return super.onOptionsItemSelected(item);
    }
}
