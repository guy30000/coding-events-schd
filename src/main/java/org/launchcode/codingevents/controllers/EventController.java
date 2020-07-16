package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.data.EventRepository;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @GetMapping
    public String displayAllEvents(@RequestParam(required=false) Integer categoryId, Model model, String description ){

        if (categoryId == null) {
            model.addAttribute("title", "All Events");
            model.addAttribute("events", eventRepository.findAll());
        } else {
            Optional<EventCategory> result = eventCategoryRepository.findById(categoryId);
                    if (!result.isPresent()){
                        model.addAttribute("title", "Invalid Category ID");
                    } else {
                        EventCategory caregory = result.get();
                        model.addAttribute("title", "Events in Category: " + caregory.getName() );
                        model.addAttribute("events", caregory.getEvents());
                    }
        }
        return "events/index";
    }

    @GetMapping("create")
    public String displayCreatEventForm(Model model) {
//        if (eventCategoryRepository == null){
//            model.addAttribute("title", "Create Catagory for Event");
//            model.addAttribute(new EventCategory());
//            return "category/add";
//        } This is simple thing to make it redirect if there are no cats
        model.addAttribute("title", "Create Events");
        model.addAttribute(new Event());
        model.addAttribute("categories", eventCategoryRepository.findAll());
        return "events/create";
    }

    @PostMapping("create")
    public String processEventForm(@ModelAttribute @Valid Event newEvent, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("title", "Create Event");
            model.addAttribute(new Event());
            model.addAttribute("categories", eventCategoryRepository.findAll());
            return "events/create";
        }
        //For some reasy which I have yet to figure out. I had to write a loop to set the catagory. I don't recall a similer problem in prior runs
        for (EventCategory testCat : eventCategoryRepository.findAll()) {
            System.out.println(testCat + "  xxeeeeeee Printing testcat id= " + testCat.getId()  +" name=" + testCat.getName());
            System.out.println(newEvent + "  xxeeeeeee Printing newEventCat plain= " + newEvent.getEventCategory() + " name=" + newEvent.getEventCategory().getName()+" ID=" + newEvent.getEventCategory().getId() );
            if (testCat.getId() == Integer.parseInt((newEvent.getEventCategory().getName()))){
                newEvent.setEventCategory(testCat);
                eventRepository.save(newEvent);
                break;
            }
        }
        return "redirect:";  //Redirects into root of specific controller
    }

    @GetMapping("delete")
    public String displaydeleteform(Model model){
        model.addAttribute("title", "Delete Events");
        model.addAttribute("events" , eventRepository.findAll());
        return "events/delete";
    }

    @PostMapping("delete")
    public String processdeleteform(@RequestParam(required = false) int[] eventIds){
        if (eventIds!=null){
            for (int id : eventIds){
                eventRepository.deleteById(id);
            }
        }
        return "redirect:";
    }

}
