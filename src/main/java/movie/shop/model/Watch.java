package movie.shop.model;

import jakarta.persistence.*;
import movie.shop.model.Movie;
import movie.shop.model.User;

import java.util.List;
@Entity
@Table(name="watch")
public class Watch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")  // Naziv stupca koji će povezivati Course s Category
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Naziv stupca koji će povezivati Course s Category
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "category_id")  // Naziv stupca koji će povezivati Course s Category
    private Category category;


    public Watch(Long id) {
        this.id = id;

    }

    public Watch() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie= movie;
    }

    public Watch(User createdBy) {
        this.createdBy = createdBy;
    }



    public User getUser() {
        return createdBy;
    }

    public void setUser(User createdBy) {
        this.createdBy = createdBy;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


}
