

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

/**
 * @author gwallace4
 *
 */
public class DBManager {
	
	private static final String connectionString = "jdbc:oracle:thin:@shifu:1521:csora11g";
	//private static final String connectionString = "jdbc:postgresql://localhost/GradProject";
	private static final String driverString = "oracle.jdbc.driver.OracleDriver";
	//private static final String driverString = "org.postgresql.Driver";
	private static final String DDLScriptPath = "scripts/DDL";
	private static final String DMLScriptPath = "scripts/DML";
	
	private static final String DDLInitFilename = "Initialize.txt";
	private static final String DDLClearFilename = "Clear.txt";
	
	private static final String helpString = 	"Commands:\n" +
												"--------------\n" +
												/*"Query: \"-q\", \"-query\"\n" +*/
												"Script: \"-s\", \"-script\"\n" +
												"Exit: \"-e\" \"-exit\"\n" +
												"Reset DB: \"-r\" \"-reset\"\n" +
												"--------------\n" +
												"Usage:\n" +
												"--------------\n" +
												/*"\"-q select * from table\"\n" +*/
												"\"-s query_file\"\n" +
												"\"-s query_file.sql\"\n" +
												"--------------\n";
	
	private enum Command{
		QUERY,
		SCRIPT,
		HELP,
		RESET,
		EXIT
	}
	
	
	/**
	 * @author gwallace4
	 *	This enumeration is to distinguish between SQL query scripts and SQL update scripts.
	 *	i.e. discriminates a script by whether it is DDL or DML
	 */
	private enum ScriptType{
		QUERY,
		UPDATE
	}
	
	//Mapping of a String to a Command enum
	private HashMap<String, Command> commandMap;
	
	//Mapping of an sql filename to an interactive query object
	//private HashMap<String, InteractiveQuery> dynamicScriptMap;
	
	private Connection conn = null;
	private Statement stmt = null;
	
	//Scanner to retrieve input from the user
	private Scanner in;
	
	//Logger to log errors and other important information
	private static Logger logger;
	
	/**
	 * Default constructor
	 */
	public DBManager(){
		logger = Logger.getLogger(this.getClass().getName());
		in = new Scanner(System.in);
		initializeCommandMap();
	}
	
	/**
	 * Main text-based prompt loop.
	 */
	public void beginTextPrompt(){
		while(this.prompt()){}
	}
	
	/** 
	 * Connects to the Oracle database using the inputted username and password.
	 * 
	 * @param username
	 * @param password
	 * @return True if connection succeeded, false otherwise.
	 */
	public boolean connect(String username, char[] password){
		try{
			//Load the driver
			System.out.println("DBManager::Loading driver...");
			Class.forName(driverString);
			System.out.println("DBManager::Driver loaded!");
			
			System.out.println("DBManager::Connecting to the database...");
			
			//TODO: Actually connect
			conn = DriverManager.getConnection(connectionString, username, new String(password));
			stmt = conn.createStatement();
			
			//Initialize the dynamic scripts
			//initializeDynamicScriptMap();
			
			System.out.println("DBManager::Connection successful!");

			//Destroy the password in memory
			for(int i = 0; i < password.length; ++i){
				password[i] = '#';
			}
			
			cleanDatabase();
			initializeDatabase();
			
			return true;
		}catch (SQLException sqle){
			System.err.println("[ERROR] SQL error.");
			System.err.println(sqle.getMessage());
			logger.log(Level.SEVERE, Arrays.toString(sqle.getStackTrace()));
			System.exit(-1);
			//sqle.printStackTrace();
		}catch(ClassNotFoundException cnfe){
			System.err.println("DBManager::[SEVERE] The database driver could not be found.");
			System.err.println(cnfe.getMessage());
			logger.log(Level.SEVERE, Arrays.toString(cnfe.getStackTrace()));
			//cnfe.printStackTrace();
		}catch (Exception e){ //TODO: specialize the exceptions
			System.err.println("DBManager::[ERROR] That's unexpected...");
			logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
			e.printStackTrace();
		}
		return false;
	}
	
	public void exit() throws SQLException
	{
		conn.close();
		stmt.close();
	}
	
