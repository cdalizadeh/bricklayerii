import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

public class Tetris extends Panel implements KeyListener
{
	private final int LEFT = 0;
	private final int RIGHT = 1;
	private final int DOWN = 2;
	private final int UP = 3;
	private final int CELL = 30;
	private final Dimension BOARD = new Dimension(10, 22); //2 CELLS added to height for buffer at top
	private final Dimension offset = new Dimension(BOARD.width * (CELL + 1), CELL);
	private Piece piece = new Piece(offset, CELL);
	private Piece nextpiece = new Piece(offset, CELL); //displayed in nextpiece panel
	private Field field = new Field(BOARD.width, BOARD.height, offset);
	private Background background = new Background();

	int level = 1;
	int delay = 500;
	int rate = 10;
	int fallcounter = 0; //increases each time task is run
	int fallmax = 50; //maximum value of fallcounter, decreases to change difficulty
	int[] movecounter = new int[4]; //increases if key is pressed
	int[] movemax = new int[] {27, 27, 10, 20}; //maximum value of movecounter, decreases as key is held
	int[] initialmovemax = new int[] {27, 27, 10, 20}; //restores movemax when kes is released
	Timer timer = new Timer();
	TimerTask task = new TimerTask()
	{
		public void run()
		{
			for (int i = 0; i < movecounter.length; i++)
			{
				if (movecounter[i] > 0)
				{
					movecounter[i]++;
					if (movecounter[i] >= movemax[i])
					{
						if (i == 3 && piece.rotate(field))
						{
						}
						else if (piece.move(i, field))
						{
							newPiece();
						}
						movecounter[i] = 1;
						if (i == 0 || i == 1)
						{
							movemax[i] = (int) (movemax[i] * 0.5) + 1; //maximum value of movecounter is decreased while the key is held
						}
					}
				}
			}
			if (fallcounter >= fallmax)
			{
				if (piece.move(DOWN, field)) //true if piece is added to field
				{
					newPiece();
				}
				fallcounter = 0;
			}
			fallcounter++;
			repaint();
		}
	};

	public Tetris()
	{
		timer.scheduleAtFixedRate(task, delay, rate);
	}

	public Dimension getPreferredSize() //indicates window size based on CELL and BOARD
	{
		return new Dimension(BOARD.width * (CELL + 1) + 2 * offset.width, BOARD.height * CELL + 1);
	}

	private BufferedImage osi = null;

