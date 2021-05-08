package ru.sfedu.HospitalityNetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/hosts-offer/more")
    public String allOfferHostMore(Model model) {
        Iterable<OfferHost> offerHosts = offerHostRepo.findAll();
        model.addAttribute("offerHosts", offerHosts);
        return "hosts-offer";
    }

    @GetMapping("/hosts-offer/{id}")
    public String offerHostDetails(
            @AuthenticationPrincipal User user,
            @PathVariable(value = "id") long id,
            Model model) {
        if(!offerHostRepo.existsById(id)) {
            return "redirect:/hosts-offer";
        }
        Optional<OfferHost> post = offerHostRepo.findById(id);
        ArrayList<OfferHost> res = new ArrayList<>();
        post.ifPresent(res::add);
        res.get(0).setViews(res.get(0).getViews() + 1);
        offerHostRepo.save(res.get(0));
        model.addAttribute("offerHost", res);
        if (post.get().getAuthorId() == user.getId()) {
            return "my-offer-host-details";
        }
        return "offer-host-details";
    }

    @GetMapping("/hosts-offer/{id}/edit")
    public String offerHostEdit(@PathVariable(value = "id") long id, Model model) {
        if(!offerHostRepo.existsById(id)) {
            return "redirect:/hosts-offer";
        }
        Optional<OfferHost> post = offerHostRepo.findById(id);
        ArrayList<OfferHost> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "my-offer-host-edit";
    }

    @PostMapping("/hosts-offer/{id}/edit")
    public String offerHostUpdate(
            @RequestParam String name,
            @RequestParam String aboutOffer,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String addressHouse,
            @RequestParam String aboutHouse,
            @PathVariable(value = "id") long id,
            Model model) {
        OfferHost offerHost = offerHostRepo.findById(id).orElseThrow();
        offerHost.setName(name);
        offerHost.setAboutOffer(aboutOffer);
        offerHost.setCountry(country);
        offerHost.setCity(city);
        offerHost.setAddressHouse(addressHouse);
        offerHost.setAboutHouse(aboutHouse);
        offerHostRepo.save(offerHost);
        return "redirect:/hosts-offer";
    }

    @PostMapping("/hosts-offer/{id}/edit/complete")
    public String offerHostUpdateComplete(
            @RequestParam String name,
            @RequestParam String aboutOffer,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String addressHouse,
            @RequestParam String aboutHouse,
            @PathVariable(value = "id") long id,
            Model model) {
        OfferHost offerGuest = offerHostRepo.findById(id).orElseThrow();
        offerGuest.setName(name);
        offerGuest.setAboutOffer(aboutOffer);
        offerGuest.setCountry(country);
        offerGuest.setCity(city);
        offerGuest.setAddressHouse(addressHouse);
        offerGuest.setAboutHouse(aboutHouse);
        offerHostRepo.save(offerGuest);
        return "redirect:/hosts-offer";
    }

    @PostMapping("/hosts-offer/{id}/remove")
    public String offerHostDelete(@PathVariable(value = "id") long id, Model model) {
        OfferHost offerHost = offerHostRepo.findById(id).orElseThrow();
        offerHostRepo.delete(offerHost);
        return "redirect:/hosts-offer";
    }

}
