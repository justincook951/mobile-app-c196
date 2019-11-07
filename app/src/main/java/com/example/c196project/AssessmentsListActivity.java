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

import com.example.c196project.database.assessment.AssessmentEntity;
import com.example.c196project.ui.AssessmentsAdapter;
import com.example.c196project.viewmodel.assessment.AssessmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssessmentsListActivity extends AppCompatActivity
{

    @BindView(R.id.assessments_recycler)
    RecyclerView assessmentsRecyclerView;

    private List<AssessmentEntity> assessmentList = new ArrayList<>();
    private AssessmentsAdapter assessmentsAdapter;
    private AssessmentViewModel assessmentViewModel;
    private FloatingActionButton addAssessmentFab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        addAssessmentFab = findViewById(R.id.add_assessment_fab);
        addAssessmentFab.setOnClickListener(view -> {
            Intent intent = new Intent(this.getApplicationContext(), AssessmentsEditorActivity.class);
            startActivity(intent);
        });
        initRecyclerView();
        initViewModel();
    }

    private void initViewModel()
    {
        final Observer<List<AssessmentEntity>> assessmentObserver = new Observer<List<AssessmentEntity>>()
        {
            @Override
            public void onChanged(@Nullable List<AssessmentEntity> assessmentEntities)
            {
                assessmentList.clear();
                assessmentList.addAll(assessmentEntities);
                if (assessmentsAdapter == null) {
                    assessmentsAdapter = new AssessmentsAdapter(assessmentEntities, AssessmentsListActivity.this);
                    assessmentsRecyclerView.setAdapter(assessmentsAdapter);
                }
                else {
                    assessmentsAdapter.notifyDataSetChanged();
                }
            }
        };

        assessmentViewModel = ViewModelProviders.of(this)
                .get(AssessmentViewModel.class);
        assessmentViewModel.liveDataAssessments.observe(this, assessmentObserver);
    }

    private void initRecyclerView()
    {
        assessmentsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        assessmentsRecyclerView.setLayoutManager(layoutManager);

        assessmentsAdapter = new AssessmentsAdapter(assessmentList, this);
        assessmentsRecyclerView.setAdapter(assessmentsAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                assessmentsRecyclerView.getContext(), layoutManager.getOrientation()
        );
        assessmentsRecyclerView.addItemDecoration(divider);
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
