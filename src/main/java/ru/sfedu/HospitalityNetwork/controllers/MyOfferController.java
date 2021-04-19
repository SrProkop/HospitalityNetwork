package ru.sfedu.HospitalityNetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sfedu.HospitalityNetwork.models.OfferGuest;
import ru.sfedu.HospitalityNetwork.models.OfferHost;
import ru.sfedu.HospitalityNetwork.models.User;
import ru.sfedu.HospitalityNetwork.repo.OfferGuestRepo;
import ru.sfedu.HospitalityNetwork.repo.OfferHostRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Controller
public class MyOfferController {

    @Autowired
    OfferGuestRepo offerGuestRepo;
    @Autowired
    OfferHostRepo offerHostRepo;

    @GetMapping("/my-offers")
    public String allMyOffer(@AuthenticationPrincipal User user, Model model) {
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
        return "my-offers";
    }

    @GetMapping("/offer-guest-add")
    public String offerGuestAdd(Model model) {
        return "offer-guest-add";
    }

    @PostMapping("/offer-guest-add")
    public String offerGuestAdd(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String aboutOffer,
            Model model) {
        OfferGuest post = new OfferGuest(name, country, city, aboutOffer, user);
        offerGuestRepo.save(post);
        return "redirect:/guests-offer";
    }

    @PostMapping("/offer-guest-add/complete")
    public String offerGuestAddComplete(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String aboutOffer,
            @PathVariable(value = "id") long id,
            Model model) {
        OfferGuest post = new OfferGuest(name, country, city, aboutOffer, user);
        offerGuestRepo.save(post);
        return "redirect:/my-offers";
    }

    @GetMapping("/offer-host-add")
    public String offerHostAdd(Model model) {
        return "offer-host-add";
    }

    @PostMapping("/offer-host-add")
    public String offerHostAdd(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String aboutOffer,
            Model model) {
        OfferHost post = new OfferHost(name, country, city, aboutOffer, user);
        offerHostRepo.save(post);
        return "redirect:/hosts-offer";
    }

    @PostMapping("/offer-host-add/complete")
    public String offerHostAddComplete(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String aboutOffer,
            @PathVariable(value = "id") long id,
            Model model) {
        OfferHost post = new OfferHost(name, country, city, aboutOffer, user);
        offerHostRepo.save(post);
        return "redirect:/my-offers";
    }
}
