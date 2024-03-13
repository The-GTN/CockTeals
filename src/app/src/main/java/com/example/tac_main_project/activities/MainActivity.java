package com.example.tac_main_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tac_main_project.R;

public class MainActivity extends AppCompatActivity {

    private Button cocktailsButton;
    private Button mealsButton;
    private Button randomButton;
    private String viewMode = "grid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) viewMode = savedInstanceState.getString("viewMode");

        this.cocktailsButton = (Button) findViewById(R.id.buttonCocktails);
        this.mealsButton = (Button) findViewById(R.id.buttonMeals);
        this.randomButton = (Button) findViewById(R.id.buttonRandom);

        affectClickListeners();

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("viewMode",viewMode);
        super.onSaveInstanceState(savedInstanceState);
    }

    protected void affectClickListeners() {
        cocktailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cocktailsApp = new Intent(getApplicationContext(), MainActivityCocktails.class);
                startActivity(cocktailsApp);
            }
        });

        mealsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mealsApp = new Intent(getApplicationContext(), MainActivityMeals.class);
                startActivity(mealsApp);
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent randomApp = new Intent(getApplicationContext(), MainActivityRandom.class);
                startActivity(randomApp);
            }
        });
    }


    protected void affectClickListeners2() {
        cocktailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cocktailsApp = new Intent(getApplicationContext(), MainActivityCocktails.class);
                cocktailsApp.putExtra("viewMode",viewMode);
                startActivityForResult(cocktailsApp, 0);
            }
        });

        mealsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mealsApp = new Intent(getApplicationContext(), MainActivityMeals.class);
                mealsApp.putExtra("viewMode",viewMode);
                startActivityForResult(mealsApp, 0);
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent randomApp = new Intent(getApplicationContext(), MainActivityRandom.class);
                randomApp.putExtra("viewMode",viewMode);
                startActivityForResult(randomApp, 0);
            }
        });
    }

}