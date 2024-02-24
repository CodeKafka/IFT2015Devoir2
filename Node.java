/* Creation d'une classe statique permet de l'existance d'objets 
 * Noeud qui peuvent etre créé et utilises independamment de l'existance d'une 
 * LinkedList; classe Node ne depant pas d'une instance de la classe 
 * englobante LinkedList */ 

public static class Node {
    Integer valeur; 
    Node next; 

    Node(Integer valeurInput) {
        this.valeur = valeurInput; 
        this.next = null; 
    }
}
