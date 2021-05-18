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
import ru.sfedu.HospitalityNetwork.repo.OfferGuestRepo;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class OfferGuestController {

    @Autowired
    OfferGuestRepo offerGuestRepo;

    //Удалить
    @GetMapping("/guests-offer")
    public String allOfferGuest(Model model) {
        Iterable<OfferGuest> offerGuests = offerGuestRepo.findAll();
        model.addAttribute("offerGuests", offerGuests);
        return "guests-offer";
    }

    @GetMapping("/guests-offer/page{number}")
    public String allOfferGuestMore(Model model,
                                   @PathVariable(value = "number") int number) {
        Iterable<OfferGuest> offerGuests = offerGuestRepo.findAll();
        ArrayList list = new ArrayList();
        for (OfferGuest offerGuest : offerGuests) {
            list.add(offerGuest);
        }
        if (list.size() - number*5 < -4 && number != 1) {
            return "redirect:/";
        }
        ArrayList listForPage = new ArrayList();

        for (int i = (number * 5) - 5; i < list.size() && i < number*5; i++ ) {
            listForPage.add(list.get(i));
        }
        int previous = number - 1;
        int next = number + 1;
        if (previous > 0) {
            model.addAttribute("previous", previous);
            model.addAttribute("namePrevious", "<<< Предыдущая");
        }
        if(list.size() - (number*5) > 0) {
            model.addAttribute("next", next);
            model.addAttribute("nameNext", "Следующая >>>");
        }
        model.addAttribute("offerGuests", listForPage);

        return "guests-offer";
    }

    @GetMapping("/guests-offer/{id}")
    public String offerGuestDetails(
            @AuthenticationPrincipal User user,
            @PathVariable(value = "id") long id,
            Model model) {
        if(!offerGuestRepo.existsById(id)) {
            return "redirect:/guests-offer";
        }
        Optional<OfferGuest> post = offerGuestRepo.findById(id);
        ArrayList<OfferGuest> res = new ArrayList<>();
        post.ifPresent(res::add);
        res.get(0).setViews(res.get(0).getViews() + 1);
        offerGuestRepo.save(res.get(0));
        model.addAttribute("offerGuest", res);
        if (post.get().getAuthorId() == user.getId()) {
            return "my-offer-guest-details";
        }
        return "offer-guest-details";
    }

    @GetMapping("/guests-offer/{id}/edit")
    public String offerGuestEdit(@PathVariable(value = "id") long id, Model model) {
        if(!offerGuestRepo.existsById(id)) {
            return "redirect:/guests-offer";
        }
        Optional<OfferGuest> post = offerGuestRepo.findById(id);
        ArrayList<OfferGuest> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "my-offer-guest-edit";
    }

    @PostMapping("/guests-offer/{id}/edit")
    public String offerGuestUpdate(
            @RequestParam String name,
            @RequestParam String aboutOffer,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String causeVisit,
            @RequestParam String aboutBaggage,
            @PathVariable(value = "id") long id,
            Model model) {
        OfferGuest offerGuest = offerGuestRepo.findById(id).orElseThrow();
        offerGuest.setName(name);
        offerGuest.setAboutOffer(aboutOffer);
        offerGuest.setCountry(country);
        offerGuest.setCity(city);
        offerGuest.setCauseVisit(causeVisit);
        offerGuest.setAboutBaggage(aboutBaggage);
        offerGuestRepo.save(offerGuest);
        return "redirect:/guests-offer";
    }

    @PostMapping("/guests-offer/{id}/edit/complete")
    public String offerGuestUpdateComplete(
            @RequestParam String name,
            @RequestParam String aboutOffer,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String causeVisit,
            @RequestParam String aboutBaggage,
            @PathVariable(value = "id") long id,
            Model model) {
        OfferGuest offerGuest = offerGuestRepo.findById(id).orElseThrow();
        offerGuest.setName(name);
        offerGuest.setAboutOffer(aboutOffer);
        offerGuest.setCountry(country);
        offerGuest.setCity(city);
        offerGuest.setCauseVisit(causeVisit);
        offerGuest.setAboutBaggage(aboutBaggage);
        offerGuestRepo.save(offerGuest);
        return "redirect:/guests-offer";
    }

    @PostMapping("/guests-offer/{id}/remove")
    public String offerGuestDelete(@PathVariable(value = "id") long id, Model model) {
        OfferGuest offerGuest = offerGuestRepo.findById(id).orElseThrow();
        offerGuestRepo.delete(offerGuest);
        return "redirect:/guests-offer";
    }

}
