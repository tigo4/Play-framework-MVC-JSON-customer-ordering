package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

import org.json.*;

import org.joda.time.DateTime;

public class Application extends Controller {

    public static void index() {
        System.out.println("hello1");
        render();
    }

    public static void addJson(String json) {
        System.out.println("hello2");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);
            System.out.println(jsonArray.length());
        } catch (Exception e) {
            json = e.getMessage();
            e = null;
            render(json);
        }
        JSONObject jsonObject = null;
        List list = new ArrayList<DateTime>();
        String dateTime = null;
        System.out.println("\n\n********** unsorted ****************");
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                dateTime = jsonObject.getString("duetime");
                DateTime joda = DateTime.parse(dateTime);
                System.out.println(joda);
                list.add(joda);
            } catch (Exception e) {
                json = e.getMessage();
                e = null;
                render(json);
            }
        }

        Collections.sort(list);
        System.out.println("\n\n===== sorted ======================");
        for (int ii=0; ii<list.size(); ii++) {
            System.out.println(list.get(ii));
        }

        json = jsonArray.toString();
        //System.out.println(json);
        render(json);

    }

}

