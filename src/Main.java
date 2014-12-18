import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
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
				
				if(!gui.keyWordText.getText().equals("") && !gui.selectedList.isEmpty())
				{
					DefaultListModel<File> tempModel = gui.selectedList;
					
					for(int i = 0; i < tempModel.getSize(); ++i)
					{
						File directory = tempModel.getElementAt(i);

						Searcher t = new Searcher(gui, directory);
						gui.addThread(t);
						t.start();
					}

				}
				else
				{
					JOptionPane.showMessageDialog(null, "Key word or list of selected directories is empty");
				}
				
			}
		});
	}	
}