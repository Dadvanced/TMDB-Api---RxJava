import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TmdbApiService {
    private static final String API_BASE_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY = "censored";

    private final OkHttpClient client;

    public TmdbApiService() {
        this.client = new OkHttpClient();
    }

    public Observable<List<Movie>> searchMovies(String query) {
        return Observable.create((ObservableOnSubscribe<List<Movie>>) emitter -> {
            try {
                List<Movie> movies = performSearch(query);
                emitter.onNext(movies);
                emitter.onComplete();
            } catch (IOException e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }


    private List<Movie> performSearch(String query) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_BASE_URL + "/search/movie").newBuilder();
        urlBuilder.addQueryParameter("api_key", API_KEY);
        urlBuilder.addQueryParameter("query", query);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray resultsArray = jsonResponse.getJSONArray("results");

            List<Movie> movies = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject movieObject = resultsArray.getJSONObject(i);
                String title = movieObject.getString("title");
                String year = movieObject.getString("release_date");
                int imdbId = movieObject.getInt("id");

                String moviePosterUrl = "";
                if (movieObject.has("poster_path") && !movieObject.isNull("poster_path")) {
                    String posterPath = movieObject.getString("poster_path");
                    moviePosterUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
                }

                Movie movie = new Movie(title, year, imdbId, moviePosterUrl);
                movies.add(movie);
            }
            return movies;
        }
    }
}
