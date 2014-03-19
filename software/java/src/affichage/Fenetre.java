package affichage;

import java.awt.Component;
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

	public Fenetre(){
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Matrice");
		this.setSize(300, 120);

		//Les données du tableau
		Object[][] data = {{"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
				{"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"},
				{"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"}};

		//Les titres des colonnes
		Integer[]  title = new Integer[64];
		for (int i = 0; i < 64; i++)
		{
			title[i] = i;
		}
		JTable tableau = new JTable(data, title);

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
		tableau.getColumnModel().getColumn(0).setCellRenderer(new RowHeaderRenderer());

		tableau.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane thePane = new JScrollPane(tableau);
		//Nous ajoutons notre tableau à notre contentPane dans un scroll
		//Sinon les titres des colonnes ne s'afficheront pas !
		this.getContentPane().add(new JScrollPane(thePane));
		
	}  

	public static void main(String[] args){
		Fenetre fen = new Fenetre();
		fen.setVisible(true);
	}   
}