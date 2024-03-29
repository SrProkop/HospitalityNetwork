package ru.sfedu.HospitalityNetwork.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @GetMapping("/feedback")
    public String about(Model model) {
        model.addAttribute("title", "Обратная связь");
        return "feedback";
    }

}
