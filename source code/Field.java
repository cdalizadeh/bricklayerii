import java.awt.*;

public class Field
{
	private int[][] Field;
	private final int buffer = 2;
	private Dimension offset;
	public int linescleared = 0;
	public boolean recentclear = false;

	public Field(int x, int y, Dimension temp)
	{
		offset = temp;
		Field = new int[x][y]; //field of static values
	}

	public void display (Graphics g, int CELL, Color[] colors)
	{
		int columns = Field.length;
		int rows = Field[0].length - 2;
		g.setColor(Color.BLACK);
		g.fillRect(offset.width, offset.height, columns * CELL, rows * CELL);
		g.setColor(new Color(20, 20, 20));
		for (int i = 0; i < columns + 1; i++)
		{
			g.drawLine(i * CELL + offset.width, offset.height, i * CELL + offset.width, CELL * rows + offset.height);
		}
		for (int i = 0; i < rows + 1; i++)
		{
			g.drawLine(offset.width, CELL * i + offset.height, CELL * columns + offset.width, CELL * i + offset.height);
		}
		for (int i = 0; i < Field.length; i++)
		{
			for (int j = 2; j < Field[i].length; j++) //excludes CELLS within buffer region
			{
				if (Field[i][j] > 0)
				{
					g.setColor(colors[Field[i][j] - 1]);
					g.fillRect(i * CELL + 1 + offset.width, j * CELL + 1 - buffer * CELL + offset.height, CELL - 1, CELL - 1);
				}
			}
		}
	}

	public void add(byte[][] newpiece, Point position, int color) //adds piece to array, given shape, position, and colour
	{
		for (int i = 0; i < newpiece.length; i++)
		{
			for (int j = 0; j < newpiece[i].length; j++)
			{
				if (newpiece[i][j] > 0)
				{
					Field[position.x + i][position.y + j] = color;
				}
			}
		}
	}

	public boolean legalitycheck(byte[][] newpiece, Point position) // determines whether positioning of a piece is legal or not
	{
		for (int i = 0; i < newpiece.length; i++)
		{
			for (int j = 0; j < newpiece[i].length; j++)
			{
				if (newpiece[i][j] > 0 && (position.x + i < 0 || position.x + i >= Field.length || position.y + j >= Field[0].length || Field[position. x + i][position.y + j] > 0))
				{
					return false;
				}
			}
		}
		return true;
	}

	public void clearcheck(Field field) //checks to see if a specific position must be cleared
	{
		boolean clear;
		for (int j = 0; j < Field[0].length; j++)
		{
			clear = true;
			for (int i = 0; i < Field.length; i++)
			{
				if (Field[i][j] == 0)
				{
					clear = false;
					break;
				}
			}
			if (clear == true)
			{
				field.clear(j);
			}
		}
	}
	public void clear(int j) //clears position given row j, which is eliminated
	{
		for ( ; j > 0; j--)
		{
			for (int i = 0; i < Field.length; i++)
			{
				Field[i][j] = Field[i][j - 1];
			}
		}
		for (int i = 0; i < Field.length; i++)
		{
			Field[i][0] = 0;
		}
		linescleared++;
		recentclear = true;
	}
}