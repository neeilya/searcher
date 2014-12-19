import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingUtilities;

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
	private Date createdFrom, createdTo;
	
	// ------------------------------------------------------------------------------------------------------------
	
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
		this.setDates(gui.createdFromText.getText(), gui.createdToText.getText());
	}
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * Set dates for search mask
	 * @param dateFromStr
	 * @param dateToStr
	 */
	private void setDates(String dateFromStr, String dateToStr)
	{
		if(!dateFromStr.equals(""))
		{
			try
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				this.createdFrom = dateFormat.parse(gui.createdFromText.getText());
			}
			catch (ParseException e)
			{
				// some code
			}			
		}

		if(!dateToStr.equals(""))
		{
			try
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				this.createdTo = dateFormat.parse(gui.createdToText.getText());
			}
			catch (ParseException e)
			{
				// some code
			}			
		}
	}
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * Search algorithm
	 * @param path
	 * @throws InterruptedException 
	 */
	private void search(String path) throws InterruptedException
	{
		if(interrupted())
		{
			throw new InterruptedException("Interrupted");
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
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * Check if file corresponds to specified mask
	 * @param file
	 * @return
	 */
	private boolean maskApproved(final File file)
	{
		if(file.getName().contains(this.key))
		{
			// if sizeFrom only specified
			if(!gui.sizeFromText.getText().equals("") && gui.sizeToText.getText().equals(""))
			{
				if((file.length() / (1024 * 1024)) < Integer.parseInt(gui.sizeFromText.getText()))
					return false;
			}
			
			// if sizeTo only specified
			if(gui.sizeFromText.getText().equals("") && !gui.sizeToText.getText().equals(""))
			{
				if((file.length() / (1024 * 1024)) > Integer.parseInt(gui.sizeToText.getText()))
					return false;
			}
			
			// if both sizeTo AND sizeFrom specified
			if(!gui.sizeFromText.getText().equals("") && !gui.sizeToText.getText().equals(""))
			{
				if((file.length() / (1024 * 1024)) < Integer.parseInt(gui.sizeFromText.getText())
						|| (file.length() / (1024 * 1024)) > Integer.parseInt(gui.sizeToText.getText()))
					return false;
			}
			
			// if createdFrom only specified
			if(createdFrom != null && createdTo == null)
			{
				if(!new Date(file.lastModified()).after(this.createdFrom))
					return false;
			}
			
			// if createdTo only specified
			if(createdFrom == null && createdTo != null)
			{
				if(!new Date(file.lastModified()).before(this.createdTo))
					return false;
			}
			
			// if both createdFrom AND createdTo specified
			if(createdFrom != null && createdTo != null)
			{
				if((new Date(file.lastModified())).after(this.createdFrom) != true
					|| ((new Date(file.lastModified())).before(this.createdTo)) != true)
					return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	// ------------------------------------------------------------------------------------------------------------
	
	/**
	 * Runnable
	 */
	@Override
	public void run()
	{
		try
		{
			// run recursive search
			search(this.initialDirectory.getPath());
		}
		catch(InterruptedException e)
		{
			// some code
		}

		// update counter at GUI
		gui.threadCounter--;
		gui.threadsCountLabel.setText("Threads active: " + gui.threadCounter);
		
		// remove finished thread from GUI
		for(int i = 0; i < gui.selectedList.size(); ++i)
		{
			final int t = i;
			if(gui.selectedList.get(i).getAbsolutePath().equals(this.initialDirectory.getAbsolutePath()))
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						gui.selectedList.remove(t);
					}
				});
			}
		}
		
		// remove finished thread from threadList
		for(int i = 0; i < gui.threadList.size(); ++i)
		{
			if(gui.threadList.get(i).getName().equals(this.getName()))
			{
				gui.removeThread(i);
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