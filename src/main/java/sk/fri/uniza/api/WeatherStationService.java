package sk.fri.uniza.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import retrofit2.Call;
import retrofit2.http.*;
import sk.fri.uniza.model.Data;
import sk.fri.uniza.model.Location;
import sk.fri.uniza.model.Token;
import sk.fri.uniza.model.WeatherData;

import java.util.List;
import java.util.Map;


public interface WeatherStationService {

    // ... getCurrentWeatherAsMap(station);
    @GET("/weather/{station}/current")
    Call<Map<String, String>> getCurrentWeatherAsMap(
            @Path("station") String station);

    // ... getCurrentWeatherAsMap(station, fields);
    @GET("/weather/{station}/current")
    Call<Map<String, String>> getCurrentWeatherAsMap(
         @Path("station") String station,@Query("fields") List<String> fields);

    // ... getStationLocations();
    @GET("/weather/locations")
    Call<List<Location>> getStationLocations();

    // ... getCurrentWeather(station);
    @GET("/weather/{station}/current")
    Call<WeatherData> getCurrentWeather(
            @Path("station") String station);

    // ... getCurrentWeather(station, fields);
    @GET("/weather/{station}/current")
    Call<WeatherData> getCurrentWeather(
            @Path("station") String station,
            @Query("fields") List<String> fields);

    //...getHistoryWeather(station, from, to);
    @GET("/weather/{station}/history")
    Call<List<WeatherData>> getHistoryWeather(
            @Path("station") String station,
            @Query("from") String from,
            @Query("to")  String to);


    // ... getHistoryWeather(  station, from, to, fields);
    @GET("/weather/{station}/history")
    Call<List<WeatherData>> getHistoryWeather(
            @Path("station") String station,
            @Query("from") String from,
            @Query("to")  String to,
            @Query("fields") List<String> fields
            );

    // ... getToken(authorization, claims);

    @FormUrlEncoded
    @POST("/apikey/createjwt")
    Call<Token> getToken(
            @Header("Authorization") String authorization,
            @Field("claims") List<String> claims);


    ///////////////////////////////////////////////////////////////////////////////////////// AUTH
    ///////////////////////////////////////////////////////////////////////////////////////// AUTH
    ///////////////////////////////////////////////////////////////////////////////////////// AUTH
    ///////////////////////////////////////////////////////////////////////////////////////// AUTH


    // ... getStationLocationsAuth(authorization);
    @GET("/weatherAuth/locations")
    Call<List<Location>> getStationLocationsAuth(
            @Header("Authorization") String authorization);


    // ... getCurrentWeatherAuth(authorization, station);
    @GET("/weatherAuth/{station}/current")
    Call<WeatherData> getCurrentWeatherAuth(
            @Header("Authorization") String authorization,
            @Path("station") String station);

    // ... getCurrentWeatherAuth(authorization, station, fields);
    @GET("/weatherAuth/{station}/current")
    Call<WeatherData> getCurrentWeatherAuth(
            @Header("Authorization") String authorization,
            @Path("station") String station,
            @Query("fields") List<String> fields);


    // ... getHistoryWeatherAuth(authorization, station, from, to);
    @GET("/weather/{station}/history")
    Call<List<WeatherData>> getHistoryWeatherAuth(
            @Header("Authorization") String authorization,
            @Path("station") String station,
            @Query("from") String from,
            @Query("to")  String to);


    // ... getHistoryWeatherAuth(authorization, station, from, to, fields);
    @GET("/weather/{station}/history")
    Call<List<WeatherData>> getHistoryWeatherAuth(
            @Header("Authorization") String authorization,
            @Path("station") String station,
            @Query("from") String from,
            @Query("to")  String to,
            @Query("fields") List<String> fields
    );

    // ... getCurrentWeatherAsMap(station);
    @POST("/household/{householdID}/{fieldID}")
    Call<Data> sendHouseHoldData(
            @Path("householdID") long householdID,
            @Path("fieldID") String fieldID,
            @Body Data data
            );

}
