package cmd;
//Accurate File Finder v1.1 by ViveTheModder
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main 
{
	static int foundFileCnt, foundFileCntPrev, option, totalPathCnt;
	static File base;
	static String deniedDirs="";
	static String fileName;
	static String results="";
	static final long GB = 1073741824;
	static final long MB = 1048576;
	static final long TB = 1099511627776L;
	static final String BAD_CHARS = "'<>,:/\\/|?*";
	static final String HTML_TEXT = "<html><div style='font-family: Segoe UI;'>";
	static final String HTML_TEXT_CENTER = "<html><div style='font-family: Segoe UI; text-align: center;'>";
	static final String WINDOW_TITLE = "File Finder";
	public static boolean isFileNameForbidden(String fileName)
	{
		char[] fileNameArr = fileName.toCharArray();
		char[] badCharsArr = BAD_CHARS.toCharArray();
		for (char ch: fileNameArr)
		{
			for (int i=0; i<badCharsArr.length; i++)
			{
				if (i==0 && badCharsArr[i]=='*') continue;
				if (i==badCharsArr.length-1 && badCharsArr[i]=='*') continue;
				if (ch==badCharsArr[i]) return true;
			}
		}
		return false;
	}
	public static String getFileDateModified()
	{
		Date d = new Date(base.lastModified());
		String format=null;
		Locale loc = Locale.getDefault();
		String locAsString = loc.toString();
		if (locAsString.contains("US") || locAsString.contains("CA") || locAsString.contains("PH"))
			format="MM/dd/yyyy h:mm:ss a";
		else if (locAsString.contains("CN") || locAsString.contains("JP") || locAsString.contains("KR"))
			format="yyyy/MM/dd HH:mm:ss";
		else format="dd/MM/yyyy HH:mm;ss";
		return new SimpleDateFormat(format).format(d);
	}
	public static String getFileSize()
	{
		long size = base.length();
		double newSize=0;
		String unit=" ";
		if (size>=TB) 
		{
			newSize=(double)size/TB; unit+="TB";
		}
		else if (size>=GB) 
		{
			newSize=(double)size/GB; unit+="GB";
		}
		else if (size>=MB) 
		{
			newSize=(double)size/MB; unit+="MB";
		}
		else if (size>=1024)
		{
			newSize=(double)size/1024; unit+="KB";
		}
		else unit+="Bytes";
		if (size>=1024) return String.format("%.3f", newSize)+unit;
		else return size+unit;
	}
	public static void exportCSV() throws IOException
	{
		File csv = new File("results.csv");
		FileWriter csvWriter = new FileWriter(csv);
		String output = "file-name,folder-name,date-modified,file-type,file-size\n"; //CSV header
		output+=results;
		csvWriter.write(output);
		csvWriter.close();
		Desktop.getDesktop().open(csv);
	}
	public static void findFile()
	{
		String absPath = base.getAbsolutePath();
		String currName = base.getName();
		if (base.isFile()) //file check
		{
			String[] currNameArray = currName.split("\\.");
			String currExt = "."+currNameArray[currNameArray.length-1];
			String currNameWithNoExt = currName.replace(currExt, "");
			foundFileCntPrev = foundFileCnt;
			if (fileName.equals(currName)) foundFileCnt++;
			else
			{
				if (currName.contains(fileName)) foundFileCnt++; 
				else if (fileName.equals("*.*")) foundFileCnt++;
				else if (fileName.startsWith("*."))
				{
					if (fileName.endsWith(currExt)) foundFileCnt++;
				}
				else if (fileName.endsWith("*"))
				{
					String[] fileNameArray = fileName.split("\\.");
					String fileNameWithNoExt = fileNameArray[0];
					if (fileNameWithNoExt.equals(currNameWithNoExt)) foundFileCnt++;
				}
			}
			if (foundFileCntPrev!=foundFileCnt)
			{
				if (option!=0) results+=absPath+"\n";
				else results+=currName+","+absPath.replace(currName, "")+","+getFileDateModified()+","+currExt+","+getFileSize()+"\n";
			}
			totalPathCnt++;
		}
		else if (base.isDirectory()) //folder check
		{
			File[] list = base.listFiles();
			if (list!=null)
			{
				for (File f: list) //subfolder check
				{
					base = f;
					findFile();
				}
			}
			else deniedDirs+=absPath+"\n";
			totalPathCnt++;
		}
		Progress.label.setText(HTML_TEXT_CENTER+"Scanning...<br>"+currName+HTML_TEXT_CENTER+"<br>Files found so far: "+foundFileCnt);
	}
	public static void main(String[] args) throws IOException
	{
		while(true)
		{
			String dir = JOptionPane.showInputDialog(HTML_TEXT+"Enter a directory to search files in:");
			if (dir==null) System.exit(1);
			if (dir.isEmpty()) 
			{
				base = new File("."); //current directory
				break;
			}
			base = new File(dir);
			if (!base.isDirectory()) 
				JOptionPane.showMessageDialog(null, HTML_TEXT+"Invalid directory!", WINDOW_TITLE, JOptionPane.ERROR_MESSAGE, null);
			else break;
		}
		while (true)
		{
			fileName = JOptionPane.showInputDialog(HTML_TEXT+"Enter a file name (or part of it) to search for:");
			if (fileName==null) System.exit(1);
			if (isFileNameForbidden(fileName)) 
				JOptionPane.showMessageDialog(null, HTML_TEXT+"Invalid file name!", WINDOW_TITLE, JOptionPane.ERROR_MESSAGE, null);
			else break;
		}
		option = JOptionPane.showConfirmDialog(null, HTML_TEXT+"Do you want more detailed results in a CSV file?", WINDOW_TITLE, JOptionPane.YES_NO_OPTION);
		Progress.setProgressReport();
		long start = System.currentTimeMillis();
		findFile();
		long end = System.currentTimeMillis();
		double time = (end-start)/(double)1000;
		int mins = (int)(time/60); double sec = time%60;
		
		String msg = HTML_TEXT+totalPathCnt+" navigations were made, and "+foundFileCnt+" file(s) were found.";
		msg+="<br>Time: "+mins+" minute(s) and "+String.format("%.3f", sec)+" second(s).";
		if (foundFileCnt>0) msg+="<br>Click OK to show results.";
		
		Progress.frame.setVisible(false);
		if (option==0) exportCSV();
		else JOptionPane.showMessageDialog(null, msg, WINDOW_TITLE, JOptionPane.INFORMATION_MESSAGE);
		if (foundFileCnt>0)
		{
			msg="Results:\n"+results;
			if (deniedDirs.length()!=0) msg+="\nDenied Directories:\n"+deniedDirs;
			//courtesy of ZeroDevs; this displays the results in a scrollable text area
			JOptionPane.showMessageDialog(null,
			new JScrollPane(new JTextArea(msg)
			{
				private static final long serialVersionUID = 1L;
				{
					setEditable(false);
					setRows(10);
				}
			}), WINDOW_TITLE, JOptionPane.INFORMATION_MESSAGE);
		}
		System.exit(0);
	}
}