package com.bash.backingapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = GridRemoteViewsFactory.class.getSimpleName();

    Context mContext;
    private BroadcastReceiver mIntentListener;
    ArrayList<String> mIngredientStrings = new ArrayList<String>();

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

        if (mIntentListener == null) {
            mIntentListener = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    mIngredientStrings = intent.getStringArrayListExtra(MainActivity.INGREDIENTS_STRINGS_KEY);
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.bash.backingapp.broadcast.INGREDIENTS_STRINGS");
            mContext.registerReceiver(mIntentListener, filter);
        }
    }

    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() { }

    @Override
    public void onDestroy() {
        if (mIntentListener != null) {
            mContext.unregisterReceiver(mIntentListener);
            mIntentListener = null;
        }
    }

    @Override
    public int getCount() {
        return mIngredientStrings.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item_ingredient);
        views.setTextViewText(R.id.widget_ingredientItem, mIngredientStrings.get(i));
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}