package ex3;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * En este Test no repito todas las pruebas que hice en el ejercicio 1 y 2
 * Hago un test para comprobar que realmente funciona igualmente con un Objeto
 *
 */
class HashTableTest {

    HashTable hs = new HashTable();

    /**
     * Método size
     * No funcionaba, la variable size no se incrementaba en ningún lado
     *
     * Contemple el error que si sobreescribe un elemento no tiene que aumentar el tamaño de size.
     */
    @Test
    void size() {
        for (int i = 0; i <50 ; i++) { // estos se sobreescrien luego
            hs.put(String.valueOf(i), new Cliente("Nombre: " + i, "Email: " + i));
        }
        assertEquals(50, hs.size());

        // SobreEscribe y size es correcto
        hs.put("0", new Cliente("Nuevo: ", "SobreEscrito: "));
        assertEquals(50, hs.size());

        // SobreEscribe los mismos 50 de antes
        for (int i = 5; i <50 ; i++) {
            hs.put(String.valueOf(i), new Cliente("NombreNuevo: " , "EmailNuevo: " ));
        }
        assertEquals(50, hs.size());

        // Aqui BORRO elemento y verifico que size sea corrrecto
        hs.drop("10");
        assertEquals(49, hs.size());
        // Aqui BORRO elemento y verifico que size sea corrrecto
        hs.drop("49");
        assertEquals(48, hs.size());

    }



    /**
     * RealSize()
     * En este ejercicio con el Initial_Size de que multiplica por 2 a cada momento
     * hace dificil comprobarlo
     */

    @Test
    void realSize() {

        hs.put("0",new Cliente("Nombre: ", "Email: " ));
        assertEquals(16, hs.realSize());

        hs.put("3",new Cliente("otro: ", "mas: " ));
        assertEquals(16, hs.realSize());


    }




