package game.shop.controller;
import game.shop.model.Course;
import game.shop.model.UserDetails;
import game.shop.repositories.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class CourseController {
    @Autowired
    CourseRepository courseRepository;








    @GetMapping("/course")
    public String showGames (Model model,@AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId(); // ili koristite metodu kojom dobavljate ID korisnika
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
        model.addAttribute("course", new Course());
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("added", false);
        model.addAttribute("activeLink", "Igre");

        return "course";
    }

    @PostMapping("/course/add")
    public String addCourse (@Valid Course course, BindingResult result, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            model.addAttribute("course", course);
            model.addAttribute("courses", courseRepository.findAll());
            model.addAttribute("added", true);
            model.addAttribute("activeLink", "Igre");
            return "course";
        }
        courseRepository.save(course);
        return "redirect:/course";
    }

    @GetMapping("/course/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        model.addAttribute("user", user);
        Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        model.addAttribute("course", course);
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("activeLink", "Kategorije");
        return "course_edit";
    }

    @PostMapping("course/edit/{id}")
    public String editCategory (@PathVariable("id") Long id, @Valid Course course, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("course", course);
            model.addAttribute("activeLink", "Igre");
            return "course_edit";
        }
        courseRepository.save(course);
        return "redirect:/course";
    }


    @GetMapping("/course/delete/{id}")
    public String deleteGame(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

            Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pogrešan ID"));
            courseRepository.delete(course);


        return "redirect:/course";
    }

}
