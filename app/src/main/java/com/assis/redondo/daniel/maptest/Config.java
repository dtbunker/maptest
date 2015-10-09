package com.assis.redondo.daniel.maptest;

/**
 * Created by DT on 10/8/15.
 */
public class Config {

    public static final String BASE_API_METHOD_URL = "http://api.99taxis.com";
    public static final String API_METHOD_GET_LAST_LOCATIONS = "/lastLocations";

    public static final String RELOAD_LOCATIONS = "ReloadLocations";


    //api.99taxis.com/lastLocations?sw=-23.612474,-46.702746&ne=-23.589548,-46.673392

    public class RequestKeys {
        public static final String EXTREME_SOUTH_WEST = "sw";
        public static final String EXTREME_NORTH_EAST = "ne";
        public static final String AND = "&";
        public static final String EQUALS = "=";
        public static final String QUERY_STRING = "?";
        public static final String COMMA = ",";

}

}
