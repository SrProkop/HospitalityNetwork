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
import ru.sfedu.HospitalityNetwork.models.OfferHost;
import ru.sfedu.HospitalityNetwork.models.User;
import ru.sfedu.HospitalityNetwork.repo.OfferHostRepo;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class OfferHostController {

    @Autowired
    OfferHostRepo offerHostRepo;

    @GetMapping("/hosts-offer")
    public String allOfferHost(Model model) {
        Iterable<OfferHost> offerHosts = offerHostRepo.findAll();
        model.addAttribute("offerHosts", offerHosts);
        return "hosts-offer";
    }

    @GetMapping("/hosts-offer/{id}")
    public String offerHostDetails(@PathVariable(value = "id") long id, Model model) {
        if(!offerHostRepo.existsById(id)) {
            return "redirect:/hosts-offer";
        }
        Optional<OfferHost> post = offerHostRepo.findById(id);
        ArrayList<OfferHost> res = new ArrayList<>();
        post.ifPresent(res::add);
        res.get(0).setViews(res.get(0).getViews() + 1);
        offerHostRepo.save(res.get(0));
        model.addAttribute("offerHost", res);
        return "offer-host-details";
    }

}
