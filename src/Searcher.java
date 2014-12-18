import java.io.File;

import javax.swing.SwingUtilities;

/**
 * Searcher class
 * @author Ilya
 *
 */
public class Searcher implements Runnable {
	
	private GUI gui;
	private String key;
	
	public Searcher(GUI gui)
	{
		this.gui = gui;
		this.key = this.gui.keyWordText.getText();
	}
	
	private void search(String path)
	{
		
		File file = new File(path);
		
		for(final File currentFile: file.listFiles())
		{
			if(currentFile.isDirectory() && currentFile.listFiles() != null)
			{
				search(currentFile.getAbsolutePath());
			}
			else
			{
				if(currentFile.getName().contains(this.key))
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							gui.resultList.addElement(currentFile);
						}
					});
				}
			}
		}

	}

	@Override
	public void run()
	{
		search(this.gui.currentDirectory.getPath());
	}
}