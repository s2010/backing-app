package com.bash.backingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.bash.backingapp.models.Ingredient;
import com.bash.backingapp.models.Recipe;
import com.bash.backingapp.utilities.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.ListItemClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String RECIPE_DETAIL_KEY = "recipedetail";
    public static final String INGREDIENTS_STRINGS_KEY = "ingredientsstringskey";

    private List<Recipe> mRecipeList = new ArrayList<>();
    private RecyclerView mRecipeListRecyclerView;
    private RecipeListAdapter mRecipeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecipeList.clear();

        mRecipeListRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
        if(findViewById(R.id.v_tablet_pixel)!=null) {
            mRecipeListRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }else{
            mRecipeListRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }
        mRecipeListRecyclerView.setHasFixedSize(false);
        mRecipeListAdapter = new RecipeListAdapter(mRecipeList, this, this);
        mRecipeListRecyclerView.setAdapter(mRecipeListAdapter);

        loadRecipes();
    }

    @Override
    public void OnListItemClick(Recipe recipe) {

        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        List<Ingredient> ingredientList = recipe.getIngredients();
        ArrayList<String> ingredientsStrings = new ArrayList<String>();
        for (int i=0; i<ingredientList.size(); i++) {
            ingredientsStrings.add( //" • " +
                    ingredientList.get(i).getQuantity() + " " +
                            ingredientList.get(i).getMeasure() + " " +
                            ingredientList.get(i).getIngredient() + "\n");
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.bash.backingapp.broadcast.INGREDIENTS_STRINGS");
        broadcastIntent.putStringArrayListExtra(INGREDIENTS_STRINGS_KEY,ingredientsStrings);
        sendBroadcast(broadcastIntent);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.widget_listview);

        BakingWidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipe.getName(), appWidgetIds);

        Intent recipeDetailIntent = new Intent(MainActivity.this, RecipeDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_DETAIL_KEY, recipe);
        recipeDetailIntent.putExtras(bundle);
        startActivity(recipeDetailIntent);

    }

    public void loadRecipes() {

        Call<ArrayList<Recipe>> call = ApiUtils.getRecipes();

        call.enqueue(new Callback<ArrayList<Recipe>>() {

            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                mRecipeList = response.body();
                mRecipeListAdapter.setRecipeListData(mRecipeList);
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d(TAG,"FAILED");
                t.printStackTrace();
                showEmptyView();
            }
        });
    }

    private void showEmptyView() {
        Toast.makeText(this, "EMPTY LIST", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"EMPTY LIST");
    }

}