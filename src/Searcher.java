import java.io.File;

/**
 * Searcher class
 * @author Ilya
 *
 */
public class Searcher extends Thread {
	
	private GUI gui;
	private String key;
	private File initialDirectory;
	
	public Searcher(GUI gui, File initialDirectory)
	{
		this.gui = gui;
		this.key = this.gui.keyWordText.getText();
		this.initialDirectory = initialDirectory;
	}
	
	private void search(String path)
	{
		if(interrupted())
		{
			/**
			 * Interrupt again because multiple calls of interrupted()
			 * cause interrupted state to be false
			 */
			this.interrupt();

			return;
		}

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
		search(this.initialDirectory.getPath());			
	}
}