import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;


public class Main {

	public static void main(String[] args) {
		Random rand = new Random(123456789);
		
//		Cuboid[][][] dataCube = null;
		
		//int[][][] cubeDataArray = null;
		
//		HashMap<String, Integer> xdimensionMap = new HashMap<String, Integer>();
//		HashMap<String, Integer> yDimensionMap = new HashMap<String, Integer>();
//		HashMap<String, Integer> zDimensionMap = new HashMap<String, Integer>();
		
		DataCube cube[] = null;
		
		Object[][] dataTable = null;
		
		DBManager manager = new DBManager();
		manager.connect(args[0], args[1].toCharArray());
		
		//manager.beginTextPrompt();
		
		try {
			ResultSet [] results = manager.processDMLScript("cube.sql");
			//cube = new DataCube(results);
			//DataCube.generateCubes(results);
			dataTable = DBManager.resultSetToArray(results[0]);
			for(int i = 0; i < dataTable.length; ++i)
			{
				System.out.println(Arrays.toString(dataTable[i]));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		int yLen = cubeDataArray.length;
//		int xLen = cubeDataArray[0].length;
//		int zLen = cubeDataArray[0][0].length;
//		Cuboid[][][] dataCube = new Cuboid[yLen][xLen][zLen];
//		
//		
//		for(int y = 0; y < yLen; ++y)
//		{
//			for (int x = 0; x < xLen; ++x)
//			{
//				for(int z = 0; z < zLen; ++z)
//				{
//					//dataCube[x][y][z] = new Cuboid<Integer>(rand.nextInt(9), rand.nextInt(9), rand.nextInt(9));
//					dataCube[y][x][z] = new Cuboid(x, y, z, cubeDataArray);
//				}
//			}
//		}
		
		try {
			//XMLParser.writeXML(cube, new FileOutputStream(new File("randomCube.xml")));
			XMLParser.writeXML(dataTable, new FileOutputStream(new File("DataTable.xml")));
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		
		
		try {
			manager.exit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

}
