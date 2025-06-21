package com.s23010535.govisaviya;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    @SerializedName("main")
    public Main main;

    @SerializedName("weather")
    public List<Weather> weather;

    @SerializedName("name")
    public String name;

    public class Main {
        @SerializedName("temp")
        public double temp;
    }

    public class Weather {
        @SerializedName("description")
        public String description;
    }
}
