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

import org.apache.commons.lang3.exception.ExceptionUtils;

public class Application extends Controller {

    public static void index() {
        Logger.debug("index");
        render();
    }

    public static void sortJson(final String body) {

        /***** non-blocking *****/
        /*
           the http request will stay connected (blocked) but
           the request execution will be automaticaly 
           popped out of the thread to free server resources (non-block)
           and accept new requests
        */
        Promise<String> promise = new Job() {
            public String doJobWithResult() throws Exception {
                return process(body);
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
            Logger.error(json + ExceptionUtils.getStackTrace(e), e);
            e = null;
            return json;
        }

        JSONObject jsonObject = null;
        List list = new ArrayList<JSONObject>();

        Logger.debug("\n\n********** unsorted ****************");
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                //dateTime = jsonObject.getString("duetime");
                //joda = DateTime.parse(dateTime);
                Logger.debug(jsonObject+"");
                list.add(jsonObject);
            } catch (Exception e) {
                json = e.getMessage();
                Logger.error(json + ExceptionUtils.getStackTrace(e), e);
                e = null;
                return json;
            }
        }

        Collections.sort(list, new Application().new CustomComparator());

        StringBuilder sb = new StringBuilder("");
        Logger.debug("\n\n===== sorted ======================");
        sb.append("[");
        for (int ii=0; ii<list.size(); ii++) {
            Logger.debug(list.get(ii)+"");
            sb.append(list.get(ii)+"");
            if (ii < (list.size()-1))
                sb.append(",");
        }
        sb.append("]");

        json = sb.toString();
    
        Logger.debug(json);

        return json;

    }

    public class CustomComparator implements Comparator<JSONObject> {

        @Override
        public int compare(JSONObject a, JSONObject b) {
            try {
                DateTime aDT = DateTime.parse(a.getString("duetime")); 
                DateTime bDT = DateTime.parse(b.getString("duetime")); 
                return aDT.isAfter(bDT) ? 1 : aDT.isBefore(bDT) ? -1 : 0;
            } catch (Exception e) {
                Logger.error(e.getMessage() + " " + ExceptionUtils.getStackTrace(e), e);
                e = null;
            }
            return 0;
        }

    }


}

