package com.example.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.c196project.database.coursenote.CourseNoteEntity;
import com.example.c196project.ui.CourseNotesAdapter;
import com.example.c196project.viewmodel.coursenote.CourseNoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.COURSE_ID;

public class CourseNotesListActivity extends AppCompatActivity
{

    @BindView(R.id.course_notes_recycler)
    RecyclerView courseNotesRecyclerView;

    private List<CourseNoteEntity> courseNoteList = new ArrayList<>();
    private CourseNotesAdapter courseNotesAdapter;
    private CourseNoteViewModel courseNoteViewModel;
    private FloatingActionButton addCourseNoteFab;
    private int courseId;
    Bundle kvExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_notes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        addCourseNoteFab = findViewById(R.id.add_course_note_fab);
        addCourseNoteFab.setOnClickListener(view -> {
            Intent intent = new Intent(this.getApplicationContext(), CourseNotesEditorActivity.class);
            intent.putExtra(COURSE_ID, courseId);
            startActivity(intent);
        });
        initRecyclerView();
        initViewModel();
    }

    private void initViewModel()
    {
        final Observer<List<CourseNoteEntity>> courseNoteObserver = new Observer<List<CourseNoteEntity>>()
        {
            @Override
            public void onChanged(@Nullable List<CourseNoteEntity> courseNoteEntities)
            {
                kvExtras = getIntent().getExtras();
                courseId = kvExtras.getInt(COURSE_ID);
                courseNoteList.clear();
                for (CourseNoteEntity entity : courseNoteEntities) {
                    if (entity.getCourseId() == courseId) {
                        courseNoteList.add(entity);
                    }
                    else {
                        Log.i("MethodCalled", "Did not add coursenote to course list because " + entity.getCourseId() + " did not equal " + courseId);
                    }
                }
                if (courseNotesAdapter == null) {
                    courseNotesAdapter = new CourseNotesAdapter(courseNoteEntities, CourseNotesListActivity.this);
                    courseNotesRecyclerView.setAdapter(courseNotesAdapter);
                }
                else {
                    courseNotesAdapter.notifyDataSetChanged();
                }
            }
        };

        courseNoteViewModel = ViewModelProviders.of(this)
                .get(CourseNoteViewModel.class);
        courseNoteViewModel.liveDataCourseNotes.observe(this, courseNoteObserver);
    }

    private void initRecyclerView()
    {
        courseNotesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        courseNotesRecyclerView.setLayoutManager(layoutManager);

        courseNotesAdapter = new CourseNotesAdapter(courseNoteList, this);
        courseNotesRecyclerView.setAdapter(courseNotesAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                courseNotesRecyclerView.getContext(), layoutManager.getOrientation()
        );
        courseNotesRecyclerView.addItemDecoration(divider);
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
