package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        System.out.println("hello1");
        render();
    }

    public static void addJson(String json) {
        System.out.println("hello2");
        System.out.println(json);
        render(json);
    }

}
