package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.data.EventRepository;
import org.launchcode.codingevents.data.TagRepository;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventCategory;
import org.launchcode.codingevents.models.Tag;
import org.launchcode.codingevents.models.dto.EventTagDTO;
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

    @Autowired
    private TagRepository tagRepository;

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
            //For some reasy which I have yet to figure out. I had to write a loop to set the catagory. I don't recall a similer problem in prior runs
            //after coding for 6.1 it suddenly started working with the correct coding.
//            if (testCat.getId() == Integer.parseInt((newEvent.getEventCategory().getName()))){
//                newEvent.setEventCategory(testCat);
//                eventRepository.save(newEvent);
//                break;
//            }
            eventRepository.save(newEvent);
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

    @GetMapping("detail")
    public String displayEventDetails(@RequestParam Integer eventId, Model model){
        Optional<Event> result = eventRepository.findById(eventId);
        if (!result.isPresent()){
            model.addAttribute("title", "Invalid Event ID: " + eventId);
        } else {
            Event event = result.get();
            model.addAttribute("title",  event.getName() + " Details");
            model.addAttribute("event" , event);
            model.addAttribute("tags", event.getTags());
        }
        return "events/detail";
    }

    @GetMapping("add-tag")
    public String displayAddTagForm(@RequestParam Integer eventId, Model model) {

        Optional<Event> result = eventRepository.findById(eventId);
        Event event = result.get();
        model.addAttribute("title", "Add Tag to: " + event.getName());
        model.addAttribute("tags", tagRepository.findAll());
        EventTagDTO eventTag = new EventTagDTO();
        eventTag.setEvent(event);
        model.addAttribute("eventTag", eventTag);
        return "events/add-tag";
    }

    @PostMapping("add-tag")
    public String processAddTagForm(@ModelAttribute @Valid EventTagDTO eventTag, Model model, Errors errors){
        if (!errors.hasErrors()){
            Event event = eventTag.getEvent();
            Tag tag = eventTag.getTag();
            if (!event.getTags().contains(tag)){
                System.out.println("xxddee " + tag + " " + event.getName());
                event.addTag(tag);
                eventRepository.save(event);
            }
            return "redirect:detail?eventId=" + event.getId();
        }
    return "redirect:add-tag";
    }



}
