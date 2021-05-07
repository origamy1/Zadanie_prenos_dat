package sk.fri.uniza;

import retrofit2.Call;
import retrofit2.Response;
import sk.fri.uniza.model.Location;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import sk.fri.uniza.model.Token;
import sk.fri.uniza.model.WeatherData;


/**
 * Hello IoT!
 */
public class App {
    public static void main(String[] args) {
        IotNode iotNode = new IotNode();
        // Vytvorenie požiadavky na získanie údajov o aktuálnom počasí z
        // meteo stanice s ID: station_1
        Call<Map<String, String>> currentWeather =
                iotNode.getWeatherStationService()
                        .getCurrentWeatherAsMap("station_1",
                                List.of("time", "date",
                                        "airTemperature", "rainIntensity" ));



        try {
            // Odoslanie požiadavky na server pomocou REST rozhrania
            Response<Map<String, String>> response = currentWeather.execute();

            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
                //Získanie údajov vo forme Mapy stringov
                Map<String, String> body = response.body();
                System.out.println(body);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        // Vytvorenie požiadavky na získanie údajov o všetkých meteo staniciach
        Call<List<Location>> stationLocations =
                iotNode.getWeatherStationService().getStationLocations();


        try {
            Response<List<Location>> response = stationLocations.execute();

            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
                //Získanie údajov vo forme Zoznam lokacií
                List<Location> body = response.body();

                System.out.println(body);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        final Call<WeatherData> weather_asClass = iotNode.getWeatherStationService().getCurrentWeather("station_1");


        try {
            Response<WeatherData> responseAsClass = weather_asClass.execute();
            if(responseAsClass.isSuccessful()){
                final WeatherData body = responseAsClass.body();
                System.out.println(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Call<List<WeatherData>> hist_1 = iotNode.getWeatherStationService().getHistoryWeather("station_1","3/05/2021 15:00", "6/05/2021 15:00");

        try {
            final Response<List<WeatherData>> response_hist = hist_1.execute();
            if(response_hist.isSuccessful()){
                final List<WeatherData> body = response_hist.body();
                System.out.println(body);
            }else{
                System.out.println("Chyba pri načítanie historických dát:"+response_hist.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        iotNode.getAverageTemperature("station_1","6/05/2021 14:00", "6/05/2021 16:00");

        System.out.println("\ntoken je:"+iotNode.getToken().getToken());

        iotNode.getLocationsOfStation();
    }
}