package ex2;

// Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
// Modified by Fernando Porrino Serrano for academic purposes.

import java.util.ArrayList;

public class HashTable {
    private int INITIAL_SIZE = 16;
    private int size ;
    private HashEntry[] entries = new HashEntry[INITIAL_SIZE];


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
     * Fallaba al introducir un elemento con la misma key
     *
     * Solucionado: Ahora lo sobreEscribe
     * @param key key
     * @param value valor
     *    Cuando ya tenía hecho esto, re-leo el ejercicio y entonces veo que pedías que no borrara el código que no sirviera.
     *    Pero ahora veo, que si le pongo el código que borré costará mucho más de ver y entender este código.
     *    Y habiendo una copia en el ex2. creo que se puede comparar bien y mejor para ver la diferencia de código
     */
    public void put(String key, String value) {
        boolean yaSobreEscrito = false;

        // Controla que se introduzca un valor para la key
        if (key.length()== 0){
            System.out.println("Valor de key no válido ");
        }else {
            int hash = getHash(key);
            final HashEntry hashEntry = new HashEntry(key, value);// borra valors prev i next i don valor key, value

            if (entries[hash] == null) {
                entries[hash] = hashEntry; // AQUI POSA VALOR  key, value A LA POSICIO hash (que es key) si esta buit
                size++;
            } else {
                HashEntry temp = entries[hash];   // temporal guarda el dato que hi havia
                while (temp.next != null) {
                    /**
                     * Viendo el nombre del método es mas fácil de saber que hace que no pas viendo unas líneas de código
                     *
                     * */
                    yaSobreEscrito = sobreEscribirElemento(key, value, yaSobreEscrito, temp);
                    temp = temp.next;
                }
                /**
                 * Aqui repito el mismo bucle anterior para que entre en el caso de que no haya colisiones
                 */
                yaSobreEscrito = sobreEscribirElemento(key, value, yaSobreEscrito, temp);
                /**
                 * AQUI entrara si no a sobreEscrito ningún elemento
                 */
                añadeColision(yaSobreEscrito, hashEntry, temp);
            }
        }
    }

    /**
     * Extracción de método
     * Viendo el nombre del método es mas fácil de saber que hace que no pas viendo unas líneas de código
     * Aqui solo entrará si no a sobreEscrito ningun elemento
     * por tanto añade colisión nueva
     *
     * @param yaSobreEscrito booleano para saber si habia sobreEscrito o no
     * @param hashEntry  Contiene el valor del elemento que introduce put()
     * @param temp Esto es el hashentry con su hash
     */
    private void añadeColision(boolean yaSobreEscrito, HashEntry hashEntry, HashEntry temp) {
        if (!yaSobreEscrito){
            temp.next = hashEntry;  // temporal buit igual key, value
            hashEntry.prev = temp;  // guarda a previus el ultim valor que hi havia
            size++;
        }
    }

    /**
     * Extracción de método
     * Primero que de esta forma con el nombre del método ya se sabe que hace el código
     * y después es que se repetia dos veces
     * Aqui mientras va avanzando para buscar el último elemento, voy comprobando que la key introducida
     * no sea de un elemento ya existente, de ser así, lo sobreescribe.
     * Y pongo un boleano para cuando llegue al último elemento no escriba nada, si ya sobreEscribió
     *
     * @param key
     * @param value
     * @param yaSobreEscrito booleano para saber si entro aquí o no entró
     * @param temp
     * @return
     */
    private boolean sobreEscribirElemento(String key, String value, boolean yaSobreEscrito, HashEntry temp) {
        if (temp.key.equals(key)) {
            temp.value = value;
            yaSobreEscrito = true;
        }
        return yaSobreEscrito;
    }

    /**
     * Returns 'null' if the element is not found.
     *
     * -> Este método funciona correctamente
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
     *     -> Si borrabas primer elemento, lo borraba xtodo, colisiones incluidas
     *     -> Si el elemento a borrar tenia un key mayor que colisiones anteriores daba NULL POINTER EXECEPTIONS
     *
     *          xTODO solucionado
     */
    public void drop(String key) {
        int hash = getHash(key);
        if(entries[hash] != null) {

            HashTable.HashEntry temp = entries[hash];
            while( (!temp.key.equals(key)) && (temp.next != null)) {

                temp = temp.next;
            }
            /**
             * UFF  con el while anterior verificando el null o el if de acontinuación verificando la key
             * soluciono el tema que cuando borraba un elemento que ya habia sido borrado
             * Sucedia que el elemento siguiente tenia una key superior a la que estaba buscando para eliminar
             * Entonces por mucho next que hiciera, nunca encontraba el elemento que quier borrar y daba Null pointer Exception
             *
             * En el caso de que el elemento que busco para borrar estuviera detras de otros elementos, entonces si que
             * en el while anterior va entrando y haciendo next buscando el elemento, y si no existe no hay problema .
             *
             */
            if (temp.key.equals(key)) { // UFFF verifica key - si la key no existe se lo salta xtodo y no hace nada.
                if (temp.prev == null) {
                    // Extración de método,
                    eliminarPrimerElemento(hash);
                } else {
                    // Extracción de método
                    eliminaColisión(temp);
                }
                size--;
            }
        }
    }

    /**
     * Extracción de método, se ve más claro con el nombre del método que hace este código
     * @param temp
     */
    private void eliminaColisión(HashEntry temp) {
        if (temp.next != null)
            temp.next.prev = temp.prev;   //esborrem temp, per tant actualitzem l'anterior al següent
        temp.prev.next = temp.next;                         //esborrem temp, per tant actualitzem el següent de l'anterior
    }

    /**
     * Extracción de método, se ve más claro con el nombre del método que hace este código
     * @param hash
     */
    private void eliminarPrimerElemento(int hash) {
        /**
         * Aqui no actuaba correctamente
         * Ahora en caso de ser el primer elemento lo borra dejando los siguientes
         */
        entries[hash] = entries[hash].next;  // AQUI al primer valor le doy el valor del siguiente
        if (entries[hash] != null)
            entries[hash].prev = null; // En caso de no haber siguiente pues hash borrado entero
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

        public HashEntry(String key, String value) {
            this.key = key;
            this.value = value;
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