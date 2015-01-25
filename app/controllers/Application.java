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

public class Application extends Controller {

    public static void index() {
        Logger.debug("index");
        render();
    }

    public static void sortJson(final String json) {

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

    public static void sortJsonBlock(final String json) {
        String result = process(json);
        renderArgs.put("json", result);
        renderTemplate("Application/sortJson.html");
    }

    private static String process(String json) {
        Logger.debug("sortJson");
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
        List list = new ArrayList<DateTime>();
        String dateTime = null;
        DateTime joda = null;
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        Logger.debug("\n\n********** unsorted ****************");
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                dateTime = jsonObject.getString("duetime");
                joda = DateTime.parse(dateTime);
                Logger.debug(joda+"");
                // payload
                list.add(joda);
                map.put(joda.toString(), jsonObject);
            } catch (Exception e) {
                json = e.getMessage();
                e = null;
                Logger.error(json);
                return json;
            }
        }

        Collections.sort(list);
        StringBuilder sb = new StringBuilder("");
        Logger.debug("\n\n===== sorted ======================");
        sb.append("[");
        for (int ii=0; ii<list.size(); ii++) {
            Logger.debug(list.get(ii)+"");
            sb.append("{");
            try {
                sb.append("\"id\":\"" + map.get(list.get(ii).toString()).getInt("id") + "\",");
                sb.append("\"name\":\"" + map.get(list.get(ii).toString()).getString("name") + "\",");
                sb.append("\"duetime\":\"" + map.get(list.get(ii).toString()).getString("duetime") + "\",");
                sb.append("\"jointime\":\"" + map.get(list.get(ii).toString()).getString("jointime") + "\"");
            } catch (Exception e) {
                json = e.getMessage();
                e = null;
                Logger.error(json);
                return json;
            }
            sb.append("}");
            if (ii < (list.size()-1))
                sb.append(",");
        }
        sb.append("]");

        json = sb.toString();
    
        return json;

    }

}

