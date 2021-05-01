package ru.sfedu.HospitalityNetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.sfedu.HospitalityNetwork.models.OfferGuest;
import ru.sfedu.HospitalityNetwork.models.User;
import ru.sfedu.HospitalityNetwork.repo.UserRepo;

import java.io.File;
import java.io.IOException;
import java.nio.channels.MulticastChannel;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ProfileController {

    @Autowired
    UserRepo userRepo;

    @Value("${upload.path}")
    private String uploadPath;

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
            @RequestParam String fullName,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String emailAddress,
            @RequestParam String aboutUser,
            @RequestParam("file") MultipartFile file) throws IOException {
        User myAccount = userRepo.findById(user.getId()).orElseThrow();
        myAccount.setFullName(fullName);
        myAccount.setCountry(country);
        myAccount.setCity(city);
        myAccount.setEmailAddress(emailAddress);
        myAccount.setAboutUser(aboutUser);

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();

        file.transferTo(new File(uploadPath + "/" + resultFilename));

        myAccount.setAvatar(resultFilename);

        userRepo.save(myAccount);
        return "redirect:/profile";
    }

    @PostMapping("/profile/edit/complete")
    public String profileEditComplete(
            @AuthenticationPrincipal User user,
            @RequestParam String fullName,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String emailAddress,
            @RequestParam String aboutUser,
            Model model) {
        User myAccount = userRepo.findById(user.getId()).orElseThrow();
        myAccount.setFullName(fullName);
        myAccount.setCountry(country);
        myAccount.setCity(city);
        myAccount.setEmailAddress(emailAddress);
        myAccount.setAboutUser(aboutUser);
        userRepo.save(myAccount);
        return "redirect:/profile";
    }

}
