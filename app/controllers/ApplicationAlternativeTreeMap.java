package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

import org.json.*;

import org.joda.time.DateTime;
import play.libs.*;
import play.libs.F.*;
import play.jobs.*;

public class ApplicationAlternativeTreeMap extends Controller {

    public static void sortJsonAlternativeTreeMap(final String json) {

        /***** non-blocking *****/
        /*
           the http request will stay connected (blocked) but
           the request execution will be automaticaly
           popped out of the thread to free server resources (non-block)
           and accept new requests
        */
        Promise<String> promise = new Job() {
            public String doJobWithResult() throws Exception {
                return process(json);
            }
        }.now();
        await(promise, new F.Action<String>() {
            public void invoke(String result) {
                renderArgs.put("json", result);
                renderTemplate("Application/sortJson.html");
            }
        });

    }

    private static String process(String json) {
        Logger.debug("sortJsonAlternativeTreeMap");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);
            Logger.debug(jsonArray.length()+"");
        } catch (Exception e) {
            json = e.getMessage();
            e = null;
            Logger.error(json);
            return json;
        }
        JSONObject jsonObject = null;
        // List list = new ArrayList<DateTime>();
        // Sorted by default
        // not needed now Set<DateTime> set = new TreeSet<DateTime>();
        String dateTime = null;
        DateTime joda = null;
        TreeMap<String, JSONObject> map = new TreeMap<String, JSONObject>();
        Logger.debug("\n\n********** unsorted ****************");
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                dateTime = jsonObject.getString("duetime");
                joda = DateTime.parse(dateTime);
                Logger.debug(joda+"");
                // payload
                //list.add(joda);
                // not needed now since TreeMap is sorted set.add(joda);
                map.put(joda.toString(), jsonObject);
            } catch (Exception e) {
                json = e.getMessage();
                e = null;
                Logger.error(json);
                return json;
            }
        }

        // Set is ordered by default
        //Collections.sort(list);
        StringBuilder sb = new StringBuilder("");
        Logger.debug("\n\n===== sorted ======================");
        sb.append("[");

        for (String mapDateTime : map.keySet()){
            Logger.debug(mapDateTime+"");
            sb.append("{");
            try {
                sb.append("\"id\":\"" + map.get(mapDateTime).getInt("id") + "\",");
                sb.append("\"name\":\"" + map.get(mapDateTime).getString("name") + "\",");
                sb.append("\"duetime\":\"" + map.get(mapDateTime.toString()).getString("duetime") + "\",");
                sb.append("\"jointime\":\"" + map.get(mapDateTime.toString()).getString("jointime") + "\"");
            } catch (Exception e) {
                json = e.getMessage();
                e = null;
                Logger.error(json);
                return json;
            }
            sb.append("}");
            if (!mapDateTime.equals(map.lastKey()))
                sb.append(",");
            else
                break;

        }
        sb.append("]");

        json = sb.toString();

        return json;

    }

}

