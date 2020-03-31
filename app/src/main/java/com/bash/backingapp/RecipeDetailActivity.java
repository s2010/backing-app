package com.bash.backingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.bash.backingapp.models.Recipe;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailsFragment.OnStepClickListener,StepDetailsFragment.OnNavClickListener {
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    public static final String DETAIL_RECIPE_KEY = "stepdetailkey";
    public static final String STEP_ID_KEY = "stepselectedkey";
    private static final String TAG_RECIPE_FRAGMENT = "recipeFragment";
    private static final String TAG_STEP_FRAGMENT = "stepFragment";

    private boolean mTwoPane = true;
    private Recipe mRecipe;
    private int mStepSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipedetail);

        mTwoPane = (findViewById(R.id.div_twopanes)!=null)? true : false;

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeDetailsFragment recipeFragment = null;
        StepDetailsFragment stepFragment = null;

        if(savedInstanceState!=null) {

            mRecipe = savedInstanceState.getParcelable(MainActivity.RECIPE_DETAIL_KEY);
            mStepSelected = savedInstanceState.getInt(STEP_ID_KEY);

            recipeFragment = (RecipeDetailsFragment) fragmentManager.findFragmentByTag(TAG_RECIPE_FRAGMENT);
            recipeFragment.setRecipe(mRecipe);

        } else {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mRecipe = (Recipe) extras.getParcelable(MainActivity.RECIPE_DETAIL_KEY);
            }

            mStepSelected = 0;

            if (mTwoPane) {
                recipeFragment = new RecipeDetailsFragment(mRecipe);
                stepFragment = new StepDetailsFragment(mRecipe.getSteps().get(mStepSelected));
                fragmentManager.beginTransaction()
                        .add(R.id.recipedetails_container, recipeFragment, TAG_RECIPE_FRAGMENT)
                        .add(R.id.stepdetails_container, stepFragment, TAG_STEP_FRAGMENT)
                        .commit();

            } else {

                recipeFragment = new RecipeDetailsFragment(mRecipe);
                fragmentManager.beginTransaction().add(R.id.recipedetails_container, recipeFragment, TAG_RECIPE_FRAGMENT).commit();

            }

        }
        if(mRecipe!=null) {
            setTitle(mRecipe.getName());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MainActivity.RECIPE_DETAIL_KEY,mRecipe);
        outState.putInt(STEP_ID_KEY,mStepSelected);
        super.onSaveInstanceState(outState);
    }

    // Define the behavior for onStepSelected
    @Override
    public void onStepSelected(int stepId) {

        mStepSelected = stepId;

        if (mTwoPane) {

            // If two panes, replace right fragment in the same activity
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailsFragment stepFragment = new StepDetailsFragment(mRecipe.getSteps().get(stepId));
            fragmentManager.beginTransaction().replace(R.id.stepdetails_container, stepFragment,TAG_STEP_FRAGMENT).commit();

        } else {

            // If single pane, open a new activity for step details
            Intent stepDetailIntent = new Intent(this, StepDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(DETAIL_RECIPE_KEY, mRecipe);
            bundle.putInt(STEP_ID_KEY,stepId);
            stepDetailIntent.putExtras(bundle);
            startActivity(stepDetailIntent);

        }
    }

    @Override
    public void onNavigate(int position) {
        // Navigation buttons are not active in tablet mode.
    }
}
