package ru.sfedu.HospitalityNetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sfedu.HospitalityNetwork.models.Comment;
import ru.sfedu.HospitalityNetwork.models.OfferGuest;
import ru.sfedu.HospitalityNetwork.models.User;
import ru.sfedu.HospitalityNetwork.repo.CommentRepo;
import ru.sfedu.HospitalityNetwork.repo.OfferGuestRepo;
import ru.sfedu.HospitalityNetwork.repo.OfferHostRepo;
import ru.sfedu.HospitalityNetwork.repo.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CommentController {

    @Autowired
    CommentRepo commentRepo;
    @Autowired
    UserRepo userRepo;

    @PostMapping("/comment/{id}/remove")
    public String CommentDelete(@PathVariable(value = "id") long id, Model model) {
        Comment comment = commentRepo.findById(id).orElseThrow();
        long userId = comment.getUserTo().getId();
        commentRepo.delete(comment);
        return "redirect:/user/id" + userId;
    }

    @GetMapping("/user{id}/comments/page{number}")
    public String CommentsForUser(@PathVariable(value = "id") long id,
                                  @PathVariable(value = "number") int number,
                                  @AuthenticationPrincipal User user,
                                  Model model) {
        Iterable<Comment> comments = commentRepo.findAll();
        List<Comment> listComment = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getUserTo().getId() == id) {
                listComment.add(comment);
            }
        }
        if (listComment.size() - number*5 < -4 && number != 1) {
            return "redirect:/";
        }
        ArrayList listForPage = new ArrayList();
        for (int i = (number * 5) - 5; i < listComment.size() && i < number*5; i++ ) {
            listForPage.add(listComment.get(i));
        }
        int previous = number - 1;
        int next = number + 1;
        if (previous > 0) {
            model.addAttribute("previous", previous);
            model.addAttribute("namePrevious", "<<< Предыдущая");
        }
        if(listComment.size() - (number*5) > 0) {
            model.addAttribute("next", next);
            model.addAttribute("nameNext", "Следующая >>>");
        }
        model.addAttribute("comments", listForPage);
        model.addAttribute("myId", user.getId());
        return "comments";
    }

    @PostMapping("/user{id}/comments/page{number}")
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
