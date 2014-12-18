import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * @author Ilya
 *
 */
@SuppressWarnings("serial")
public class Main extends JFrame {

	
	/**
	 * Main class fields
	 */
	public GUI gui;
	
	// ------------------------------------------------------------------
	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		
		//construct
		final GUI gui = new GUI();
		
		//setting window properties
		gui.setSize(900, 500);
		gui.setLocationRelativeTo(null);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
		gui.setResizable(false);
		gui.construct();
		
		gui.searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				gui.resultList.clear();
				
				if(!gui.keyWordText.getText().equals(""))
				{
					Thread t = new Thread(new Searcher(gui));
					t.start();					
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Input is empty");
				}
				
			}
		});
	}	
}