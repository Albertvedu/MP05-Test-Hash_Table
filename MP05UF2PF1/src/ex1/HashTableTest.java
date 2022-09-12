package ex1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    HashTable hs = new HashTable();

    /**
     * Método size
     * No funcionaba, la variable size no se incrementaba en ningún lado, ni put(), ni drop()
     *
     * Contemple el error que si sobreescribe un elemento no tiene que aumentar el tamaño de size.
     */
    @org.junit.jupiter.api.Test
    void size() {
        for (int i = 0; i <50 ; i++) { // estos se sobreescrien luego
            hs.put(String.valueOf(i), "valor "+i);
        }
        assertEquals(50, hs.size());

        // Si NO se introduce valor a key, da un mensaje de error y no insertar nada
        hs.put("", "valor nuevo");
        assertEquals(50, hs.size());


        // SobreEscribe los mismos 50 de antes
        for (int i = 0; i <50 ; i++) {
            hs.put(String.valueOf(i), "Nuevo "+i);
        }
        assertEquals(50, hs.size());

        // AQUI introduzco key ya existente para verificar si el size sigue siendo correcto
        hs.put("0", "valorsa 0");
        assertEquals(50, hs.size());

        // Aqui BORRO elemento y verifico que size sea corrrecto
        hs.drop("0");
        assertEquals(49, hs.size());
        // Aqui BORRO elemento y verifico que size sea corrrecto
        hs.drop("49");
        assertEquals(48, hs.size());
     //   assertEquals("", hs.toString());
    }



    /**
     * RealSize()
     * Devuelve correctamente el tamaño fijo del array
     */

    @org.junit.jupiter.api.Test
    void realSize() {

        hs.put("1","prueba 1");
        assertEquals(16, hs.realSize());

        for (int i = 0; i < 500 ; i++) {
            hs.put(String.valueOf(i), "Valor Nuevo: " + i);
        }
        assertEquals(16, hs.realSize());

    }




    /**  Método PUT()
     * En estas pruebas unitarias provoco colisiones con el mismo hash
     * para ver que realmente los coloca en el orden adecuado y donde toca
     * Según compruebo las colisiones estan bien, en su orden
     *
     * Tambien compruebo que si introduzco 100 valores, que ocupe las 16 posiciones del array
     *
     * FALLABA al escribir un elemento con la misma key, lo repetia
     * Solucionado: Ahora lo sobreescribe
     *
     * * FALLABA si NO introducias valor a key, te insertaba un elemento, pero no controlabas donde
     * Solucionado: Ahora no inserta nada y devuelve un mensaje de error
     */
    @Test
    void put() {

        hs.put("12","valor 1");
        assertEquals ("\n bucket[1] = [12, valor 1]", hs.toString());

        // AQUI INTRODUZCO LA MISMA CLAVE Y NO FUNCIONABA CORRECTAMENTE, YA QUE LO QUE HACIA ERA DUPLICAR EL ELEMENTO
        hs.put("12","valor 2");
        assertEquals ("\n bucket[1] = [12, valor 2]", hs.toString());

        //Colisión en posición "0"
        hs.put("0","Cosa A");
        assertEquals ("\n bucket[0] = [0, Cosa A]\n" +
                " bucket[1] = [12, valor 2]", hs.toString());

        //Colisión en la posición "0"
        hs.put("11","Cosa B");
        assertEquals ("\n bucket[0] = [0, Cosa A] -> [11, Cosa B]\n" +
                " bucket[1] = [12, valor 2]", hs.toString());

        //Colisión en la posición "0"
        hs.put("22","Cosa C");
        assertEquals ("\n bucket[0] = [0, Cosa A] -> [11, Cosa B] -> [22, Cosa C]\n" +
                " bucket[1] = [12, valor 2]", hs.toString());

        //Colisión en la posición "0"
        hs.put("33","Cosa D");
        hs.put("","prueba Key errónea");
        assertEquals ("\n bucket[0] = [0, Cosa A] -> [11, Cosa B] -> [22, Cosa C] -> [33, Cosa D]\n" +
                " bucket[1] = [12, valor 2]", hs.toString());

        /**
         * Apartir de aqui compruebo si añade correctamente en xtodo el tamaño del array.
         *
         *      PRIMERO BORRO TODOS LOS DATOS PARA COMERZAR DE NUEVO
         */

        for (int i = 0; i < 34 ; i++) {
            hs.drop(String.valueOf(i));
        }
        assertEquals("", hs.toString());

        /**
         * AQUI INSERTO VALORES PARA COMPROBAR TAMAÑO ARRAY
         * Va haciendo colisiones
         * Lo hace correctamente
         * Omito el assertEquals por que el array es enorme
         */
        for (int i = 0; i < 500 ; i++) {
            hs.put(String.valueOf(i), "Valor Nuevo: " + i);
        }
      //  assertEquals("muchos muchos", hs.toString());
    }


    /**
     *  GET()
     *  Todo bien, no detecto errores
     */
    @org.junit.jupiter.api.Test
    void get() {

        /**    Aqui introduzco un dato y acontinuación lo borro y verifico
         *  que el método get devuelve el valor correcto
         */
        hs.put("0","0");
        assertEquals("0", hs.get("0"));
        hs.drop("0"); // borrandolo

        /**
         * Aqui en la posición cero no hay elemento y devuelve correctamente null
         */
        assertEquals(null, hs.get("0"));

        //Aqui le pido un elemento que no existe y devuelve null correctamente
        assertEquals(null, hs.get("11"));

    }


    /**
     * Errores encontrados:
     *     -> Si borrabas primer elemento, lo borraba xtodo, colisiones incluidas
     *     -> Si el elemento a borrar tenia un key mayor que colisiones anteriores daba NULl POINTER EXECEPTIONS
     *
     *          TODO solucionado
     */

    @org.junit.jupiter.api.Test
    void drop() {


        hs.put("12","valor 1");
        assertEquals ("\n bucket[1] = [12, valor 1]", hs.toString());
        hs.put("1","valor 2");

        //Colisión en posición "1"
        assertEquals ("\n bucket[1] = [12, valor 1] -> [1, valor 2]", hs.toString());
        hs.put("0","Cosa A");
        assertEquals ("\n bucket[0] = [0, Cosa A]\n" +
                " bucket[1] = [12, valor 1] -> [1, valor 2]", hs.toString());

        //Colisión en la posición "0"
        hs.put("11","Cosa B");
        assertEquals ("\n bucket[0] = [0, Cosa A] -> [11, Cosa B]\n" +
                " bucket[1] = [12, valor 1] -> [1, valor 2]", hs.toString());

        //Colisión en la posición "0"
        hs.put("22","Cosa C");
        assertEquals ("\n bucket[0] = [0, Cosa A] -> [11, Cosa B] -> [22, Cosa C]\n" +
                " bucket[1] = [12, valor 1] -> [1, valor 2]", hs.toString());

        //Colisión en la posición "0"
        hs.put("33","Cosa D");
        assertEquals ("\n bucket[0] = [0, Cosa A] -> [11, Cosa B] -> [22, Cosa C] -> [33, Cosa D]\n" +
                " bucket[1] = [12, valor 1] -> [1, valor 2]", hs.toString());
        hs.put("3","Cosa 3");
        assertEquals ("\n bucket[0] = [0, Cosa A] -> [11, Cosa B] -> [22, Cosa C] -> [33, Cosa D]\n" +
                " bucket[1] = [12, valor 1] -> [1, valor 2]\n" + " bucket[3] = [3, Cosa 3]", hs.toString());

        //          BORRA última posición... correctamente
        hs.drop("33");
        assertEquals("\n bucket[0] = [0, Cosa A] -> [11, Cosa B] -> [22, Cosa C]\n bucket[1] = [12, valor 1] -> [1, valor 2]\n" + " bucket[3] = [3, Cosa 3]", hs.toString());

        // BORRA posición intermedia correctamente
        hs.drop("11");
               assertEquals("\n bucket[0] = [0, Cosa A] -> [22, Cosa C]\n bucket[1] = [12, valor 1] -> [1, valor 2]\n"
                       + " bucket[3] = [3, Cosa 3]", hs.toString());
            // BORRA el primer elemento correctamente despues de reparar el ERROR
        hs.drop("0");
        assertEquals("\n bucket[0] = [22, Cosa C]\n bucket[1] = [12, valor 1] -> [1, valor 2]\n"
                + " bucket[3] = [3, Cosa 3]", hs.toString());

        // Borrar uno que no existe...habiendo otros destras (colisiones) SOLUCIONADO
        hs.put("33","prueba 33");
        hs.put("55", "prueba 55");
        hs.drop("44");
        hs.drop("66");
        assertEquals("\n bucket[0] = [22, Cosa C] -> [33, prueba 33] -> [55, prueba 55]\n bucket[1] = [12, valor 1] -> [1, valor 2]\n"
                + " bucket[3] = [3, Cosa 3]",hs.toString());
    }
}