	public void paint(Graphics gthis)
	{
		if (osi == null || osi.getWidth() != this.getWidth() || osi.getHeight() != this.getHeight())
		{
			osi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		Graphics2D g = osi.createGraphics();
		g.drawImage(Background.background, 0, 0, null);
		field.display(g, CELL, piece.colors);
		piece.display(g, CELL);
		g.setColor(Color.WHITE);
		g.drawRect(offset.width - 2, offset.height - 2, BOARD.width * CELL + 4, (BOARD.height - 2)* CELL + 4);
		g.drawRect(offset.width - 1, offset.height - 1, BOARD.width * CELL + 2, (BOARD.height - 2)* CELL + 2);
		nextpiece.displayNext(g, CELL, offset);
		drawLevelBox(g, offset, level);
		drawLinesBox(g, offset);
		gthis.drawImage(osi, 0, 0, null);
	}

	public void update(Graphics g)
	{
		paint(g);
	}

	public void drawLevelBox(Graphics g, Dimension offset, int level) //draws box that says
	{
		int width;
		width = (int) (offset.width / 3);
		g.setColor(Color.BLACK);
		g.fillRect(offset.width * 2 + width, width, width, width);
		g.setColor(Color.WHITE);
		g.drawRect(offset.width * 2 + width - 1, width - 1, width + 1, width + 1);
		g.setFont(Font.decode("Calibri-BOLD-20"));
		g.setColor(Color.WHITE);
		drawCentred((Graphics2D) g, "LEVEL:", offset.width * 2 + width + (int) (width / 2), width + (int) (width / 4));
		g.setFont(Font.decode("Calibri-BOLD-48"));
		drawCentred((Graphics2D) g, "" + level, offset.width * 2 + width + (int) (width / 2), width + (int) (3 * width / 5));
	}

	public void drawLinesBox(Graphics g, Dimension offset)
	{
		Dimension d = getSize();
		int width;
		width = (int) (offset.width / 3);
		g.setColor(Color.BLACK);
		g.fillRect(offset.width * 2 + width, d.height - width * 2, width, width);
		g.setColor(Color.WHITE);
		g.drawRect(offset.width * 2 + width - 1, d.height - width * 2 - 1, width + 1, width + 1);
		g.setFont(Font.decode("Calibri-BOLD-20"));
		g.setColor(Color.WHITE);
		drawCentred((Graphics2D) g, "LINES:", offset.width * 2 + width + (int) (width / 2), d.height - width * 2 + (int) (width / 4));
		g.setFont(Font.decode("Calibri-BOLD-48"));
		drawCentred((Graphics2D) g, "" + field.linescleared, offset.width * 2 + width + (int) (width / 2), d.height - width * 2 + (int) (3 * width / 5));
	}

	public void drawCentred(Graphics2D g, String s, int x, int y) //draws centred text
	{
		FontMetrics fm = g.getFontMetrics();
		g.drawString(s, x - fm.stringWidth(s) / 2, y + fm.getAscent() / 2);
	}

	public void newPiece()
	{
		piece = nextpiece;
		nextpiece = new Piece(offset, CELL);
		fallcounter = (int) (fallmax / 4);
		if (!field.legalitycheck(piece.Tetromino[piece.type][piece.orientation], piece.position)) //true if placement of piece is illegal
		{
			try{Thread.sleep(2000);}catch(Exception e){System.out.print("Exception" + e);}
			System.exit(0);
		}
		if (field.recentclear == true) //true if a clear recently occurred
		{
		 	if (field.linescleared / 10 > level - 1 && level < 10)
			{
				level = field.linescleared / 10 + 1; //level is increased as needed
				background.changeImage(level);
				fallmax = (int) (fallmax * 0.8); //maximum value of fallcounter decreases by 20% for each level
			}
			field.recentclear = false;
		}
		movemax[0] = initialmovemax[0];
		movemax[1] = initialmovemax[1];
	}

	public void keyTyped(KeyEvent e){}
	public void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_UP)
		{
			if (movecounter[UP] == 0)
			{
				movemax[UP] = initialmovemax[UP];
				movecounter[UP] = movemax[UP] - 1;
			}
		}
		else if(code == KeyEvent.VK_LEFT)
		{
			if (movecounter[LEFT] == 0)
			{
				movemax[LEFT] = initialmovemax[LEFT];
				movecounter[LEFT] = movemax[LEFT] - 1;
			}
		}
		else if(code == KeyEvent.VK_RIGHT)
		{
			if (movecounter[RIGHT] == 0)
			{
				movemax[RIGHT] = initialmovemax[RIGHT];
				movecounter[RIGHT] = movemax[RIGHT] - 1;
			}
		}
		else if (code == KeyEvent.VK_DOWN)
		{
			if (movecounter[DOWN] == 0)
			{
				movemax[DOWN] = initialmovemax[DOWN];
				movecounter[DOWN] = movemax[DOWN] - 1;
			}
		}
		else if(code == KeyEvent.VK_SPACE)
		{
			piece.drop(field);
			newPiece();
		}
	}
	public void	keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_UP)
		{
			movecounter[UP] = 0;
		}
		else if(code == KeyEvent.VK_LEFT)
		{
			movecounter[LEFT] = 0;
		}
		else if(code == KeyEvent.VK_RIGHT)
		{
			movecounter[RIGHT] = 0;
		}
		else if (code == KeyEvent.VK_DOWN)
		{
			movecounter[DOWN] = 0;
		}
	}
}