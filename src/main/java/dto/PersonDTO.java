/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Hobby;
import entities.Person;
import java.util.List;

/**
 *
 * @author rasmu
 */
public class PersonDTO {
    private Long id;
    private String email;
    private int phone;
    private String firstName;
    private String lastName;
    private List<Hobby> hobbyList;

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.email = person.getEmail();
        this.phone = person.getPhone();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.hobbyList = person.getHobbyList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    
}
