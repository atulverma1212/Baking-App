package com.example.averma1212.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.averma1212.bakingapp.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP on 21-12-2017.
 */

public class mAdapter extends RecyclerView.Adapter<mAdapter.recipeNameHolder> {
    private String[][] names;
    final private ListItemClickListener mListItemClickListener;

    public mAdapter(String[][] names,ListItemClickListener listItemClickListener) {
        this.names = names;
        mListItemClickListener = listItemClickListener;
    }

    public interface ListItemClickListener {
        void onListItemClicked(int clickedItemIndex);
    }

    @Override
    public recipeNameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_name_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new recipeNameHolder(view);
    }

    @Override
    public void onBindViewHolder(recipeNameHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(names==null) return 0;
        return names[0].length;
    }

    class recipeNameHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.recipe_name_tv)
        TextView recipeName;
        @BindView(R.id.recipe_name_iv)
        ImageView recipeImage;
        public recipeNameHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            recipeName.setOnClickListener(this);
        }

        void bind(int listIndex){
            recipeName.setText(names[0][listIndex]);
            if(names[1][listIndex].length()>0){
                Picasso.with(recipeImage.getContext())
                        .load(names[1][listIndex])
                        .into(recipeImage);
            } else {
                recipeImage.setVisibility(View.GONE);
            }
        }


        @Override
        public void onClick(View view) {
                int clickedPosition =  getAdapterPosition();
                mListItemClickListener.onListItemClicked(clickedPosition);
        }
    }
}
