import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class Main extends JFrame {

	
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
	
	private Button searchButton;
	
	public JList<Object> explorerJList;
	public JList<File> selectedJList, resultJList;
	
	public DefaultListModel<Object> explorerList;
	public DefaultListModel<File> selectedList, resultList;
	
	public JScrollPane explorerPane;
	public JScrollPane selectedPane;
	public JScrollPane resultPane;
	
	public File currentDirectory = null;
	
	// ------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public Main()
	{		
		//initializing panels
		this.toolsPanel = new JPanel();
		this.explorerPanel = new JPanel();
		this.resultPanel = new JPanel();
		this.statusPanel = new JPanel();
		this.threadPanel = new JPanel();
		this.rightArea = new JPanel();
		
		//initializing buttons
		this.searchButton = new Button("GO");
		
		//setting layout
		this.setLayout(new BorderLayout());
		this.rightArea.setLayout(new BorderLayout());
		this.resultPanel.setLayout(new BorderLayout());
		this.explorerPanel.setLayout(new BorderLayout());
		this.threadPanel.setLayout(new BorderLayout());
		
		//adding panels
		this.add(toolsPanel, BorderLayout.NORTH);
		this.add(explorerPanel, BorderLayout.CENTER);
		this.add(statusPanel, BorderLayout.SOUTH);
		this.add(rightArea, BorderLayout.EAST);

		
		//adding buttons
		this.toolsPanel.add(searchButton);
		
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
	
		//adding keyListeners to buttons
		this.searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				Thread t = new Thread(new Searcher(currentDirectory));
				t.start();
				
			}
		});
		
	}
	
	// -----------------------------------------------------------------	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//construct
		Main frame = new Main();
		
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
	
	// ------------------------------------------------------------------
	
	private class Searcher implements Runnable {
		
		private File directory;
		
		private DefaultListModel<File> results;
		
		public Searcher(File directory)
		{
			this.directory = directory;
		}
		
		private void search(String path, String indent)
		{
			
			File file = new File(path);
			
			for(File currentFile: file.listFiles())
			{
				if(currentFile.isDirectory())
				{
					search(currentFile.getAbsolutePath(), indent + "\t");
				}
				if(currentFile.getName().contains("search"))
				{
				 	resultList.addElement(currentFile);
				}
			}
			
		}

		@Override
		public void run()
		{
			search(this.directory.getPath(), "");
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
