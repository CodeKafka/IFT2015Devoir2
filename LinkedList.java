public class LinkedList {

    /* Permet d'initialiser le Noeud de tete, de queue, 
     * ainsi que le taille de la liste chainee */ 
    private Node head;
    private Node tail; 
    private int size; 

    // Accesseur
    public int size() {
        return this.size;
    }

    // Accesseur
    public Node getHead() {
        return this.head; 
    }

    // Accesseur 
    public Node getTail() {
        return this.tail; 
    }

    // Constructeur
    public LinkedList() {
        this.head = new Node(null);
        this.tail = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Integer first() {
        if (head.next != null) {

        // Retourn null si la liste est vide
        return head.next.valeur; 
        }
        return null;
    }

    // Verifie que la tail existe et retourne sa valeur
    public Integer last() {
        if (!isEmpty()) {
            return tail.valeur;
        }
        return null;
    }

    public void addFirst(Integer valeurInput) {
        /* Creer un nouveau Noed, cree un lien nouveau Noeud --> (ancient) 1er element 
         * et cree un noeud head --> nouveau Noeud (qui est le nouveau 1er element) */ 
        Node nouveauNoeud = new Node(valeurInput); 
        nouveauNoeud.next = head.next;
        head.next = nouveauNoeud; 
        // Lorsque la liste est vide, le premier element ajoute est a la tail.
        if (isEmpty()) {
            tail = nouveauNoeud;
        }
        size++;

        if (size == 1) {
            tail = nouveauNoeud;
        }
    }


    /* Creer un nouveau Noeud, cree un lien (acient) tail --> nouveau Noeud, 
     * et declare nouveau tail <-- nouveau Noeud */
    public void addLast(Integer valeurInput) {
        Node nouveauNoeud = new Node(valeurInput);
        if (isEmpty()) {
            head.next = nouveauNoeud;
            tail = nouveauNoeud;
        } else {
            tail.next = nouveauNoeud;
            tail = nouveauNoeud;
        }
        size++;
    }

    public void removeFirst() {
        if (!isEmpty()) {

            if (size() == 1) {
                head.next = null; 
                tail = null;
                size--;
                return;
            }

            Node temp = head.next;
            head.next = temp.next;
            size--;
        }

        if (isEmpty()) {
            tail = null; 
        }
    }

    public void  removeLast() {
        if (!isEmpty()) {
            if (size() == 1) {
                removeFirst();
                return;
            }

            Node pointer = head; 
            while (pointer.next.next != null) {
                pointer = pointer.next;
            }
            pointer.next = null;
            tail = pointer; 
            size--; 


        }   
    }


    /* Creation d'une classe statique permet de l'existance d'objets 
     * Noeud qui peuvent etre créé et utilises independamment de l'existance d'une 
     * LinkedList; classe Node ne depant pas d'une instance de la classe 
     * englobante LinkedList */ 

    private static class Node {
        Integer valeur; 
        Node next; 

        Node(Integer valeurInput) {
            this.valeur = valeurInput; 
            this.next = null; 
        }
    }

    public void removeValue(int value) {
        Node pointer = head()
        if (isEmpty){
            return;
        }

        if (size() == 1 && (first().valeur == value)) {
            removeFirst();
            return;
        }
        else if (size() == 1 && (first().valeur != value)) {
            return;
        }

        else if (pointer.next == )
        
        if (ponter.valeur == value) {

        }

    }

}
