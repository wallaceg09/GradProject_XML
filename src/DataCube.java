import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;


public class DataCube {
	private String[] xDimensionValues;
	private String[] yDimensionValues;
	private String[] zDimensionValues;
	
	private Cuboid[][][] cuboidArray;
	
	public DataCube(ResultSet[] results)
	{
		int[][][] cubeDataArray = null;
		try {

			cubeDataArray = processResultSet(results);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int yLen = cubeDataArray.length;
		int xLen = cubeDataArray[0].length;
		int zLen = cubeDataArray[0][0].length;
		
		cuboidArray = new Cuboid[yLen][xLen][zLen];
		
		
		for(int y = 0; y < yLen; ++y)
		{
			for (int x = 0; x < xLen; ++x)
			{
				for(int z = 0; z < zLen; ++z)
				{
					//dataCube[x][y][z] = new Cuboid<Integer>(rand.nextInt(9), rand.nextInt(9), rand.nextInt(9));
					cuboidArray[y][x][z] = new Cuboid(x, y, z, cubeDataArray);
				}
			}
		}
	}
	//TODO: Handle more than three dimensions
	private int[][][] processResultSet(ResultSet[] results) throws SQLException
	{
		int[][][] output = null;
		HashMap<String, HashMap<String, Integer>> dimensionPositionMap = new HashMap<String, HashMap<String, Integer>>();
		for(ResultSet result : results)
		{
			int positions[] = new int[3];
			
			ResultSetMetaData meta = result.getMetaData();
			String[] columnNames = new String[meta.getColumnCount()];
			
			SQLCubeRowHandler handler = new SQLCubeRowHandler();
			
			for(int i = 0; i < meta.getColumnCount(); ++i)
			{
				//TODO: Handle more than 3 dimensions
				dimensionPositionMap.put(meta.getColumnLabel(i+1), new HashMap<String, Integer>());
				//System.out.println(columnNames[i] + " : " + meta.getColumnClassName(i+1));
			}
			//result.findColumn("TIME");
			
			while(result.next())
			{
				handler.newRow(result);
			}
			
			output = handler.getDataValueArray();
			
			for(int y = 0; y < output.length; ++y)
			{
				for(int x = 0; x < output[0].length; ++x)
				{
					for(int z = 0; z < output[0][0].length; ++z)
					{
						System.out.printf("[%d][%d][%d] : %d\n", y, x, z, output[y][x][z]);
					}
				}
			}
			//TODO: Move this to constructor
			/*
			xDimensionValues = handler.getXDimensionValueArray();
			yDimensionValues = handler.getYDimensionValueArray();
			zDimensionValues = handler.getZDimensionValueArray();
			*/
			
//			for(int x = 0; x < xDimensionValues.length; ++x)
//			{
//				System.out.printf("[%d] : %s\n", x, xDimensionValues[x]);
//			}
//			
//			for(int y = 0; y < yDimensionValues.length; ++y)
//			{
//				System.out.printf("[%d] : %s\n", y, yDimensionValues[y]);
//			}
//			
//			for(int z = 0; z < zDimensionValues.length; ++z)
//			{
//				System.out.printf("[%d] : %s\n", z, zDimensionValues[z]);
//			}
		}
		return output;
	}
	
	public Cuboid[][][] getCuboidArray()
	{
		return cuboidArray;
	}
	
	public String[] getXDimensionValues()
	{
		return xDimensionValues;
	}
	
	public String[] getYDimensionValues()
	{
		return yDimensionValues;
	}
	
	public String[] getZDimensionValues()
	{
		return zDimensionValues;
	}
}
