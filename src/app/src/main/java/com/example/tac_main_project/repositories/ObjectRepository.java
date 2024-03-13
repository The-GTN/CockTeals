package com.example.tac_main_project.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.tac_main_project.models.dao.MainDao;
import com.example.tac_main_project.models.database.MainDatabase;
import com.example.tac_main_project.models.entities.MainEntity;
import com.example.tac_main_project.models.objects.Item;
import com.example.tac_main_project.services.RequestService;
import com.example.tac_main_project.utils.GetTask;
import com.example.tac_main_project.utils.RequestUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ObjectRepository {

    /**
     * The mutable live data list of the viewModel
     */
    private MutableLiveData<List<Item>> model;

    /**
     * The request queue for Volley request
     */
    private RequestQueue queue;

    /**
     * The context to init Volley queue and check database
     */
    private Context context;

    /**
     * The cocktailService to perform request on API
     */
    private RequestService cocktailService;

    /**
     * The room database containing favorites items saved
     */
    private MainDatabase db;

    /**
     * The DAO to perform Room SQL request
     */
    private MainDao mainDao;

    /**
     * The request tools to parse and convert the data
     */
    private RequestUtils utils = new RequestUtils();

    /**
     * The ObjectRepository constructor
     * @param context The context to init Volley queue and check database
     * @param model The mutable live data list of the viewModel
     * @param urlKey The key word for the API url (cocktail or meal)
     * @param mainKey The first key of the API response ( drinks or meals )
     * @param objKey The key work for the JSON property ( Drink or Meal )
     * @param option The request option parameter in API url (i for id, s for search ...)
     */
    public ObjectRepository(Context context, MutableLiveData<List<Item>> model, String urlKey, String mainKey, String objKey, String option){

        this.model = model;
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
        this.cocktailService = new RequestService(queue,urlKey,mainKey,objKey,option,model);
        if(doesDatabaseExist(context,urlKey)) {
            String path = "/data/user/0/com.example.tac_main_project/databases/".concat(urlKey);
            this.db = Room.databaseBuilder(context, MainDatabase.class, urlKey)
                    .createFromFile(new File(path))
                    .build();
        }
        else {
            this.db = Room.databaseBuilder(context, MainDatabase.class,urlKey).build();
        }
        this.mainDao = db.CocktailDao();
    }

    /**
     * This method call the requestService to perform a search on the search option of the API
     * @param query The string the user entered on the search toolbar widget
     */
    public void getRequestResult(String query){
        this.cocktailService.getDataRequest(query);
    }

    /**
     * This method call the requestService to perform a search on the ID search option of the API
     * @param id
     */
    public void getIdResult(String id) { this.cocktailService.getDataRequestID(id); }

    /**
     * This method call the requestService to perform the random option of the API
     */
    public void getRandomResult() { this.cocktailService.getDataRandom(); }

    /**
     * Async method to insert an item to the Room database via the DAO
     * @param mainEntity the item to insert
     */
    public void insertCocktailToLocal(MainEntity mainEntity){

        AsyncTask.execute(() -> mainDao.insertAll(mainEntity));
    }

    /**
     * Async method to delete an item from the Room database via the DAO
     * @param mainEntity the item to remove
     */
    public void deleteCocktailLocal(MainEntity mainEntity){

        AsyncTask.execute(() -> mainDao.delete(mainEntity));
    }

    /**
     * Async method to get all the items saved in the Room database via the DAO
     * @return The list of Cocktails/Meals items stored in the database
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<Item> getAllCocktailLocal() throws ExecutionException, InterruptedException {

        GetTask getTask = new GetTask();
        return getTask.execute(this.mainDao).get();

    }

    /**
     * Method to check if a Room database already exists on the device
     * @param context The context of the current activity
     * @param dbName The database name to check the existence
     * @return True if database already exists, False otherwise
     */
    private boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

}