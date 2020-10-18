package com.kino.kreports.utils.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class GsonUtil {

    public static final GsonBuilder GSON_BUILDER = new GsonBuilder().setPrettyPrinting();
    public static Gson GSON = GSON_BUILDER.create();

    public static final JsonParser PARSER = new JsonParser();

    public static void createGSON(){
        GSON = GSON_BUILDER.create();
    }
}
