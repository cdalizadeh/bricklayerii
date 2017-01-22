import java.awt.*;

public class Piece
{
	public final byte[][][][] Tetromino = new byte[][][][]{{{{0,1,0,0},{0,1,0,0},{0,1,0,0},{0,1,0,0}}, {{0,0,0,0},{0,0,0,0},{1,1,1,1},{0,0,0,0}}, {{0,0,1,0},{0,0,1,0},{0,0,1,0},{0,0,1,0}}, {{0,0,0,0},{1,1,1,1},{0,0,0,0},{0,0,0,0}}},
														{{{1,1,0},{0,1,0},{0,1,0}}, {{0,0,0},{1,1,1},{1,0,0}}, {{0,1,0},{0,1,0},{0,1,1}}, {{0,0,1},{1,1,1},{0,0,0}}},
														{{{0,1,0},{0,1,0},{1,1,0}}, {{0,0,0},{1,1,1},{0,0,1}}, {{0,1,1},{0,1,0},{0,1,0}}, {{1,0,0},{1,1,1},{0,0,0}}},
														{{{1,1},{1,1}}, {{1,1},{1,1}}, {{1,1},{1,1}}, {{1,1},{1,1}}},
														{{{0,1,0},{1,1,0},{1,0,0}}, {{0,0,0},{1,1,0},{0,1,1}}, {{0,0,1},{0,1,1},{0,1,0}}, {{1,1,0},{0,1,1},{0,0,0}}},
														{{{0,1,0},{1,1,0},{0,1,0}}, {{0,0,0},{1,1,1},{0,1,0}}, {{0,1,0},{0,1,1},{0,1,0}}, {{0,1,0},{1,1,1},{0,0,0}}},
														{{{1,0,0},{1,1,0},{0,1,0}}, {{0,0,0},{0,1,1},{1,1,0}}, {{0,1,0},{0,1,1},{0,0,1}}, {{0,1,1},{1,1,0},{0,0,0}}}}; //4D array holding all piece information needed

	final Color[] colors = {Color.CYAN, Color.BLUE, Color.ORANGE, Color.YELLOW, Color.GREEN, new Color(255, 0, 255), Color.RED};
	public int type = (int) (Math.random() * 7);
	public int orientation = 0;
	public Point position = new Point(3, 1); //as measured in CELLS
	public Point nextboxposition = new Point(0, 0); //position within display box, measured in pixels
	private Dimension offset;
	private int CELL;

	public Piece(Dimension temp, int cell)
	{
		CELL = cell;
		offset = temp;
		if (type == 3)
		{
			position = new Point(4,1);
		}
		positionInNextBox();
	}

	public void display (Graphics g, int CELL)
	{
		g.setColor(colors[type]);
		for (int i = 0; i < Tetromino[type][orientation].length; i++)
		{
			for (int j = 0; j < Tetromino[type][orientation][i].length; j++)
			{
				if (Tetromino[type][orientation][i][j] == 1)
				{
					if (!(position.y == 1 && j == 0)) //excludes CELLS within buffer region
					{
						g.fillRect(position.x * CELL + i * CELL +  1 + offset.width, position.y * CELL + j * CELL + 1 - 2 * CELL + offset.height, CELL - 1, CELL - 1);
					}
				}
			}
		}
	}

	public void displayNext (Graphics g, int CELL, Dimension offset)
	{
		int width = (int) (offset.width / 3);
		g.setColor(Color.BLACK);
		g.fillRect(width, width, CELL * 4 + 1, CELL * 4 + 1);
		g.setColor(Color.WHITE);
		g.drawRect(width - 1, width - 1, CELL * 4 + 1 + 1, CELL * 4 + 1 + 1);
		g.setColor(colors[type]);
		for (int i = 0; i < Tetromino[type][orientation].length; i++)
		{
			for (int j = 0; j < Tetromino[type][orientation][i].length; j++)
			{
				if (Tetromino[type][orientation][i][j] == 1)
				{
					g.fillRect(nextboxposition.x + width + i * CELL + 1, nextboxposition.y + j * CELL + 1 + width, CELL - 1, CELL - 1);
				}
			}
		}
	}

	public void positionInNextBox() //positions each piece within display according to type
	{
		switch(type)
		{
			case 0: nextboxposition = new Point(0, (int) (0.5 * CELL));
				break;
			case 1:
			case 2: nextboxposition = new Point((int) (0.5 * CELL), CELL);
				break;
			case 3: nextboxposition = new Point(CELL, CELL);
				break;
			case 4:
			case 5:
			case 6: nextboxposition = new Point((int) (0.5 * CELL), CELL);
				break;
		}
	}

	public boolean rotate(Field field)
	{
		if (field.legalitycheck(Tetromino[type][(orientation + 1) % 4], position)) //true if resulting position is legal
		{
			orientation = (orientation + 1) % 4;
		}
		return true;
	}

	public boolean move(int direction, Field field)
	{
		if (direction == 0)
		{
			position.x = position.x - 1;
			if (!field.legalitycheck(Tetromino[type][orientation], position)) //reverses movement if resulting position is illegal
			{
				position.x = position.x + 1;
			}
		}
		else if (direction == 1)
		{
			position.x = position.x + 1;
			if (!field.legalitycheck(Tetromino[type][orientation], position)) //reverses movement if resulting position is illegal
			{
				position.x = position.x - 1;
			}
		}
		else if (direction == 2)
		{
			position.y = position.y + 1;
			if (!field.legalitycheck(Tetromino[type][orientation], position)) //reverses movement if resulting position is illegal, adds piece to field
			{
				position.y = position.y - 1;
				field.add(Tetromino[type][orientation], position, type + 1);
				field.clearcheck(field);
				return true;
			}
		}
		return false;
	}

	public void drop(Field field)
	{
		for( ; field.legalitycheck(Tetromino[type][orientation], position); position.y++)
		{
		}
		position.y--;
		field.add(Tetromino[type][orientation], position, type + 1);
		field.clearcheck(field);
	}
}