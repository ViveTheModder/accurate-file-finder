package cmd;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Progress 
{
	static JFrame frame = new JFrame(Main.WINDOW_TITLE);
	static JLabel label = new JLabel();
	public static void setProgressReport()
	{
		JPanel panel = new JPanel();
		panel.add(label);
		frame.add(panel);
		frame.setSize(1024,128);
		frame.setLocationRelativeTo(null); //center window
		frame.setDefaultCloseOperation(0); //prevent window from being closed
		frame.setVisible(true);
	}
}
