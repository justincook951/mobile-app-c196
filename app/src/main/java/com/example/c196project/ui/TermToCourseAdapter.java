package com.example.c196project.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.R;
import com.example.c196project.database.course.CourseEntity;
import com.example.c196project.utilities.Standardizer;
import com.example.c196project.viewmodel.mtmrelationships.TermToCourseViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermToCourseAdapter extends RecyclerView.Adapter<TermToCourseAdapter.ViewHolder>
{

    private final List<CourseEntity> courses;
    private final int termId;
    private TermToCourseViewModel ttcViewModel;
    private List<CourseEntity> coursesAlreadyInTerm;

    public TermToCourseAdapter(List<CourseEntity> courses, FragmentActivity activity, int termId)
    {
        this.courses = courses;
        this.termId = termId;
        ttcViewModel = ViewModelProviders.of(activity).get(TermToCourseViewModel.class);
        coursesAlreadyInTerm = ttcViewModel.getMatchedEntries(termId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_term_to_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final CourseEntity course = courses.get(position);
        holder.courseNameTextView.setText(course.getTitle());
        String courseDateString = Standardizer.standardizeDateString(course.getStartDate(), course.getEndDate());
        holder.courseDateTextView.setText(courseDateString);
        holder.status_replaceable_text.setText(course.getStatus());
        coursesAlreadyInTerm = ttcViewModel.getMatchedEntries(termId);
        if (coursesAlreadyInTerm == null) {
            coursesAlreadyInTerm = ttcViewModel.getMatchedEntries(termId);
        }
        for (CourseEntity courseLooper : coursesAlreadyInTerm) {
            if (courseLooper.getId() == course.getId()) {
                holder.addToTerm.setChecked(true);
                break;
            }
        }
        holder.addToTerm.setOnClickListener(view -> {
            if ( ((CheckBox)view).isChecked() ){
                ttcViewModel.saveRelationship(termId, course.getId());
            }
            else {
                ttcViewModel.deleteRelationship(termId, course.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.course_name_text)
        TextView courseNameTextView;

        @BindView(R.id.course_date_text)
        TextView courseDateTextView;

        @BindView(R.id.status_replaceable_text)
        TextView status_replaceable_text;

        @BindView(R.id.add_to_term)
        CheckBox addToTerm;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
