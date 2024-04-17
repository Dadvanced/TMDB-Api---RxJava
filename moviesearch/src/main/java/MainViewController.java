import io.reactivex.rxjava3.disposables.Disposable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.util.List;

public class MainViewController {
    @FXML
    private TextField searchField;

    @FXML
    private ListView<Movie> resultListView;

    @FXML
    private ImageView moviePosterImageView;

    private final TmdbApiService tmdbApiService = new TmdbApiService();
    private Disposable searchSubscription;

    @FXML
    private void search() {
        String query = searchField.getText();

        if (searchSubscription != null && !searchSubscription.isDisposed()) {
            searchSubscription.dispose();
        }

        searchSubscription =tmdbApiService.searchMovies(query)
                .subscribe(this::updateResults);
    }

    private void updateResults(List<Movie> movies) {
        Platform.runLater(() -> {
            resultListView.getItems().clear();
            resultListView.getItems().addAll(movies);
        });
    }

    @FXML
    public void initialize() {
        resultListView.setCellFactory(new Callback<ListView<Movie>, ListCell<Movie>>() {
            @Override
            public ListCell<Movie> call(ListView<Movie> param) {
                return new ListCell<Movie>() {
                    @Override
                    protected void updateItem(Movie item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.toString());
                        }
                    }
                };
            }
        });

        resultListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String posterUrl = newValue.getMoviePosterUrl();
                Image posterImage = new Image(posterUrl);
                moviePosterImageView.setImage(posterImage);
            }
        });
    }
}