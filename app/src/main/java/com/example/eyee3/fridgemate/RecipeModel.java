package com.example.eyee3.fridgemate;

public class RecipeModel {
    String cardName;
    //int imageResourceId;
    String ingredientsList;
    String link;
    int isfav;
    int isturned;

    public int getIsturned() {
        return isturned;
    }

    public void setIsturned(int isturned) {
        this.isturned = isturned;
    }

    public int getIsfav() {
        return isfav;
    }

    public void setIsfav(int isfav) {
        this.isfav = isfav;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getIngredientsList() { return ingredientsList; }

    public void setIngredientsList(String ingredientsList) { this.ingredientsList = ingredientsList; }

    public String getLink() { return link; }

    public void setLink(String link) {
        this.link = link;
    }
}