	/**
	 * Loads and executes the table creation scripts to create the tables on the database.
	 * Internally called by DBManager.connect()
	 */
	private void initializeDatabase(){
		System.out.println("DBManager::Initializing database...");
		
		String fullPath = DDLScriptPath + "/" + DDLInitFilename;
		try {
			processSQLBatch(fullPath);
			System.out.println("DBManager::Database initialized!");
		} catch (FileNotFoundException e) {
			String logString = String.format("Initialization file was not found. Program cannot continue.\nMissing file: \"%s\"", fullPath);
			logger.log(Level.SEVERE, logString);
			e.printStackTrace();
			System.exit(-1);
		} catch (SQLException e) {
			String logString = String.format("[ERROR] There was an SQL error when initializing the database!:\n%s", e.getMessage());
			logger.log(Level.SEVERE, logString);
		}
		
	}
	
	/**
	 * Resets the database
	 */
	private void resetDatabase(){
		try {
			cleanDatabase();
		} catch (SQLException e) {
			logger.log(Level.INFO, "Could not clean the database");
		}
		initializeDatabase();
	}
	
	/**
	 * Loads and executes the table deletion scripts to delete the tables on the database.
	 * Internally called by DBManager.prompt()
	 * 
	 * @throws SQLException 
	 */
	public void cleanDatabase() throws SQLException{
		System.out.println("DBManager::clearing database...");
		String fullPath = DDLScriptPath + "/" + DDLClearFilename;
		try {
			processSQLBatch(fullPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			String logString = String.format("Database could not be purged.\nMissing file \"%s\"", fullPath);
			logger.log(Level.SEVERE, logString);
			e.printStackTrace();
		}
		//conn.close();
		System.out.println("DBManager::database cleared!");
	}
	
	/**
	 * Initializes the command map.
	 * Maps a string to a Command enum.
	 * eg: "-help" -> Command.HELP
	 */
	private void initializeCommandMap(){
		System.out.println("DBManager::initializing command map...");
		commandMap = new HashMap<String, Command>();
		
		commandMap.put("-q", Command.QUERY);
		commandMap.put("-query", Command.QUERY);
		
		commandMap.put("-s", Command.SCRIPT);
		commandMap.put("-script", Command.SCRIPT);
		
		commandMap.put("-e", Command.EXIT);
		commandMap.put("-exit", Command.EXIT);
		
		commandMap.put("-help", Command.HELP);
		commandMap.put("-h", Command.HELP);
		
		commandMap.put("-reset", Command.RESET);
		commandMap.put("-r", Command.RESET);
		
		System.out.println("DBManager::command map initialized!");;
	}
	
	/**
	 * Initializes the map of interactive queries. These queries must be hard coded.
	 */
	/*
	private void initializeDynamicScriptMap(){
		dynamicScriptMap = new HashMap<String, InteractiveQuery>();
		
		try {
			dynamicScriptMap.put("test2.sql", new InteractiveQuery(conn, "select ident from tmp where ident < ?", new String[] {"ident"}));
			dynamicScriptMap.put("03.sql", new InteractiveQuery(conn, 	"select distinct Item.ItemNumber, Weight, ExpectedPrice, FirstName, midinit, LastName, country, filename " +
																		"from item, keywords, customer " +
																		"where item.ItemNumber = keywords.ItemNumber and Customer.Email = item.email and (description like ('%'||?||'%') or keyword = ?)", new String[] {"keyword", "keyword"}));
			dynamicScriptMap.put("14.sql", new InteractiveQuery(conn, 	"select m.messageId, m.text, m.createtime, c.rcount " +
																		"from message m " +
																		"join query_14 b on (m.messageId= b.messageId) " +
																		"join query14_a c on (m.messageid= c.omessageid) " +
																		"where m.threadId= ? ", new String[] {"threadID"})); 
		} catch (SQLException e) {
			//TODO: change this...
			String logString = String.format("Could not initialize the interactive query \"%s\"", "test.sql");
			logger.log(Level.SEVERE, logString);
		}
	}*/
	
	/**
	 * Prompts the user for its next instruction.
	 * If the user prompts for exit then DBManager.cleanDatabase() is called to delete the database.
	 * 
	 * @return False if the user prompted for exit, True otherwise
	 */
	public boolean prompt(){
		boolean output = true;

		//Prompt user for command
		System.out.println(StringUtil.separateLine(72));
		System.out.println("Command? (-h for help)");
		
		Command cmd = getCommand(in.next());
		
		if(cmd != null){
			switch(cmd){
			case SCRIPT:
				//If command is "Query" process the query
				/*
				try {
					processDMLScript(in.next());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					logger.log(Level.INFO, "Could not execute the script...");
					e1.printStackTrace();
				}*/
				break;
			case QUERY:
				System.out.println("Query");
				break;
			case HELP:
				//If the command is "Help" then display the help string
				System.out.println(helpString);
				break;
			case RESET:
				this.resetDatabase();
				break;
			case EXIT:
				//If the command is "Exit" then clean the database
				try {
					cleanDatabase();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				output = false;
				break;
			default:
				break;
			}
		}else{
				logger.log( Level.INFO, "[ERROR] Command not identified!");
				System.out.println(helpString);
		}
		System.out.println(StringUtil.separateLine(72));
		return output;
	}
	
	/**
	 * Determines if a filename has the appropriate extension. If it does not then the extension is added and returned
	 * 
	 * @param queryName
	 * @return	Script filename with the extension ".sql" assuredly appended to the end
	 */
	private String ensureExtension(String queryName){
		//Determine if ".sql" should be appended to queryName
		String queryWithExtension = queryName;
		String[] brokenString = queryName.split("\\.");
		if(brokenString.length > 1){
			if(brokenString[brokenString.length - 1].equals("sql")){
				//System.out.println("[DEBUG] No need to append extension!");
			}else{
				logger.log(Level.INFO, "Extension not supported!\n" +
						"Files must be \".sql files\"");
			}
		}else{
			//System.out.println("[DEBUG] Appending extension!");
			queryWithExtension = queryName + ".sql";
		}
		return queryWithExtension;
	}
	
	/**
	 * Processes and executes an sql DDL script
	 * 
	 * @param ddlFilename
	 * @throws SQLException 
	 */
	private void processDDLScript(String ddlFilename) throws SQLException{
		String ddlFileNameExt = ensureExtension(ddlFilename);
		String completeFilepath = String.format("%s/%s/%s", System.getProperty("user.dir"), DDLScriptPath, ddlFileNameExt);
		processScript(completeFilepath, ScriptType.UPDATE);
	}
	
	/**
	 * Processes and executes an sql DML script.
	 * If the script is an interactive query then it bypasses the file-opening stage and runs InteractiveQuery.process() 
	 * 
	 * @param dmlFilename
	 * @throws SQLException 
	 */
	
	public ResultSet[] processDMLScript(String dmlFilename) throws SQLException{
		String dmlFileNameExt = ensureExtension(dmlFilename);
		//InteractiveQuery iQuery = getInteractiveQuery(dmlFileNameExt);
		
		ResultSet[] results = null;
		
		//If the filename is a known interactive query
		/*if(iQuery != null){
			results = new ResultSet[1];
			results[0] = iQuery.process(in);
		}else{*/
			String completeFilepath = String.format("%s/%s/%s", System.getProperty("user.dir"), DMLScriptPath, dmlFileNameExt);
			results = processScript(completeFilepath, ScriptType.QUERY);
		//}
		//Print out the results to the user
		//printResultSets(results);
		return results;
	}
	
	/**
	 * Opens a file containing an SQL query, then executes the query.
	 * 
	 * @param fullFilePath
	 * @throws SQLException 
	 */
	private ResultSet[] processScript(String fullFilePath, ScriptType type) throws SQLException{
		//TODO: Query processing logic
		
		ResultSet[] results = null;
		System.out.printf("DBManager::Opening %s...\n", fullFilePath);
		try {
			//Attempt to open the sql script
			BufferedReader reader = new BufferedReader(new FileReader(fullFilePath));
			System.out.printf("DBManager::Successfully opened %s!\n", fullFilePath);
			//Load script from queryName.sql
			StringBuilder sb = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null){
				sb.append(" " + line);
			}
			
			String fullQuery = sb.toString().replaceAll("\\s+", " ");
			//Break up the queries into single statements
			String[] queryList = fullQuery.split(";");
			
			results = new ResultSet[queryList.length];
			
			switch (type){
			case QUERY:
				//Run each individual statement
				for(int i = 0; i < queryList.length; ++i){
					//System.out.printf("[debug]Executing query: %s\n", queryList[i]);
					try{
						results[i] = stmt.executeQuery(queryList[i]); 
					}catch (SQLException sqle){
						String logstring = String.format("Error processing query \"%s\": %s", queryList[i], sqle.getMessage());
						logger.log(Level.SEVERE, logstring);
					}
				}
				
				break;
			case UPDATE:
				for(int i = 0; i < queryList.length; ++i){
					//System.out.printf("[debug]Executing update: %s\n", queryList[i]);
					try {
						stmt.executeUpdate(queryList[i]);						
					} catch (SQLException sqle){
						String logstring = String.format("Error processing query \"%s\": %s", queryList[i], sqle.getMessage());
						logger.log(Level.SEVERE, logstring);
					}
				}
				break;
			}

			System.out.println("Query has run successfully!");
			
			
			System.out.println();
			
			reader.close();
			
		} catch (FileNotFoundException e) {
			String logString = String.format("There was an error opening the file \"%s\"", fullFilePath);
			logger.log(Level.INFO,logString);
		} catch (IOException e) {
			
			String logString = String.format("There was an error reading from the file \"%s\"", fullFilePath);
			logger.log(Level.INFO, logString);
			//e.printStackTrace();
		}
		
		return results;
	}
	
	/**
	 * Opens a file containing a list of the filenames of sql scripts, then processes each of those scripts.
	 * 
	 * @param fileName
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 */
	private void processSQLBatch(String fileName) throws FileNotFoundException, SQLException{
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line = "";
			try {
				//While there is a line to process...
				while((line = reader.readLine())!=null){
					//TODO: Process the file
					System.out.printf("DBManager::Running %s\n", line);
					//TODO: Create method: processDDLFile
					try {
						processDDLScript(line);
					} catch (SQLException e) {
						String logString = String.format("There was an error in the script \"%s\": %s", line, e.getMessage());
						logger.log(Level.INFO, logString);
						reader.close();
						throw new SQLException();
					}
				}
			} catch (IOException e) {
				String logString = String.format("There was an error reading from %s.\n", fileName);
				logger.log(Level.INFO, logString);
				e.printStackTrace();
			} /*catch (SQLException e) {
				String logString = String.format("There was an error in the query file \"%s\".", line);
				e.printStackTrace();
			}*/
		} catch (FileNotFoundException e1) {
			String logString = String.format("The file %s could not be opened or does not exist.\n", fileName);
			logger.log(Level.INFO, logString);
			//e1.printStackTrace();
			throw new FileNotFoundException();
		}
	}
	
