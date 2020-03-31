package com.bash.backingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.bash.backingapp.models.Recipe;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder>{
    private static final String TAG = RecipeListAdapter.class.getSimpleName();
    private List<Recipe> mRecipeList;
    private final Context mContext;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void OnListItemClick(Recipe recipe);
    }

    public RecipeListAdapter(List<Recipe> recipeList, ListItemClickListener listener, Context context) {

        mRecipeList = recipeList;

        mOnClickListener = listener;
        mContext = context;
    }
    @NonNull
    @Override
    public RecipeListAdapter.RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.RecipeListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipeList == null ? 0 : mRecipeList.size();
    }

    public void setRecipeListData(List<Recipe> movieItemList) {
        mRecipeList = movieItemList;
        notifyDataSetChanged();
    }

    public class RecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_recipeName;
        TextView tv_servings;
        ImageView iv_thumb;
        View v_click;

        public RecipeListViewHolder(View itemView) {
            super(itemView);
            tv_recipeName = itemView.findViewById(R.id.tv_recipe_name);
            tv_servings = itemView.findViewById(R.id.tv_servings);
            iv_thumb = itemView.findViewById(R.id.iv_thumb);
            v_click = itemView.findViewById(R.id.viewClicker);
            v_click.bringToFront();
            v_click.setOnClickListener(this);
        }


        void bind(int listIndex) {
            Recipe recipe = mRecipeList.get(listIndex);
            tv_recipeName.setText(recipe.getName());
            tv_servings.append(recipe.getServings().toString());
            String thumbsrc = recipe.getImage();
            if (thumbsrc.length()==0) {
                iv_thumb.setImageResource(R.drawable.muffin);
            } else {
                try {
                    Picasso.with(mContext)
                            .load(thumbsrc)
                            .placeholder(R.drawable.muffin)
                            .error(R.mipmap.ic_launcher)
                            .into(iv_thumb);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.OnListItemClick(mRecipeList.get(clickedPosition));
        }
    }
}
