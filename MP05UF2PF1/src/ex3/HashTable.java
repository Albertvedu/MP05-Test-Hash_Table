package ex3;

// Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
// Modified by Fernando Porrino Serrano for academic purposes.

import java.util.ArrayList;

public class HashTable {
    private int INITIAL_SIZE = 16;
    private int size ;
    private HashEntry[] entries = new HashEntry[INITIAL_SIZE];
    private HashEntry[] tempEntries = new HashEntry[INITIAL_SIZE];


    /**
     * Error: Devolvía siempre cero
     * La variable no se incrementaba en ningún lado
     * Solucionado
     * @return Devuelve size
     */
    public int size(){
        return this.size;
    }

    /**
     * Funciona correctamente
     * @return Devuelve Real size
     */
    public int realSize(){
        return this.INITIAL_SIZE;
    }


    /**
     * En este métdo voy añadiendo elementos. Y si la posición esta ocupada, doblo el tamaño del array, hasta que encuentre
     * una posición libre para insertar el elemento.
     *
     * @param key key
     * @param value valor
     *        Cuando ya tenía hecho esto, re-leo el ejercicio y entonces veo que pedías que no borrara el código que no sirviera.
     *        Pero ahora veo, que si le pongo el código que borré costará mucho más de ver y entender este código.
     *        Y habiendo una copia en el ex2. creo que se puede comparar bien y mejor para ver la diferencia de código
     */
    public void put(String key, Cliente value) {
        boolean yaSobreEscrito = false;

        // Controla que se introduzca un valor para la key
        if (key.length()== 0){
            System.out.println("Valor de key no válido ");
        }else {
            int hash = getHash(key);
            final HashEntry hashEntry = new HashEntry(key, value);

            /**
             * SobreEscribe Elemento con la misma key
             * Extracción de meétodo, se ve el código mas claro
             */
            yaSobreEscrito = sobreEscribirElemento(key, value, yaSobreEscrito);

            if (!yaSobreEscrito) {  // Si no sobreEscribió, entonces entrará aquí

                if (entries[hash] == null) {  // Si la posición está vacia, Escribe
                    entries[hash] = hashEntry; // AQUI POSA VALOR  key, value A LA POSICIO hash (que es key) si esta buit
                    size++;
                } else {  // si la posición esta ocupada hará lo siguiente
                    /**
                     * Extracción de método, hace mas entendible el método put() solo con una mirada
                     */
                    hash = duplicarArray(key, hash);
                        // Una vez duplicado el array, vuelve a comprobar si tiene sitio libre para el elemento
                        // que acabar de insertar con el put()

                    /***
                     * AQUI OBSERVE PROBLEMA:
                     * con hash = 1, por mucho que doblara tamaño array, entraba en un bucle que nunca terminaba
                     * hash = 1 siempre daba hash 1 con cualquier tamaño de array
                     * Lo arregle toscamente, poniendo un contador.
                     * si supera un determinado número de vueltas, doblando el array, pues le sumo 1 al hash
                     * y SOLUCIONADO
                     */
                    int contadorLoco = 0;

                    while (entries[hash] != null) { // Si la posición sigue ocupada, vuelve a duplicar Array
                        contadorLoco ++;
                        duplicarArray(key, hash);

                        if (contadorLoco > 30000) hash++ ; // Si no encuentra un hash diferente, pues lo cambio
                    }
                    entries[hash] = hashEntry; // Finalmente, encontró sitio y aqui escribe el nuevo elemento
                    size++;
                }
            }
        }
    }

    /**
     * Extracción de método
     * Considero que ver mucho código apelotonado hace que cueste más de leer, aunque este comentado
     * En este método hace copia del array , duplica el array y rellena el array actual creado con
     * tamaño duplicado, con lo datos guardados en array temporal.
     *
     * Aqui duplico el array hasta encontrar posición libre para el nuevo elemento
     * @param key
     * @param hash
     * @param tempEntries Es un duplicado de Entries, que es el array de tamaño Initial_Size
     * @return
     */
    private int duplicarArray(String key, int hash) {
        tempEntries = entries;  // Hago un copia de Entries y la pongo en tempEntries.
        entries = new HashEntry[INITIAL_SIZE];

        for (int i = 0; i < tempEntries.length; i++) { // coge los valores de tempEntries y los escribe en entries

            if (tempEntries[i] != null) {  // Si hay valor en el temporal... entrará
                hash = getHash(String.valueOf(i));

                if (entries[hash] != null ) { // SI la posición hash este ocupada irá duplicando el array
                    INITIAL_SIZE = INITIAL_SIZE * 2; // lo duplica
                    hash = getHash(String.valueOf(i)); // Obtine nuevo hash, con el NUEVO valor de Initial_Size
                    entries = tempEntries; // regreso valores de temporal a emtries pa empezar de nuevo
                    duplicarArray(key, hash);  // ACCIÓN RECURSIVA
                    entries[hash] = tempEntries[i];  // Bucle for que rellena el array con los datos anteriores
                }else {
                    entries[hash] = tempEntries[i];  // Bucle for que rellena el array con los datos anteriores
                }
            }
        }
        return hash;
    }

