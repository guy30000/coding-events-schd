package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventData;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("events")
public class EventController {



    @GetMapping
    public String displayAllEvents(Model model, String description ){
        model.addAttribute("title", "All Events");
        model.addAttribute("events", EventData.getAll());
        return "events/index";
    }

    @GetMapping("create")
    public String displayCreatEventForm(Model model) {
        model.addAttribute("title", "Create Events");
        model.addAttribute(new Event());
        model.addAttribute("types", EventType.values());
        return "events/create";
    }

    @PostMapping("create")
    public String processEventForm(@ModelAttribute @Valid Event newEvent, Errors errors, Model model){
        if (errors.hasErrors()){
            model.addAttribute("title", "Create Events");
            model.addAttribute("types", EventType.values());
            //model.addAttribute("errors", errors);
            //model.addAttribute("errors", "Bad Data");
            return "events/create";
        }
        EventData.add(newEvent);
        return "redirect:";  //Redirects into root of specific controller
    }

    @GetMapping("delete")
    public String displaydeleteform(Model model){
        model.addAttribute("title", "Delete Events");
        model.addAttribute("events" , EventData.getAll());
        return "events/delete";
    }

    @PostMapping("delete")
    public String processdeleteform(@RequestParam(required = false) int[] eventIds){
        if (eventIds!=null){
            for (int id : eventIds){
                EventData.remove(id);
            }
        }
        return "redirect:";
    }

}
