package ru.sfedu.HospitalityNetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.sfedu.HospitalityNetwork.models.OfferGuest;
import ru.sfedu.HospitalityNetwork.models.User;
import ru.sfedu.HospitalityNetwork.repo.UserRepo;

import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/profile")
    public String allOfferGuest(
            @AuthenticationPrincipal User user,
            Model model) {
        Optional<User> optionalUser = userRepo.findById(user.getId());
        User profileUser = optionalUser.get();
        model.addAttribute("user", profileUser);
        return "profile";
    }

}
