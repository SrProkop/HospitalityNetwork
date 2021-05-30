package ru.sfedu.HospitalityNetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.sfedu.HospitalityNetwork.models.*;
import ru.sfedu.HospitalityNetwork.repo.CommentRepo;
import ru.sfedu.HospitalityNetwork.repo.OfferGuestRepo;
import ru.sfedu.HospitalityNetwork.repo.OfferHostRepo;
import ru.sfedu.HospitalityNetwork.repo.UserRepo;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class ProfileController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    CommentRepo commentRepo;
    @Autowired
    OfferHostRepo offerHostRepo;
    @Autowired
    OfferGuestRepo offerGuestRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/profile")
    public String myProfile(
            @AuthenticationPrincipal User user,
            Model model) {
        Optional<User> optionalUser = userRepo.findById(user.getId());
        User profileUser = optionalUser.get();
        model.addAttribute("user", profileUser);
        model.addAttribute("myId", user.getId());


        Iterable<OfferHost> offerHosts = offerHostRepo.findAll();
        List<OfferHost> listOfferHost = new ArrayList<>();
        for (OfferHost offerHost : offerHosts) {
            if (offerHost.getAuthor().getId() == user.getId()) {
                listOfferHost.add(offerHost);
            }
        }
        if (listOfferHost.size() != 0) {
            model.addAttribute("offerHosts", listOfferHost);
        } else {
            model.addAttribute("addOfferHost", "Добавить предложение хоста");
        }

        Iterable<OfferGuest> offerGuests = offerGuestRepo.findAll();
        List<OfferGuest> listOfferGuest = new ArrayList<>();
        for (OfferGuest offerGuest : offerGuests) {
            if (offerGuest.getAuthor().getId() == user.getId()) {
                listOfferGuest.add(offerGuest);
            }
        }
        if (listOfferGuest.size() != 0) {
            model.addAttribute("offerGuests", listOfferGuest);
        } else {
            model.addAttribute("addOfferGuest", "Добавить предложение гостя");
        }

        Iterable<Comment> comments = commentRepo.findAll();
        List<Comment> listComment = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getUserTo().getId() == user.getId()) {
                listComment.add(comment);
            }
        }
        if (listComment.size() > 0) {
            model.addAttribute("comment", listComment);
            model.addAttribute("quantityComment", listComment.size());
        } else {
            model.addAttribute("comment", null);
            model.addAttribute("warning", "У данного пользователя ещё нет комментариев!");
            model.addAttribute("quantityComment", 0);
        }
        return "profile";
    }

    @PostMapping("/profile")
    public String addCommentForProfile(
            @AuthenticationPrincipal User userFrom,
            @Valid Comment comment,
            BindingResult bindingResult,
            Model model) {
        comment.setUserFrom(userFrom);
        comment.setUserTo(userFrom);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            System.out.println(errorsMap.size());
            for (Map.Entry<String, String> item : errorsMap.entrySet()) {
                System.out.println("Key = " + item.getKey() + "   Value = " + item.getValue());
            }
            model.mergeAttributes(errorsMap);
            return myProfile(userFrom, model);
        } else {
            commentRepo.save(comment);
        }

        return "redirect:/profile";
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
            @RequestParam(defaultValue = "false") boolean checkbox,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {
        User myAccount = userRepo.findById(user.getId()).orElseThrow();
        myAccount.setFullName(fullName);
        myAccount.setCountry(country);
        myAccount.setCity(city);
        myAccount.setEmailAddress(emailAddress);
        myAccount.setAboutUser(aboutUser);
        if (!file.isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            myAccount.setAvatar(resultFilename);
        }

        if (checkbox) {
            myAccount.setAvatar("def.jpg");
        }

        if (checkErrors(fullName, country, city, emailAddress, aboutUser, model)) {
            return profileEdit(myAccount, model);
        }


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
            Model model,
            @RequestParam("file") MultipartFile file) throws IOException {
        User myAccount = userRepo.findById(user.getId()).orElseThrow();
        myAccount.setFullName(fullName);
        myAccount.setCountry(country);
        myAccount.setCity(city);
        myAccount.setEmailAddress(emailAddress);
        myAccount.setAboutUser(aboutUser);
        if (!file.isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            myAccount.setAvatar(resultFilename);
        }


        userRepo.save(myAccount);
        return "redirect:/profile";
    }

    public Boolean checkErrors(String fullName,
                             String country,
                             String city,
                             String emailAddress,
                             String aboutUser,
                             Model model) {
        boolean check = false;
        if(fullName.trim().length() == 0) {
            model.addAttribute("fullNameError", "Имя не может быть пустым");
            check = true;
        } else if (fullName.length() > 48) {
            model.addAttribute("fullNameError", "Максимальная длина имени 48 символов");
            check = true;
        }
        if(country.length() > 64) {
            model.addAttribute("countryError", "Максимальная длина 64 символа");
            check = true;
        }
        if(city.length() > 64) {
            model.addAttribute("cityError", "Максимальная длина 64 символа");
            check = true;
        }
        if(emailAddress.length() > 64) {
            model.addAttribute("emailError", "Максимальная длина почты 64 символа");
            check = true;
        }
        if(aboutUser.length() > 512) {
            model.addAttribute("aboutUserError", "Максимальная длина информации 512 символов");
            check = true;
        }

        return check;
    }

}
