package sk.fri.uniza;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sk.fri.uniza.api.WeatherStationService;
import sk.fri.uniza.model.Data;
import sk.fri.uniza.model.Location;
import sk.fri.uniza.model.Token;
import sk.fri.uniza.model.WeatherData;

import java.io.IOException;
import java.util.Base64;
import java.util.List;


public class IotNode {
    private final Retrofit retrofit;
    private final Retrofit retrofitHouseHold;
    private final WeatherStationService weatherStationService;
    private final WeatherStationService houseHoldService;
    private Token aToken;

    public IotNode() {

        retrofit = new Retrofit.Builder()
                // Url adresa kde je umietnená WeatherStation služba
                .baseUrl("http://localhost:9000/")
                // Na konvertovanie JSON objektu na java POJO použijeme
                // Jackson knižnicu
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        retrofitHouseHold = new Retrofit.Builder()
                // Url adresa kde je umietnená WeatherStation služba
                .baseUrl("http://localhost:8080/")
                // Na konvertovanie JSON objektu na java POJO použijeme
                // Jackson knižnicu
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        // Vytvorenie inštancie komunikačného rozhrania
        weatherStationService = retrofit.create(WeatherStationService.class);
        houseHoldService = retrofitHouseHold.create(WeatherStationService.class);
    }

    public WeatherStationService getWeatherStationService() {
        return weatherStationService;
    }

    public WeatherStationService getHouseHoldService(){
        return houseHoldService;
    }

    public double getAverageTemperature(String station,String from, String to){

            final List<WeatherData> body = historyNoAuth(station,from,to,List.of("airTemperature"));
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

        return pom;
    }


    public Token getToken(){

        Token body = null;
        final Call<Token> token = getWeatherStationService().getToken(
                "Basic " + Base64.getEncoder()
                        .encodeToString("admin:heslo".getBytes()), List.of("all"));

        Object o = send_Request_to_Server(token);
        body = (Token) o;
        aToken = body;
        return body;

    }

