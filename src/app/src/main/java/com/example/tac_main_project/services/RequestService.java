package com.example.tac_main_project.services;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tac_main_project.models.objects.Item;
import com.example.tac_main_project.utils.RequestUtils;
import com.example.tac_main_project.models.view.ListViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

public class RequestService {

    /**
     * The request queue for Volley
     */
    public RequestQueue queue;

    /**
     * The first key of the API response ( drinks or meals )
     */
    public String mainKey;

    /**
     * The request option parameter in API url (i for id, s for search ...)
     */
    public String option;

    /**
     * The key word of the two API url ( cocktail or meal )
     */
    public String urlKey;

    /**
     * The viewModel mutable list to change after request
     */
    public MutableLiveData<List<Item>> model;

    /**
     * The key work for the JSON property ( Drink or Meal )
     */
    public String objKey;

    /**
     * The RequestService constructor
     * @param queue The request queue for Volley
     * @param urlKey The key word of the two API url ( cocktail or meal )
     * @param mainKey The first key of the API response ( drinks or meals )
     * @param objKey The key work for the JSON property ( Drink or Meal )
     * @param option The request option parameter in API url (i for id, s for search ...)
     * @param model The viewModel mutable list to change after request
     */
    public RequestService(RequestQueue queue, String urlKey, String mainKey, String objKey, String option, MutableLiveData<List<Item>> model) {

        this.queue = queue;
        this.mainKey = mainKey;
        this.option = option;
        this.urlKey = urlKey;
        this.model = model;
        this.objKey = objKey;
    }

    /**
     * Request method to get a random item from the API
     */

    public void getDataRandom() {
        String url = "https://www.the".concat(this.urlKey).concat("db.com/api/json/v1/1/random.php");
        this.mainGetDataRequest(url);
    }

    /**
     * Request method to perform a search with the name of an item from the API
     * @param foodName The name of the searched item
     */
    public void getDataRequest(String foodName) {
        String url = "https://www.the".concat(this.urlKey).concat("db.com/api/json/v1/1/search.php?").concat(this.option).concat("=").concat(foodName);
        this.mainGetDataRequest(url);
    }

    /**
     * Request method to perform a search with the id of an item from the API
     * @param id The id of the searched item
     */
    public void getDataRequestID(String id) {
        String url = "https://www.the".concat(this.urlKey).concat("db.com/api/json/v1/1/lookup.php?i=").concat(id);
        this.mainGetDataRequest(url);
    }

    /**
     * The main method of requesting the API, it performs a GET request with volley on the right url
     * then depending on the result, it changes the Mutable LiveDate list in the model to update view
     * @param url The requested url with the right options and keys
     */
    public void mainGetDataRequest(String url) {
        String mainKey = this.mainKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray listData = obj.getJSONArray(mainKey);
                            RequestUtils utils = new RequestUtils();
                            model.setValue(utils.JsonArrayToObject(urlKey,listData,"str".concat(objKey),"strInstructions","str".concat(objKey).concat("Thumb"),"id".concat(objKey)));


                        } catch (JSONException e) {
                            model.setValue(new ArrayList<Item>());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                model.setValue(new ArrayList<Item>());
                System.out.println("That didn't work!");
            }
        });
        this.queue.add(stringRequest);
    }

}
