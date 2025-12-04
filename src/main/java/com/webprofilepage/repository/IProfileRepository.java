/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.webprofilepage.repository;

import com.webprofilepage.model.Profile;

/**
 *
 * @author kosmo
 */
public interface IProfileRepository {

    Profile getProfile();

    void saveProfile(Profile profile);

    void deleteProfile();

    public void resetProfile();
}
