package com.example.tac_main_project.activities;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MenuItem;
import android.text.method.ScrollingMovementMethod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tac_main_project.adapters.IngredientsAdapter;
import com.example.tac_main_project.R;


public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String imageUrl = getIntent().getExtras().getString("imageUrl");
        String name = getIntent().getExtras().getString("name");
        String theme = getIntent().getExtras().getString("theme");
        String description = getIntent().getExtras().getString("description");
        String[] ingredients = getIntent().getExtras().getStringArray("ingredients");
        String[] measures = getIntent().getExtras().getStringArray("measures");
        byte[] byteArray = getIntent().getExtras().getByteArray("bytearray");

        ImageView mainImage = (ImageView) findViewById(R.id.mainImage_details);
        TextView descriptionItem = (TextView) findViewById(R.id.description_item);
        descriptionItem.setMovementMethod(new ScrollingMovementMethod());

        if (byteArray != null) Glide.with(this).load(byteArray).into(mainImage);
        else Glide.with(this).load(imageUrl).into(mainImage);
        descriptionItem.setText(description);

        this.configureIngredients(theme, ingredients, measures);

        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureIngredients(String theme, String[] ingredients, String[] measures) {


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_ingredients);

        RecyclerView.LayoutManager HorizontalLayout =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(HorizontalLayout);


        IngredientsAdapter adapter = new IngredientsAdapter(this, theme, ingredients, measures);
        recyclerView.setAdapter(adapter);

    }



}