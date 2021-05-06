package sk.fri.uniza;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sk.fri.uniza.api.WeatherStationService;
import sk.fri.uniza.model.WeatherData;

import java.io.IOException;
import java.util.List;


public class IotNode {
    private final Retrofit retrofit;
    private final WeatherStationService weatherStationService;

    public IotNode() {

        retrofit = new Retrofit.Builder()
                // Url adresa kde je umietnená WeatherStation služba
                .baseUrl("http://localhost:9000/")
                // Na konvertovanie JSON objektu na java POJO použijeme
                // Jackson knižnicu
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        // Vytvorenie inštancie komunikačného rozhrania
        weatherStationService = retrofit.create(WeatherStationService.class);

    }

    public WeatherStationService getWeatherStationService() {
        return weatherStationService;
    }

    public double getAverageTemperature(String station,String from, String to){
        final Call<List<WeatherData>> historyWeather = weatherStationService.getHistoryWeather(station, from, to, List.of("airTemperature"));
        try {
            final Response<List<WeatherData>> response = historyWeather.execute();
            final List<WeatherData> body = response.body();

            int count = 0;
            double pom = 0;
            for (WeatherData var : body)
            {
                pom = pom + var.getAirTemperature();
                System.out.println(var.getAirTemperature());
                count++;
            }
            pom = pom/count;
            System.out.println("Average temperature between-"+from+ " and-"+to+"is:"+pom);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
}