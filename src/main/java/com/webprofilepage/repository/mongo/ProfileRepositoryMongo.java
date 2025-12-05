package com.webprofilepage.repository.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.webprofilepage.model.Profile;
import com.webprofilepage.repository.IProfileRepository;
import org.bson.Document;
import utils.MongoDBConnection;

/**
 * Implementación del repositorio de perfiles usando MongoDB.
 * <p>
 * Esta clase gestiona un único documento de perfil dentro de la colección
 * <strong>profile</strong>. Si no existe, genera uno por defecto.
 * También permite guardar, eliminar o restablecer el perfil.
 * </p>
 *
 * @author camil
 */
public class ProfileRepositoryMongo implements IProfileRepository {

    /** Colección MongoDB donde se almacena el perfil. */
    private final MongoCollection<Document> collection;

    /**
     * Inicializa el repositorio conectándose a la colección "profile".
     */
    public ProfileRepositoryMongo() {
        this.collection = MongoDBConnection.getDatabase().getCollection("profile");
    }

    /**
     * Obtiene el único perfil almacenado.  
     * Si no existe, crea uno con valores por defecto y lo retorna.
     *
     * @return el perfil almacenado o uno nuevo si no existía
     */
    @Override
    public Profile getProfile() {
        Document doc = collection.find().first();
        if (doc == null) {
            Profile defaultProfile = new Profile(
                    "Programador Web",
                    "Biografía por defecto",
                    "Experiencia por defecto",
                    "contacto@ejemplo.com",
                    "default.png",
                    "banner.jpeg"
            );
            saveProfile(defaultProfile);
            return defaultProfile;
        }
        return documentToProfile(doc);
    }

    /**
     * Guarda o reemplaza el perfil actual usando operación upsert.
     * Solo se mantiene un documento en la colección.
     *
     * @param profile perfil a almacenar
     */
    @Override
    public void saveProfile(Profile profile) {
        Document doc = profileToDocument(profile);
        collection.replaceOne(new Document(), doc, new ReplaceOptions().upsert(true));
    }

    // --- Métodos auxiliares ---

    /**
     * Convierte un documento MongoDB en un objeto {@link Profile}.
     * Aplica valores por defecto cuando faltan campos.
     *
     * @param doc documento obtenido de MongoDB
     * @return objeto Profile con valores válidos
     */
    private Profile documentToProfile(Document doc) {
        Profile p = new Profile();

        p.setName(doc.getString("name") != null ? doc.getString("name") : "Programador Web");
        p.setBio(doc.getString("bio") != null ? doc.getString("bio") : "Biografía por defecto");
        p.setExperience(doc.getString("experience") != null ? doc.getString("experience") : "Experiencia por defecto");
        p.setContact(doc.getString("contact") != null ? doc.getString("contact") : "contacto@ejemplo.com");

        Object picObj = doc.get("profilePicture");
        p.setProfilePicture(picObj != null ? picObj.toString() : "default.png");

        String bannerColor = doc.getString("bannerColor");
        p.setBannerColor(bannerColor != null ? bannerColor : "#4A90E2");

        return p;
    }

    /**
     * Convierte un objeto {@link Profile} en un documento para MongoDB.
     *
     * @param p perfil a convertir
     * @return documento listo para almacenar
     */
    private Document profileToDocument(Profile p) {
        return new Document()
                .append("name", p.getName())
                .append("bio", p.getBio())
                .append("experience", p.getExperience())
                .append("contact", p.getContact())
                .append("profilePicture", p.getProfilePicture())
                .append("bannerColor", p.getBannerColor());
    }

    /**
     * Elimina todos los documentos de la colección de perfiles.
     * Solo debe existir uno, pero se borra por seguridad.
     */
    @Override
    public void deleteProfile() {
        collection.deleteMany(new Document());
    }

    /**
     * Restablece el perfil al estado inicial con valores por defecto.
     */
    @Override
    public void resetProfile() {
        collection.deleteMany(new Document());

        Profile defaultProfile = new Profile(
                "Programador Web",
                "Biografía por defecto",
                "Experiencia por defecto",
                "contacto@ejemplo.com",
                "default.png",
                "#4A90E2"
        );
        saveProfile(defaultProfile);
    }
}
