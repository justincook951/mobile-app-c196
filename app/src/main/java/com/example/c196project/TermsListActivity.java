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

import com.example.c196project.database.term.TermEntity;
import com.example.c196project.ui.TermsAdapter;
import com.example.c196project.viewmodel.term.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsListActivity extends AppCompatActivity
{

    @BindView(R.id.terms_recycler)
    RecyclerView termsRecyclerView;

    private List<TermEntity> termList = new ArrayList<>();
    private TermsAdapter termsAdapter;
    private TermViewModel termViewModel;
    private FloatingActionButton addTermFab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        addTermFab = findViewById(R.id.add_term_fab);
        addTermFab.setOnClickListener(view -> {
            Intent intent = new Intent(this.getApplicationContext(), TermsEditorActivity.class);
            startActivity(intent);
        });
        initRecyclerView();
        initViewModel();
    }

    private void initViewModel()
    {
        final Observer<List<TermEntity>> termObserver = new Observer<List<TermEntity>>()
        {
            @Override
            public void onChanged(@Nullable List<TermEntity> termEntities)
            {
                termList.clear();
                termList.addAll(termEntities);
                if (termsAdapter == null) {
                    termsAdapter = new TermsAdapter(termEntities, TermsListActivity.this);
                    termsRecyclerView.setAdapter(termsAdapter);
                }
                else {
                    termsAdapter.notifyDataSetChanged();
                }
            }
        };

        termViewModel = ViewModelProviders.of(this)
                .get(TermViewModel.class);
        termViewModel.liveDataTerms.observe(this, termObserver);
    }

    private void initRecyclerView()
    {
        termsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        termsRecyclerView.setLayoutManager(layoutManager);

        termsAdapter = new TermsAdapter(termList, this);
        termsRecyclerView.setAdapter(termsAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                termsRecyclerView.getContext(), layoutManager.getOrientation()
        );
        termsRecyclerView.addItemDecoration(divider);
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
