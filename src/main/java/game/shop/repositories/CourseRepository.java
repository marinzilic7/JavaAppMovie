package game.shop.repositories;

import game.shop.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository <Course, Long> {}