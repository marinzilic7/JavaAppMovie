package movie.shop.controller;

import movie.shop.model.Category;
import movie.shop.model.UserDetails;
import movie.shop.model.Watch;
import movie.shop.repositories.CategoryRepository;

import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;


@Controller
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    WatchRepository watchRepository;









    @GetMapping("/category")
    public String showCategories (Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId();
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("added", false);
        model.addAttribute("activeLink", "Kategorije");

        List<Watch> watches = watchRepository.findByCreatedBy(userDetails.getUser());

        int watchCount = watches.size();

        if (watchCount > 0) {
            model.addAttribute("watches", watches);
            model.addAttribute("watchCount", watchCount);
            model.addAttribute("prikazi", true);
        } else {
            model.addAttribute("prikazi", false);
        }

        return "category";
    }

    @PostMapping("/category/add")
    public String dodajKategoriju (@Valid Category category, BindingResult result, Model model,RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            model.addAttribute("category", category);
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("added", true);
            model.addAttribute("activeLink", "Kategorije");
            return "category";
        }
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("successCategory", "Kategorija je uspješno dodana!");

        return "redirect:/category";
    }

    @GetMapping("/category/delete/{id}")
    public String izbrisiKategoriju (@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

//

        try {
            categoryRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successCategory", "Kategorija je uspješno izbrisana.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorCategory", "Greska kod brisanja kategorije (Strani ključ).");
        }
        return "redirect:/category";
    }
}
