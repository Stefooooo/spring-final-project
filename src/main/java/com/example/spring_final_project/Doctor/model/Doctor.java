package com.example.spring_final_project.Doctor.model;

import com.example.spring_final_project.Appointment.model.Appointment;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String password;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    private String email;

    @Column(name = "years_of_expertise")
    private int yearsOfExpertise;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "doctor")
    private Set<Appointment> appointments;

    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    @Column
    private String description;

    public Doctor(String firstName, String password, String lastName, Specialty specialty) {
        this.firstName = firstName;
        this.password = password;
        this.lastName = lastName;
        this.specialty = specialty;
        this.appointments = new HashSet<>();
    }

    public Doctor(String firstName, String lastName, int yearsOfExpertise) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearsOfExpertise = yearsOfExpertise;
    }

    public UUID getId() {
        return id;
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

    public int getYearsOfExpertise() {
        return yearsOfExpertise;
    }

    public void setYearsOfExpertise(int yearsOfExpertise) {
        this.yearsOfExpertise = yearsOfExpertise;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
