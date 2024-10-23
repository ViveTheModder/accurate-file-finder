package cmd;
//Accurate File Finder by ViveTheModder
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main 
{
	static int foundFileCnt, foundFileCntPrev, totalPathCnt;
	static File base;
	static String fileName;
	static String results="";
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
	public static void findFile()
	{
		if (base.isFile()) //file check
		{
			String currName = base.getName();
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
				String absPath = base.getAbsolutePath();
				results+=absPath+"\n";
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
			else
			{
				JOptionPane.showMessageDialog(null, HTML_TEXT+"Access is denied.", WINDOW_TITLE, JOptionPane.ERROR_MESSAGE, null);
				System.exit(1);	
			}
			totalPathCnt++;
		}
		Progress.label.setText(HTML_TEXT_CENTER+"Scanning...<br>"+base.getName()+HTML_TEXT_CENTER+"<br>Files found so far: "+foundFileCnt);
	}
	public static void main(String[] args)
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
		JOptionPane.showMessageDialog(null, msg, WINDOW_TITLE, JOptionPane.INFORMATION_MESSAGE);
		if (foundFileCnt>0)
		{
			msg="Results:\n"+results;
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