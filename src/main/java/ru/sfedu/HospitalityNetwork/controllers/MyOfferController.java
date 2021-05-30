package ru.sfedu.HospitalityNetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.sfedu.HospitalityNetwork.models.Comment;
import ru.sfedu.HospitalityNetwork.models.OfferGuest;
import ru.sfedu.HospitalityNetwork.models.OfferHost;
import ru.sfedu.HospitalityNetwork.models.User;
import ru.sfedu.HospitalityNetwork.repo.OfferGuestRepo;
import ru.sfedu.HospitalityNetwork.repo.OfferHostRepo;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Controller
public class MyOfferController {

    @Autowired
    OfferGuestRepo offerGuestRepo;
    @Autowired
    OfferHostRepo offerHostRepo;

    @Value("${upload.path}")
    private String uploadPath;


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
        if (listOfferHost.size() == 0) {
            model.addAttribute("addOfferHost", "Добавить предложение хоста");
        }
        Iterable<OfferGuest> offerGuests = offerGuestRepo.findAll();
        List<OfferGuest> listOfferGuest = new ArrayList<>();
        for (OfferGuest offerGuest : offerGuests) {
            if (offerGuest.getAuthor().getId() == user.getId()) {
                listOfferGuest.add(offerGuest);
            }
        }
        model.addAttribute("offerGuests", listOfferGuest);
        if (listOfferGuest.size() == 0) {
            model.addAttribute("addOfferGuest", "Добавить запрос гостя");
        }
        return "my-offers";
    }

    @GetMapping("/offer-guest-add")
    public String offerGuestAdd(
            @AuthenticationPrincipal User user,
            Model model) {
        Iterable<OfferGuest> offerGuests = offerGuestRepo.findAll();
        List<OfferGuest> listOfferGuest = new ArrayList<>();
        for (OfferGuest offerGuest : offerGuests) {
            if (offerGuest.getAuthor().getId() == user.getId()) {
                return "redirect:/my-offers";
            }
        }
        return "offer-guest-add";
    }

    @PostMapping("/offer-guest-add")
    public String offerGuestAdd(
            @AuthenticationPrincipal User user,
            @Valid OfferGuest offerGuest,
            BindingResult bindingResult,
            Model model) {
        offerGuest.setAuthor(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            System.out.println(errorsMap.size());
            for (Map.Entry<String, String> item : errorsMap.entrySet()) {
                System.out.println("Key = " + item.getKey() + "   Value = " + item.getValue());
            }
            model.mergeAttributes(errorsMap);
            return offerGuestAdd(user, model);
        } else {
            offerGuestRepo.save(offerGuest);
        }

        return "redirect:/profile";
    }

    @PostMapping("/offer-guest-add/complete")
    public String offerGuestAddComplete(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String aboutOffer,
            @RequestParam String causeVisit,
            @RequestParam String aboutBaggage,
            @PathVariable(value = "id") long id,
            Model model) {
        OfferGuest post = new OfferGuest(name, country, city, aboutOffer, user, causeVisit, aboutBaggage);
        offerGuestRepo.save(post);
        return "redirect:/my-offers";
    }

    @GetMapping("/offer-host-add")
    public String offerHostAdd(
            @AuthenticationPrincipal User user,
            Model model) {
        Iterable<OfferHost> offerHosts = offerHostRepo.findAll();
        List<OfferHost> listOfferHost = new ArrayList<>();
        for (OfferHost offerHost : offerHosts) {
            if (offerHost.getAuthor().getId() == user.getId()) {
                return "redirect:/my-offers";
            }
        }
        return "offer-host-add";
    }

    @PostMapping("/offer-host-add")
    public String offerHostAdd(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file,
            @Valid OfferHost offerHost,
            BindingResult bindingResult,
            Model model) throws IOException {
        offerHost.setAuthor(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            System.out.println(errorsMap.size());
            model.mergeAttributes(errorsMap);
            return offerHostAdd(user, model);
        } else {
            if (!file.isEmpty()) {
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + resultFilename));

                offerHost.setAvatar(resultFilename);
            }
            offerHostRepo.save(offerHost);
        }
        return "redirect:/profile";
    }

    @PostMapping("/offer-host-add/complete")
    public String offerHostAddComplete(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String addressHouse,
            @RequestParam String aboutHouse,
            @RequestParam String aboutOffer,
            @PathVariable(value = "id") long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        OfferHost offerHost = new OfferHost(name, country, city, aboutOffer, user, addressHouse, aboutHouse);
        if (!file.isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            offerHost.setAvatar(resultFilename);
        }
        offerHostRepo.save(offerHost);
        return "redirect:/my-offers";
    }
}