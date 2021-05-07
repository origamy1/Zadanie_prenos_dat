package sk.fri.uniza;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sk.fri.uniza.api.WeatherStationService;
import sk.fri.uniza.model.Location;
import sk.fri.uniza.model.Token;
import sk.fri.uniza.model.WeatherData;

import java.io.IOException;
import java.util.Base64;
import java.util.List;


public class IotNode {
    private final Retrofit retrofit;
    private final WeatherStationService weatherStationService;
    private Token aToken;

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
        double pom = 0;
        try {
            final Response<List<WeatherData>> response = historyWeather.execute();
            final List<WeatherData> body = response.body();

            int count = 0;

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
        return pom;
    }


    public Token getToken(){

           Token body = null;
        final Call<Token> token = getWeatherStationService().getToken(
                "Basic " + Base64.getEncoder()
                        .encodeToString("admin:heslo".getBytes()), List.of("all"));

        try {
            final Response<Token> response_Token = token.execute();

            if(response_Token.isSuccessful()){
                body = response_Token.body();
                System.out.println("\ntoto je token:"+body.getToken());
            }else{

                System.out.println("Chyba pri ziskavani tokenu:"+response_Token.errorBody().string());
            }

        } catch (IOException e) {

            e.printStackTrace();

        }
        aToken = body;
        return body;

    }

     public List<Location> getLocationsOfStation(){
         List<Location> body = null;
         final Call<List<Location>> stationLocationsAuth = getWeatherStationService().getStationLocationsAuth(aToken.getToken());

         try {
             final Response<List<Location>> responseLoc = stationLocationsAuth.execute();

             if(responseLoc.isSuccessful()){
                body = responseLoc.body();
                 System.out.println("\n Miesta staníc ->Autorizovaný prístup:\n");
                 System.out.println(body.toString());
             }else{
                 System.out.println(responseLoc.errorBody().string());

             }
         } catch (IOException e) {
             e.printStackTrace();
         }
         return body;
     }

     public WeatherData getAuthWeatherAtStation(String station){
         WeatherData body = null;

         final Call<WeatherData> currentWeatherAuth = getWeatherStationService().getCurrentWeatherAuth(aToken.getToken(), station);
         try {
             final Response<WeatherData> responseWeath= currentWeatherAuth.execute();
             if(responseWeath.isSuccessful()){
                 body = responseWeath.body();
                 System.out.println("\n Aktuálne počasie na stanici-> Autorizovaný prístup:\n");
                 System.out.println(body.toString());
             }else{
                 System.out.println(responseWeath.errorBody().string());
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
         return body;
     }

     public WeatherData getAuthWeatherAtStation(String station, List<String> fields){
         WeatherData body = null;

         final Call<WeatherData> currentWeatherAuth = getWeatherStationService().getCurrentWeatherAuth(aToken.getToken(), station,fields);
         try {
             final Response<WeatherData> responseWeath= currentWeatherAuth.execute();
             if(responseWeath.isSuccessful()){
                 body = responseWeath.body();
                 System.out.println("\n Aktuálne počasie na stanici, vybrane udaje-> Autorizovaný prístup:\n");
                 System.out.println(body.toString());
             }else{
                 System.out.println(responseWeath.errorBody().string());
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
         return body;
     }


    public List<WeatherData> getAuthHistory_Weather(String station, String from, String to){
        List<WeatherData> body = null;

        final Call<List<WeatherData>> historyWeatherAuth = getWeatherStationService().getHistoryWeatherAuth(aToken.getToken(), station, from, to);
        try {
            final Response<List<WeatherData>> responseHist = historyWeatherAuth.execute();
            if(responseHist.isSuccessful()){
                body = responseHist.body();
                System.out.println("\n História počasia na stanici od do -> Autorizovaný prístup:\n");
                System.out.println(body.toString());
            }else{
                System.out.println(responseHist.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }


    public List<WeatherData> getAuthHistory_Weather(String station, String from, String to, List<String> fields){
        List<WeatherData> body = null;

        final Call<List<WeatherData>> historyWeatherAuth = getWeatherStationService().getHistoryWeatherAuth(aToken.getToken(), station, from, to,fields);
        try {
            final Response<List<WeatherData>> responseHist = historyWeatherAuth.execute();
            if(responseHist.isSuccessful()){
                body = responseHist.body();
                System.out.println("\n História počasia od do, vybrané údaje -> Autorizovaný prístup:\n");
                System.out.println(body.toString());
            }else{
                System.out.println(responseHist.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }



    public List<WeatherData> historyNoAuth(String station, String from, String to){

        List<WeatherData> body = null;

        final Call<List<WeatherData>> hist_1 = getWeatherStationService().getHistoryWeather(station,from,to);

        try {
            final Response<List<WeatherData>> response_hist = hist_1.execute();
            if(response_hist.isSuccessful()){
                body = response_hist.body();
                System.out.println(body);
            }else{
                System.out.println("Chyba pri načítanie historických dát:"+response_hist.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;

    }

    public List<WeatherData> historyNoAuth(String station, String from, String to, List<String> fields){

        List<WeatherData> body = null;

        final Call<List<WeatherData>> hist_1 = getWeatherStationService().getHistoryWeather(station,from,to,fields);

        try {
            final Response<List<WeatherData>> response_hist = hist_1.execute();
            if(response_hist.isSuccessful()){
                body = response_hist.body();
                System.out.println(body);
            }else{
                System.out.println("Chyba pri načítanie historických dát:"+response_hist.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;

    }

    public WeatherData curWeather_noAuth(String station){

        WeatherData body = null;
        final Call<WeatherData> weather_asClass =  getWeatherStationService().getCurrentWeather( station);


        try {
            Response<WeatherData> responseAsClass = weather_asClass.execute();
            if(responseAsClass.isSuccessful()){
                body = responseAsClass.body();
                System.out.println(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }


    public List<Location> meteoData_of_stations_noAuth(){

        List<Location> body = null;
        Call<List<Location>> stationLocations = getWeatherStationService().getStationLocations();

        try {
            Response<List<Location>> response = stationLocations.execute();

            if (response.isSuccessful()) { // Dotaz na server bol neúspešný
                //Získanie údajov vo forme Zoznam lokacií
                body = response.body();
                System.out.println(body);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;
    }

//    public Object universal(Object o1){
//
//        try {
//            final Response<WeatherData> responseWeath= o1.execute();
//            if(responseWeath.isSuccessful()){
//                body = responseWeath.body();
//                System.out.println("\n Autorizovaný prístup:\n");
//                System.out.println(body.toString());
//            }else{
//                System.out.println(responseWeath.errorBody().string());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

}



