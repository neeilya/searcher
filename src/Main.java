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
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		
		//construct
		final GUI gui = new GUI();
		
		//setting window properties
		gui.setSize(1100, 700);
		gui.setLocationRelativeTo(null);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
		gui.construct();
		
		// assign clickListener to search button
		gui.searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				// reset count values from previous research if there was one
				gui.clearAll();
				
				// -------------------------------------------------------------------------------
				
				/**
				 * Check if keyWord is empty
				 */
				if(gui.keyWordText.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Key word cannot be empty");
					return;
				}
				
				// -------------------------------------------------------------------------------
				
				/**
				 * Check if list of directories is empty
				 */
				if(gui.selectedList.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "List of directories where to search cannot be empty");
					return;
				}
				
				// -------------------------------------------------------------------------------
				
				/**
				 * Check sizeFrom range textfields
				 */
				if(!gui.sizeFromText.getText().equals(""))
				{
					try
					{
						Integer.parseInt(gui.sizeFromText.getText());
					}
					catch(NumberFormatException e)
					{
						JOptionPane.showMessageDialog(null, "size range must be typed by integers");
						return;
					}
				}
				
				// -------------------------------------------------------------------------------
				
				/**
				 * Check sizeTo range textfields
				 */
				if(!gui.sizeToText.getText().equals(""))
				{
					try
					{
						Integer.parseInt(gui.sizeToText.getText());
					}
					catch(NumberFormatException e)
					{
						JOptionPane.showMessageDialog(null, "size range must be typed by integers");
						return;
					}
				}
				
				// -------------------------------------------------------------------------------
				
				// disable corresponding GUI buttons while searching
				gui.searchParamsEnabled(false);
				
				// -------------------------------------------------------------------------------
				
				// run thread for each selected directory
				for(int i = 0; i < gui.selectedList.getSize(); ++i)
				{
					Searcher t = new Searcher(gui, gui.selectedList.getElementAt(i));
					
					gui.addThread(t);
					
					t.start();
				}
				
			}
		});
	}	
}