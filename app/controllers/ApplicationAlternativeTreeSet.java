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

public class ApplicationAlternativeTreeSet extends Controller {

    public static void sortJsonAlternativeTreeSet(final String json) {

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
        Logger.debug("sortJsonAlternativeTreeSet");
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
        Set<DateTime> set = new TreeSet<DateTime>();
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
                //list.add(joda);
                set.add(joda);
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

        Iterator hashSetIterator = set.iterator();
        int ii = 0;
        while(hashSetIterator.hasNext()){
            joda = (DateTime) hashSetIterator.next();
            Logger.debug(joda+"");
            sb.append("{");
            try {
                sb.append("\"id\":\"" + map.get(joda.toString()).getInt("id") + "\",");
                sb.append("\"name\":\"" + map.get(joda.toString()).getString("name") + "\",");
                sb.append("\"duetime\":\"" + map.get(joda.toString()).getString("duetime") + "\",");
                sb.append("\"jointime\":\"" + map.get(joda.toString()).getString("jointime") + "\"");
            } catch (Exception e) {
                json = e.getMessage();
                e = null;
                Logger.error(json);
                return json;
            }
            sb.append("}");
            if (hashSetIterator.hasNext())
                sb.append(",");
            else
                break;

            ii++;
        }
        sb.append("]");

        json = sb.toString();

        return json;

    }

}

