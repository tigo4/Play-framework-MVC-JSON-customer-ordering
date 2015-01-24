package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

import org.json.*;

import org.joda.time.DateTime;

public class Application extends Controller {

    private boolean debug = false;
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public static void index() {
        if (debug)
            System.out.println("index");
        render();
    }

    public static void addJson(String json) {
        if (debug)
            System.out.println("addJson");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);
            if (debug)
                System.out.println(jsonArray.length());
        } catch (Exception e) {
            json = e.getMessage();
            e = null;
            render(json);
        }
        JSONObject jsonObject = null;
        List list = new ArrayList<DateTime>();
        String dateTime = null;
        DateTime joda = null;
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        if (debug)
            System.out.println("\n\n********** unsorted ****************");
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                dateTime = jsonObject.getString("duetime");
                joda = DateTime.parse(dateTime);
                if (debug)
                    System.out.println(joda);
                // payload
                list.add(joda);
                map.put(joda.toString(), jsonObject);
            } catch (Exception e) {
                json = e.getMessage();
                e = null;
                render(json);
            }
        }

        Collections.sort(list);
        StringBuilder sb = new StringBuilder("");
        if (debug)
            System.out.println("\n\n===== sorted ======================");
        sb.append("[");
        for (int ii=0; ii<list.size(); ii++) {
            if (debug)
                System.out.println(list.get(ii));
            sb.append("{");
            try {
                sb.append("\"id\":\"" + map.get(list.get(ii).toString()).getInt("id") + "\",");
                sb.append("\"name\":\"" + map.get(list.get(ii).toString()).getString("name") + "\",");
                sb.append("\"duetime\":\"" + map.get(list.get(ii).toString()).getString("duetime") + "\",");
                sb.append("\"jointime\":\"" + map.get(list.get(ii).toString()).getString("jointime") + "\"");
            } catch (Exception e) {
                json = e.getMessage();
                e = null;
                render(json);
            }
            sb.append("}");
            if (ii < (list.size()-1))
                sb.append(",");
        }
        sb.append("]");

        json = sb.toString();

        render(json);

    }

}

