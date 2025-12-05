package utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Gestiona la conexión con MongoDB para la aplicación.
 * <p>
 * Esta clase crea un cliente MongoDB reutilizable mediante un bloque estático.
 * Permite obtener la base de datos configurada y cerrar la conexión cuando
 * la aplicación termine.
 * </p>
 *
 * @author camil
 */
public class MongoDBConnection {

    /** Cadena de conexión al servidor MongoDB local. */
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";

    /** Nombre de la base de datos usada por la aplicación. */
    private static final String DB_NAME = "webprofiledb";

    /** Cliente MongoDB compartido. */
    private static final MongoClient client;

    // Inicializa el cliente al cargar la clase.
    static {
        client = MongoClients.create(CONNECTION_STRING);
    }

    /**
     * Obtiene la instancia de la base de datos configurada.
     *
     * @return la base de datos webprofiledb
     */
    public static MongoDatabase getDatabase() {
        return client.getDatabase(DB_NAME);
    }

    /**
     * Cierra el cliente MongoDB si está abierto.
     * Debe llamarse al apagar la aplicación.
     */
    public static void close() {
        if (client != null) client.close();
    }
}
