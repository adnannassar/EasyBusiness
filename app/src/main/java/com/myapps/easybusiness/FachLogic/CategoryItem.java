package com.myapps.easybusiness.FachLogic;

import  androidx.annotation.NonNull;

public class CategoryItem {
    private  String categoryName;
    private  int categoryImageFlag;

    public CategoryItem(String categoryName, int categoryImageFlag) {
        this.categoryName = categoryName;
        this.categoryImageFlag = categoryImageFlag;
    }

    public int getCategoryImageFlag() {
        return categoryImageFlag;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @NonNull
    @Override
    public String toString() {
        return categoryName;
    }
}
