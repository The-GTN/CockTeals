package com.example.tac_main_project.utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import com.example.tac_main_project.models.entities.MainEntity;
import com.example.tac_main_project.models.objects.Item;

/**
 * This class provides parser and formatting methods tools for requests.
 */
public class RequestUtils {

    /**
     * Constructor
     */
    public RequestUtils() {

    }

    /**
     * This method convert a Cocktail/Meal object to an entity, allowing inserts in Room
     * @param entity the entity to convert
     * @return The converted object entity.
     */
    public Item cocktailEntityToObject(MainEntity entity) {
        Item convertedObj = new Item(
                entity.cocktailName, entity.description, entity.viewUrl,
                stringToListString(entity.ingredients),stringToListString(entity.measures),
                entity.theme, true, entity.viewBytesArray,entity.i);
        return convertedObj;
    }

    /**
     * This method convert an entity to a Cocktail/Meal object, allowing inserts in LiveData list
     * @param object the Cocktail/Meal object to convert
     * @return The converted object Cocktail/Meal.
     */
    public MainEntity cocktailObjToEntity(Item object) {
        MainEntity convertedEntity = new MainEntity();
        convertedEntity.cocktailName = object.getName();
        convertedEntity.viewUrl = object.getViewUrl();
        convertedEntity.description = object.getDescription();
        convertedEntity.viewBytesArray = object.getByteArray();
        convertedEntity.ingredients = listStringToString(object.getIngredients());
        convertedEntity.measures = listStringToString(object.getMeasures());
        convertedEntity.theme = object.getTheme();
        convertedEntity.i = object.getId();
        return convertedEntity;
    }

    /**
     * This method joins a string list together with the operator '/' to a single string .
     * @param list array of string to join
     * @return The final string of the array concat
     */

    private String listStringToString(String[] list) {
        String res = "";
        for(int i = 0; i<list.length;i++) {
            if(list[i] != null)
                res = res.concat(list[i]).concat("/");
        }
        return res;
    }

    /**
     * This method split a string to an array of string using the '/' operator
     * @param str string to split
     * @return the array of string
     */

    private String[] stringToListString(String str) {
        return str.split("/");
    }

    /**
     * This method is a parser of JSON Object designed specially for the API response, each property
     * has to be indicate because it changes between mealdb API and cocktaildb API.
     * @param theme the type of API response (meal or drink)
     * @param listData the JSONArray to parse
     * @param name the name property of the JSON object (strDrink or strMeal)
     * @param inst the instruction property of the JSON object (strInstructions)
     * @param img the image thumbnail property of the JSON object (strDrinkThumbnail or strMealThumbnail)
     * @param id the id property of the JSON object
     * @return the list of Cocktails/Meals object corresponding to the JSON object array
     */

    public List<Item> JsonArrayToObject(String theme,JSONArray listData, String name, String inst, String img, String id){

        List<Item> requestItemList = new ArrayList<Item>();

        int limit;
        if (theme.equals("meal")) limit = 20;
        else limit = 15;

        for(int i=0;i<listData.length();i++){
            try {
                String n = listData.getJSONObject(i).getString(name);
                String dest = listData.getJSONObject(i).getString(inst);
                String image = listData.getJSONObject(i).getString(img);
                String[][] ingredients_measures = setIngredientsMeasures(limit,i,listData);
                String[] ingredients = ingredients_measures[0];
                String[] measures = ingredients_measures[1];
                String theid = listData.getJSONObject(i).getString(id);
                requestItemList.add(new Item(n,dest,image,ingredients,measures,theme,false,null,theid));
            }

            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return requestItemList;

    }

    /**
     * This method retrieve all ingredient and measures of an item, the API response is design like :
     * {
     *     ingredient1,
     *     ingredient2,
     *     ingredient3
     *     .
     *     .
     *     .
     * }
     * So each fields has to be parsed
     * @param limit is fixed to 15 the response never exceed 15 fields of ingredients and
     *              because all ingredients fields are present in the API response
     *              ( even if the field is null, exemple ingredient15 : null )
     * @param i the index of the current object
     * @param listData the JSON object array
     * @return The array containing the string array of ingredients and measeures
     * @throws JSONException
     */
    private String[][] setIngredientsMeasures(int limit, int i, JSONArray listData) throws JSONException {
        String[][] res = new String[2][];
        String[] ingredients = new String[limit];
        String[] measures = new String[limit];
        boolean moreIngredients = true;
        int j = 1;
        String ingr = "strIngredient";
        String meas = "strMeasure";
        String ingr_j;
        String meas_j;
        while(moreIngredients){
            if (j > limit) moreIngredients = false;
            else {
                ingr_j = ingr.concat(String.valueOf(j));
                if (listData.getJSONObject(i).isNull(ingr_j)) moreIngredients = false;
                else {
                    ingr_j = listData.getJSONObject(i).getString(ingr_j);
                    meas_j = listData.getJSONObject(i).getString(meas.concat(String.valueOf(j)));
                    ingredients[j-1] = ingr_j;
                    measures[j-1] = meas_j;
                    j++;
                }
            }
        }
        res[0] = ingredients;
        res[1] = measures;
        return res;
    }
}
