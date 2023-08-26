package com.choudhary.mausamapp.Models;

import java.util.List;

public class Food {

    private List<Meals> meals;

    public Food(List<Meals> foodlist) {
        this.meals = foodlist;
    }

    public List<Meals> getFoodlist() {
        return meals;
    }

    public void setFoodlist(List<Meals> foodlist) {
        this.meals = foodlist;
    }
}
