import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class GameSolver {
    int largeurGrilleDeJeu;
    int compteur;
    FilePersonalisee grilleDeJeu;
    ArrayStack indicesHumainsATransformer;
    


    public void initialiseGrilleDeJeu(String nomDuFichier) {
        try (Scanner scanner = new Scanner(new File(nomDuFichier))) {
            if (scanner.hasNextLine()) {
                String[] dimensions = scanner.nextLine().split(" ");
                int n = Integer.parseInt(dimensions[0]);
                int m = Integer.parseInt(dimensions[1]);
                this.largeurGrilleDeJeu = m;
                this.grilleDeJeu = new FilePersonalisee(n * m); // Initialise la grille avec la capacité n*m
                
                while (scanner.hasNextInt()) {
                    this.grilleDeJeu.enqueue(scanner.nextInt());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


public void resoudreChaqueGrille(String nomDuFichier) {
    try (Scanner scanner = new Scanner(new File(nomDuFichier))) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                // Lecture des dimensions de la grille
                String[] dimensions = line.split(" ");
                int n = Integer.parseInt(dimensions[0]);
                int m = Integer.parseInt(dimensions[1]);
                this.largeurGrilleDeJeu = m;
                
                // Initialisation de la grille de jeu et des indices des humains à transformer
                this.grilleDeJeu = new FilePersonalisee(n * m);
                this.indicesHumainsATransformer = new ArrayStack(n * m); 

                // Lecture et stockage des états de chaque cellule de la grille
                for (int i = 0; i < n; i++) {
                    if (scanner.hasNextLine()) {
                        line = scanner.nextLine().trim();
                        String[] values = line.split(" ");
                        for (String value : values) {
                            this.grilleDeJeu.enqueue(Integer.parseInt(value));
                        }
                    }
                }

                // Résolution pour la grille actuelle
                outputResultat();
                
                // Réinitialisation pour la prochaine grille
                this.compteur = 0;
                this.grilleDeJeu = null;
                this.indicesHumainsATransformer = null;
            }
        }
    } catch (FileNotFoundException e) {
        System.err.println("Fichier non trouvé : " + e.getMessage());
    }
}



    public void analyserVoisin(int N) {
        // Parcourir chaque élément de la file
        for (int i = 0; i < this.grilleDeJeu.size(); i++) {
                if (this.grilleDeJeu.tab[i] == 2) {
                // Vérifier chaque voisin (gauche, droite, haut, bas)
                if (i % N != 0 && this.grilleDeJeu.tab[i - 1] == 1) { // gauche
                    this.indicesHumainsATransformer.push(i - 1);
                }
                if ((i + 1) % N != 0 && i + 1 < this.grilleDeJeu.capacite && this.grilleDeJeu.tab[i + 1] == 1) { // droite
                    this.indicesHumainsATransformer.push(i + 1);
                }
                if (i - N >= 0 && this.grilleDeJeu.tab[i - N] == 1) { // haut
                    this.indicesHumainsATransformer.push(i - N);
                }
                if (i + N < this.grilleDeJeu.capacite && this.grilleDeJeu.tab[i + N] == 1) { // bas
                    this.indicesHumainsATransformer.push(i + N);
                }
            }
        }
    }

    public void changeToZombie(int index) {
        this.grilleDeJeu.tab[index] = 2;
    }


    public void solve(int largeurGrille) {
        // Verifie si la pile etait vide avant l'analyse
        boolean pileEtaitVideAvantAnalyse = this.indicesHumainsATransformer.isEmpty();
        analyserVoisin(largeurGrille); // N doit être connu ou passé en paramètre
                        
        /* Verifie si la pile est encore vide, apres l'analyse 
         * Si aucun voisin humain n'existe, la pile devrait etre vide */ 
        boolean pileEstVideApresAnalyse = this.indicesHumainsATransformer.isEmpty();

        if (pileEtaitVideAvantAnalyse && pileEstVideApresAnalyse) {
            // Si la pile était avant et apres l'analyse tous les voisins ont ete transforme
            // a l'irération précédente ou alors il n'y avait aucun voisin a tranformer
            return;
        }

        // Autrement, la pile est necessairement pleine et il y a des humains a transformer
        while (!this.indicesHumainsATransformer.isEmpty()) {
            int index = this.indicesHumainsATransformer.pop();
            changeToZombie(index);
        }
        // Si la pile n'etait pas vide apres l'analyse, il a fallut une iteration pour 
        // transformer les humain. On incremente alors le compteur. La prochaine iteration 
        // verifiera si la pile est vide avant et apres l'analyse et s'arretera si c'est le cas 
        // (avec la valeur du compteur fixe a la valeur du compteur a l'iteration precendance)
        this.compteur++;
        solve(largeurGrille); // Appel récursif
    }

    public boolean verifierExistanceHumainNonInfecte() {
        for (int i = 0; i < this.grilleDeJeu.size(); i++) {
            if (this.grilleDeJeu.tab[i] == 1) {
                return true;
            }
        }
        return false;
    }

    public void outputResultat() {
        solve(this.largeurGrilleDeJeu); 
        if (verifierExistanceHumainNonInfecte()) {
            System.out.println("-1");
            return;
        }

        System.out.println(this.compteur + 1);
    }







    public static class FilePersonalisee {
        Integer[] tab; 
        int capacite;
        private int size;

        FilePersonalisee(int tailleVoulue) {
            this.capacite = tailleVoulue;
            this.size = 0;
            this.tab = new Integer[tailleVoulue];
        }

        public int size() {
            return this.size;
        }

        public boolean isEmpty() {
            return this.size == 0; 
        }

        /* La methode en fait rien si la taille size a atteint 
         * la capacite [size() ==  capacite] */ 
        public void enqueue(Integer valeur) { 

            if(size() != capacite) {
                tab[size()] = valeur;
                this.size++;
            }

        }
        public String toString() {
            StringBuilder sb = new StringBuilder("Contenu de la File: [");
            for (int i = 0; i < size(); i++) {
                sb.append(tab[i]);
                if (i >= 0 && (i!= size() -1 )) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }


    }





    public static class ArrayStack  {
        int capacite;

        private Integer[] pile; // utilisation d'un simple tableau pour l'implementation
        
        /* Le haut de la pile a comme index -1 lorsque le tableau  est vide 
           0 lorsque le tableau a un element, 1 lorsque le tableau a 2 element, 
           etc. */
        int dessus = -1; 

        // Constructeur
        ArrayStack(int capaciteVoulue) {
            this.capacite = capaciteVoulue;
            // safe cast; compiler may give warning
            this.pile = new Integer[capaciteVoulue]; 
        }

        public void push(Integer element) throws IllegalStateException {
            if (dessus + 1 == capacite) {
            int nouvelleCapacite = capacite * 2;
                Integer[]  nouvellePile = new Integer[nouvelleCapacite]; ; 
                for (int i = 0 ; i <= dessus; i++ ) {
                    nouvellePile[i] = pile[i];
                }
                pile = nouvellePile; 
                capacite = nouvelleCapacite;

            }
            //incremente l'index de dessus de pile
           dessus++;           
            pile[dessus] = element; 
        }

        public Integer pop() {
            if (dessus == -1) {
                return null;
            }

            Integer elementRetourne = pile[dessus];
            //Efface artificiellement l'element et
            // Decremente pour avoir un index de dessus valide               
            pile[dessus--] = null;
            return elementRetourne;
        }

        public boolean isEmpty() {
          // Lorsque le dessus a l'index -1, on sait que la pile est vide
            return (dessus == -1);
        }
    

        public Integer top() {
            if (isEmpty()) return null;
            return pile[dessus];
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("Contenu de la pile: [");
            for (int i = dessus; i >= 0; i--) {
                sb.append(pile[i]);
                if (i > 0) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }
    }   
    public static void main(String[] args) {

        GameSolver gameSolver = new GameSolver();
        gameSolver.resoudreChaqueGrille("sample.txt");

                                                                
    }

    
}
