package com.bash.backingapp;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class BakingWidgetProvider extends AppWidgetProvider {
    private static final String TAG = BakingWidgetProvider.class.getSimpleName();

    private static String mRecipeName;

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           String recipename, int[] appWidgetIds) {
        mRecipeName = recipename;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews rv = getRecipeGridRemoteView(context);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getRecipeGridRemoteView(Context context) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        rv.setTextViewText(R.id.appwidget_text, mRecipeName);
        Intent widgetintent = new Intent(context, GridWidgetService.class);
        rv.setRemoteAdapter(R.id.widget_listview, widgetintent);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        rv.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        return rv;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}