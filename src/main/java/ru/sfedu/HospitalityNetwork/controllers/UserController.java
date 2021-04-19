package ru.sfedu.HospitalityNetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.sfedu.HospitalityNetwork.models.OfferGuest;
import ru.sfedu.HospitalityNetwork.models.User;
import ru.sfedu.HospitalityNetwork.repo.UserRepo;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/user/id{id}")
    public String UserDetails(@PathVariable(value = "id") long id, Model model) {
        if(!userRepo.existsById(id)) {
            return "redirect:/";
        }
        Optional<User> optionalUser = userRepo.findById(id);
        User user = optionalUser.get();
        model.addAttribute("user", user);
        return "user-details";
    }
}
