package com.example.tac_main_project.models.objects;


public class Item {

    protected String description;
    protected String name;
    protected String viewUrl;
    protected String theme;
    protected String[] ingredients;
    protected String[] measures;
    protected Boolean isFav;
    protected byte[] byteArray;
    protected String i;

    public Item(String name, String description, String viewUrl, String[] ingredients, String[] measures, String theme, Boolean isFav, byte[] byteArray, String id) {
        this.description = description;
        this.name = name;
        this.viewUrl = viewUrl;
        this.ingredients = ingredients;
        this.measures = measures;
        this.theme = theme;
        this.isFav = isFav;
        this.byteArray = byteArray;
        this.i = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public String[] getIngredients() { return ingredients;}

    public void setIngredients(String[] ingredients) { this.ingredients = ingredients; }

    public String[] getMeasures() { return measures;}

    public void setMeasures(String[] measures) { this.measures = measures; }

    public String getTheme() { return theme;}

    public void setTheme(String theme) { this.theme = theme; }

    public Boolean getFav() { return isFav; }

    public void setFav(Boolean fav) { this.isFav = fav; }

    public byte[] getByteArray() { return byteArray; }

    public void setByteArray(byte[] bytesArray) { this.byteArray = bytesArray; }

    public String getId() { return i;}

    public void setId(String id) { this.i = id; }

    @Override
    public String toString() {
        return " (Object: " + this.name + ")";
    }

}
