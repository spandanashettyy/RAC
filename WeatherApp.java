import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherApp {

    // Your OpenWeatherMap API key (replace with your own key)
    private static final String API_KEY = "your_api_key_here";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        // Example city to fetch weather data
        String city = "London";

        try {
            // Fetch weather data
            String response = fetchWeatherData(city);
            
            // Parse and display the data
            if (response != null) {
                displayWeatherData(response);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to fetch weather data from the API
    private static String fetchWeatherData(String city) throws Exception {
        String urlString = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) { // HTTP OK
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } else {
            System.out.println("Error: Unable to fetch weather data. HTTP response code: " + responseCode);
            return null;
        }
    }

    // Method to parse and display weather data
    private static void displayWeatherData(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

        // Extract and display relevant data
        String cityName = jsonObject.get("name").getAsString();
        JsonObject main = jsonObject.getAsJsonObject("main");
        double temperature = main.get("temp").getAsDouble();
        double feelsLike = main.get("feels_like").getAsDouble();
        int humidity = main.get("humidity").getAsInt();
        JsonObject weather = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject();
        String description = weather.get("description").getAsString();

        System.out.println("Weather Data for " + cityName + ":");
        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Feels Like: " + feelsLike + "°C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Description: " + description);
    }
}