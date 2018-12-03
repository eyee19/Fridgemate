package com.example.eyee3.fridgemate;

import android.graphics.Bitmap;

public class RecipeModel { //Class for structuring a recipe item
    String cardName;
    String ingredientsList;
    String link;
    Bitmap pictureLink;

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

    public Bitmap getPictureLink() { return pictureLink; }

    public void setPictureLink(Bitmap pictureLink) {
        this.pictureLink = pictureLink;
    }
}
