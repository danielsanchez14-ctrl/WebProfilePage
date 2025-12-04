/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.webprofilepage.repository;

import com.webprofilepage.model.Skill;
import java.util.List;

/**
 *
 * @author kosmo
 */
public interface ISkillRepository {

    List<Skill> getAllSkills();

    Skill getSkillById(String id);

    void addSkill(Skill skill);

    void updateSkill(Skill skill);

    void deleteSkill(String id);

    boolean existsByName(String name);
}
