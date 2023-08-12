package course.shop.controller;
import course.shop.model.Course;
import course.shop.model.User;
import course.shop.model.UserDetails;
import course.shop.model.Category;
import course.shop.repositories.CategoryRepository;
import course.shop.repositories.CourseRepository;
import course.shop.repositories.UserRepository;
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
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;






    @GetMapping("/course")
    public String showCourses (Model model,@AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId(); // ili koristite metodu kojom dobavljate ID korisnika
        List<Category> categories = categoryRepository.findAll();
        System.out.println(categories.size());
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("categories", categories);
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
        model.addAttribute("course", new Course());
        model.addAttribute("added", false);
        model.addAttribute("activeLink", "Igre");
        User userr = userDetails.getUser();
        System.out.println("User je" + userr);
        Long userIdd = user.getUserId();
        System.out.println("ID korisnika: " + userIdd);

        return "course";
    }

    @PostMapping("/course/add")
    public String addCourse (@Valid Course course, BindingResult result, Model model, RedirectAttributes redirectAttributes,UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        if (result.hasErrors()) {
            List<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("course", course);
            model.addAttribute("courses", courseRepository.findAll());
            model.addAttribute("added", true);
            model.addAttribute("activeLink", "Igre");
            return "course";
        }
        Long userIdd = user.getUserId();
        User selectedUser = userRepository.findById(userIdd).orElse(null);
        course.setUser(selectedUser);
        Long categoryId = course.getCategory().getId();
        Category selectedCategory = categoryRepository.findById(categoryId).orElse(null);
        course.setCategory(selectedCategory);

        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("successCourse", "Tečaj je uspješno dodan!");
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
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "course_edit";
    }

    @PostMapping("course/edit/{id}")
    public String editCoruse (@PathVariable("id") Long id, @Valid Course course, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        if (result.hasErrors()) {
            model.addAttribute("course", course);
            model.addAttribute("activeLink", "Igre");
            return "course_edit";
        }
        Long userIdd = user.getUserId();
        User selectedUser = userRepository.findById(userIdd).orElse(null);
        course.setUser(selectedUser);
        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("successCourse", "Tečaj je uspješno uredjen!");
        return "redirect:/course";
    }


    @GetMapping("/course/delete/{id}")
    public String deleteGame(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

            Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pogrešan ID"));
            courseRepository.delete(course);
        redirectAttributes.addFlashAttribute("successCourse", "Tečaj je uspješno izbrisan!");


        return "redirect:/course";
    }

}
