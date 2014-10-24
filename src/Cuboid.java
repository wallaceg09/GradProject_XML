import java.util.ArrayList;


/**
 * @author gwallace4
 *
 * @param 
 */
public class Cuboid {
	public static final int FRONT 	= 0;
	public static final int BACK 	= 1;
	public static final int LEFT 	= 2;
	public static final int RIGHT 	= 3;
	public static final int TOP		= 4;
	public static final int BOTTOM 	= 5;
	
	@Deprecated
	int xValue;
	@Deprecated
	int yValue;
	@Deprecated
	int zValue;
		
	/**
	 * [0] = front
	 * [1] = back
	 * [2] = left
	 * [3] = right
	 * [4] = top
	 * [5] = bottom
	 */
	int[] faceValues;
	
	String[] xDimNames;
	String[] yDimNames;
	String[] zDimNames;
	
	@Deprecated
	public Cuboid(int xIn, int yIn, int zIn)
	{
		xValue = xIn;
		yValue = yIn;
		zValue = zIn;
	}
	
	public Cuboid(int xPosition, int yPosition, int zPosition, int [][][] dataArray)
	{
		int maxY = dataArray.length;
		int maxX = dataArray[0].length;
		int maxZ = dataArray[0][0].length;
		
		faceValues = new int[6];
		
		xDimNames = yDimNames = zDimNames = new String[6];
		
		//TODO: reverse axes perhaps?
		faceValues[FRONT] = dataArray[yPosition][xPosition][zPosition];
		faceValues[BACK] = dataArray[yPosition][maxX-xPosition-1][maxZ-zPosition-1];
		faceValues[LEFT] = dataArray[yPosition][maxX-xPosition-1][zPosition];
		faceValues[RIGHT] = dataArray[yPosition][xPosition][maxZ-zPosition-1];
		faceValues[TOP] = dataArray[yPosition][xPosition][maxZ-zPosition-1];
		faceValues[BOTTOM] = dataArray[maxY - yPosition-1][xPosition][zPosition];
	}
	
	public int getValue(int face)
	{
		return faceValues[face];
	}
	
	@Deprecated
	public int getXValue() {
		return xValue;
	}
	@Deprecated
	public int getYValue() {
		return yValue;
	}
	@Deprecated
	public int getZValue() {
		return zValue;
	}
	
	
}
