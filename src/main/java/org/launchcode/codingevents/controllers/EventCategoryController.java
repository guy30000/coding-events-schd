package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("category")
public class EventCategoryController {

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @GetMapping
    public String displayAllCats(Model model, String  name){
        model.addAttribute("title", "All Categories");
        model.addAttribute("categories", eventCategoryRepository.findAll());
        return "category/index";
    }

    @GetMapping("add")
    public String displayAddCat(Model model){
        model.addAttribute("title", "Add New Category");
        model.addAttribute(new EventCategory());
        return "category/add";
    }

    @PostMapping("add")
    public String processAddCat(@ModelAttribute @Valid EventCategory newEventCat, Errors errors, Model model){
        if (errors.hasErrors()){
            model.addAttribute("title", "Add New Category");
            return "category/add";
        }

        eventCategoryRepository.save(newEventCat);
        return "redirect:";
    }

}
