package com.example.tac_main_project.utils;

import android.os.AsyncTask;

import com.example.tac_main_project.models.dao.MainDao;
import com.example.tac_main_project.models.entities.MainEntity;
import com.example.tac_main_project.models.objects.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides asynchronous tasks event to retrieve items from Room
 */
public class GetTask extends AsyncTask<MainDao,Void, List<Item>> {

    protected void onPostExecute(List<Item> result) {

    }

    /**
     * Main method of an AsyncTask
     * @param mainDaos, the dao allowing the task to call the get method of Room
     * @return converted list of entity items to Cocktails/Meals objects
     */
    @Override
    protected List<Item> doInBackground(MainDao... mainDaos) {

        List<Item> convertedList = new ArrayList<Item>();

        List<MainEntity> entityList = new ArrayList<MainEntity>();

        entityList.addAll(mainDaos[0].getAll());

        for(int i=0;i<entityList.size();i++){
            try {
                convertedList.add(new RequestUtils().cocktailEntityToObject(entityList.get(i)));
            } catch (Error e) {
                e.printStackTrace();
            }
        }
        return convertedList;
    }
}