    public void  sendField(int fieldIDtoResent,long householdIDtoSend){
        final WeatherData data_stat1 = getAuthWeatherAtStation("station_1");
        String date = data_stat1.getDate();
        final String time = data_stat1.getTime();

        final Double  airTemperature = data_stat1.getAirTemperature();
        final Integer batteryLife = data_stat1.getBatteryLife();
        final Double windSpeed = data_stat1.getWindSpeed();


        date = date.replace(".","/");
        String dateToSend = date+" "+time;

        Data data = new Data();
        data.setDateTime(dateToSend);


        System.out.println(data);

        String whatData = null;
        switch (fieldIDtoResent){
            case 1 :
                data.setValue(String.valueOf(airTemperature));
                whatData = "airTemp";
                data.setType("double");
                break;
            case 2 :
                data.setValue(String.valueOf(batteryLife));
                whatData = "bateryLife";
                data.setType("integer");
                break;
            case 3 :
                data.setValue(String.valueOf(windSpeed));
                whatData = "windSpeed";
                data.setType("double");
                break;
            default:
                data.setValue(String.valueOf(airTemperature));
                whatData = "airTemp";
                data.setType("double");
                break;

        }
        final Call<Data> mapCall = getHouseHoldService().sendHouseHoldData(householdIDtoSend, whatData, data);// dateToSend, String.valueOf(airTemperature), "double"


        try {
            final Response<Data> response = mapCall.execute();
            if(response.isSuccessful()) {
                System.out.println("\nIS HERE<<\n");
                Data body = response.body();
                System.out.println(body);
            }else{
                System.out.println("Požiadavka nebola úspešná");
                System.out.println(response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

     public List<Location> getLocationsOfStation(){
         List<Location> body = null;
         Call<List<Location>> stationLocationsAuth = getWeatherStationService().getStationLocationsAuth(aToken.getToken());

         Object o = send_Request_to_server_Auth(stationLocationsAuth);
         body = (List<Location>) o;

         return body;
     }

     public WeatherData getAuthWeatherAtStation(String station){
         WeatherData body = null;

         Call<WeatherData> currentWeatherAuth = getWeatherStationService().getCurrentWeatherAuth(aToken.getToken(), station);
         Object o = send_Request_to_server_Auth(currentWeatherAuth);
         body = (WeatherData) o;
         return body;
     }

     public WeatherData getAuthWeatherAtStation(String station, List<String> fields){
         WeatherData body = null;

         Call<WeatherData> currentWeatherAuth = getWeatherStationService().getCurrentWeatherAuth(aToken.getToken(), station,fields);
         Object o = send_Request_to_server_Auth(currentWeatherAuth);
         body = (WeatherData) o;
         return body;
     }


    public List<WeatherData> getAuthHistory_Weather(String station, String from, String to){
        List<WeatherData> body = null;

        Call<List<WeatherData>> historyWeatherAuth = getWeatherStationService().getHistoryWeatherAuth(aToken.getToken(), station, from, to);
        Object o = send_Request_to_server_Auth(historyWeatherAuth);
        body = (List<WeatherData>) o;
        return body;
    }


    public List<WeatherData> getAuthHistory_Weather(String station, String from, String to, List<String> fields){
        List<WeatherData> body = null;

        Call<List<WeatherData>> historyWeatherAuth = getWeatherStationService().getHistoryWeatherAuth(aToken.getToken(), station, from, to,fields);
        Object o = send_Request_to_server_Auth(historyWeatherAuth);
        body = (List<WeatherData>) o;

        return body;
    }


    public List<WeatherData> historyNoAuth(String station, String from, String to){

        List<WeatherData> body = null;

        Call<List<WeatherData>> hist_1 = getWeatherStationService().getHistoryWeather(station,from,to);
        Object oBody = send_Request_to_server_NoAuth(hist_1);
        body = (List<WeatherData>)oBody;


        return body;

    }

    public List<WeatherData> historyNoAuth(String station, String from, String to, List<String> fields){

        List<WeatherData> body = null;

        Call<List<WeatherData>> hist_1 = getWeatherStationService().getHistoryWeather(station,from,to,fields);
        Object oBody = send_Request_to_server_NoAuth(hist_1);
        body = (List<WeatherData>)oBody;

        return body;

    }

    public WeatherData curWeather_noAuth(String station){

        WeatherData body = null;
        Call<WeatherData> weather_asClass =  getWeatherStationService().getCurrentWeather( station);
        Object oBody = send_Request_to_server_NoAuth(weather_asClass);
        body = (WeatherData)oBody;

        return body;
    }


    public List<Location> meteoData_of_stations_noAuth(){

        List<Location> body = null;
        Call<List<Location>> stationLocations = getWeatherStationService().getStationLocations();
        Object oBody = send_Request_to_server_NoAuth(stationLocations);
        body = (List<Location>)oBody;

        return body;
    }


    public Object send_Request_to_server_Auth(Call obj){
        System.out.println("\n Prístup s autorizáciou!:\n");
        return send_Request_to_Server(obj);
    }


    public Object send_Request_to_server_NoAuth(Call obj){
        System.out.println("\n Bez autorizácie prístup!!:\n");
        return send_Request_to_Server(obj);
    }

    public Object send_Request_to_Server(Call obj){
        Response response = null;
        Object body = null;
        try {
            response= obj.execute();
            if(response.isSuccessful()){
                body = response.body();

                if(body.getClass() != Token.class) { // ak je to iná klása ako Token tak sa vypíše jeho obsah cez toString()
                    System.out.println(body.toString());
                }else{ // ak je to klása Token vypíše sa cez metódu getToken()  využítá reflexia
                    System.out.println("\n\nZískaný Token:");
                    System.out.println(((Token) body).getToken());
                }

            }else{
                System.out.println(response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

}



