/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.webprofilepage.controller;

import com.webprofilepage.model.Profile;
import com.webprofilepage.model.Skill;
import com.webprofilepage.repository.IProfileRepository;
import com.webprofilepage.repository.ISkillRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.mongo.SkillRepositoryMongo;
import repository.mongo.ProfileRepositoryMongo;

import java.io.IOException;
import java.util.List;

@WebServlet("/edit")
public class SkillController extends HttpServlet {
    private final ISkillRepository skillRepo = new SkillRepositoryMongo();
    private final IProfileRepository profileRepo = new ProfileRepositoryMongo();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Siempre enviamos el perfil + habilidades a edit.jsp
        Profile profile = profileRepo.getProfile();
        List<Skill> skills = skillRepo.getAllSkills();

        req.setAttribute("profile", profile);
        req.setAttribute("skills", skills);

        // Acción opcional (por si se quiere precargar datos en edición)
        String action = req.getParameter("action");
        String id = req.getParameter("id");

        if ("edit".equals(action) && id != null) {
            Skill skill = skillRepo.getSkillById(id);
            if (skill != null) {
                req.setAttribute("skillToEdit", skill);
            }
        }

        req.getRequestDispatcher("/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                String name = req.getParameter("name");
                String levelStr = req.getParameter("level");

                if (name == null || name.trim().isEmpty() || levelStr == null) {
                    req.setAttribute("errorMessage", "Nombre y nivel son obligatorios.");
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                int level;
                try {
                    level = Integer.parseInt(levelStr);
                    if (level < 1 || level > 100) {
                        req.setAttribute("errorMessage", "El nivel debe estar entre 1 y 100.");
                        forwardWithProfileAndSkills(req, resp);
                        return;
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("errorMessage", "Nivel inválido.");
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                // Validar duplicado (RF2.5)
                if (skillRepo.existsByName(name.trim())) {
                    req.setAttribute("errorMessage", "La habilidad ya existe: " + name);
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                Skill skill = new Skill(name.trim(), level);
                skillRepo.addSkill(skill);

            } else if ("update".equals(action)) {
                String id = req.getParameter("id");
                String name = req.getParameter("name");
                String levelStr = req.getParameter("level");

                if (id == null || name == null || levelStr == null) {
                    req.setAttribute("errorMessage", "Datos incompletos para actualizar.");
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                int level;
                try {
                    level = Integer.parseInt(levelStr);
                    if (level < 1 || level > 100) {
                        req.setAttribute("errorMessage", "El nivel debe estar entre 1 y 100.");
                        forwardWithProfileAndSkills(req, resp);
                        return;
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("errorMessage", "Nivel inválido.");
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                // Verificar si el nombre ya existe en otra habilidad
                Skill existingSkill = skillRepo.getSkillById(id);
                if (existingSkill == null) {
                    req.setAttribute("errorMessage", "Habilidad no encontrada.");
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                // Evitar duplicado solo si el nombre cambia
                if (!existingSkill.getName().equalsIgnoreCase(name.trim()) && skillRepo.existsByName(name.trim())) {
                    req.setAttribute("errorMessage", "Ya existe otra habilidad con ese nombre: " + name);
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                existingSkill.setName(name.trim());
                existingSkill.setLevel(level);
                skillRepo.updateSkill(existingSkill);

            } else if ("delete".equals(action)) {
                String id = req.getParameter("id");
                try {
                    if (id != null) {
                        skillRepo.deleteSkill(id);
                    }
                } catch (Exception e) {
                    req.setAttribute("errorMessage", "Error al eliminar: " + e.getMessage());
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }
            }

            // Éxito: redirigir para evitar reenvíos
            resp.sendRedirect(req.getContextPath() + "/edit");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Error interno: " + e.getMessage());
            forwardWithProfileAndSkills(req, resp);
        }
    }

    // Método auxiliar para evitar repetir código
    private void forwardWithProfileAndSkills(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Profile profile = profileRepo.getProfile();
        List<Skill> skills = skillRepo.getAllSkills();
        req.setAttribute("profile", profile);
        req.setAttribute("skills", skills);
        req.getRequestDispatcher("/edit.jsp").forward(req, resp);
    }
}
