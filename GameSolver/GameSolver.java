import java.io.File; // Importe la classe File
import java.io.FileNotFoundException; // Importe cette classe pour gérer les erreurs
import java.util.Scanner; // Importe la classe Scanner pour lire les fichiers texte "sample.txt"

/**
 * Classe permettant de résoudre le jeu de contamination par zombies. 
 * Cette classe utilise une approche récursive et itérative pour transformer l'ensemble des humain susceptible d'être 
 * contraminé. Lorsque l'état final la grille est atteint et qu'aucun autre humain ne peut être contaminé, 
 * le programme indique s'il reste encore des humains sains. S'il ne reste plus d'humain sain, le programme 
 * affiche le nombre d'itérations nécessaire pour obtenir un contamination totale, et ce, pour chaque grille 
 * du fichier 'sample.txt'.
 */
public class GameSolver {
    int largeurGrilleDeJeu; // Largeur de la grille de jeu 
    int compteur; // Sert à compter le nombre d'itérations nécessaires pour résoudre le jeu
    FilePersonalisee grilleDeJeu; // Structure de données personnalisée pour stocker la grille de jeu
    ArrayStack indicesHumainsATransformer; // Pile pour stocker les indices des éléments à transformer
    

    /**
     * Initialise la grille de jeu en considérer une portion du fichier donné.
     * Le fichier doit contenir les dimensions de la grille suivies des valeurs de chaque cellule.
     * Le fichier peut contenir les information de plusieurs grilles, présentées séquentiellement 
     * @param nomDuFichier Le chemin vers le fichier contenant les données de la grille (répertoire courant)
     */
    public void initialiseGrilleDeJeu(String nomDuFichier) {
        try (Scanner scanner = new Scanner(new File(nomDuFichier))) {
            if (scanner.hasNextLine()) {
                String[] dimensions = scanner.nextLine().split(" ");
                int n = Integer.parseInt(dimensions[0]); // Nombre de lignes
                int m = Integer.parseInt(dimensions[1]); // Nombre de colonnes
                this.largeurGrilleDeJeu = m;
                this.grilleDeJeu = new FilePersonalisee(n * m); // Initialise la grille avec la capacité n*m
                
                while (scanner.hasNextInt()) {
                    this.grilleDeJeu.enqueue(scanner.nextInt()); // Enfile les valeurs de la grille
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lit un fichier contenant plusieurs grilles de jeu et les résout une par une.
     * Chaque grille est délimitée par l'information de dimension de la grille suivante. 
     * @param nomDuFichier Le chemin vers le fichier contenant les grilles de jeu.
     */
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

    /**
     * Analyse les voisins de chaque élément de la grille et stocke les indices des éléments à transformer.
     * @param N La largeur de la grille de jeu.
     */
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


    /**
     * Transforme un élément de la grille en zombie.
     * @param index L'indice de l'élément à transformer.
     */
    public void changeToZombie(int index) {
        this.grilleDeJeu.tab[index] = 2;
    }

    /**
     * Résout la grille de jeu en transformant les éléments nécessaires jusqu'à ce qu'il n'y ait plus d'éléments à transformer.
     * @param largeurGrille La largeur de la grille de jeu.
     */
    public void solve(int largeurGrille) {
        // Vérifie si la pile était vide avant l'analyse
        boolean pileEtaitVideAvantAnalyse = this.indicesHumainsATransformer.isEmpty();
        analyserVoisin(largeurGrille); // N doit être connu ou passé en paramètre
                        
        /* Vérifie si la pile est encore vide après l'analyse.
         * Si aucun voisin humain n'existe, la pile devrait être vide. */ 
        boolean pileEstVideApresAnalyse = this.indicesHumainsATransformer.isEmpty();

        if (pileEtaitVideAvantAnalyse && pileEstVideApresAnalyse) {
            // Si la pile était vide avant et après l'analyse, tous les voisins ont été transformés
            // lors de l'itération précédente ou il n'y avait aucun voisin à transformer.
            return;
        }

        // Autrement, la pile est nécessairement pleine et il y a des humains à transformer.
        while (!this.indicesHumainsATransformer.isEmpty()) {
            int index = this.indicesHumainsATransformer.pop();
            changeToZombie(index);
        }
        // Si la pile n'était pas vide après l'analyse, il a fallu une itération pour
        // transformer les humains. On incrémente alors le compteur. La prochaine itération
        // vérifiera si la pile est vide avant et après l'analyse et s'arrêtera si c'est le cas
        // (avec la valeur du compteur fixée à la valeur du compteur à l'itération précédente).
        this.compteur++;
        solve(largeurGrille); // Appel récursif
    }

    /**
     * Vérifie l'existence d'humains non infectés dans la grille de jeu.
     * @return true s'il existe au moins un humain non infecté, false autrement.
     */
    public boolean verifierExistanceHumainNonInfecte() {
        for (int i = 0; i < this.grilleDeJeu.size(); i++) {
            if (this.grilleDeJeu.tab[i] == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Affiche le résultat de la résolution de la grille de jeu.
     * Imprime -1 si des humains non infectés restent, ou le nombre d'itérations sinon.
     */
    public void outputResultat() {
        solve(this.largeurGrilleDeJeu); 
        if (verifierExistanceHumainNonInfecte()) {
            System.out.println("-1");
            return;
        }

        System.out.println(this.compteur + 1);
    }

    /**
     * Classe interne représentant une file personnalisée pour gérer les éléments de la grille de jeu.
     */
    public static class FilePersonalisee {
        Integer[] tab; // Tableau pour stocker les éléments de la file
        int capacite; // Capacité maximale de la file
        private int size; // Nombre d'éléments actuellement dans la file

        /**
         * Constructeur pour initialiser la file avec une capacité spécifique.
         * @param tailleVoulue La capacité initiale de la file.
         */
        FilePersonalisee(int tailleVoulue) {
            this.capacite = tailleVoulue;
            this.size = 0;
            this.tab = new Integer[tailleVoulue];
        }

        /**
         * Retourne le nombre d'éléments dans la file.
         * @return Le nombre d'éléments.
         */
        public int size() {
            return this.size;
        }

        /**
         * Vérifie si la file est vide.
         * @return true si la file est vide, false autrement.
         */
        public boolean isEmpty() {
            return this.size == 0; 
        }

        /**
         * Ajoute un élément à la fin de la file si elle n'est pas pleine.
         * @param valeur L'élément à ajouter.
         */
        public void enqueue(Integer valeur) { 
            if(size() != capacite) {
                tab[size()] = valeur;
                this.size++;
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("Contenu de la File: [");
            for (int i = 0; i < size(); i++) {
                sb.append(tab[i]);
                if (i >= 0 && (i != size() -1 )) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }
    }

    /**
     * Classe interne représentant une pile personnalisée pour gérer les indices des éléments à transformer.
     */
    public static class ArrayStack {
        int capacite; // Capacité initiale de la pile
        private Integer[] pile; // Tableau pour stocker les éléments de la pile
        int dessus = -1; // Indice du dernier élément ajouté à la pile

        /**
         * Constructeur pour initialiser la pile avec une capacité spécifique.
         * @param capaciteVoulue La capacité initiale de la pile.
         */
        ArrayStack(int capaciteVoulue) {
            this.capacite = capaciteVoulue;
            this.pile = new Integer[capaciteVoulue];
        }

        /**
         * Ajoute un élément au sommet de la pile. Double la capacité de la pile si nécessaire.
         * @param element L'élément à ajouter.
         * @throws IllegalStateException Si la pile est pleine.
         */
        public void push(Integer element) throws IllegalStateException {
            if (dessus + 1 == capacite) {
                int nouvelleCapacite = capacite * 2;
                Integer[] nouvellePile = new Integer[nouvelleCapacite];
                System.arraycopy(pile, 0, nouvellePile, 0, pile.length);
                pile = nouvellePile;
                capacite = nouvelleCapacite;
            }
            pile[++dessus] = element; // Incrémente l'indice du dessus et ajoute l'élément
        }

        /**
         * Retire et retourne l'élément au sommet de la pile.
         * @return L'élément au sommet de la pile, ou null si la pile est vide.
         */
        public Integer pop() {
            if (dessus == -1) {
                return null;
            }
            Integer elementRetourne = pile[dessus];
            pile[dessus--] = null; // Efface l'élément et décrémente l'indice du dessus
            return elementRetourne;
        }

        /**
         * Vérifie si la pile est vide.
         * @return true si la pile est vide, false autrement.
         */
        public boolean isEmpty() {
            return dessus == -1;
        }

        /**
         * Retourne l'élément au sommet de la pile sans le retirer.
         * @return L'élément au sommet de la pile, ou null si la pile est vide.
         */
        public Integer top() {
            if (isEmpty()) return null;
            return pile[dessus];
        }

        @Override
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
    
    /**
     * Point d'entrée principal du programme.
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        GameSolver gameSolver = new GameSolver();
        gameSolver.resoudreChaqueGrille("sample.txt");
    }
}

