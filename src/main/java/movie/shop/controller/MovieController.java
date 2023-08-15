package movie.shop.controller;
import movie.shop.model.*;
import movie.shop.repositories.CategoryRepository;
import movie.shop.repositories.MovieRepository;
import movie.shop.repositories.UserRepository;
import jakarta.validation.Valid;
import movie.shop.repositories.WatchRepository;
import org.springframework.dao.DataIntegrityViolationException;
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
public class MovieController {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WatchRepository watchRepository;







    @GetMapping("/movie")
    public String showMovies(Model model,@AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId(); // ili koristite metodu kojom dobavljate ID korisnika
        List<Category> categories = categoryRepository.findAll();
        System.out.println(categories.size());
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("categories", categories);
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
        model.addAttribute("movie", new Movie());
        model.addAttribute("added", false);
        model.addAttribute("activeLink", "Igre");
        User userr = userDetails.getUser();
        System.out.println("User je" + userr);
        Long userIdd = user.getUserId();
        System.out.println("ID korisnika: " + userIdd);

        List<Watch> watches = watchRepository.findByCreatedBy(userDetails.getUser());

        int watchCount = watches.size();

        if (watchCount > 0) {
            model.addAttribute("watches", watches);
            model.addAttribute("watchCount", watchCount);
            model.addAttribute("prikazi", true);
        } else {
            model.addAttribute("prikazi", false);
        }

        return "movie";
    }

    @PostMapping("/movie/add")
    public String addMovie(@Valid Movie movie, BindingResult result, Model model, RedirectAttributes redirectAttributes, UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        if (result.hasErrors()) {
            List<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("movie", movie);
            model.addAttribute("movies", movieRepository.findAll());
            model.addAttribute("added", true);
            model.addAttribute("activeLink", "Igre");
            return "movie";
        }
        Long userIdd = user.getUserId();
        User selectedUser = userRepository.findById(userIdd).orElse(null);
        movie.setUser(selectedUser);
        Long categoryId = movie.getCategory().getId();
        Category selectedCategory = categoryRepository.findById(categoryId).orElse(null);
        movie.setCategory(selectedCategory);

        movieRepository.save(movie);
        redirectAttributes.addFlashAttribute("successMovie", "Uspjesno dodano!");
        return "redirect:/movie";
    }



    @GetMapping("/movie/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model,@AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        model.addAttribute("user", user);
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        model.addAttribute("movie", movie);
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("activeLink", "Kategorije");
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        User userr = userDetails.getUser();
        System.out.println("User je" + userr);
        Long userIdd = user.getUserId();
        List<Watch> watches = watchRepository.findByCreatedBy(userDetails.getUser());

        int watchCount = watches.size();

        if (watchCount > 0) {
            model.addAttribute("watches", watches);
            model.addAttribute("watchCount", watchCount);
            model.addAttribute("prikazi", true);
        } else {
            model.addAttribute("prikazi", false);
        }
        return "movie_edit";
    }

    @PostMapping("movie/edit/{id}")
    public String editMovie (@PathVariable("id") Long id, @Valid Movie movie, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        if (result.hasErrors()) {
            model.addAttribute("course", movie);
            model.addAttribute("activeLink", "Igre");
            return "movie_edit";
        }
        Long userIdd = user.getUserId();
        User selectedUser = userRepository.findById(userIdd).orElse(null);
        movie.setUser(selectedUser);
        movieRepository.save(movie);
        redirectAttributes.addFlashAttribute("successMovie", "Uspjesno uredjeno!");
        return "redirect:/movie";
    }


    @GetMapping("/movie/delete/{id}")
    public String deleteMovie(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

        Movie movie = movieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pogrešan ID"));
        try {
            movieRepository.delete(movie);
            redirectAttributes.addFlashAttribute("successMovie", "Uspjesno izbrisano!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Greska kod brisanja filma (Strani ključ).");
        }
        return "redirect:/movie";
    }

}
