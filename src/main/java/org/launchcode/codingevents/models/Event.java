package org.launchcode.codingevents.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
//
//
@Entity
public class Event extends AbstractEntity {

    @Size(min = 3, max = 20, message = "Name must be 3 - 20 characters")
    @NotBlank(message = "Name is required")
    private String name;

    @Size(max = 500, message = "Desc must be less than 500 characters")
    private String description;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String contactEmail;

    @ManyToOne
    @NotNull(message = "Category is required")
    private EventCategory eventCategory;

    public Event(){}

    public Event(String name, String description, String contactEmail, EventCategory eventCategory) {
        this.name= name;
        this.description = description;
        this.contactEmail = contactEmail;
        this.eventCategory= eventCategory;   }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    @Override
    public String toString() {
        return name;
    }

}
