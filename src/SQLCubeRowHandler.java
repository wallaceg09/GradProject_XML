import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;


public class SQLCubeRowHandler {
	private int nextXPosition = 0;
	private int nextYPosition = 0;
	private int nextZPosition = 0;
	/*
	private HashMap<String, Integer> xDimensionPositionMap;
	private HashMap<String, Integer> yDimensionPositionMap;
	private HashMap<String, Integer> zDimensionPositionMap;
	*/
	private ArrayList<SQLCubeRow> cubeRows;
	
	private ArrayList<String> xDimensionValueArray;
	private ArrayList<String> yDimensionValueArray;
	private ArrayList<String> zDimensionValueArray;
	
	
	
	
	public SQLCubeRowHandler()
	{
		//TODO: Completely remove
		/*
		xDimensionPositionMap = new HashMap<String, Integer>();
		yDimensionPositionMap = new HashMap<String, Integer>();
		zDimensionPositionMap = new HashMap<String, Integer>();
		*/
		//TODO: End completely remove TODO
		
		xDimensionValueArray = new ArrayList<String>();
		yDimensionValueArray = new ArrayList<String>();
		zDimensionValueArray = new ArrayList<String>();
		
		cubeRows = new ArrayList<SQLCubeRow>();
	}
	
	public void newRow(ResultSet resultSet)
	{
		SQLCubeRow row =new SQLCubeRow(resultSet);
		cubeRows.add(row);
		
		//TODO: Completely remove
		/*if(xDimensionPositionMap.get(row.getxDimensionName()) == null)
		{
			xDimensionPositionMap.put(row.getxDimensionName(), nextXPosition++);
		}
		if(yDimensionPositionMap.get(row.getyDimensionName()) == null)
		{
			yDimensionPositionMap.put(row.getyDimensionName(), nextYPosition++);
		}
		if(zDimensionPositionMap.get(row.getzDimensionName()) == null)
		{
			zDimensionPositionMap.put(row.getzDimensionName(), nextZPosition++);
		}*/
		//TODO: End completely remove TODO
		
		String xDimName = row.getxDimensionName();
		String yDimName = row.getyDimensionName();
		String zDimName = row.getzDimensionName();
		
		if(xDimensionValueArray.indexOf(xDimName)==-1)
		{
			xDimensionValueArray.add(xDimName);
		}
		if(yDimensionValueArray.indexOf(yDimName) == -1)
		{
			yDimensionValueArray.add(yDimName);
		}
		if(zDimensionValueArray.indexOf(zDimName) == -1)
		{
			zDimensionValueArray.add(zDimName);
		}
	}
	
	/**
	 * @return Three dimensional array representing the current three dimensional cubette. Stored in y,x,z format.
	 */
	@Deprecated
	public int[][][] _getArray()
	{
		int[][][] output = new int[nextYPosition][nextXPosition][nextZPosition];
		for(SQLCubeRow row : cubeRows)
		{
			//output[yDimensionPositionMap.get(row.getyDimensionName())][xDimensionPositionMap.get(row.getxDimensionName())][zDimensionPositionMap.get(row.getzDimensionName())] = ((java.math.BigDecimal) row.getValue()).intValueExact();
		}
		//TODO: Implement
		return output;
	}
	public int[][][] getDataValueArray()
	{
		int[][][] output = new int[yDimensionValueArray.size()][xDimensionValueArray.size()][zDimensionValueArray.size()];
		for(SQLCubeRow row : cubeRows)
		{
			int y = yDimensionValueArray.indexOf(row.getyDimensionName());
			int x = xDimensionValueArray.indexOf(row.getxDimensionName());
			int z = zDimensionValueArray.indexOf(row.getzDimensionName());
			output[y][x][z] = ((java.math.BigDecimal) row.getValue()).intValueExact();
		}
		//TODO: Implement
		return output;
	}
	
	public String[] getXDimensionValueArray()
	{
		String[] output = new String[0];
		return xDimensionValueArray.toArray(output);
	}
	public String[] getYDimensionValueArray()
	{
		String[] output = new String[0];
		return yDimensionValueArray.toArray(output);
	}
	public String[] getZDimensionValueArray()
	{
		String[] output = new String[0];
		return zDimensionValueArray.toArray(output);
	}
}
