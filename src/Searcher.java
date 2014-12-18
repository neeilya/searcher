import java.io.File;

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
					gui.addResult(currentFile);
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