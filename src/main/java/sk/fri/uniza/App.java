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



//        try {
//            // Odoslanie požiadavky na server pomocou REST rozhrania
//            Response<Map<String, String>> response = currentWeather.execute();
//
//            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
//                //Získanie údajov vo forme Mapy stringov
//                Map<String, String> body = response.body();
//                System.out.println(body);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        iotNode.meteoData_of_stations_noAuth();
        iotNode.curWeather_noAuth("station_3");
        iotNode.historyNoAuth("station_1","6/05/2021 14:00", "6/05/2021 16:00");
        iotNode.getAverageTemperature("station_1","6/05/2021 14:00", "6/05/2021 16:00");
        iotNode.getToken(); //  získa sa zároveň nastaví.
        iotNode.getLocationsOfStation();
        iotNode.getAuthWeatherAtStation("station_2");
        iotNode.getAuthWeatherAtStation("station_2",List.of("time","date"));
        iotNode.getAuthHistory_Weather("station_2","3/05/2021 15:00", "6/05/2021 15:00");
        iotNode.getAuthHistory_Weather("station_2","3/05/2021 15:00", "6/05/2021 15:00",List.of("time", "date"));
    }
}