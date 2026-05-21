package src.main.java.server;

import java.util.Map;

public class RouteResolver {
    public static String resolve(String route, Map<String, String> routes){
        return routes.get(route);
    }
}
