package utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DB_NAME = "webprofiledb";
    private static MongoClient client;

    static {
        client = MongoClients.create(CONNECTION_STRING);
    }

    public static MongoDatabase getDatabase() {
        return client.getDatabase(DB_NAME);
    }

    public static void close() {
        if (client != null) client.close();
    }
}