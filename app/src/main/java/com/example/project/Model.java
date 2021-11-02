package com.example.project;

public class Model {
    private static Model __instance = null;

    private Model() {
    }

    public static Model instance() {
        if (__instance == null) {
            __instance = new Model();
        }
        return __instance;
    }

    private boolean indications = true;
    private Object image = null;

    public void setIndications(boolean truefalse) {
        this.indications = truefalse;
    }

    public boolean getIndications() {
        return indications;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public Object getImage() {
        return image;
    }
}
