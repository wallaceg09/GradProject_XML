import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLCubeRow {
	private String xDimensionName;
	private String yDimensionName;
	private String zDimensionName;
	private Object value;
	
	public SQLCubeRow(ResultSet resultSet)
	{
		try {
			xDimensionName = resultSet.getString(1);
			yDimensionName = resultSet.getString(2);
			zDimensionName = resultSet.getString(3);
			
			value = resultSet.getObject(4);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			xDimensionName = yDimensionName = zDimensionName = "";
		}
	}

	public String getxDimensionName() {
		return xDimensionName;
	}

	public String getyDimensionName() {
		return yDimensionName;
	}

	public String getzDimensionName() {
		return zDimensionName;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "SQLCubeRow [xDimensionName=" + xDimensionName
				+ ", yDimensionName=" + yDimensionName + ", zDimensionName="
				+ zDimensionName + ", value=" + value + "]";
	}
	
}
