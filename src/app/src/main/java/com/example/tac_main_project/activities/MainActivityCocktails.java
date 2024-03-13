package com.example.tac_main_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.tac_main_project.R;
import com.example.tac_main_project.adapters.MainCocktailsAdapter;
import com.example.tac_main_project.models.view.ListViewModel;

public class MainActivityCocktails extends AppCompatActivity {

    private ListViewModel model;

    private String current_query = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_items);
        this.configureViewPager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.model = new ViewModelProvider(this).get(ListViewModel.class);

        this.model.initListViewModel(this);

        if(savedInstanceState != null) {
            String viewMode = savedInstanceState.getString("viewMode");
            model.getCurrentViewMode().setValue(viewMode);
            this.current_query = savedInstanceState.getString("query");
            if (current_query != null) model.searchForCocktails(current_query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem mySearch = menu.findItem(R.id.search);
        SearchView mySearchView = (SearchView) mySearch.getActionView();
        mySearchView.setQueryHint("Search");

        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hideSoftKeyboard(MainActivityCocktails.this);
                current_query = query;
                model.searchForCocktails(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem language = menu.findItem(R.id.action_settings_language);
        MenuItem viewMode = menu.findItem(R.id.action_settings_mode);
        MenuItem help = menu.findItem(R.id.action_settings_help);

        language.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent languageSettings = new Intent(getApplicationContext(), LanguageSettingsActivity.class);
                startActivity(languageSettings);
                return true;
            }
        });

        help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast toast = Toast.makeText(getApplicationContext(), "Please help", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });

        viewMode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(model.getCurrentViewMode().getValue().equals("grid")){
                    model.getCurrentViewMode().setValue("list");
                    viewMode.setTitle("View : Grid");
                }else{
                    model.getCurrentViewMode().setValue("grid");
                    viewMode.setTitle("View : List");
                }
                return true;
            }
        });


        return true;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    private void configureViewPager(){

        ViewPager pager = (ViewPager) findViewById(R.id.activity_main_viewpager);

        pager.setAdapter(new MainCocktailsAdapter(getSupportFragmentManager(), getResources().getIntArray(R.array.colorPagesViewPager)) {
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("viewMode",this.model.getCurrentViewMode().getValue());
        savedInstanceState.putString("query",current_query);
        super.onSaveInstanceState(savedInstanceState);
    }







}