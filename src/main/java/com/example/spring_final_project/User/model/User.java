package com.example.spring_final_project.User.model;

import com.example.spring_final_project.Appointment.model.Appointment;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    private int age;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Appointment> appointments;


    public long getId() {
        return id;
    }

    public User() {
        appointments = new HashSet<>();
    }

    public User(String firstName, String lastName, int age, boolean isActive, Set<Appointment> appointments) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.isActive = isActive;
        this.appointments = appointments;
    }
}
