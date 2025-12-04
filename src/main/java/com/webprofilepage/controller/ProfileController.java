/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.webprofilepage.controller;

import com.webprofilepage.model.Profile;
import com.webprofilepage.model.Skill;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author kosmo
 */
@WebServlet(name = "ProfileController", urlPatterns = {"/", "/profile"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
public class ProfileController extends HttpServlet {

    private IProfileRepository profileRepo = new ProfileRepositoryMongo();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Reinicio total: si se pasa ?resetdb=true, borra perfil y habilidades
        if ("true".equals(request.getParameter("resetdb"))) {
            profileRepo = new ProfileRepositoryMongo();

            // Borra el perfil
            MongoCollection<Document> profileCol = MongoDBConnection.getDatabase().getCollection("profile");
            profileCol.deleteMany(new Document());

            // Borra todas las habilidades
            MongoCollection<Document> skillsCol = MongoDBConnection.getDatabase().getCollection("skills");
            skillsCol.deleteMany(new Document());

            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Profile profile = profileRepo.getProfile();
        List<Skill> skills = new SkillRepositoryMongo().getAllSkills();

        request.setAttribute("profile", profile);
        request.setAttribute("skills", skills);
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String bio = request.getParameter("bio");
        String experience = request.getParameter("experience");
        String contact = request.getParameter("contact");

        Profile profile = profileRepo.getProfile();
        // Solo actualizar si el parámetro no es null ni vacío
        if (name != null && !name.trim().isEmpty()) {
            profile.setName(name.trim());
        }
        if (bio != null && !bio.trim().isEmpty()) {
            profile.setBio(bio.trim());
        }
        if (experience != null && !experience.trim().isEmpty()) {
            profile.setExperience(experience.trim());
        }
        if (contact != null && !contact.trim().isEmpty()) {
            profile.setContact(contact.trim());
        }

        // Subir foto de perfil
        Part filePart = request.getPart("profilePicture");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadDir = getServletContext().getRealPath("/uploads");
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String fullPath = uploadDir + File.separator + fileName;
            filePart.write(fullPath);

            // Redimensionar imagen a 180x180
            try {
                resizeImage(fullPath, 180, 180);
            } catch (IOException e) {
                // Si falla el redimensionado, usamos la original
                System.out.println("No se pudo redimensionar la imagen. Se usa la original.");
            }

            profile.setProfilePicture(fileName);
        }

        // banner
        String bannerColor = request.getParameter("bannerColor");
        if (bannerColor != null && bannerColor.matches("^#[0-9A-Fa-f]{6}$")) {
            profile.setBannerColor(bannerColor.trim());
        } else {
            if (profile.getBannerColor() == null || profile.getBannerColor().isEmpty()) {
                profile.setBannerColor("#4A90E2");
            }
        }

        profileRepo.saveProfile(profile);
        System.out.println("Perfil guardado: " + profile.getName() + " | Banner: " + profile.getBannerColor());
        response.sendRedirect(request.getContextPath() + "/skill"); // Redirige a la página de edición
    }

    // --- Método auxiliar para redimensionar imagen ---
    private void resizeImage(String filePath, int targetWidth, int targetHeight) throws IOException {
        File file = new File(filePath);
        String fileName = file.getName().toLowerCase();
        String format;
        if (fileName.endsWith(".png")) {
            format = "png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            format = "jpg";
        } else if (fileName.endsWith(".gif")) {
            format = "gif";
        } else {
            System.out.println("Formato no soportado: " + fileName + ". No se redimensionará.");
            return;
        }

        BufferedImage originalImage = ImageIO.read(file);
        if (originalImage == null) {
            throw new IOException("No se pudo decodificar la imagen: " + filePath);
        }

        // Corrección clave: usa el tipo correcto según el formato
        int imageType = "png".equals(format) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, imageType);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        ImageIO.write(resizedImage, format, file);
    }
}
