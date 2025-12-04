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

/**
 * Controlador para gestionar las operaciones CRUD de habilidades de programación.
 * 
 * Este servlet implementa el patrón MVC (Modelo-Vista-Controlador) y actúa como
 * intermediario entre las vistas JSP y los repositorios de datos MongoDB.
 * 
 * @author Mateo
 * @see Skill
 * @see ISkillRepository
 * @see IProfileRepository
 */
@WebServlet("/edit")
public class SkillController extends HttpServlet {
    
    /**
     * Repositorio para gestionar operaciones CRUD de habilidades.
     * Utiliza MongoDB como base de datos.
     */
    private final ISkillRepository skillRepo = new SkillRepositoryMongo();
    
    /**
     * Repositorio para gestionar el perfil del usuario.
     * Utiliza MongoDB como base de datos.
     */
    private final IProfileRepository profileRepo = new ProfileRepositoryMongo();

    
    /**
     * Maneja las peticiones GET para mostrar la parte de edición de habilidades.
     * 
     * Este método carga el perfil del usuario y la lista de habilidades desde la
     * base de datos y las envía a la vista JSP. También maneja la operación de
     * edición cuando se recibe el parámetro "action=edit".
     * 
     * @param req  Objeto HttpServletRequest con los datos de la petición
     * @param resp Objeto HttpServletResponse para enviar la respuesta
     * @throws ServletException Si ocurre un error relacionado con el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Cargar el perfil del usuario desde la base de datos
        Profile profile = profileRepo.getProfile();
        
        // Obtener todas las habilidades registradas
        List<Skill> skills = skillRepo.getAllSkills();

        // Establecer los atributos en el request para que estén disponibles en la JSP
        req.setAttribute("profile", profile);
        req.setAttribute("skills", skills);

        // Obtener los parámetros de acción e ID desde la URL
        String action = req.getParameter("action");
        String id = req.getParameter("id");

        // Si la acción es "edit" y se proporciona un ID, cargar la habilidad a editar
        if ("edit".equals(action) && id != null) {
            Skill skill = skillRepo.getSkillById(id);
            // Solo establecer el atributo si la habilidad existe
            if (skill != null) {
                req.setAttribute("skillToEdit", skill);
            }
        }
        
        // Redirigir a la página de edición (edit.jsp)
        req.getRequestDispatcher("/edit.jsp").forward(req, resp);
    }

    /**
     * Maneja las peticiones POST para procesar las operaciones CRUD de habilidades.
     * 
     * Este método procesa tres tipos de operaciones basadas en el parámetro "action":
     * 
     *   add: Agrega una nueva habilidad
     *   update: Actualiza una habilidad existente
     *   delete: Elimina una habilidad
     * 
     * Todas las operaciones incluyen validaciones completas y manejo de errores.
     * Después de una operación exitosa, se redirige al usuario a la página de
     * edición para evitar reenvíos duplicados del formulario.
     *
     * @param req  Objeto HttpServletRequest con los datos del formulario
     * @param resp Objeto HttpServletResponse para enviar la respuesta
     * @throws ServletException Si ocurre un error relacionado con el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Obtener el parámetro "action" que indica la operación a realizar
        String action = req.getParameter("action");

        try {
            // Agregar nueva habilidad
            if ("add".equals(action)) {
                // Obtener los parámetros del formulario
                String name = req.getParameter("name");
                String levelStr = req.getParameter("level");

                // Verificar que los campos obligatorios no estén vacíos
                if (name == null || name.trim().isEmpty() || levelStr == null) {
                    req.setAttribute("errorMessage", "Nombre y nivel son obligatorios.");
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                // Parsear y validar el nivel
                int level;
                try {
                    level = Integer.parseInt(levelStr);
                    
                    // Verificar que el nivel esté en el rango permitido
                    if (level < 1 || level > 100) {
                        req.setAttribute("errorMessage", "El nivel debe estar entre 1 y 100.");
                        forwardWithProfileAndSkills(req, resp);
                        return;
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("errorMessage", "Nivel inválido. Debe ser un número entero.");
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                // Verificar que no exista una habilidad con el mismo nombre
                if (skillRepo.existsByName(name.trim())) {
                    req.setAttribute("errorMessage", "La habilidad ya existe: " + name);
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                // Si todas las validaciones pasaron, crear y agregar la nueva habilidad
                Skill skill = new Skill(name.trim(), level);
                skillRepo.addSkill(skill);

            // Actualizar habilidad existente
            } else if ("update".equals(action)) {
                // Obtener los parámetros del formulario
                String id = req.getParameter("id");
                String name = req.getParameter("name");
                String levelStr = req.getParameter("level");

                // Verificar que todos los campos necesarios estén presentes
                if (id == null || name == null || levelStr == null) {
                    req.setAttribute("errorMessage", "Datos incompletos para actualizar.");
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                // Parsear y validar el nivel
                int level;
                try {
                    level = Integer.parseInt(levelStr);
                    
                    // Verificar que el nivel esté en el rango permitido
                    if (level < 1 || level > 100) {
                        req.setAttribute("errorMessage", "El nivel debe estar entre 1 y 100.");
                        forwardWithProfileAndSkills(req, resp);
                        return;
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("errorMessage", "Nivel inválido. Debe ser un número entero.");
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                // Verificar que la habilidad existe en la base de datos
                Skill existingSkill = skillRepo.getSkillById(id);
                if (existingSkill == null) {
                    req.setAttribute("errorMessage", "Habilidad no encontrada.");
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                // Evitar duplicados solo si el nombre cambia
                // (Permitir actualizar el nivel sin cambiar el nombre)
                if (!existingSkill.getName().equalsIgnoreCase(name.trim()) 
                        && skillRepo.existsByName(name.trim())) {
                    req.setAttribute("errorMessage", "Ya existe otra habilidad con ese nombre: " + name);
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }

                // Si todas las validaciones pasaron, actualizar la habilidad
                existingSkill.setName(name.trim());
                existingSkill.setLevel(level);
                skillRepo.updateSkill(existingSkill);

            // Eliminar habilidad
            } else if ("delete".equals(action)) {
                String id = req.getParameter("id");
                
                try {
                    // Verificar que se proporcionó un ID válido
                    if (id != null) {
                        skillRepo.deleteSkill(id);
                    }
                } catch (Exception e) {
                    // Capturar errores específicos de la operación de eliminación
                    req.setAttribute("errorMessage", "Error al eliminar: " + e.getMessage());
                    forwardWithProfileAndSkills(req, resp);
                    return;
                }
            }

           /** Después de procesar exitosamente la operación, redirigir al usuario
            * a la página de edición mediante GET para evitar reenvíos duplicados
            * del formulario si el usuario recarga la página
            */
            resp.sendRedirect(req.getContextPath() + "/edit");

        } catch (Exception e) {
            // Capturar cualquier error no manejado durante el procesamiento
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
