package com.example.tac_main_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.tac_main_project.R;
import com.example.tac_main_project.adapters.MainRandomAdapter;
import com.example.tac_main_project.models.view.ListViewModel;

public class MainActivityRandom extends AppCompatActivity {

    private ListViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_random);
        this.configureViewPager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        this.model = new ViewModelProvider(this).get(ListViewModel.class);

        this.model.initListViewModel(this);

        model.searchRandomCocktail();
        model.searchRandomMeal();

        if(savedInstanceState != null) {
            String viewMode = savedInstanceState.getString("viewMode");
            model.getCurrentViewMode().setValue(viewMode);
            model.searchRandomCocktail();
            model.searchRandomMeal();
        }

        ImageButton button = (ImageButton) findViewById(R.id.shuffle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.searchRandomCocktail();
                model.searchRandomMeal(); }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_random, menu);

        MenuItem language = menu.findItem(R.id.action_settings_language2);
        MenuItem viewMode = menu.findItem(R.id.action_settings_mode2);
        MenuItem help = menu.findItem(R.id.action_settings_help2);

        language.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent languageSettings = new Intent(getApplicationContext(), LanguageSettingsActivity.class);
                startActivity(languageSettings);
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

        help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast toast = Toast.makeText(getApplicationContext(), "Please help", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("viewMode",this.model.getCurrentViewMode().getValue());
        super.onSaveInstanceState(savedInstanceState);
    }

    private void configureViewPager(){

        ViewPager pager = (ViewPager) findViewById(R.id.activity_main_viewpager2);

        pager.setAdapter(new MainRandomAdapter(getSupportFragmentManager(), getResources().getIntArray(R.array.colorPagesViewPager)) {
        });
    }


}
