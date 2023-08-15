package movie.shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;


    @Entity
    @Table(name="categories")
    public class Category {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        @Column(nullable = false)
        @NotBlank(message = "Unesite naziv kategorije.")
        String name;

        @OneToMany(mappedBy = "category")
        List<Movie> movie;


        @OneToMany(mappedBy = "category")
        List<Watch> watches;




        public Category(Long id, String name ) {
            this.id = id;
            this.name = name;

        }

        public Category() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Movie> getCourses() {
            return movie;
        }

        public void setCourses(List<Movie> cours) {
            this.movie = cours;
        }

        public List<Watch> getWatches() {
            return watches;
        }

        public void setWatches(List<Watch> cours) {
            this.watches = watches;
        }



    }


