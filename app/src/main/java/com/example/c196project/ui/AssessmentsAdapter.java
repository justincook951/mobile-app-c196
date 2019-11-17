package com.example.c196project.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.AssessmentsEditorActivity;
import com.example.c196project.R;
import com.example.c196project.database.assessment.AssessmentEntity;
import com.example.c196project.utilities.Standardizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.ASSESSMENT_ID;

public class AssessmentsAdapter extends RecyclerView.Adapter<AssessmentsAdapter.ViewHolder>
{

    private final List<AssessmentEntity> assessments;
    private final Context context;

    public AssessmentsAdapter(List<AssessmentEntity> assessments, Context context)
    {
        this.assessments = assessments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_assessment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final AssessmentEntity assessment = assessments.get(position);
        holder.assessmentNameTextView.setText(assessment.getTitle());
        String assessmentDateString = Standardizer.standardizeSingleDateString(assessment.getEndDate());
        holder.assessmentDateTextView.setText(assessmentDateString);
        holder.edit_assessment.setOnClickListener(view -> {
            Intent intent = new Intent(context, AssessmentsEditorActivity.class);
            // Pass extra value in the style of key => value pairing
            intent.putExtra(ASSESSMENT_ID, assessment.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.assessment_name_text)
        TextView assessmentNameTextView;

        @BindView(R.id.assessment_date_text)
        TextView assessmentDateTextView;

        @BindView(R.id.edit_assessment)
        FloatingActionButton edit_assessment;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
