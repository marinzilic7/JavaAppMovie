package game.shop.repositories;

import game.shop.model.Category;
import game.shop.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository <Category, Long> {}