package test;

import hexapode.enums.Mode;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import util.DataSaver;

/**
 * WARNING:
 * A priori, "Markov.java", qui implémente la compression de matrice, est buggée.
 * On utilisera à sa place MarkovNCoups avec un nombre de coups égal à 1.
 * 
 * Contient la matrice de transition sur n coups. C'est-à-dire que la matrice prend en coup
 * en entrée et renvoie n coups.
 * Sérialisable afin d'avoir des données persistantes.
 * @author pf
 *
 */

public class MarkovNCoups implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    protected short matrice[][];
    protected transient List<char[]> positionsViables; // transient = pas sauvegardé
    protected transient Random randomgenerator = new Random();
    protected int nbEtatsParPattes;
    protected int dimension_x;
    protected int dimension_y;
    protected int diviseur = 0; // en puissance de 2
    protected int etat_suivant = 0;
    protected int n;
    protected int etat_suivant_sauv;
    protected int etat_precedent_sauv;
    private int indice = 0;
    private transient int total = 0;
    private transient int convergence = 0;
    
    
    /**
     * Chargement de l'apprentissage réalisé pour ce mode.
     * @param mode (biphasé ou triphasé)
     */
    public static MarkovNCoups getLearnedMarkov(Mode mode, int combien_coups)
    {
        return charger_matrice("markov"+combien_coups+"coups_"+mode+".dat");
    }
    
    /**
     * Chargement d'une matrice.
     */
    public static MarkovNCoups charger_matrice(String filename)
    {
        MarkovNCoups out = DataSaver.charger_matrice_n_coups(filename);
        // Initialisation des attributs transient
        out.randomgenerator = new Random();
        out.getPositionsViables();
        return out;
    }
    
    /**
     * Constructeur pour une nouvelle matrice
     * @param n, le nombre de coups à anticiper
     * @param dimension
     */
    public MarkovNCoups(int n, int nbEtatsParPattes)
    {
        this.n = n;
        this.nbEtatsParPattes = nbEtatsParPattes;
        // dimension = nbEtatsParPattes^6
        int dimension = nbEtatsParPattes*nbEtatsParPattes*nbEtatsParPattes;
        dimension *= dimension;
        dimension_x = dimension;
        dimension_y = 1;

        for(int i = 0; i < n; i++)
            dimension_y *= dimension_x;
        System.out.println(dimension_y*dimension_x+" éléments dans la matrice");

        matrice = new short[dimension_x][dimension_y];
        for(int i = 0; i < dimension_x; i++)
            for(int j = 0; j < dimension_y; j++)
                matrice[i][j] = 0;
        getPositionsViables();
    }
    
    /**
     * Retourne vrai si la position est viable, faux sinon.
     * @param position
     * @return
     */
    public boolean estViable(String position)
    {
        for(char[] pos: positionsViables)
            if(Integer.parseInt(String.valueOf(pos)) == Integer.parseInt(position))
                return true;
        return false;
    }
    
    /**
     * Charge les positions stables.
     */
    protected void getPositionsViables()
    {
        double pos[] = DataSaver.charger_matrice_equilibre("markov_equilibre.dat");
        positionsViables = new LinkedList<char[]>();
        for(int i=0; i< pos.length; i++)
            if(pos[i] > 4)
                positionsViables.add(Integer.toBinaryString(i).toCharArray());
    }
    
    /**
     * Renvoie une position stable au hasard.
     * @return
     */
    private String getRandomPositionViable()
    {
        /* Ce bloc permet de piocher une transition parmi les �tats d'�quilibres */
        int r = randomgenerator.nextInt(positionsViables.size());//Pioche un int
        String out = String.valueOf(positionsViables.get(r));

        while(out.length() < 6)
            out = "0" + out;

        return out;
    }
    
    /**
     * Donne le prochain état de manière équiprobable. Utilisé pour les tests.
     * @return
     */
    public String next()
    {
        for(int i = 0; i < n-1; i++)
            etat_suivant_sauv /= dimension_x;

        etat_precedent_sauv = etat_suivant_sauv;

        String s = new String();

        for(int i = 0; i < n; i++)
            s += getRandomPositionViable();

        etat_suivant_sauv = string2index(s);
        return s;
    }

    /**
     * Surcouche user-friendly de nextValidation
     * @param e
     * @return
     */
    public String nextValidation(String e)
    {
        return nextValidation(string2index(e));
    }

    /**
     * Renvoie la prochain état en validation. Utilise les résultats obtenus
     * précédemment et stockés dans compressed_matrix
     * @param numeroEtatActuel
     * @return
     */
    public String nextValidation(int numeroEtatActuel)
    {
        etat_suivant /= dimension_x;
        // On ne prend en compte l'entrée que quand on a fini d'utiliser toutes les coups associés à l'état précédent.
        if(indice == 0)
        {
            indice = n-1;
            etat_precedent_sauv = numeroEtatActuel;
            int lineSum = 0;
            
            for(int j = 0; j < dimension_y; j++)
                lineSum += matrice[numeroEtatActuel][j];
            
            if(lineSum == 0)
            {
                etat_suivant = randomgenerator.nextInt(dimension_y);
                etat_suivant_sauv = etat_suivant; // on le sauvegarde tant qu'il est entier
                return index2string(etat_suivant % dimension_x).substring(0, 6);
            }
            
            int r = randomgenerator.nextInt(lineSum);
            
            lineSum = 0;
            
            for(int j = 0; j < dimension_y; j++)
            {
                lineSum += matrice[numeroEtatActuel][j];
                if(lineSum > r)
                {
                    etat_suivant = j;
                    return index2string(etat_suivant % dimension_x).substring(0, 6);
                }
            }
        // Cas impossible
        return null;
        }
        else // si indice != 0
        {
            indice--;
            return index2string(etat_suivant % dimension_x).substring(0, 6);
        }
    }
    
    public void razConvergence()
    {
        convergence = 0;
        total = 0;
    }
    
    public double getConvergence()
    {
        if(total != 0)
            return ((double) convergence)/((double) total);
        else
            return 0;
    }
        
    /**
     * Met à jour la matrice avec le résultat d'un test.
     * @param resultat
     * @param etatPrecedent
     * @param etatSuivant
     */
    public void updateMatrix(int resultat, int etat_precedent, int etat_suivant)
    {
//        System.out.println("updateMatrix: "+index2string(etat_precedent)+" "+index2string(etat_suivant)+": "+ resultat);
        int note_actuelle = matrice[etat_precedent][etat_suivant];
        if(resultat != 0 && (resultat >> diviseur) == 0)
            convergence++;
        total++;
        
        resultat >>= diviseur;
        // S'il y a un overflow, on divise par 2 toutes les notes de la lignes
        // Les prochaines notes aussi seront divisées par deux.
        if((note_actuelle+resultat) != (int)(short)(note_actuelle+resultat))
        {
            System.out.println("Overflow des notes!");
            for(int i = 0; i < dimension_x; i++)
                for(int j = 0; j < dimension_y; j++)
                    matrice[i][j] >>= 1;
            diviseur++;
            resultat >>= 1;
        }
            
        matrice[etat_precedent][etat_suivant] += resultat;
    }
    
    /**
     * Convertit, à partir du nombre d'états, un String en nombre.
     * @param e
     * @return
     */
    public int string2index(String e)
    {
        int num = 0;
        for(int i = 0; i < n*6; i++)
        {
            num *= nbEtatsParPattes;
            try {
                num += Integer.parseInt(String.valueOf(e.charAt(n*6-1-i)));
            }
            catch(Exception exception)
            {
            }
        }
        return num;
    }

    /**
     * Convertit, à partir du nombre d'états, un nombre en String.
     * @param num
     * @return
     */
    public String index2string(int num)
    {
        String out = new String();
        for(int i = 0; i < n*6; i++)
        {
            out += String.valueOf(num%nbEtatsParPattes);
            num /= nbEtatsParPattes;
        }
        return out;
    }


}
