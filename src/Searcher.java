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
	private final int SPACE = 32;
	
	
	/**
	 * fields
	 */
	public JPanel toolsPanel,
		explorerPanel,
		resultPanel,
		statusPanel,
		threadPanel,
		rightArea;
	
	public JList<Object> explorerJList;
	public JList<File> selectedJList;
	
	public DefaultListModel<Object> explorerList;
	public DefaultListModel<File> selectedList;
	
	public JScrollPane explorerPane;
	public JScrollPane selectedPane;
	
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
		this.threadPanel.setLayout(new BorderLayout());
		
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
	
		explorerList = new DefaultListModel<Object>();
		selectedList = new DefaultListModel<File>();
		
		explorerListUpdate(currentDirectory);
		
		explorerJList = new JList<Object>(explorerList);
		selectedJList = new JList<File>(selectedList);
		
		explorerPane = new JScrollPane(explorerJList);
		selectedPane = new JScrollPane(selectedJList);
		
		this.explorerPanel.add(explorerPane);
		this.threadPanel.add(selectedPane);
		
		this.explorerJList.addMouseListener(new listClickListener());
		this.explorerJList.addKeyListener(new listKeyListener());
		
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
				explorerListUpdate(currentDirectory);
				
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
			
			switch(e.getKeyCode())
			{
				case BACKSPACE:
				case ENTER:
					
					setDirectory(e);
					explorerListUpdate(currentDirectory);
					
					break;
					
				case SPACE:
					if(!explorerJList.getSelectedValue().getClass().equals(String.class))
					{
						if(((File) explorerJList.getSelectedValue()).listFiles() != null)
						{
							if(!selectedList.contains((File) explorerJList.getSelectedValue()))
								selectedList.addElement((File) explorerJList.getSelectedValue());
						}
						break;
					}
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
			if(explorerJList.getSelectedValue().getClass().equals(String.class))
			{
				currentDirectory = currentDirectory.getParentFile();
			}
			else
			{
				//setting currentDirectory
				if(((File) explorerJList.getSelectedValue()).isDirectory())
					currentDirectory = (File) explorerJList.getSelectedValue();
			}
		}
		else
		{
			switch((int) ((KeyEvent) key).getKeyCode())
			{
				case ENTER:
					if(explorerJList.getSelectedValue().getClass().equals(String.class))
					{
						currentDirectory = currentDirectory.getParentFile();
					}
					else
					{
						//setting currentDirectory
						if(((File) explorerJList.getSelectedValue()).isDirectory())
							currentDirectory = (File) explorerJList.getSelectedValue();
					}
					break;
					
				case BACKSPACE:
					if(currentDirectory != null)
						currentDirectory = currentDirectory.getParentFile();
					break;
			}
		}
	}
	
	// ------------------------------------------------------------------
	
	/**
	 * DefaultModelList update list function
	 * @param files - list of File objects
	 */
	private void explorerListUpdate(File directory)
	{
		explorerList.clear();
		selectedList.clear();
		
		if(directory != null)
		{
			explorerList.addElement((String) "..");
			
			for(File f: directory.listFiles())
			{
				//check if file is hidden (secured?)
				if(!f.isHidden() && f.isDirectory() && f.listFiles() != null)
					explorerList.addElement((File) f);
			}
		}
		else
		{
			for(File f: File.listRoots())
			{
				explorerList.addElement((File) f);
			}
		}
	}
}
