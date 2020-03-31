package com.bash.backingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bash.backingapp.models.Ingredient;
import com.bash.backingapp.models.Recipe;
import com.bash.backingapp.models.Step;

import java.util.List;

public class RecipeDetailsFragment extends Fragment implements StepListAdapter.StepItemClickListener {
    private static final String TAG = RecipeDetailsFragment.class.getSimpleName();

    private Recipe mRecipe;
    private static TextView tvIngredientsTitle;
    private static TextView tvIngredients;
    private RecyclerView mStepListRecyclerView;
    private StepListAdapter mStepListAdapter;

    OnStepClickListener mCallback;

    public interface OnStepClickListener {
        void onStepSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    public RecipeDetailsFragment() {}

    @SuppressLint("ValidFragment")
    public RecipeDetailsFragment(Recipe recipe) {
        mRecipe = recipe;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.fragment_recipedetail, container, false);
        tvIngredients = rootView.findViewById(R.id.tv_ingredients);
        tvIngredientsTitle = rootView.findViewById(R.id.tv_title_ingredients);

        if(tvIngredients!=null) {
            setIngredientList();
        }

        mStepListRecyclerView = rootView.findViewById(R.id.rv_steps);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mStepListRecyclerView.setLayoutManager(layoutManager);
        mStepListRecyclerView.setHasFixedSize(false);

        List<Step> mStepList = mRecipe.getSteps();
        mStepListAdapter = new StepListAdapter(mStepList, getActivity(), this);
        mStepListRecyclerView.setAdapter(mStepListAdapter);

        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        this.mRecipe = recipe;
    }

    public void setIngredientList() {

        List<Ingredient> ingredientList = mRecipe.getIngredients();
        tvIngredients.setText("");
        for (int i=0; i<ingredientList.size(); i++) {
            tvIngredients.append( " • " +
                    ingredientList.get(i).getQuantity() + " " +
                    ingredientList.get(i).getMeasure() + " " +
                    ingredientList.get(i).getIngredient() + "\n");
        }
        tvIngredientsTitle.setText("Ingredients for " + mRecipe.getName() + ":");
    }

    @Override
    public void OnListItemClick(Step step) {
        mCallback.onStepSelected(step.getId());
    }
}
