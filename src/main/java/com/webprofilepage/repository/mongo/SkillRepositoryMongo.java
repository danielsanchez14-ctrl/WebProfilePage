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

public class SkillRepositoryMongo implements ISkillRepository {
    private final MongoCollection<Document> collection;

    public SkillRepositoryMongo() {
        collection = MongoDBConnection.getDatabase().getCollection("skills");
    }

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

    @Override
    public boolean existsByName(String name) {
        return collection.find(Filters.eq("name", name)).first() != null;
    }

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

    @Override
    public void deleteSkill(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID de habilidad inválido");
        }
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

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

    @Override
    public void updateSkill(Skill skill) {
        Document doc = new Document()
                .append("name", skill.getName())
                .append("level", skill.getLevel());
        collection.replaceOne(Filters.eq("_id", new ObjectId(skill.getId())), doc);
    }
}