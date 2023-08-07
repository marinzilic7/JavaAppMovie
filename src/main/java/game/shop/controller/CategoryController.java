package game.shop.controller;

import game.shop.model.Category;
import game.shop.model.Course;
import game.shop.model.UserDetails;
import game.shop.repositories.CategoryRepository;
import game.shop.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;








    @GetMapping("/category")
    public String showGames (Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId(); // ili koristite metodu kojom dobavljate ID korisnika
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("added", false);
        model.addAttribute("activeLink", "Igre");

        return "category";
    }


}
