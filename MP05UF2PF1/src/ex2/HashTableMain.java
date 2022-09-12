package ex2;

/**
 * -> Refactor. con Refactor delegate
 * Para separar los métodos del Main
 *
 * Clase creada para el main que he extraido de la clase original
 * juntamente con el método Log que es private
 */
public class HashTableMain {
    public static void main(String[] args) {
        HashTable hashTable = new HashTable();

        // Put some key values.
        for (int i = 0; i < 30; i++) {
            final String key = String.valueOf(i);
            hashTable.put(key, key);
        }

        // Print the HashTable structure
        log("****   HashTable  ***");
        log(hashTable.toString());
        log("\nValue for key(20) : " + hashTable.get("20"));
    }

    private static void log(String msg) {
        System.out.println(msg);
    }

}