    /**
     * Extracción de método
     * este método Sobre Escribe Elemento que tenga la misma key
     *
     * @param key
     * @param value
     * @param yaSobreEscrito
     * @return
     */
    private boolean sobreEscribirElemento(String key, Cliente value, boolean yaSobreEscrito) {
        for (int i = 0; i < INITIAL_SIZE; i++) {

            if ( entries[i] != null && entries[i].key.equals(key)  ) {
                entries[i].value = value.nombre + " " + value.email; // le doy el nuevo valor de nombre y email
                yaSobreEscrito = true; // indico que ya sobreEscribio, para que no escriba de nuevo
            }
        }
        return yaSobreEscrito;
    }


    /**
     * Returns 'null' if the element is not found.
     *
     * Este método funciona correctamente
     */
    public String get(String key) {
        int hash = getHash(key);
        if(entries[hash] != null) {
            HashEntry temp = entries[hash];

            while( !temp.key.equals(key))
                temp = temp.next;

            return temp.value;
        }

        return null;
    }


    /**
     * Errores encontrados:
     *     -> Aqui con el métod del ejercico anterior no funcionaba
     *     -> Lo he modificado, y a quedado de lo más simple posible, al no haber colisiones, es solo eliminar elemento
     *
     *          xTODO solucionado
     */
    public void drop(String key) {
       // int hash = getHash(key);

        for (int i = 0; i < entries.length; i++) {

            if ( entries[i] != null){
                 if( entries[i].key.equals(key)){
                     entries[i] = null;
                     size --;
                 }
            }
        }
    }



    /**
     * Obtiene el hash, funciona ok.
     * @param key
     * @return
     */
    private int getHash(String key) {
        // piggy backing on java string
        // hashcode implementation.
        return key.hashCode() % INITIAL_SIZE;
    }

    private class HashEntry {
        String key;
        String value;

        // Linked list of same hash entries.
        HashEntry next;
        HashEntry prev;

        public HashEntry(String key, Cliente value) {
            this.key = key;
            this.value = value.nombre + " " + value.email;
            this.next = null;
            this.prev = null;
        }

        @Override
        public String toString() {
            return "[" + key + ", " + value + "]";
        }
    }

    @Override
    public String toString() {
        int bucket = 0;
        StringBuilder hashTableStr = new StringBuilder();
        for (HashEntry entry : entries) {
            if(entry == null) {
                bucket++;
                continue;
            }
            hashTableStr.append("\n bucket[")
                    .append(bucket)
                    .append("] = ")
                    .append(entry.toString());
            bucket++;
            HashEntry temp = entry.next;
            while(temp != null) {
                hashTableStr.append(" -> ");
                hashTableStr.append(temp.toString());
                temp = temp.next;
            }
        }
        return hashTableStr.toString();
    }

    public ArrayList<String> getCollisionsForKey(String key) {
        return getCollisionsForKey(key, 1);
    }

    public ArrayList<String> getCollisionsForKey(String key, int quantity){
        /*
          Main idea:
          alphabet = {0, 1, 2}

          Step 1: "000"
          Step 2: "001"
          Step 3: "002"
          Step 4: "010"
          Step 5: "011"
           ...
          Step N: "222"

          All those keys will be hashed and checking if collides with the given one.
        * */

        final char[] alphabet = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        ArrayList<Integer> newKey = new ArrayList();
        ArrayList<String> foundKeys = new ArrayList();

        newKey.add(0);
        int collision = getHash(key);
        int current = newKey.size() -1;

        while (foundKeys.size() < quantity){
            //building current key
            String currentKey = "";
            for(int i = 0; i < newKey.size(); i++)
                currentKey += alphabet[newKey.get(i)];

            if(!currentKey.equals(key) && getHash(currentKey) == collision)
                foundKeys.add(currentKey);

            //increasing the current alphabet key
            newKey.set(current, newKey.get(current)+1);

            //overflow over the alphabet on current!
            if(newKey.get(current) == alphabet.length){
                int previous = current;
                do{
                    //increasing the previous to current alphabet key
                    previous--;
                    if(previous >= 0)  newKey.set(previous, newKey.get(previous) + 1);
                }
                while (previous >= 0 && newKey.get(previous) == alphabet.length);

                //cleaning
                for(int i = previous + 1; i < newKey.size(); i++)
                    newKey.set(i, 0);

                //increasing size on underflow over the key size
                if(previous < 0) newKey.add(0);

                current = newKey.size() -1;
            }
        }

        return  foundKeys;
    }
    /**
     * Extracción de Clase con Delegate
     * Lo extraje para separar los métodos del main
     * De esta forma da mas claridad al código ya que el main no cuesta de encontrar
     * Aqui había el main
     *
     */
}