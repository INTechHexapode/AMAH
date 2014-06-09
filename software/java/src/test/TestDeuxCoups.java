package test;

import java.util.Random;

import hexapode.Deplacement;
import hexapode.enums.Mode;

/**
 * Test pour une marche markovienne qui prévoit les deux prochains coups.
 * @author pf
 *
 */
public class TestDeuxCoups extends Test
{

    private boolean non_viable = false;
    private Random randomgenerator;
    
    public TestDeuxCoups(Deplacement deplacement, int nbIteration,
            double consecutiveLearnTime, double pauseTime,
            boolean restartMarkov)
    {
        super(deplacement, nbIteration, consecutiveLearnTime, pauseTime, restartMarkov);
        randomgenerator = new Random();
    }

    public TestDeuxCoups(Deplacement deplacement)
    {
        super(deplacement);
    }

    @Override
    public void onStart()
    {
        etat_actuel = etat_suivant;
        note = 0;        
    }
   
    @Override
    public void onBreak()
    {
        deplacement.desasserv();
    }

    @Override
    public void proceedTest()
    {
        note = 0;
        markov.next();
        // On le fait deux fois
        for(int k = 0; k < 2; k++)
        {
            String etat_test;
            etat_suivant = new String(etat_actuel);
    
            for(int i = 0; i < 6; i++)
                if(randomgenerator.nextBoolean()) // la patte bouge
                {
                    if(etat_suivant.charAt(i) == '0')
                        etat_suivant = etat_suivant.substring(0,i) + "1" + etat_suivant.substring(i+1);
                    else if(etat_suivant.charAt(i) == '1')
                        etat_suivant = etat_suivant.substring(0,i) + "2" + etat_suivant.substring(i+1);
                    else if(etat_suivant.charAt(i) == '2')
                        etat_suivant = etat_suivant.substring(0,i) + "0" + etat_suivant.substring(i+1);
                }
            etat_test = new String();
            for(int i = 0; i < 6; i++)
                if(etat_suivant.charAt(i) == '1' ||
                (etat_actuel.charAt(i) == '1' && etat_suivant.charAt(i) == '2')) // on lève la patte ou on la met à l'avant: dans les deux cas, le mouvement est en l'air.
                    etat_test += "1";
                else
                    etat_test += "0";
            non_viable = !markov.estViable(etat_test);
                    
            //On demande � l'hexapode de se mettre en position
            try
            {
                deplacement.goto_etat(etat_suivant);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            note += updateNoteTruly();
        }

    }

    // Le updateNote n'est pas adapté à ce test...
    @Override
    public void updateNote()
    {}

    public int updateNoteTruly()
    {
        int note = 0;
        int nbRetourArriere = 0;

        if(non_viable)
            return note;

        for(int i = 0; i < 6; i++)
        {
            if(etat_actuel.charAt(i) == '0')
            {
                if(etat_suivant.charAt(i) == '1')
                {
                    note += 5;
                }
                else
                {
                    nbRetourArriere++;
                }
            }
            else
            {
                if(etat_suivant.charAt(i) == '1')
                {
                    note += 5;
                }
            }
        }
        
        if(nbRetourArriere >= 3)
        {
            note += nbRetourArriere * 10;
        }
        else
        {
            note -=  -40;
        }
        
        return note;
    }
    
    @Override
    public void validTest()
    {
        etat_suivant = markov.nextValidation(markov.string2index(etat_actuel));
        System.out.println(etat_suivant);
        //On demande � l'hexapode de se mettre en position
        try
        {
            deplacement.goto_etat(etat_suivant);
        } catch (Exception e)
        {
            e.printStackTrace();
        }        
    }

    @Override
    public void init()
    {
        if(validation)
            markov = chargement_matrice();
        else
            markov = new MarkovNCoups(2, 2);

        deplacement.setMode(Mode.BIPHASE);
        
        // le premier état (mis dans etat_actuel dans start)
        etat_suivant = new String("000000");
        try
        {
            deplacement.goto_etat(etat_suivant);
        } catch (Exception e)
        {
            e.printStackTrace();
        }        
    }

    @Override
    public void terminate() {
        super.terminate();  // sauvegarde
        deplacement.desasserv();
    }

    
}
