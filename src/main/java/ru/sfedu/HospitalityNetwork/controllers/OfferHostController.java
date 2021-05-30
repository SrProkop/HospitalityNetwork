package ru.sfedu.HospitalityNetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.sfedu.HospitalityNetwork.models.OfferGuest;
import ru.sfedu.HospitalityNetwork.models.OfferHost;
import ru.sfedu.HospitalityNetwork.models.User;
import ru.sfedu.HospitalityNetwork.repo.OfferHostRepo;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class OfferHostController {

    @Autowired
    OfferHostRepo offerHostRepo;

    @Value("${upload.path}")
    private String uploadPath;

    // Удалить
    @GetMapping("/hosts-offer")
    public String allOfferHost(Model model) {
        Iterable<OfferHost> offerHosts = offerHostRepo.findAll();
        model.addAttribute("offerHosts", offerHosts);
        return "hosts-offer";
    }

    @GetMapping("/hosts-offer/page{number}")
    public String allOfferHostMore(Model model,
                                   @PathVariable(value = "number") int number) {
        Iterable<OfferHost> offerHosts = offerHostRepo.findAll();
        ArrayList list = new ArrayList();
        for (OfferHost offerHost : offerHosts) {
            list.add(offerHost);
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
        model.addAttribute("offerHosts", listForPage);

        return "hosts-offer";
    }

    @PostMapping("/hosts-offer/page{number}")
    public String searchOfferHost(
                @RequestParam String country,
                @RequestParam String city,
                @PathVariable(value = "number") int number,
                Model model) {
        System.out.println(country + "  " + city);
        Iterable<OfferHost> offerHosts = offerHostRepo.findAll();
        ArrayList<OfferHost> list = new ArrayList();
        ArrayList<OfferHost> listCountry = new ArrayList();
        ArrayList<OfferHost> listCountryAndCity = new ArrayList();
        for (OfferHost offerHost : offerHosts) {
            list.add(offerHost);
        }
        if (country.trim().length() != 0) {
            for (OfferHost offerHost : list) {
                if (offerHost.getCountry().equalsIgnoreCase(country)) {
                    listCountry.add(offerHost);
                }
            }
            list = listCountry;
        }
        if (city.trim().length() != 0) {
            for (OfferHost offerHost : list) {
                if (offerHost.getCity().equalsIgnoreCase(city)) {
                    listCountryAndCity.add(offerHost);
                }
            }
            list = listCountryAndCity;
        }
        if (city.trim().length() == 0 && country.trim().length() == 0) {
            allOfferHostMore(model, 1);
        }
        model.addAttribute("offerHosts", list);
        return "hosts-offer";
    }

    @PostMapping("/hosts-offer/page{number}/next")
    public String allOfferHostNext(Model model,
                                   @PathVariable(value = "number") int number) {
        number++;
        return "/hosts-offer/page{number}";
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
            @RequestParam(defaultValue = "false") boolean checkbox,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            @PathVariable(value = "id") long id,
            @Valid OfferHost offerHost,
            BindingResult bindingResult,
            Model model) throws IOException {
        Optional<OfferHost> offerHostOptional = offerHostRepo.findById(id);
        offerHost.setAuthor(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            System.out.println(errorsMap.size());
            model.mergeAttributes(errorsMap);
            return offerHostEdit(id, model);
        } else {
            if (!file.isEmpty()) {
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + resultFilename));

                offerHost.setAvatar(resultFilename);
            } else {
                offerHost.setAvatar(offerHostOptional.get().getAvatar());
            }
            if (checkbox) {
                offerHost.setAvatar(null);
            }
            offerHostRepo.save(offerHost);
        }
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
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {
        OfferHost offerHost = offerHostRepo.findById(id).orElseThrow();
        offerHost.setName(name);
        offerHost.setAboutOffer(aboutOffer);
        offerHost.setCountry(country);
        offerHost.setCity(city);
        offerHost.setAddressHouse(addressHouse);
        offerHost.setAboutHouse(aboutHouse);
        if (!file.isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            offerHost.setAvatar(resultFilename);
        }
        offerHostRepo.save(offerHost);
        return "redirect:/hosts-offer";
    }

    @PostMapping("/hosts-offer/{id}/remove")
    public String offerHostDelete(@PathVariable(value = "id") long id, Model model) {
        OfferHost offerHost = offerHostRepo.findById(id).orElseThrow();
        offerHostRepo.delete(offerHost);
        return "redirect:/hosts-offer";
    }

}