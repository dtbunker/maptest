package com.assis.redondo.daniel.maptest.utils.toolbar;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.assis.redondo.daniel.maptest.R;
import com.assis.redondo.daniel.maptest.utils.Typefaces;
import com.assis.redondo.daniel.maptest.utils.fonts.Fonts;


public class ToolBarHelper {

    private static final String TAG = ToolBarHelper.class.getSimpleName();

    private final Context context;
    private Toolbar toolBar;
    private AppCompatActivity mActivity;
    private TextView toolbarTitle;

    public ToolBarHelper(Context context) {
        this.context = context;

    }

    public void init(AppCompatActivity activity) {
        this.mActivity = activity;
        toolBar = (Toolbar) activity.findViewById(R.id.toolBarLayout);
        toolbarTitle = (TextView) activity.findViewById(R.id.toolbarTitle);

        activity.setSupportActionBar(toolBar);
        Typefaces.setTextFont(context.getAssets(), toolbarTitle, Fonts.RB_REGULAR);


    }

    public void setTitle(String title ) {
        mActivity.getSupportActionBar().setTitle("");
        toolbarTitle.setText(title);
        mActivity.getSupportActionBar().setTitle(title);
    }

    public Toolbar getToolBar(){
        return toolBar;
    }

    private void changeVisibility(View view, int visibility) {
        if(view != null)
            view.setVisibility(visibility);
    }


    public void setDisplayHomeAsUpEnabled(boolean displayHomeAsUpEnabled) {
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setBackNavigationIcon() {
        toolBar.setNavigationIcon(context.getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
    }
    public void setHomeNavigationIcon() {
        toolBar.setNavigationIcon(context.getResources().getDrawable(R.drawable.ic_menu_black_24dp));
    }

    public void hideHomeButton(boolean hide) {
        if(hide){
            toolBar.setNavigationIcon(null);
        }
    }
}