    /**  Método PUT()
     * AQUI en este método ya no existe colisiones. De existir colisión , dobla el tamaño del array
     *
     * FALLABA al insertar hash 1, SI hash 1 ya existía.
     *
     * FALLABA al escribir un elemento con la misma key, lo repetia
     * Solucionado: Ahora lo sobreescribe
     *
     * * FALLABA si NO introducias valor a key, te insertaba un elemento, pero no controlabas donde
     * Solucionado: Ahora no inserta nada y devuelve un mensaje de error
     */
    @Test
    void put() {
                    // Inserto un elemento
        hs.put("12",new Cliente("Primero: " , "primero: " ));
        hs.put("", new Cliente("valor: " , "key erróneo: "));
        assertEquals ("\n bucket[1] = [12, Primero:  primero: ]", hs.toString());

                //SobreEscribo el primero, creando 34 elementos más
        for (int i = 0; i < 34 ; i++) {
            hs.put(String.valueOf(i),new Cliente("Nombre: " + i, "Email: " + i));
        }
        assertEquals("\n" +
                " bucket[38] = [17, Nombre: 17 Email: 17]\n" +
                " bucket[39] = [18, Nombre: 18 Email: 18]\n" +
                " bucket[40] = [19, Nombre: 19 Email: 19]\n" +
                " bucket[51] = [2, Nombre: 2 Email: 2]\n" +
                " bucket[62] = [20, Nombre: 20 Email: 20]\n" +
                " bucket[63] = [21, Nombre: 21 Email: 21]\n" +
                " bucket[64] = [22, Nombre: 22 Email: 22]\n" +
                " bucket[65] = [23, Nombre: 23 Email: 23]\n" +
                " bucket[66] = [24, Nombre: 24 Email: 24]\n" +
                " bucket[67] = [25, Nombre: 25 Email: 25]\n" +
                " bucket[68] = [26, Nombre: 26 Email: 26]\n" +
                " bucket[69] = [27, Nombre: 27 Email: 27]\n" +
                " bucket[70] = [28, Nombre: 28 Email: 28]\n" +
                " bucket[71] = [29, Nombre: 29 Email: 29]\n" +
                " bucket[93] = [30, Nombre: 30 Email: 30]\n" +
                " bucket[94] = [31, Nombre: 31 Email: 31]\n" +
                " bucket[95] = [32, Nombre: 32 Email: 32]\n" +
                " bucket[96] = [33, Nombre: 33 Email: 33]\n" +
                " bucket[132] = [5, Nombre: 5 Email: 5]\n" +
                " bucket[133] = [6, Nombre: 6 Email: 6]\n" +
                " bucket[147] = [10, Nombre: 10 Email: 10]\n" +
                " bucket[155] = [7, Nombre: 7 Email: 7]\n" +
                " bucket[156] = [8, Nombre: 8 Email: 8]\n" +
                " bucket[157] = [11, Nombre: 11 Email: 11]\n" +
                " bucket[189] = [16, Nombre: 16 Email: 16]\n" +
                " bucket[241] = [3, Nombre: 3 Email: 3]\n" +
                " bucket[242] = [4, Nombre: 4 Email: 4]\n" +
                " bucket[282] = [9, Nombre: 9 Email: 9]\n" +
                " bucket[286] = [13, Nombre: 13 Email: 13]\n" +
                " bucket[287] = [14, Nombre: 14 Email: 14]\n" +
                " bucket[288] = [15, Nombre: 15 Email: 15]\n" +
                " bucket[497] = [0, Nombre: 0 Email: 0]\n" +
                " bucket[498] = [12, Nombre: 12 Email: 12]\n" +
                " bucket[499] = [1, Nombre: 1 Email: 1]", hs.toString());
                // SobreEscribo los primeros 24 dejando los diez últimos de anteriores
        for (int i = 0; i < 12 ; i++) {
            hs.put(String.valueOf(i),new Cliente("nuevo: " + i, "elemeto: " + i));
        }
        for (int i = 13; i < 24 ; i++) {
            hs.put(String.valueOf(i),new Cliente("nuevo: " + i, "elemeto: " + i));
        }

        // Con estos 'for' he dejado sin sobreescribir un elemento de en medio
        // correctamente el 12 no es sobreescrito y sale con el valor inicial 'Nombre'
        // Y en las ultimas posiciones observamos que tampoco sobreescribio nada por que no había valores allí
         assertEquals("\n" +
                 " bucket[38] = [17, nuevo: 17 elemeto: 17]\n" +
                 " bucket[39] = [18, nuevo: 18 elemeto: 18]\n" +
                 " bucket[40] = [19, nuevo: 19 elemeto: 19]\n" +
                 " bucket[51] = [2, nuevo: 2 elemeto: 2]\n" +
                 " bucket[62] = [20, nuevo: 20 elemeto: 20]\n" +
                 " bucket[63] = [21, nuevo: 21 elemeto: 21]\n" +
                 " bucket[64] = [22, nuevo: 22 elemeto: 22]\n" +
                 " bucket[65] = [23, nuevo: 23 elemeto: 23]\n" +
                 " bucket[66] = [24, Nombre: 24 Email: 24]\n" +
                 " bucket[67] = [25, Nombre: 25 Email: 25]\n" +
                 " bucket[68] = [26, Nombre: 26 Email: 26]\n" +
                 " bucket[69] = [27, Nombre: 27 Email: 27]\n" +
                 " bucket[70] = [28, Nombre: 28 Email: 28]\n" +
                 " bucket[71] = [29, Nombre: 29 Email: 29]\n" +
                 " bucket[93] = [30, Nombre: 30 Email: 30]\n" +
                 " bucket[94] = [31, Nombre: 31 Email: 31]\n" +
                 " bucket[95] = [32, Nombre: 32 Email: 32]\n" +
                 " bucket[96] = [33, Nombre: 33 Email: 33]\n" +
                 " bucket[132] = [5, nuevo: 5 elemeto: 5]\n" +
                 " bucket[133] = [6, nuevo: 6 elemeto: 6]\n" +
                 " bucket[147] = [10, nuevo: 10 elemeto: 10]\n" +
                 " bucket[155] = [7, nuevo: 7 elemeto: 7]\n" +
                 " bucket[156] = [8, nuevo: 8 elemeto: 8]\n" +
                 " bucket[157] = [11, nuevo: 11 elemeto: 11]\n" +
                 " bucket[189] = [16, nuevo: 16 elemeto: 16]\n" +
                 " bucket[241] = [3, nuevo: 3 elemeto: 3]\n" +
                 " bucket[242] = [4, nuevo: 4 elemeto: 4]\n" +
                 " bucket[282] = [9, nuevo: 9 elemeto: 9]\n" +
                 " bucket[286] = [13, nuevo: 13 elemeto: 13]\n" +
                 " bucket[287] = [14, nuevo: 14 elemeto: 14]\n" +
                 " bucket[288] = [15, nuevo: 15 elemeto: 15]\n" +
                 " bucket[497] = [0, nuevo: 0 elemeto: 0]\n" +
                 " bucket[498] = [12, Nombre: 12 Email: 12]\n" +
                 " bucket[499] = [1, nuevo: 1 elemeto: 1]", hs.toString());

        /**
         * AQUI INSERTO VALORES PARA COMPROBAR TAMAÑO ARRAY
         * Va haciendo colisiones
         * Lo hace correctamente
         * Omito el assertEquals por que el array es enorme
         */
//        for (int i = 0; i < 500 ; i++) {
//            hs.put(String.valueOf(i),new Cliente("Nombre: " + i, "Email: " + i));
//        }
      //  assertEquals("muchos muchos", hs.toString());
    }


    /**
     *  GET()
     *  Todo bien, no detecto errores
     */
    @Test
    void get() {

        /**    Aqui introduzco un dato y acontinuación lo borro y verifico
         *  que el método get devuelve el valor correcto
         */
        hs.put("0",new Cliente("Nombre: " , "Email: " ));
        assertEquals("Nombre:  Email: " , hs.get("0"));
        hs.drop("0"); // borrandolo

        /**
         * Aqui en la posición cero no hay elemento y devuelve correctamente null
         */
        assertEquals(null, hs.get("0"));

        //Aqui le pido un elemento que no existe y devuelve null correctamente
        assertEquals(null, hs.get("11"));

    }


    /**   Aqui al no haber colisiones es xtodo más simple
     *    elimina el elemto que coincide con la key sin problema
     */

    @Test
    void drop() {


        hs.put("12",new Cliente("Nombre: " , "Email: " ));
        assertEquals ("\n bucket[1] = [12, Nombre:  Email: ]", hs.toString());

            // BORRA el primer elemento correctamente despues de reparar el ERROR
        hs.drop("12");
        assertEquals("", hs.toString());


    }
}