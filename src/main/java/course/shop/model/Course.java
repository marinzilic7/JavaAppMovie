package course.shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name="courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    @NotBlank(message = "Unesite naziv tečaja.")
    String name;

    @Column(nullable = false)
    @NotBlank(message = "Unesite opis  tečaja.")
    String opis;

    @Column(nullable = false)
    @NotBlank(message = "Unesite cijenu  tečaja.")
    String cijena;

    @ManyToOne
    @JoinColumn(name = "tečaj_id", nullable = true)
    Course parent;

    @OneToMany(mappedBy = "parent")
    List<Course> courses;

    @ManyToOne
    @JoinColumn(name = "category_id")  // Naziv stupca koji će povezivati Course s Category
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Naziv stupca koji će povezivati Course s Category
    private User createdBy;



    public Course(Long id, String name, String opis, String cijena) {
        this.id = id;
        this.name = name;
        this.opis = opis;
        this.cijena = cijena;
    }

    public Course() {
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


    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getCijena() {
        return cijena;
    }

    public void setCijena(String cijena) {
        this.cijena = cijena;
    }


    public Course getParent() {
        return parent;
    }

    public void setParent(Course parent) {
        this.parent = parent;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Course(User createdBy) {
        this.createdBy = createdBy;
    }



    public User getUser() {
        return createdBy;
    }

    public void setUser(User createdBy) {
        this.createdBy = createdBy;
    }
}
