import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


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
	
	// tools panel stuff
	public JTextField keyWordText, sizeFromText, sizeToText, createdFromText, createdToText;
	public JButton searchButton, stopAllButton, stopSelectedButton;
	
	// lists stuff
	public JList<Object> explorerJList;
	public JList<File> selectedJList, resultJList;
	
	public DefaultListModel<Object> explorerList;
	public DefaultListModel<File> selectedList, resultList;
	
	// panes stuff
	public JScrollPane explorerPane;
	public JScrollPane selectedPane;
	public JScrollPane resultPane;
	
	public File currentDirectory = null;
	
	// status panel stuff
	public JLabel statusLabel,
		foundCountLabel,
		threadsCountLabel,
		sizeFromLabel,
		sizeToLabel,
		directoriesSelectedLabel,
		createdFromLabel,
		createdToLabel,
		sizeCountLabel;
	
	private long sizeCount = 0;
	private int foundCount = 0;
	public int directoriesSelectedCounter = 0;
	
	// multithreading stuff
	private ReentrantLock lock;
	public ArrayList<Searcher> threadList;
	public int threadCounter = 0;
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public GUI()
	{
		this.lock = new ReentrantLock();
		
		//initializing panels
		this.toolsPanel = new JPanel();
		this.explorerPanel = new JPanel();
		this.resultPanel = new JPanel();
		this.statusPanel = new JPanel();
		this.threadPanel = new JPanel();
		this.rightArea = new JPanel();
				
		//toolsPanel stuff
		this.searchButton = new JButton("GO");
		this.stopAllButton = new JButton("Stop all threads");
		this.stopSelectedButton = new JButton("Stop selected thread");
		this.sizeFromLabel = new JLabel("size from (mb)", SwingConstants.RIGHT);
		this.sizeToLabel = new JLabel("size to (mb)", SwingConstants.RIGHT);
		this.createdFromLabel = new JLabel("<html>Created from<br/>dd/mm/YYYY</html>", SwingConstants.RIGHT);
		this.createdToLabel = new JLabel("<html>Created to<br/>dd/mm/YYYY</html>", SwingConstants.RIGHT);
		
		 //init textFields
		this.keyWordText = new JTextField();
		this.sizeFromText = new JTextField();
		this.sizeToText = new JTextField();
		this.createdFromText = new JTextField();
		this.createdToText = new JTextField();
		
		//setting layout
		this.setLayout(new BorderLayout());
		this.rightArea.setLayout(new GridLayout(2, 1));
		this.resultPanel.setLayout(new BorderLayout());
		this.explorerPanel.setLayout(new BorderLayout());
		this.threadPanel.setLayout(new GridLayout(0, 1));
		this.toolsPanel.setLayout(new GridLayout(1, 0));
		this.statusPanel.setLayout(new GridLayout(1, 0));
		
		//toolsPanel adding buttons and textFields
		this.toolsPanel.add(searchButton);
		this.toolsPanel.add(keyWordText);
		this.toolsPanel.add(sizeFromLabel);
		this.toolsPanel.add(sizeFromText);
		this.toolsPanel.add(sizeToLabel);
		this.toolsPanel.add(sizeToText);
		this.toolsPanel.add(createdFromLabel);
		this.toolsPanel.add(createdFromText);
		this.toolsPanel.add(createdToLabel);
		this.toolsPanel.add(createdToText);
		this.toolsPanel.add(stopAllButton);
		this.toolsPanel.add(stopSelectedButton);
		
		//adding panels
		this.add(toolsPanel, BorderLayout.NORTH);
		this.add(explorerPanel, BorderLayout.CENTER);
		this.add(statusPanel, BorderLayout.SOUTH);
		this.add(rightArea, BorderLayout.EAST);
		
		//disabling thread stopping buttons
		this.stopAllButton.setEnabled(false);
		this.stopSelectedButton.setEnabled(false);
		
		//add result, thread blocks
		this.rightArea.add(threadPanel);
		this.rightArea.add(resultPanel);
		
		// --------------------------------
		
		
		// status panel stuff
		this.statusLabel = new JLabel("Status: waiting");
		this.directoriesSelectedLabel = new JLabel("Selected directories: ");
		this.foundCountLabel = new JLabel("Found: ");
		this.threadsCountLabel = new JLabel("Threads active: ");
		this.sizeCountLabel = new JLabel("Total size: 0");
		
		this.statusPanel.add(statusLabel);
		this.statusPanel.add(foundCountLabel);
		this.statusPanel.add(threadsCountLabel);
		this.statusPanel.add(directoriesSelectedLabel);
		this.statusPanel.add(sizeCountLabel);

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
		
		this.selectedJList.addKeyListener(new selectedListKeyListener());
		
		// multithreading stuff
		this.threadList = new ArrayList<Searcher>();
		
		this.stopAllButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				for(int i = 0; i < threadList.size(); ++i)
				{
					threadList.get(i).interrupt();

				}

				for(int i = 0; i < threadList.size(); ++i)
				{
					try
					{
						threadList.get(i).join();
						
					}
					catch (InterruptedException e)
					{
						
					}
				}

				// update gui
				stopAllButton.setEnabled(false);
				stopSelectedButton.setEnabled(false);
				
				statusLabel.setText("Status: stopped");				
			}
		});
		
		this.stopSelectedButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				for(int i = 0; i < selectedJList.getSelectedValuesList().size(); ++i)
				{
					if(!threadList.get(i).isInterrupted())
						threadList.get(i).interrupt();					
				}
			}
		});
	}

	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * build window function
	 * @void
	 */
	public void construct()
	{
		//construct layout
		this.toolsPanel.setPreferredSize(new Dimension(this.getWidth(), (int) (this.getHeight() * 0.05)));
		this.explorerPanel.setPreferredSize(new Dimension((int) (this.getWidth() * 0.55), (int) (this.getHeight() * 0.8)));
		this.rightArea.setPreferredSize(new Dimension((int) (this.getWidth() * 0.45), (int) (this.getHeight() * 0.8)));
		this.statusPanel.setPreferredSize(new Dimension(this.getWidth(), (int) (this.getHeight() * 0.05)));
		
	}
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * Reset all labels
	 */
	public void clearAll()
	{
		resultList.clear();
		statusLabel.setText("Status: searching...");
		
		foundCount = 0;
		foundCountLabel.setText("Found: ");
		
		sizeCount = 0;
		sizeCountLabel.setText("Total size: ");
		
		threadCounter = 0;
		threadsCountLabel.setText("Threads active: ");
	}
	
	
	public void removeThread(int index)
	{
		lock.lock();
		this.threadList.remove(index);
		lock.unlock();
	}
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * Enable/disable buttons
	 * @param enabled
	 */
	public void searchParamsEnabled(boolean enabled)
	{
		this.keyWordText.setEnabled(enabled);
		this.searchButton.setEnabled(enabled);	
		this.sizeFromText.setEnabled(enabled);
		this.sizeToText.setEnabled(enabled);
		this.createdFromText.setEnabled(enabled);
		this.createdToText.setEnabled(enabled);
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
		
		directoriesSelectedCounter = 0;
		directoriesSelectedLabel.setText("Selected directories: 0");
		
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
	 * Add thread to threadList
	 */
	public void addThread(Searcher t)
	{
		t.setName(t.initialDirectory.getAbsolutePath());
		this.threadList.add(t);
		
		if(!stopAllButton.isEnabled())
		{
			stopAllButton.setEnabled(true);
		}
		
		if(!stopSelectedButton.isEnabled())
		{
			stopSelectedButton.setEnabled(true);
		}

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				threadsCountLabel.setText("Threads active: " + ++threadCounter);
			}
		});
	}
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * Add found file to GUI
	 */
	public void addResult(final File file)
	{
		lock.lock();
		
		SwingUtilities.invokeLater(new Runnable()
		{
					
			@Override
			public void run()
			{
				resultList.addElement(file);

				sizeCount = sizeCount + (file.length() / 1048576);
				sizeCountLabel.setText("Total size: " + sizeCount + " megabytes");
				
				foundCount++;
				foundCountLabel.setText("Found: " + foundCount + " files");
			}
		});
		
		lock.unlock();
	}
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 *  selected list key listener
	 */
	public class selectedListKeyListener extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if(!selectedJList.isSelectionEmpty())
			{
				if(e.getKeyCode() == SPACE)
				{
					selectedList.remove(selectedJList.getSelectedIndex());
					directoriesSelectedCounter--;
					directoriesSelectedLabel.setText("Selected directories: " + directoriesSelectedCounter);
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
								
								//update selected directories counter	
								directoriesSelectedCounter++;
								directoriesSelectedLabel.setText("Selected directories: " + directoriesSelectedCounter);
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