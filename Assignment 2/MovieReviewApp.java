import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class MovieReviewApp {
    private static final String API_KEY = "8801ba0f";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Movie Review Console App!");
        while (true) {
            System.out.println("\nEnter a movie title to search (or type 'exit' to quit): ");
            String movieTitle = scanner.nextLine();

            if (movieTitle.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            if (!movieTitle.trim().isEmpty()) {
                fetchMovieDetails(movieTitle);
            } else {
                System.out.println("Please enter a valid movie title.");
            }
        }

        scanner.close();
    }

    private static void fetchMovieDetails(String movieTitle) {
        try {
            // Build the API request URL
            String url = "https://www.omdbapi.com/?t=" + movieTitle.replace(" ", "+") + "&apikey=" + API_KEY;

            // Create HTTP connection
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("Response") && jsonResponse.getString("Response").equals("True")) {
                // Extract details
                String title = jsonResponse.getString("Title");
                String year = jsonResponse.getString("Year");
                String genre = jsonResponse.getString("Genre");
                String director = jsonResponse.getString("Director");
                JSONArray ratings = jsonResponse.optJSONArray("Ratings");
                String posterUrl = jsonResponse.getString("Poster");

                // Display details
                System.out.println("\n--- Movie Details ---");
                System.out.println("Title: " + title);
                System.out.println("Year: " + year);
                System.out.println("Genre: " + genre);
                System.out.println("Director: " + director);

                // Display ratings
                if (ratings != null && ratings.length() > 0) {
                    System.out.println("Ratings:");
                    for (int i = 0; i < ratings.length(); i++) {
                        JSONObject rating = ratings.getJSONObject(i);
                        System.out.println("  - " + rating.getString("Source") + ": " + rating.getString("Value"));
                    }
                } else {
                    System.out.println("Ratings: No ratings available.");
                }

                // Display poster URL
                if (!posterUrl.equals("N/A")) {
                    System.out.println("Poster URL: " + posterUrl);
                } else {
                    System.out.println("Poster URL: No poster available.");
                }
            } else {
                System.out.println("Movie not found. Please try again with a different title.");
            }

        } catch (Exception e) {
            System.out.println("Error fetching movie details: " + e.getMessage());
        }
    }
}
