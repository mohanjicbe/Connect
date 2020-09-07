package com.orane.icliniq.network;


import com.orane.icliniq.Model.Item;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArticleListParser {

    public String uname, pass;
    Item objItem;
    List<Item> listArray;
    public String url, spec_text;

    public List<Item> getData(String params) {

        try {

            listArray = new ArrayList<Item>();

            url = params;
            System.out.println("Searching url-----------------" + url);

            JSONParser jParser = new JSONParser();
            JSONArray jsonarr = jParser.getSearch(url);

            for (int i = 0; i < jsonarr.length(); i++) {
                JSONObject json_arrayobj = jsonarr.getJSONObject(i);

                objItem = new Item();
                objItem.setStitle(json_arrayobj.getString("title"));

                if (json_arrayobj.has("description")) {
                    objItem.setSdesc(json_arrayobj.getString("description"));
                } else {
                    objItem.setSdesc(json_arrayobj.getString("abstract"));
                }

                objItem.setSurl(json_arrayobj.getString("url"));
                objItem.setSphoto_url(json_arrayobj.getString("photo_url"));

                //--------------- Speciality List------------
                try {
                    JSONArray jsonArray = new JSONArray(json_arrayobj.getString("speciality"));
                    String[] strArr = new String[jsonArray.length()];

                    for (int sp = 0; sp < jsonArray.length(); sp++) {
                        strArr[sp] = jsonArray.getString(sp);
                    }

                    spec_text = Arrays.toString(strArr);
                    spec_text = spec_text.replaceAll("[\\[\\](){}]", "");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                objItem.setSspeciality(spec_text);
                //--------------- Speciality List------------

                listArray.add(objItem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listArray;
    }
}
