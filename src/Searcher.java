import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * @author Ilya
 *
 */
@SuppressWarnings("serial")
public class Searcher extends JFrame {

	/**
	 * fields
	 */
	public JPanel toolsPanel, explorerPanel, resultPanel, statusPanel;
	
	// ------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public Searcher()
	{
		//initializing panels
		this.toolsPanel = new JPanel();
		this.explorerPanel = new JPanel();
		this.resultPanel = new JPanel();
		this.statusPanel = new JPanel();
		
		//setting layout
		this.setLayout(new BorderLayout());
		
		//adding panels
		this.add(toolsPanel, BorderLayout.NORTH);
		this.add(explorerPanel, BorderLayout.CENTER);
		this.add(resultPanel, BorderLayout.EAST);
		this.add(statusPanel, BorderLayout.SOUTH);
		
		//temp backgrounds for distinguishing
		this.toolsPanel.setBackground(Color.BLUE);
		this.explorerPanel.setBackground(Color.YELLOW);
		this.resultPanel.setBackground(Color.CYAN);
		this.statusPanel.setBackground(Color.GRAY);

		//setting appropriate sizes
		this.toolsPanel.setPreferredSize(new Dimension(800, 70));
		this.resultPanel.setPreferredSize(new Dimension(300, 200));
		this.statusPanel.setPreferredSize(new Dimension(800, 40));
	}
	
	// ------------------------------------------------------------------
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//construct
		Searcher frame = new Searcher();
		
		//setting window properties
		frame.setSize(800, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	// ------------------------------------------------------------------
}
