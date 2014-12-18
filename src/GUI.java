import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class GUI extends JFrame {

	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * Some CONSTS
	 */
	private final int ENTER = 10;
	private final int BACKSPACE = 8;
	private final int SPACE = 32;
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 *  GUI class fields
	 */
	public JPanel toolsPanel,
		explorerPanel,
		resultPanel,
		statusPanel,
		threadPanel,
		rightArea;
	
	public JTextField keyWordText;
	
	public JButton searchButton;
	
	public JList<Object> explorerJList;
	public JList<File> selectedJList, resultJList;
	
	public DefaultListModel<Object> explorerList;
	public DefaultListModel<File> selectedList, resultList;
	
	public JScrollPane explorerPane;
	public JScrollPane selectedPane;
	public JScrollPane resultPane;
	
	public File currentDirectory = null;
	
	// status panel stuff
	public JLabel statusLabel,
		foundLabel,
		threadsCountLabel,
		sizeCountLabel;
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public GUI()
	{
		//initializing panels
		this.toolsPanel = new JPanel();
		this.explorerPanel = new JPanel();
		this.resultPanel = new JPanel();
		this.statusPanel = new JPanel();
		this.threadPanel = new JPanel();
		this.rightArea = new JPanel();
		
		//init keyword
		this.keyWordText = new JTextField();
		
		//initializing buttons
		this.searchButton = new JButton("GO");
		
		//setting layout
		this.setLayout(new BorderLayout());
		this.rightArea.setLayout(new BorderLayout());
		this.resultPanel.setLayout(new BorderLayout());
		this.explorerPanel.setLayout(new BorderLayout());
		this.threadPanel.setLayout(new GridLayout(0, 1));
		this.toolsPanel.setLayout(new GridLayout(1, 0));
		this.statusPanel.setLayout(new GridLayout(1, 0));
		
		//adding panels
		this.add(toolsPanel, BorderLayout.NORTH);
		this.add(explorerPanel, BorderLayout.CENTER);
		this.add(statusPanel, BorderLayout.SOUTH);
		this.add(rightArea, BorderLayout.EAST);
		
		//adding buttons
		this.toolsPanel.add(searchButton);
		this.toolsPanel.add(keyWordText);
		
		//add result, thread blocks
		this.rightArea.add(resultPanel, BorderLayout.SOUTH);
		this.rightArea.add(threadPanel, BorderLayout.NORTH);
		
		// --------------------------------
		
		currentDirectory = null;
		
		//threadList = new ArrayList<Searcher>();
		
		explorerList = new DefaultListModel<Object>();
		selectedList = new DefaultListModel<File>();
		resultList = new DefaultListModel<File>();
		
		explorerListUpdate(currentDirectory);
		
		explorerJList = new JList<Object>(explorerList);
		selectedJList = new JList<File>(selectedList);
		resultJList = new JList<File>(resultList);
		
		explorerPane = new JScrollPane(explorerJList);
		selectedPane = new JScrollPane(selectedJList);
		resultPane = new JScrollPane(resultJList);
		
		this.explorerPanel.add(explorerPane);
		this.threadPanel.add(selectedPane);
		this.resultPanel.add(resultPane);
		
		this.explorerJList.addMouseListener(new listClickListener());
		this.explorerJList.addKeyListener(new listKeyListener());
		
		// status panel stuff
		this.statusLabel = new JLabel("Status: disabled");
		this.foundLabel = new JLabel("Found: ");
		this.threadsCountLabel = new JLabel("Threads active: ");
		this.sizeCountLabel = new JLabel("Total size: 0");
		
		this.statusPanel.add(statusLabel);
		this.statusPanel.add(foundLabel);
		this.statusPanel.add(threadsCountLabel);
		this.statusPanel.add(sizeCountLabel);
		
		this.selectedJList.addKeyListener(new selectedListKeyListener());
	}

	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * build window function
	 * @void
	 */
	public void construct()
	{
		//temp backgrounds for distinguishing
		this.toolsPanel.setBackground(Color.BLUE);
		this.explorerPanel.setBackground(Color.YELLOW);
		this.resultPanel.setBackground(Color.CYAN);
		this.statusPanel.setBackground(Color.LIGHT_GRAY);
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
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * Assigning appropriate value for currentDirectory
	 * @void
	 */
	public void setDirectory(Object key)
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
	
	// ------------------------------------------------------------------------------------------------------------
	
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
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 *  selected list key listener
	 */
	class selectedListKeyListener extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if(!selectedJList.isSelectionEmpty())
			{
				if(e.getKeyCode() == SPACE)
				{
					selectedList.remove(selectedJList.getSelectedIndex());
				}				
			}
			
			super.keyPressed(e);
		}
	}
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * listClickListener
	 * @author Ilya
	 */
	public class listClickListener extends MouseAdapter {
		
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
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * listKeyListener
	 * @author Ilya
	 */
	public class listKeyListener extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			if(!explorerJList.isSelectionEmpty())
			{
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
			}
			else
			{
				if(e.getKeyCode() == BACKSPACE)
				{
					setDirectory(e);
					explorerListUpdate(currentDirectory);
				}
			}
			
			super.keyPressed(e);
		}
	}		
}