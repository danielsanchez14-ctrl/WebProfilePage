package com.webprofilepage.repository.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.webprofilepage.model.Skill;
import com.webprofilepage.repository.ISkillRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import utils.MongoDBConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del repositorio de habilidades usando MongoDB.
 * <p>
 * Esta clase gestiona operaciones CRUD sobre la colección <strong>skills</strong>.
 * Cada habilidad contiene un identificador, un nombre y un nivel asociado.
 * </p>
 *
 * @author camil
 */
public class SkillRepositoryMongo implements ISkillRepository {

    /** Colección MongoDB donde se almacenan las habilidades. */
    private final MongoCollection<Document> collection;

    /**
     * Inicializa el repositorio obteniendo la colección "skills".
     */
    public SkillRepositoryMongo() {
        collection = MongoDBConnection.getDatabase().getCollection("skills");
    }

    /**
     * Obtiene todas las habilidades almacenadas.
     *
     * @return lista de habilidades encontradas
     */
    @Override
    public List<Skill> getAllSkills() {
        List<Skill> skills = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Skill skill = new Skill();
                skill.setId(doc.getObjectId("_id").toString());
                skill.setName(doc.getString("name"));
                skill.setLevel(doc.getInteger("level", 0));
                skills.add(skill);
            }
        }
        return skills;
    }

    /**
     * Verifica si existe una habilidad con el nombre dado.
     *
     * @param name nombre de la habilidad
     * @return true si existe, false si no
     */
    @Override
    public boolean existsByName(String name) {
        return collection.find(Filters.eq("name", name)).first() != null;
    }

    /**
     * Agrega una nueva habilidad.
     * Valida que no exista otra con el mismo nombre.
     *
     * @param skill habilidad a registrar
     * @throws IllegalArgumentException si el nombre está duplicado
     */
    @Override
    public void addSkill(Skill skill) {
        if (existsByName(skill.getName())) {
            throw new IllegalArgumentException("Habilidad duplicada: " + skill.getName());
        }
        Document doc = new Document()
                .append("name", skill.getName())
                .append("level", skill.getLevel());
        collection.insertOne(doc);
    }

    /**
     * Elimina una habilidad dado su ID.
     *
     * @param id identificador de la habilidad
     * @throws IllegalArgumentException si el id es vacío o inválido
     */
    @Override
    public void deleteSkill(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID de habilidad inválido");
        }
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

    /**
     * Obtiene una habilidad por su ID.
     *
     * @param id identificador de la habilidad
     * @return la habilidad encontrada o null si no existe
     */
    @Override
    public Skill getSkillById(String id) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        if (doc == null)
            return null;

        Skill skill = new Skill();
        skill.setId(doc.getObjectId("_id").toString());
        skill.setName(doc.getString("name"));
        skill.setLevel(doc.getInteger("level"));
        return skill;
    }

    /**
     * Actualiza la información de una habilidad existente.
     *
     * @param skill habilidad con los nuevos datos
     */
    @Override
    public void updateSkill(Skill skill) {
        Document doc = new Document()
                .append("name", skill.getName())
                .append("level", skill.getLevel());
        collection.replaceOne(Filters.eq("_id", new ObjectId(skill.getId())), doc);
    }
}
