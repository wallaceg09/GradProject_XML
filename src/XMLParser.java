import java.io.File;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class XMLParser {
	public static void writeXML(DataCube cube, OutputStream outStream)
	{
		Cuboid[][][] cuboidArray = cube.getCuboidArray();
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			
			Document doc = db.newDocument();
			
			Element root = doc.createElement("datacube");
			
			//Data cube dimensions
			root.setAttribute("xSize", Integer.toString(cuboidArray.length));
			root.setAttribute("ySize", Integer.toString(cuboidArray[0].length));
			root.setAttribute("zSize", Integer.toString(cuboidArray[0][0].length));
			
			doc.appendChild(root);
			//Dimension names
			Element xDimensionArray = doc.createElement("XDimensionValues");
			String[] xDimensions = cube.getXDimensionValues();
			for(int x = 0; x < xDimensions.length; ++x)
			{
				//If the current dimension name is null then the dimension name represents "All"
				String value = (xDimensions[x] == null) ? "All" : xDimensions[x];
				
				Element dimension = doc.createElement("Dimension");
				dimension.setAttribute("index", Integer.toString(x));
				dimension.setAttribute("Value", value);
				xDimensionArray.appendChild(dimension);
			}
			root.appendChild(xDimensionArray);
			
			Element yDimensionArray = doc.createElement("YDimensionValues");
			String[] yDimensions = cube.getYDimensionValues();
			for(int y = 0; y < yDimensions.length; ++y)
			{
				//If the current dimension name is null then the dimension name represents "All"
				String value = (yDimensions[y] == null) ? "All" : yDimensions[y];
				
				Element dimension = doc.createElement("Dimension");
				dimension.setAttribute("index", Integer.toString(y));
				dimension.setAttribute("Value", value);
				yDimensionArray.appendChild(dimension);
			}
			root.appendChild(yDimensionArray);
			
			Element zDimensionArray = doc.createElement("ZDimensionValues");
			String[] zDimensions = cube.getZDimensionValues();
			for(int z = 0; z < zDimensions.length; ++z)
			{
				//If the current dimension name is null then the dimension name represents "All"
				String value = (zDimensions[z] == null) ? "All" : zDimensions[z];
				
				Element dimension = doc.createElement("Dimension");
				dimension.setAttribute("index", Integer.toString(z));
				dimension.setAttribute("Value", value);
				zDimensionArray.appendChild(dimension);
			}
			root.appendChild(zDimensionArray);
			
			
			
			for(int y = 0; y < cuboidArray.length; ++y)
			{
				for(int x = 0; x < cuboidArray[0].length; ++x)
				{
					for (int z = 0; z < cuboidArray[0][0].length; ++z)
					{
						Cuboid currentCuboid = cuboidArray[y][x][z];
						
						//Add cuboids
						Element cuboidEle = doc.createElement("cuboid");
						cuboidEle.setAttribute("x", Integer.toString(x));
						cuboidEle.setAttribute("y", Integer.toString(y));
						cuboidEle.setAttribute("z", Integer.toString(z));
						root.appendChild(cuboidEle);
						
						Element frontValueEle = doc.createElement("Front");
						frontValueEle.setTextContent(Integer.toString(currentCuboid.getValue(Cuboid.FRONT)));
						cuboidEle.appendChild(frontValueEle);
						
						Element backtValueEle = doc.createElement("Back");
						backtValueEle.setTextContent(Integer.toString(currentCuboid.getValue(Cuboid.BACK)));
						cuboidEle.appendChild(backtValueEle);
						
						Element leftValueEle = doc.createElement("Left");
						leftValueEle.setTextContent(Integer.toString(currentCuboid.getValue(Cuboid.LEFT)));
						cuboidEle.appendChild(leftValueEle);
						
						Element rightValueEle = doc.createElement("Right");
						rightValueEle.setTextContent(Integer.toString(currentCuboid.getValue(Cuboid.RIGHT)));
						cuboidEle.appendChild(rightValueEle);
						
						Element topValueEle = doc.createElement("Top");
						topValueEle .setTextContent(Integer.toString(currentCuboid.getValue(Cuboid.TOP)));
						cuboidEle.appendChild(topValueEle );
						
						Element bottomValueEle = doc.createElement("Bottom");
						bottomValueEle.setTextContent(Integer.toString(currentCuboid.getValue(Cuboid.BOTTOM)));
						cuboidEle.appendChild(bottomValueEle);
					}
				}
			}
			
			//Write to output stream
			
			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer transformer = tff.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(outStream);
			
			transformer.transform(source, result);
			
			System.out.println("\n ***XML File saved***");//TODO: Change to Logger			
		}
		catch(ParserConfigurationException pce)
		{
			pce.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeXML(Object[][] dataTable, OutputStream outStream)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			
			Document doc = db.newDocument();
			
			Element root = doc.createElement("DataArray");
			
			//Data cube dimensions
			root.setAttribute("height", Integer.toString(dataTable.length));
			root.setAttribute("width", Integer.toString(dataTable[0].length));
			
			doc.appendChild(root);
			
			for(int y = 0; y < dataTable.length; ++y)
			{
				for(int x = 0; x < dataTable[0].length; ++x)
				{
					Element dataElement = doc.createElement("DataElement");
					dataElement.setAttribute("y", Integer.toString(y));
					dataElement.setAttribute("x", Integer.toString(x));
					String nodeValue = dataTable[y][x] == null? "All" : dataTable[y][x].toString();
					dataElement.setTextContent(nodeValue);
					root.appendChild(dataElement);
				}
			}
			
			//Write to output stream
			
			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer transformer = tff.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(outStream);
			
			transformer.transform(source, result);
			
			System.out.println("\n ***XML File saved***");//TODO: Change to Logger			
		}
		catch(ParserConfigurationException pce)
		{
			pce.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
