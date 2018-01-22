package com.example.averma1212.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.averma1212.bakingapp.R;
import com.example.averma1212.bakingapp.data.Steps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.stepsNameHolder>{
    private ArrayList<Steps> mSteps;
    final private StepItemClickListener mListener;

    public StepsAdapter(ArrayList<Steps> Steps,StepItemClickListener Listener) {
        this.mSteps = Steps;
        this.mListener = Listener;
    }

    @Override
    public stepsNameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.step_name_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new StepsAdapter.stepsNameHolder(view);
    }

    @Override
    public void onBindViewHolder(stepsNameHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public interface StepItemClickListener {
        void onStepItemClicked(int clickedItemIndex);
    }

    class stepsNameHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.steps_name_tv)
        TextView stepName;
        public stepsNameHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex){
            Steps Step = mSteps.get(listIndex);
            String step = "Step "+ String.valueOf(listIndex+1) + " : " + Step.getShortDesc();
            stepName.setText(step);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onStepItemClicked(clickedPosition);
        }
    }
}
