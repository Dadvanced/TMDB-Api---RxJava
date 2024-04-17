public class Movie {
    private final String title;
    private final String year;
    private final int imdbId;
    private final String moviePosterUrl;

    public Movie(String title, String year, int imdbId, String moviePosterUrl) {
        this.title = title;
        this.year = year;
        this.imdbId = imdbId;
        this.moviePosterUrl = moviePosterUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public int getImdbId() {
        return imdbId;
    }

    public String getMoviePosterUrl() {
        return this.moviePosterUrl;
    }

    @Override
    public String toString() {
        return title + " (" + year + ") - IMdb ID: " + imdbId;
    }
}
