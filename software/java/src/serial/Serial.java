package serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Serial implements SerialPortEventListener
{
	SerialPort serialPort;
	String name;

	Serial (String name)
	{
		super();
		this.name = name;
	}

	/**
	 * A BufferedReader which will be fed by a InputStreamReader 
	 * converting the bytes into characters 
	 * making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;

	/**
	 * Appelé par le SerialManager, il donne à la série tout ce qu'il faut pour fonctionner
	 * @param port_name
	 * 					Le port où est connecté la carte
	 * @param baudrate
	 * 					Le baudrate que la carte utilise
	 */
	void initialize(String port_name, int baudrate)
	{
		CommPortIdentifier portId = null;
		try
		{
			portId = CommPortIdentifier.getPortIdentifier(port_name);
		}
		catch (NoSuchPortException e2)
		{
			e2.printStackTrace();
		}

		// open serial port, and use class name for the appName.
		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);
		} 
		catch (PortInUseException e1)
		{
			e1.printStackTrace();
		}
		try
		{
			// set port parameters
			serialPort.setSerialPortParams(baudrate,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

		}
		catch (Exception e)
		{
			System.err.println(e.toString());
		}
		
		/*
		 * A tester, permet d'avoir un readLine non bloquant! (valeur à rentrée en ms)
		 */
		try {
			serialPort.enableReceiveTimeout(1000);
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode pour parler à l'avr
	 * @param message
	 * 					Message à envoyer
	 * @param nb_lignes_reponse
	 * 					Nombre de lignes que l'avr va répondre (sans compter les acquittements)
	 * @return
	 * 					Un tableau contenant le message
	 * @throws SerialException 
	 */
	public void communiquer(String message) throws SerialException
	{
		synchronized(output)
		{
			try
			{
				message += "\r";
				output.write(message.getBytes());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("Ne peut pas parler à la carte " + this.name);
				throw new SerialException();
			}
			
		}
		return;
	}
	
	/**
	 * Doit être appelé quand on arrête de se servir de la série
	 */
	public void close()
	{
		if (serialPort != null)
		{
			System.out.println("Fermeture de "+name);
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent)
	{
	}

	public synchronized int readByte() // TODO tester
	{
        synchronized(output) {
            try
            {
                //On vide le buffer de la serie cote PC
                output.flush();
    
                return input.read();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return 1000;
        }
	}

	   public synchronized boolean readBoolean() // TODO tester
	    {
	        synchronized(output) {
	            int ABWABWA;
	            try
	            {
	                //On vide le buffer de la serie cote PC
	                output.flush();
	    
	                //evacuation de l'acquittement
	                ABWABWA = input.read();
	                //recuperation de l'id de la carte
	                return (ABWABWA == 49); // TODO vérifier
	                // 49 = '1'
	            }
	            catch (Exception e)
	            {
	                e.printStackTrace();
	            }
	            return false;
	        }
	    }

	/**
	 * Ping de la carte.
	 * Utilisé que par createSerial de SerialManager
	 * @return l'id de la carte
	 */
	synchronized String ping()
	{
		synchronized(output) {
			int ping;
			try
			{
				//On vide le buffer de la serie cote PC
				output.flush();
	
				//ping
				output.write("Q\r".getBytes());
				//evacuation de l'acquittement
				ping = input.read();
				//recuperation de l'id de la carte
				if(ping == 46)
					return "0";
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return "1";
		}
	}

}