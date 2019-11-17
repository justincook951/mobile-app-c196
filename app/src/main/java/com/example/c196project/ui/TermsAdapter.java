package com.example.c196project.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.R;
import com.example.c196project.TermsEditorActivity;
import com.example.c196project.database.term.TermEntity;
import com.example.c196project.utilities.Standardizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Const.TERM_ID;

public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.ViewHolder>
{

    private final List<TermEntity> terms;
    private final Context context;

    public TermsAdapter(List<TermEntity> terms, Context context)
    {
        this.terms = terms;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_term, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final TermEntity term = terms.get(position);
        holder.termNameTextView.setText(term.getTitle());
        String termDateString = Standardizer.standardizeDateString(term.getStartDate(), term.getEndDate());
        holder.termDateTextView.setText(termDateString);
        holder.edit_term.setOnClickListener(view -> {
            Intent intent = new Intent(context, TermsEditorActivity.class);
            // Pass extra value in the style of key => value pairing
            intent.putExtra(TERM_ID, term.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.term_name_text)
        TextView termNameTextView;

        @BindView(R.id.term_date_text)
        TextView termDateTextView;

        @BindView(R.id.edit_term)
        FloatingActionButton edit_term;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
