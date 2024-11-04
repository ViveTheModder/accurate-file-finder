package cmd;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Progress 
{
	static JFrame frame = new JFrame(Main.WINDOW_TITLE);
	static JLabel label = new JLabel();
	public static void setProgressReport()
	{
		JPanel panel = new JPanel();
		JLabel scan = new JLabel(Main.HTML_TEXT_CENTER+"Scanning...");
		scan.setHorizontalAlignment(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(Box.createVerticalGlue()); //top margin
		panel.add(scan);
		panel.add(new JLabel(" "));
		panel.add(label);
		panel.add(Box.createVerticalGlue()); //bottom margin
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		frame.add(panel);
		frame.setSize(1024,256);
		frame.setLocationRelativeTo(null); //center window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
