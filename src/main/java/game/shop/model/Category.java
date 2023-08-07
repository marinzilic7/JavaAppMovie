package game.shop.model;

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

        @ManyToOne
        @JoinColumn(name = "category_id", nullable = true)
        game.shop.model.Category  parent;

        @OneToMany(mappedBy = "parent")
        List<game.shop.model.Category> categories;





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


        public game.shop.model.Category getParent() {
            return parent;
        }

        public void setParent(game.shop.model.Category  parent) {
            this.parent = parent;
        }

        public List<game.shop.model.Category > getCategories() {
            return categories;
        }

        public void setCategories(List<game.shop.model.Category > categories) {
            this.categories = categories;
        }
    }


