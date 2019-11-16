package com.myapps.easybusiness.Gui.User;

public class profileItem {
    private String name , descreption;
    private int imageFlag;

    public profileItem(String name, String descreption, int imageFlag) {
        this.name = name;
        this.descreption = descreption;
        this.imageFlag = imageFlag;
    }

    public int getImageFlag() {
        return imageFlag;
    }

    public String getDescreption() {
        return descreption;
    }

    public String getName() {
        return name;
    }
}
