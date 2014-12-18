import java.io.File;

/**
 * Searcher class
 * @author Ilya
 *
 */
public class Searcher extends Thread {
	
	/**
	 * Searcher class fields
	 */
	private GUI gui;
	private String key;
	public File initialDirectory;
	
	/**
	 * Constructor
	 * @param gui
	 * @param initialDirectory
	 */
	public Searcher(GUI gui, File initialDirectory)
	{
		this.gui = gui;
		this.key = this.gui.keyWordText.getText();
		this.initialDirectory = initialDirectory;
	}
	
	/**
	 * Search algorythm
	 * @param path
	 */
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
				if(this.maskApproved(currentFile))
				{
					gui.addResult(currentFile);
				}
			}
		}

	}
	
	/**
	 * Check if file corresponds to specified mask
	 * @param file
	 * @return
	 */
	private boolean maskApproved(File file)
	{
		if(file.getName().contains(this.key))
		{
			// if sizeFrom only specified
			if(!gui.sizeFromText.getText().equals("") && gui.sizeToText.getText().equals(""))
			{
				return (file.length() / (1024 * 1024) > Integer.parseInt(gui.sizeFromText.getText()));
			}
			
			// if sizeTo only specified
			if(gui.sizeFromText.getText().equals("") && !gui.sizeToText.getText().equals(""))
			{
				return (file.length() / (1024 * 1024) < Integer.parseInt(gui.sizeToText.getText()));
			}
			
			// if both sizeTo AND sizeFrom specified
			if(!gui.sizeFromText.getText().equals("") && !gui.sizeToText.getText().equals(""))
			{
				return (file.length() / (1024 * 1024) > Integer.parseInt(gui.sizeFromText.getText())
						&& file.length() / (1024 * 1024) < Integer.parseInt(gui.sizeToText.getText()));
			}
			
			return true;
		}
		return false;
	}
	
	/**
	 * Runnable
	 */
	@Override
	public void run()
	{
		// run recursive search
		search(this.initialDirectory.getPath());

		// update counter at GUI
		gui.threadCounter--;
		gui.threadsCountLabel.setText("Threads active: " + gui.threadCounter);
		
		// remove finished thread from GUI
		for(int i = 0; i < gui.selectedList.size(); ++i)
		{
			final int t = i;
			if(gui.selectedList.get(i).getAbsolutePath().equals(this.initialDirectory.getAbsolutePath()))
			{
				gui.selectedList.remove(t);
			}
		}
		
		// remove finished thread from threadList
		for(int i = 0; i < gui.threadList.size(); ++i)
		{
			if(gui.threadList.get(i).getName().equals(this.getName()))
			{
				gui.threadList.remove(i);
				break;
			}
		}
		
		// if all threads finished then update GUI
		if(gui.threadList.isEmpty())
		{
			gui.stopAllButton.setEnabled(false);
			gui.stopSelectedButton.setEnabled(false);
			gui.searchButton.setEnabled(true);
			gui.statusLabel.setText("Status: waiting...");
			
			// reset toolsPanel
			gui.searchParamsEnabled(true);
			gui.directoriesSelectedCounter = 0;
			gui.directoriesSelectedLabel.setText("Selected directories: " + gui.directoriesSelectedCounter);
		}
	}
}