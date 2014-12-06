import java.io.File;
import java.util.ArrayList;


public class Searcher implements Runnable {
	
	private File directory;
	
	private ArrayList<File> results;
	
	public Searcher(File directory)
	{
		this.directory = directory;
		this.results = new ArrayList<File>();
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
				results.add(currentFile);
			}
		}
		
	}

	@Override
	public void run()
	{
		this.search(this.directory.getPath(), "");
	}

}
