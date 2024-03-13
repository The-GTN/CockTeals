package com.example.tac_main_project.models.view;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tac_main_project.models.objects.Item;
import com.example.tac_main_project.repositories.ObjectRepository;
import com.example.tac_main_project.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class ListViewModel extends ViewModel {

    private MutableLiveData<List<Item>> list = new MutableLiveData<List<Item>>();

    private ObjectRepository cocktailRepository;

    private ObjectRepository mealRepository;

    private MutableLiveData<List<Item>> favList = new MutableLiveData<List<Item>>();

    private MutableLiveData<String> viewDisplay = new MutableLiveData<String>();

    private RequestUtils utils = new RequestUtils();

    private String current_theme = "cocktail";

    public void initListViewModel(Context context){
        viewDisplay.setValue("list");
        this.cocktailRepository = new ObjectRepository(context,this.list,"cocktail","drinks","Drink","s");
        this.mealRepository = new ObjectRepository(context,this.list,"meal","meals","Meal","s");
    }

    public MutableLiveData<List<Item>> getCurrentList() {
        if (list == null) {
            list = new MutableLiveData<List<Item>>();
        }
        return list;
    }

    public MutableLiveData<List<Item>> getCurrentFavList(String theme) {
        if(theme.equals(current_theme)) {
            if (favList == null) {
                favList = new MutableLiveData<List<Item>>();
            }
        }
        else {
            current_theme = theme;
            favList = new MutableLiveData<List<Item>>();
            try { favList.setValue(getAllCocktailsLocaly(theme)); }
            catch (Exception e) {}
        }
        return favList;
    }

    public MutableLiveData<String> getCurrentViewMode() {
        if (viewDisplay == null) {
            viewDisplay = new MutableLiveData<String>();
        }
        return viewDisplay;
    }

    public List<String> getAllNamesOfFavList() {

        List<String> favoritesListNames = new ArrayList<String>();
        if (this.favList.getValue() != null)
        for(int i=0;i<this.favList.getValue().size();i++) {
            favoritesListNames.add(this.favList.getValue().get(i).getName());
        }
        return favoritesListNames;
    }


    public void searchForCocktails(String query) {
        cocktailRepository.getRequestResult(query);
    }
    public void searchForMeals(String query) {
        mealRepository.getRequestResult(query);
    }
    public void searchForCocktailId(String id) { cocktailRepository.getIdResult(id); }
    public void searchForMealId(String id) { mealRepository.getIdResult(id); }
    public void searchRandomCocktail() { cocktailRepository.getRandomResult(); }
    public void searchRandomMeal() { mealRepository.getRandomResult(); }

    public List<Item> getAllCocktailsLocaly(String theme) throws ExecutionException, InterruptedException {
        if(theme.equals("cocktail")) return this.cocktailRepository.getAllCocktailLocal();
        else return this.mealRepository.getAllCocktailLocal();
    }

    public void insertCocktailLocaly(Item cocktail, byte[] byteArray){
        cocktail.setByteArray(byteArray);
        if (cocktail.getTheme().equals("cocktail"))
            this.cocktailRepository.insertCocktailToLocal(this.utils.cocktailObjToEntity(cocktail));
        else
            this.mealRepository.insertCocktailToLocal(this.utils.cocktailObjToEntity(cocktail));
    }

    public void deleteCocktailLocaly(Item cocktail){
        if (cocktail.getTheme().equals("cocktail"))
            this.cocktailRepository.deleteCocktailLocal(this.utils.cocktailObjToEntity(cocktail));
        else
            this.mealRepository.deleteCocktailLocal(this.utils.cocktailObjToEntity(cocktail));
    }

    public boolean hasFavorites(String theme) {
        try {return this.getAllCocktailsLocaly(theme).size() != 0;}
        catch(Exception e) {return false;}
    }

}
