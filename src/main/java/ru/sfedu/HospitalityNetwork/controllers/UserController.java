package ru.sfedu.HospitalityNetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sfedu.HospitalityNetwork.models.*;
import ru.sfedu.HospitalityNetwork.repo.CommentRepo;
import ru.sfedu.HospitalityNetwork.repo.OfferGuestRepo;
import ru.sfedu.HospitalityNetwork.repo.OfferHostRepo;
import ru.sfedu.HospitalityNetwork.repo.UserRepo;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    CommentRepo commentRepo;
    @Autowired
    OfferHostRepo offerHostRepo;
    @Autowired
    OfferGuestRepo offerGuestRepo;

    @GetMapping("/user/id{id}")
    public String UserDetails(@PathVariable(value = "id") long id,
                              @AuthenticationPrincipal User myUser,
                              Model model) {
        if(!userRepo.existsById(id)) {
            return "redirect:/";
        }
        Optional<User> optionalUser = userRepo.findById(id);
        User user = optionalUser.get();
        model.addAttribute("user", user);
        model.addAttribute("myId", myUser.getId());

        Iterable<OfferHost> offerHosts = offerHostRepo.findAll();
        List<OfferHost> listOfferHost = new ArrayList<>();
        for (OfferHost offerHost : offerHosts) {
            if (offerHost.getAuthor().getId() == user.getId()) {
                listOfferHost.add(offerHost);
            }
        }
        model.addAttribute("offerHosts", listOfferHost);
        Iterable<OfferGuest> offerGuests = offerGuestRepo.findAll();
        List<OfferGuest> listOfferGuest = new ArrayList<>();
        for (OfferGuest offerGuest : offerGuests) {
            if (offerGuest.getAuthor().getId() == user.getId()) {
                listOfferGuest.add(offerGuest);
            }
        }
        model.addAttribute("offerGuests", listOfferGuest);

        if (listOfferGuest.size() == 0 && listOfferHost.size() == 0 ) {
            model.addAttribute("warningOffer", "У данного пользователя нет предложений!");
        }

        Iterable<Comment> comments = commentRepo.findAll();
        List<Comment> listComment = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getUserTo().getId() == id) {
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
        return "user-details";
    }

    @PostMapping("/user/id{id}")
    public String addComment(
            @AuthenticationPrincipal User userFrom,
            @PathVariable(value = "id") long id,
            @Valid Comment comment,
            BindingResult bindingResult,
            Model model) {
        Optional<User> userTo = userRepo.findById(id);
        comment.setUserFrom(userFrom);
        comment.setUserTo(userTo.get());

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            System.out.println(errorsMap.size());
            for (Map.Entry<String, String> item : errorsMap.entrySet()) {
                System.out.println("Key = " + item.getKey() + "   Value = " + item.getValue());
            }
            model.mergeAttributes(errorsMap);
            return UserDetails(id, userFrom, model);
        } else {
            commentRepo.save(comment);
        }

        return "redirect:/user/id{id}";
    }

    @PostMapping("/user/id{id}/addComment")
    public String addCommentComplete(
            @AuthenticationPrincipal User userFrom,
            @RequestParam String note,
            @PathVariable(value = "id") long id,
            Model model) {
        Optional<User> userTo = userRepo.findById(id);
        Comment comment = new Comment(userFrom, userTo.get(), note);
        commentRepo.save(comment);
        return "redirect:/user/id{id}";
    }

    @PostMapping("/user/id{id}/addCommentComplete")
    public String addCommentCompleteForPage(
            @AuthenticationPrincipal User userFrom,
            @RequestParam String note,
            @PathVariable(value = "id") long id,
            Model model) {
        Optional<User> userTo = userRepo.findById(id);
        Comment comment = new Comment(userFrom, userTo.get(), "note");
        commentRepo.save(comment);
        return "redirect:/user" + id + "/comments/page1";
    }
}
