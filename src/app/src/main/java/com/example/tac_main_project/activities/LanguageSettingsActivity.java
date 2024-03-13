package com.example.tac_main_project.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tac_main_project.R;

import java.util.ArrayList;
import java.util.List;

public class LanguageSettingsActivity extends AppCompatActivity {

    private ListView language_list;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.language_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.language_list = findViewById(R.id.language_list);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.getLanguageList());
        language_list.setAdapter(itemsAdapter);
        language_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selected = itemsAdapter.getItem(position);
                Toast toast = Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<String> getLanguageList() {

        List<String> list = new ArrayList<String>();

        String langue1 = "French";
        String langue2 = "English";
        String langue3 = "Spanish";
        String langue4 = "Russian";
        String langue5 = "Italian";

        list.add(langue1);
        list.add(langue2);
        list.add(langue3);
        list.add(langue4);
        list.add(langue5);

        return list;
    }
}
