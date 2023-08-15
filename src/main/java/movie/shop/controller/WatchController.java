package movie.shop.controller;


import jakarta.validation.Valid;
import movie.shop.model.*;
import movie.shop.repositories.CategoryRepository;
import movie.shop.repositories.MovieRepository;
import movie.shop.repositories.UserRepository;
import movie.shop.repositories.WatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class WatchController {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    WatchRepository watchRepository;


    @GetMapping("/watch")
    public String showWatch(Model model,@AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId(); // ili koristite metodu kojom dobavljate ID korisnika
        List<Movie> movies = movieRepository.findAll();

        model.addAttribute("watches", watchRepository.findAll());
        model.addAttribute("movies", movies);
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
        model.addAttribute("watch", new Watch());
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

        return "watch";
    }


    @GetMapping("/watch/{id}")
    public String addWatch(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes, UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();



        Movie selectedMovie = movieRepository.findById(id).orElse(null);

        if (selectedMovie == null) {
            return "redirect:/error";
        }
        Long userIdd = user.getUserId();
        User selectedUser = userRepository.findById(userIdd).orElse(null);

        Watch existingWatch = watchRepository.findByCreatedByAndMovie(selectedUser, selectedMovie);

        if (existingWatch != null) {
            redirectAttributes.addFlashAttribute("error", "Film je već dodan u listu za gledanje.");
            return "redirect:/movie";
        }

        Watch newWatch = new Watch();
        newWatch.setUser(selectedUser);
        newWatch.setMovie(selectedMovie);
        newWatch.setUser(selectedUser);

        watchRepository.save(newWatch);

        redirectAttributes.addFlashAttribute("successMovie", "Film je uspjesno dodan  za 'gledati kasnije'.");
        return "redirect:/movie";
    }

    @GetMapping("/watch/delete/{id}")
    public String deleteWatch(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

       Watch watch = watchRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pogrešan ID"));
        watchRepository.delete(watch);
        redirectAttributes.addFlashAttribute("successDeleteWatch", "Uspjesno izbrisano!");


        return "redirect:/watch";
    }

}