	/**
	 * Retrieves the command associated with a specific string.
	 * 
	 * @param stCommand String representation of the command
	 * @return Command enum if the command is found, null otherwise
	 */
	private Command getCommand(String stCommand){
		return commandMap.get(stCommand.toLowerCase());
		
	}
	
	/**
	 * Attempts to find an interactive query stored in the dynamicScriptMap map associated with a given filename.
	 * 
	 * @param filename
	 * @return InteractiveQuery object associated with the filename if it is mapped, null otherwise.
	 */
	/*
	private InteractiveQuery getInteractiveQuery(String filename){
		return dynamicScriptMap.get(filename.toLowerCase());
	}*/
	
	
	/**
	 * Prints an array of result sets.
	 * 
	 * @param rss
	 */
	private void printResultSets(ResultSet rss[]){
		if(rss != null){
			System.out.printf("Printing %d results\n", rss.length);
			
			for(int i = 0; i < rss.length; ++i){
				try {
					printResultSet(rss[i]);
				} catch (SQLException e) {
					String logString = String.format("Error with result %d: %s", i, e.getMessage());
					logger.log(Level.INFO, logString);
				}
			}			
		}
	}
	
	/**
	 * Prints the values contained in a result set that has been retrieved from a database.
	 * 
	 * @param rs
	 */
	private void printResultSet(ResultSet rs) throws SQLException{
		if(rs != null){			
			try {
				//ResultSetMetaData contains information about a result set.
				ResultSetMetaData rsmd = rs.getMetaData();
				for(int header = 0; header < rsmd.getColumnCount(); ++header){
					System.out.printf("%-20s" , rsmd.getColumnLabel(header+1));
				}
				
				System.out.println();
				
				while(rs.next()){
					for(int col = 0; col < rsmd.getColumnCount(); ++col){
						Object resultObject = rs.getObject(col+1);
						
						if(resultObject != null){
							String outputString = resultObject.toString();
							System.out.printf("%-20s", outputString);
						}else System.out.printf("%-20s", "-");
					}
					System.out.println();
					
				}
			} catch (SQLException e) {
				throw new SQLException("SQL writing error. Query likely contains DDL statement", e);
			}
		}else{
			String logstring = String.format("Null resultset. This behavior is not expected");
			logger.log(Level.SEVERE, logstring);
		}
	}
	
	public static Object[][] resultSetToArray(ResultSet result) throws SQLException
	{
		ResultSetMetaData rsmd = result.getMetaData();
		ArrayList<Object[]> list = new ArrayList<Object[]>(100);
		
		while(result.next())
		{
			ArrayList<Object> tmp = new ArrayList<Object>(rsmd.getColumnCount());
			for(int i = 0; i < rsmd.getColumnCount(); ++i)
			{
				tmp.add(i, result.getObject(i+1));
			}
			list.add(tmp.toArray());
		}
		
		return list.toArray(new Object[0][0]);
	}
}
