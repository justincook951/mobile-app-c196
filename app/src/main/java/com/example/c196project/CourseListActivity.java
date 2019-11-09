package com.example.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.TERM_ID;

public class CourseListActivity extends AppCompatActivity
{

    @BindView(R.id.courses_recycler)
    RecyclerView CoursesRecyclerView;

    private List<CourseEntity> CourseList = new ArrayList<>();
    private CoursesAdapter CoursesAdapter;
    private CourseViewModel CourseViewModel;
    private FloatingActionButton addCourseFab;
    private ArrayList<String> relatedCourseIds;
    private Bundle kvExtras;
    private Executor executor = Executors.newSingleThreadExecutor();
    private int termId;

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
        kvExtras = getIntent().getExtras();
        initRecyclerView();
        initViewModel();
        initNotifications();
    }

    private void initNotifications()
    {
    }

    private void initViewModel()
    {
        CourseViewModel = ViewModelProviders.of(this)
                .get(CourseViewModel.class);

        final Observer<List<CourseEntity>> CourseObserver = courseEntities -> {
            CourseList.clear();
            termId = 0;
            if (kvExtras != null) {
                termId = kvExtras.getInt(TERM_ID);
            }
            if (termId == 0) {
                CourseList.addAll(courseEntities);
            }
            else {
                // There's a term ID that we need to parse for related courses
                executor.execute(() -> CourseList.addAll(CourseViewModel.getCoursesByTerm(termId)));

            }
            if (CoursesAdapter == null) {
                CoursesAdapter = new CoursesAdapter(courseEntities, CourseListActivity.this);
                CoursesRecyclerView.setAdapter(CoursesAdapter);
            }
            else {
                CoursesAdapter.notifyDataSetChanged();
            }
        };

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
