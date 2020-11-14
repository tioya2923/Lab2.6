package services;

import java.security.InvalidKeyException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Nodes<K,V> implements Map<K,V> {
    public Nodes() {

    }

    public class HashEntry<K,V> implements Entry<K,V> {
        protected K key;
        protected V value;
        public HashEntry(K k, V v) {
            key = k;
            value = v;
        }
        public V getValue() {
            return value;
        }
        public K getKey() {
            return key;
        }
        public V setValue(V val) {
            V oldValue = value;
            value = val;
            return oldValue;
        }
        public boolean equals(Object o) {
            HashEntry<K,V> ent;
            try {
                ent = (HashEntry<K,V>) o;

            }catch (ClassCastException ex) {
                return false;
            }return (ent.getKey() == key) && (ent.getValue()==value);
        }
    }
    protected Entry<K,V> AVALIABLE = new HashEntry<K,V>(null, null);
    protected int n = 10; // número de elementos no dicionário
    protected int prime, capacity; // fator primo e capacidade do arranjo de bucktes
    protected Entry<K,V>[] buckt; // arranjo de buckt
    protected long scale, shift; // shift e fator de ativação
    /** Criar uma tabela de hash cm o fator primo 109345121 e a capacidade fornecida. */
    public Nodes(int cap) {
        this(109345121, cap);
    }
    /** Cria uma tabela de hash com  fator primo e a capacidade informados */
    public Nodes(int p, int cap) {
        prime = p;
        capacity = cap;
        buckt = (Entry<K,V>[]) new Entry[capacity]; // cast seguro
        java.util.Random rand = new java.util.Random();
        scale = rand.nextInt(prime -1) + 1;
        shift = rand.nextInt(prime);
    }
    /** Determina se a chave é válida */
    protected void checkKey(K k) throws InvalidKeyException {
        if(k == null) throw new InvalidKeyException ("Chave inválida: null. ");

    }
    /* Função de hash aplicando o médtodo MAD para o código de hash padrão */
    public int hashValue(K key) {
        return (int) ((Math.abs(key.hashCode()*scale + shift)%prime)%capacity);
    }
    /** Retorna o número de elementos da tabela hash. */
    public int size() {
        return n;
    }


    public boolean isEmpty() {
        return false;
    }


    public boolean containsKey(Object key) {
        return false;
    }


    public boolean containsValue(Object value) {
        return false;
    }

    /** Verifica e retorna verdadeiro caso a tabela esteja vazia */
    public boolean isEmpy() {
        return (n == 0);
    }
    /** Retorna um objecto contendo todas as chaves. */
   /*
    public Iterable<K> keys() {
        PositionList<K> keys = new NodePositionList<K>();
        for (int i=0; i<capacity; i++)
            if((buckt[i] != null) && (buckt[i] != AVALIABLE))
                keys.addLast(buckt[i].getKey());
        return keys;
    }

    */
    /** Método de pesquisa auxiliar - retorna o índice da chave encontrada ou
     * -(a + 1), onde a é o índice da primeira posiçõ vazia ou livre encontrada
     */
    protected int findEntry(K key) throws InvalidKeyException {
        int avail = -1;
        checkKey(key);
        int i = hashValue(key);
        int j = i;
        do {
            Entry<K,V> e = buckt[i];
            if(e == null) {
                if (avail < 0)
                    avail = i;  // a chave não está na tabela
                break;
            }
            if (key.equals(e.getKey()))  // a chave é encontrada
                return i; // chave encontrada
            if (e == AVALIABLE) { // bucket está desativado
                if ((avail < 0))
                    avail = i; // lembre que esta posição está livre

            }
            i = (i + 1) % capacity; // continuar procurando
        }while (i != j);
        return -(avail +1); // primeira posição vazia ou livre
    }
    /** Retorna o valor associado com a chave.
     * @param key*/
    public V get(Object key) {
        int i = 0; // método auxiloar para encontrar a chave
        try {
            i = findEntry((K) key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        if (i<0) return null; // não existe valor para esta chave
        return buckt[i].getValue(); // retorna o valor encontrado neste caso
    }

    /** Insere um par chave-valor no mapa, substituindo o anterior, se existir. */
    public V put (K key, V value) {
        int i = 0; // encontra o espaço apropriado para este elemento
        try {
            i = findEntry(key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        if (i >= 0) // esta chave tem um valor.
            return ((HashEntry<K,V>)buckt[i]).setValue(value); // define o novo valor
        if (n >= capacity/2) {
            try {
                rehash(); // rehash para manter o fator de carga <= 0.5
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            try {
                i = findEntry(key); // encontra novamente o local apropriado para este elemento
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        buckt[-i-1] = new HashEntry<K,V>(key, value); // converte para o índice próprio
        n++;
        return null;  // não existe valor antigo
    }
    /** Duplica o tamanho da tabela de hash e rehash todos os elementos. */
    protected void rehash() throws InvalidKeyException {
        capacity = 2*capacity;
        Entry<K,V>[] old = buckt;
        buckt = (Entry<K,V>[]) new Entry[capacity]; // o novo bucket é duas vezes maior
        java.util.Random rand = new java.util.Random();
        scale = rand.nextInt(-1) +1; // novo fator de ativação para o hash
        shift = rand.nextInt(prime); // novo fator de deslocamento para o hash
        for (int i = 0; i<old.length; i++) {
            Entry<K,V> e = old[i];
            if((e != null) && (e != AVALIABLE)) { // um elemento válido
                int j = -1 - findEntry(e.getKey());
                buckt[j] = e;
            }
        }
    }
    /** Remove o par de chave.valor com uma chave específica.
     * @param key*/
    public V remove (Object key) {
        int i = 0;  // encontra primeiro a chave
        try {
            i = findEntry((K) key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        if (i<0) return null; // nada para remover
        V toReturn = buckt[i].getValue();
        buckt[i] = AVALIABLE; // marca este espaço como desativo
        n--;
        return toReturn;
    }


    public void putAll(Map<? extends K, ? extends V> m) {

    }


    public void clear() {

    }


    public Set<K> keySet() {
        return null;
    }


    public Collection<V> values() {
        return null;
    }


    public Set<Entry<K, V>> entrySet() {
        return null;
    }

}

