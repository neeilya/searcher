import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	 * Some CONSTS
	 */
	private final int ENTER = 10;
	private final int BACKSPACE = 8;
	
	
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
	
	public File currentDirectory = null;
	
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
		
		currentDirectory = null;
	
		list = new DefaultListModel<Object>();
		
		listUpdate(currentDirectory);
		
		jList = new JList<>(list);
		
		pane = new JScrollPane(jList);
		
		this.explorerPanel.add(pane);
		
		this.jList.addMouseListener(new listClickListener());
		this.jList.addKeyListener(new listKeyListener());
		
	}
	
	// -----------------------------------------------------------------	
	
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
	 * listClickListener
	 * @author Ilya
	 */
	private class listClickListener extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent event) {
			
			if(event.getClickCount() == 2)
			{
				setDirectory(event);
				listUpdate(currentDirectory);
				
			}
			
			super.mouseClicked(event);
		}	
	}
	
	// ------------------------------------------------------------------
	
	/**
	 * listKeyListener
	 * @author Ilya
	 */
	private class listKeyListener extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			if(e.getKeyCode() == ENTER || e.getKeyCode() == BACKSPACE)
			{
				setDirectory(e);
				listUpdate(currentDirectory);
			}
			
			super.keyPressed(e);
		}
	}
	
	// ------------------------------------------------------------------
	
	/**
	 * build window function
	 * @void
	 */
	private void construct()
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
	 * Assigning appropriate value for currentDirectory
	 * @void
	 */
	private void setDirectory(Object key)
	{
		if(key.getClass().equals(MouseEvent.class))
		{
			if(jList.getSelectedValue().getClass().equals(String.class))
			{
				currentDirectory = currentDirectory.getParentFile();
			}
			else
			{
				//setting currentDirectory
				if(((File) jList.getSelectedValue()).isDirectory())
					currentDirectory = (File) jList.getSelectedValue();
			}
		}
		else
		{
			switch((int) ((KeyEvent) key).getKeyCode())
			{
				case ENTER:
					if(jList.getSelectedValue().getClass().equals(String.class))
					{
						currentDirectory = currentDirectory.getParentFile();
					}
					else
					{
						//setting currentDirectory
						if(((File) jList.getSelectedValue()).isDirectory())
							currentDirectory = (File) jList.getSelectedValue();
					}
					break;
				case BACKSPACE:
					if(currentDirectory != null)
						currentDirectory = currentDirectory.getParentFile();
					break;
				default:
			}
		}
	}
	
	// ------------------------------------------------------------------
	
	/**
	 * DefaultModelList update list function
	 * @param files - list of File objects
	 */
	private void listUpdate(File directory)
	{
		list.clear();
		
		if(directory != null)
		{
			list.addElement((String) "..");
			
			for(File f: directory.listFiles())
			{
				list.addElement((File) f);
			}
		}
		else
		{
			for(File f: File.listRoots())
			{
				list.addElement((File) f);
			}
		}
	}
}
