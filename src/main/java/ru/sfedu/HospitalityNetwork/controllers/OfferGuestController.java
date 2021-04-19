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
import ru.sfedu.HospitalityNetwork.repo.OfferGuestRepo;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class OfferGuestController {

    @Autowired
    OfferGuestRepo offerGuestRepo;

    @GetMapping("/guests-offer")
    public String allOfferGuest(Model model) {
        Iterable<OfferGuest> offerGuests = offerGuestRepo.findAll();
        model.addAttribute("offerGuests", offerGuests);
        return "guests-offer";
    }

    @GetMapping("/guests-offer/{id}")
    public String offerGuestDetails(@PathVariable(value = "id") long id, Model model) {
        if(!offerGuestRepo.existsById(id)) {
            return "redirect:/guests-offer";
        }
        Optional<OfferGuest> post = offerGuestRepo.findById(id);
        ArrayList<OfferGuest> res = new ArrayList<>();
        post.ifPresent(res::add);
        res.get(0).setViews(res.get(0).getViews() + 1);
        offerGuestRepo.save(res.get(0));
        model.addAttribute("offerGuest", res);
        return "offer-guest-details";
    }

}
