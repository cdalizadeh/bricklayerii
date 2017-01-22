import java.awt.*;
import java.awt.event.*;

public class Program extends Frame implements WindowListener
{
	Tetris panel = new Tetris();

	public Program()
	{
		setVisible(true);
		setTitle("BRICKLAYER II");
		setResizable(false);
		addWindowListener(this);
		panel.setFocusable(false);
		add(panel);
		pack();
		addKeyListener(panel);
		setLocationRelativeTo(null);
	}

	public void windowDeactivated(WindowEvent e)
	{
	}

	public void windowActivated(WindowEvent e)
	{
	}

	public void windowDeiconified(WindowEvent e)
	{
	}

	public void windowIconified(WindowEvent e)
	{
	}

	public void windowClosed(WindowEvent e)
	{
	}

	public void windowClosing(WindowEvent e)
	{
		System.exit(0);
	}

	public void windowOpened(WindowEvent e)
	{
	}
}