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

import com.example.c196project.database.mentor.MentorEntity;
import com.example.c196project.ui.MentorsAdapter;
import com.example.c196project.viewmodel.mentor.MentorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MentorsListActivity extends AppCompatActivity
{

    @BindView(R.id.mentors_recycler)
    RecyclerView mentorsRecyclerView;

    private List<MentorEntity> mentorList = new ArrayList<>();
    private MentorsAdapter mentorsAdapter;
    private MentorViewModel mentorViewModel;
    private FloatingActionButton addMentorFab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentors);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        addMentorFab = findViewById(R.id.add_mentor_fab);
        addMentorFab.setOnClickListener(view -> {
            Intent intent = new Intent(this.getApplicationContext(), MentorsEditorActivity.class);
            startActivity(intent);
        });
        initRecyclerView();
        initViewModel();
    }

    private void initViewModel()
    {
        final Observer<List<MentorEntity>> mentorObserver = new Observer<List<MentorEntity>>()
        {
            @Override
            public void onChanged(@Nullable List<MentorEntity> mentorEntities)
            {
                mentorList.clear();
                mentorList.addAll(mentorEntities);
                if (mentorsAdapter == null) {
                    mentorsAdapter = new MentorsAdapter(mentorEntities, MentorsListActivity.this);
                    mentorsRecyclerView.setAdapter(mentorsAdapter);
                }
                else {
                    mentorsAdapter.notifyDataSetChanged();
                }
            }
        };

        mentorViewModel = ViewModelProviders.of(this)
                .get(MentorViewModel.class);
        mentorViewModel.liveDataMentors.observe(this, mentorObserver);
    }

    private void initRecyclerView()
    {
        mentorsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mentorsRecyclerView.setLayoutManager(layoutManager);

        mentorsAdapter = new MentorsAdapter(mentorList, this);
        mentorsRecyclerView.setAdapter(mentorsAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                mentorsRecyclerView.getContext(), layoutManager.getOrientation()
        );
        mentorsRecyclerView.addItemDecoration(divider);
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
