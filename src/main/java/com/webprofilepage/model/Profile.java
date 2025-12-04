/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webprofilepage.model;

/**
 *
 * @author kosmo
 */
public class Profile {

    private String name;
    private String bio;
    private String experience;
    private String contact;
    private String profilePicture;  // nombre del archivo (ej. "foto.jpg")
    private String bannerColor;

    // Constructor vacío (requerido para Gson si decides usar JSON después)
    public Profile() {
        this.bannerColor = "#4A90E2"; // Azul por defecto
    }

    // Constructor completo
    public Profile(String name, String bio, String experience, String contact, String profilePicture, String bannerColor) {
        this.name = name;
        this.bio = bio;
        this.experience = experience;
        this.contact = contact;
        this.profilePicture = profilePicture;
        this.bannerColor = bannerColor;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getExperience() {
        return experience;
    }

    public String getContact() {
        return contact;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getBannerColor() {
        return bannerColor;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setBannerColor(String bannerColor) {
        this.bannerColor = bannerColor;
    }
}
