package com.example.c196project.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.MentorsEditorActivity;
import com.example.c196project.R;
import com.example.c196project.database.mentor.MentorEntity;
import com.example.c196project.utilities.Standardizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.MENTOR_ID;

public class MentorsAdapter extends RecyclerView.Adapter<MentorsAdapter.ViewHolder>
{

    private final List<MentorEntity> mentors;
    private final Context context;

    public MentorsAdapter(List<MentorEntity> mentors, Context context)
    {
        this.mentors = mentors;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_mentor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final MentorEntity mentor = mentors.get(position);
        holder.mentorNameTextView.setText(mentor.getName());
        holder.mentorEmailView.setText(mentor.getEmail());
        holder.mentorPhoneView.setText(mentor.getPhone());
        holder.edit_mentor.setOnClickListener(view -> {
            Intent intent = new Intent(context, MentorsEditorActivity.class);
            // Pass extra value in the style of key => value pairing
            intent.putExtra(MENTOR_ID, mentor.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mentors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.mentor_name_text)
        TextView mentorNameTextView;

        @BindView(R.id.mentor_email_text)
        TextView mentorEmailView;

        @BindView(R.id.mentor_phone_text)
        TextView mentorPhoneView;

        @BindView(R.id.edit_mentor)
        FloatingActionButton edit_mentor;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
