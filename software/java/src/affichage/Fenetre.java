package affichage;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class Fenetre extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 761831293657265052L;

	public Fenetre(int taille, Object[][] matrice){
		//Positionne la fenêtre au milieu de l'écran
		this.setLocationRelativeTo(null);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Matrice");
		this.setSize(300, 120);

		//Les données du tableau
		Object[][]   newMatrice = new Object[taille][taille+1];
		
		for (int i = 0; i < taille; i++)
		{
			newMatrice[i][0] = i;
		}
		for (int i = 0; i < taille; i++)
		{
			for (int j = 0; j < taille; j++)
			{
				newMatrice[i][j+1] = matrice[i][j];
			}
		}
		
		Object[][] data = newMatrice;
		
		

		//Les titres des colonnes
		Object[]  title = new Object[taille + 1];
		title[0] = "";
		for (int i = 1; i <= taille; i++)
		{
			title[i] = i;
		}
		JTable tableau = new JTable(data, title);

		//Classe magique qui est utilisée pour créer un titre à chaque ligne
		class RowHeaderRenderer extends DefaultTableCellRenderer {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public RowHeaderRenderer() {
		        setHorizontalAlignment(JLabel.CENTER);
		    }

		    public Component getTableCellRendererComponent(JTable table,
		            Object value, boolean isSelected, boolean hasFocus, int row,
		            int column) {
		        if (table != null) {
		            JTableHeader header = table.getTableHeader();

		            if (header != null) {
		                setForeground(header.getForeground());
		                setBackground(header.getBackground());
		                setFont(header.getFont());
		            }
		        }

		        if (isSelected) {
		            setFont(getFont().deriveFont(Font.BOLD));
		        }

		        setValue(value);
		        return this;
		    }
		}
		
		//Permet de créer un nom pour chaque ligne
		tableau.getColumnModel().getColumn(0).setCellRenderer(new RowHeaderRenderer());

		tableau.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane thePane = new JScrollPane(tableau);
		Dimension d = new Dimension(100*taille,100*taille);
		thePane.setMaximumSize(d);
		thePane.setMinimumSize(d);
		thePane.setPreferredSize(d);
		//Nous ajoutons notre tableau à notre contentPane dans un scroll
		//Sinon les titres des colonnes ne s'afficheront pas !
		this.getContentPane().add(new JScrollPane(thePane));
		
	}  

	public static void main(String[] args){
		Object[][] t = {{1,2,3}, {4,5,6}, {7,8,9}};
		Fenetre fen = new Fenetre(3, t);
		fen.setVisible(true);
	}   
}