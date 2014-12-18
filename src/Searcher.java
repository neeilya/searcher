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
		search(this.initialDirectory.getPath());

		gui.threadCounter--;
		gui.threadsCountLabel.setText("Threads active: " + gui.threadCounter);
		
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
			gui.statusLabel.setText("Status: waiting...");
		}
	}
}