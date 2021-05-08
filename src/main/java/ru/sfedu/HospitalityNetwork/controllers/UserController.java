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
import ru.sfedu.HospitalityNetwork.models.Comment;
import ru.sfedu.HospitalityNetwork.models.OfferGuest;
import ru.sfedu.HospitalityNetwork.models.OfferHost;
import ru.sfedu.HospitalityNetwork.models.User;
import ru.sfedu.HospitalityNetwork.repo.CommentRepo;
import ru.sfedu.HospitalityNetwork.repo.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    CommentRepo commentRepo;

    @GetMapping("/user/id{id}")
    public String UserDetails(@PathVariable(value = "id") long id, Model model) {
        if(!userRepo.existsById(id)) {
            return "redirect:/";
        }
        Optional<User> optionalUser = userRepo.findById(id);
        User user = optionalUser.get();
        model.addAttribute("user", user);

        Iterable<Comment> comments = commentRepo.findAll();
        List<Comment> listComment = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getUserTo().getId() == id) {
                listComment.add(comment);
            }
        }
        if (listComment.size() > 0) {
            model.addAttribute("comment", listComment);
        } else {
            model.addAttribute("warning", "У данного пользователя ещё нет отзывов!");
        }
        return "user-details";
    }

    @PostMapping("/user/id{id}")
    public String addComment(
            @AuthenticationPrincipal User userFrom,
            @RequestParam String note,
            @PathVariable(value = "id") long id,
            Model model) {
        Optional<User> userTo = userRepo.findById(id);
        Comment comment = new Comment(userFrom, userTo.get(), note);
        commentRepo.save(comment);
        return "redirect:/user/id{id}";
    }

    @PostMapping("/user/id{id}/addComment")
    public String addCommentComplete(
            @AuthenticationPrincipal User userFrom,
            @RequestParam String note,
            @PathVariable(value = "id") long id,
            Model model) {
        Optional<User> userTo = userRepo.findById(id);
        Comment comment = new Comment(userFrom, userTo.get(), "note");
        commentRepo.save(comment);
        return "redirect:/user/id{id}";
    }
}
