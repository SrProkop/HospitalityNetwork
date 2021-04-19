package ru.sfedu.HospitalityNetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sfedu.HospitalityNetwork.models.OfferGuest;
import ru.sfedu.HospitalityNetwork.models.User;
import ru.sfedu.HospitalityNetwork.repo.UserRepo;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/profile")
    public String myProfile(
            @AuthenticationPrincipal User user,
            Model model) {
        Optional<User> optionalUser = userRepo.findById(user.getId());
        User profileUser = optionalUser.get();
        model.addAttribute("user", profileUser);
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String profileEdit(
            @AuthenticationPrincipal User user,
            Model model) {
        Optional<User> optionalUser = userRepo.findById(user.getId());
        User myAccount = optionalUser.get();
        model.addAttribute("user", myAccount);
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String profileEdit(
            @AuthenticationPrincipal User user,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String password,
            Model model) {
        User myAccount = userRepo.findById(user.getId()).orElseThrow();
        myAccount.setCity(city);
        myAccount.setCountry(country);
        myAccount.setPassword(password);
        userRepo.save(myAccount);
        return "redirect:/profile";
    }

    @PostMapping("/profile/edit/complete")
    public String profileEditComplete(
            @AuthenticationPrincipal User user,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String password,
            Model model) {
        User myAccount = userRepo.findById(user.getId()).orElseThrow();
        myAccount.setCity(city);
        myAccount.setCountry(country);
        myAccount.setPassword(password);
        userRepo.save(myAccount);
        return "redirect:/profile";
    }

}
