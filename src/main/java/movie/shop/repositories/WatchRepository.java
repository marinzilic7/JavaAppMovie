package movie.shop.repositories;

import movie.shop.model.Movie;
import movie.shop.model.User;
import movie.shop.model.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.ArrayList;

public interface WatchRepository extends JpaRepository  <Watch, Long>{
    Watch findByCreatedByAndMovie(User createdBy, Movie movie);
    List<Watch> findByCreatedBy(User createdBy);
}
