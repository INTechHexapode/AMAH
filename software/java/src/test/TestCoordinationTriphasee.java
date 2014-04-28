package test;

import java.util.Random;

import hexapode.Hexapode;
import hexapode.exceptions.EnnemiException;

/**
 * Test pour la coordination des pattes avec trois états d'équilibre: A, B et C
 * @author pf
 *
 */
public class TestCoordinationTriphasee extends Test
{

    Random randomgenerator;
    
    public TestCoordinationTriphasee(Hexapode hexapode, int nbIteration,
            double consecutiveLearnTime, double pauseTime,
            boolean restartMarkov, boolean validation)
    {
        super(hexapode, nbIteration, consecutiveLearnTime, pauseTime, restartMarkov,
                validation);
        randomgenerator = new Random();
    }

    @Override
    public void onStart()
    {
        etat_actuel = etat_suivant;
        note = 0;        
    }

    @Override
    public void onExit()
    {
        super.onExit();
        calcNote();
        markov.updateMatrix(note, etat_actuel, etat_suivant);
    }
    
    @Override
    public void onBreak()
    {
        hexapode.desasserv();
    }

    @Override
    public void proceedTest()
    {
        /* 
        * 0: POUSSE
        * 1: DEBOUT
        * 2: AVANT
        */
        String etat_test;
        do {
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
                if(etat_suivant.charAt(i) == '1') // on lève la patte
                    etat_test += "1";
                else
                    etat_test += "0";
        } while(!markov.estViable(etat_test));
        
        //On demande � l'hexapode de se mettre en position
        try
        {
            hexapode.goto_etat_triphase(etat_suivant);
        } catch (EnnemiException e)
        {
            e.printStackTrace();
        }

    }

    private void calcNote()
    {
        boolean retourArriere = false;
        boolean reste_sol = false;
        note = 1;
        for(int i = 0; i < 6; i++)
        {
            if(etat_actuel.charAt(i) == '0' && etat_suivant.charAt(i) == '1' || 
               etat_actuel.charAt(i) == '1' && etat_suivant.charAt(i) == '2' ||
               etat_actuel.charAt(i) == '2' && etat_suivant.charAt(i) == '0' )
            {
                note += 20;
            }
            if(etat_actuel.charAt(i) == '2' && etat_suivant.charAt(i) == '0')
                retourArriere = true;
            if(etat_actuel.charAt(i) == '0' && etat_suivant.charAt(i) == '0')
                reste_sol = true;
        }
        if(reste_sol && retourArriere)
            note = 0;
    }
    
    @Override
    public void validTest()
    {
        etat_suivant = markov.nextValidation(markov.String2Index(etat_actuel));
        System.out.println(etat_suivant);
        //On demande � l'hexapode de se mettre en position
        try
        {
            hexapode.goto_etat_triphase(etat_suivant);
        } catch (EnnemiException e)
        {
            e.printStackTrace();
        }        
    }

    @Override
    public void init()
    {
        markov = new Markov(3);
        etat_actuel = new String("000000");
        etat_suivant = new String("000000");
    }

    @Override
    public void terminate() {
        super.terminate();  // sauvegarde
        hexapode.desasserv();
    }

    
}
