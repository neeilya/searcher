import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
		
	}
	
	// ------------------------------------------------------------------
	
	/**
	 * listClickListener - inner class so it can access our jList var
	 * @author Ilya
	 */
	private class listClickListener extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent event) {
			
			if(event.getClickCount() == 2)
			{
				if(jList.getSelectedValue().getClass().equals(String.class))
				{
					currentDirectory = currentDirectory.getParentFile();
					listUpdate(currentDirectory);
				}
				else
				{
					//setting currentDirectory
					currentDirectory = (File) jList.getSelectedValue();
					
					if(currentDirectory.isDirectory())
						listUpdate(currentDirectory);
				}
				
			}
			
			super.mouseClicked(event);
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
	 * build window function
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
}
