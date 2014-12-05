import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


/**
 * @author Ilya
 *
 */
@SuppressWarnings("serial")
public class Searcher extends JFrame {

	/**
	 * SOME CONSTANTS
	 */
	private static String ROOT_DIRECTORY = "/";
	
	/**
	 * fields
	 */
	public JPanel toolsPanel,
		explorerPanel,
		resultPanel,
		statusPanel,
		threadPanel,
		rightArea;
	
	public JList<Object> jList;
	
	public DefaultListModel<Object> list;
	
	public JScrollPane pane;
	
	public File currentDirectory;
	
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
		this.threadPanel = new JPanel();
		this.rightArea = new JPanel();
		
		//setting layout
		this.setLayout(new BorderLayout());
		this.rightArea.setLayout(new BorderLayout());
		this.explorerPanel.setLayout(new BorderLayout());
		
		//adding panels
		this.add(toolsPanel, BorderLayout.NORTH);
		this.add(explorerPanel, BorderLayout.CENTER);
		this.add(statusPanel, BorderLayout.SOUTH);
		this.add(rightArea, BorderLayout.EAST);
		
		//add result, thread blocks
		this.rightArea.add(resultPanel, BorderLayout.SOUTH);
		this.rightArea.add(threadPanel, BorderLayout.NORTH);
		
		// --------------------------------
		
		currentDirectory = new File(ROOT_DIRECTORY);
	
		list = getPreparedList(File.listRoots());
		
		jList = new JList<Object>(list);
		
		pane = new JScrollPane(jList);
		
		this.explorerPanel.add(pane);
		

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
		frame.construct();
		
	}
	
	// ------------------------------------------------------------------
	
	/**
	 * resize window function
	 * @void
	 */
	public void construct()
	{
		//temp backgrounds for distinguishing
		this.toolsPanel.setBackground(Color.BLUE);
		this.explorerPanel.setBackground(Color.YELLOW);
		this.resultPanel.setBackground(Color.CYAN);
		this.statusPanel.setBackground(Color.GRAY);
		this.threadPanel.setBackground(Color.GREEN);
		
		//construct layout
		this.toolsPanel.setPreferredSize(new Dimension(this.getWidth(), (int) (this.getHeight() * 0.1)));
		this.explorerPanel.setPreferredSize(new Dimension((int) (this.getWidth() * 0.55), (int) (this.getHeight() * 0.8)));
		this.rightArea.setPreferredSize(new Dimension((int) (this.getWidth() * 0.45), (int) (this.getHeight() * 0.8)));
		this.statusPanel.setPreferredSize(new Dimension(this.getWidth(), (int) (this.getHeight() * 0.1)));
		
		//construct result and thread blocks
		this.resultPanel.setPreferredSize(new Dimension(this.rightArea.getSize().width, (int) (this.rightArea.getPreferredSize().height * 0.5)));
		this.threadPanel.setPreferredSize(new Dimension(this.rightArea.getSize().width, (int) (this.rightArea.getPreferredSize().height * 0.5)));
	}
	
	// ------------------------------------------------------------------
	
	/**
	 * Prepare and return list of files
	 * @param files - list of elements
	 * @return DefaultListModel<Object> object
	 */
	private DefaultListModel<Object> getPreparedList(Object[] files)
	{
		DefaultListModel<Object> temp = new DefaultListModel<Object>();
		
		for(Object o: files)
		{
			temp.addElement(o);
		}
		
		return temp;
	}
}
