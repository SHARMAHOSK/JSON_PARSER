package main_parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.mysql.cj.jdbc.MysqlDataSource;

public class JSONParse implements query {

	private File location;
	private int rowAffect;
	private static MysqlDataSource db;
	
	//constructor for store location of file 
	
	public JSONParse(File location) {
		this.location = location;
	}
	
	
	// for parsing the JSON file and store into DataBase
	
	@SuppressWarnings({ "unchecked" }) 
	public String parse(){
		JSONParser parser = new JSONParser();
		Connection con = null;
		PreparedStatement stmt= null;
		ResultSet rs=null;
		try {
			
			FileReader f = new FileReader(location);
			Object obj = parser.parse(f);
			JSONObject jsonobject = (JSONObject) obj;
			JSONObject quiz = (JSONObject) jsonobject.get("quiz");
			ArrayList<String> subject =  set(quiz);
			for(String sub : subject) {
				String id = null;
				con = connect().getConnection();
				stmt = con.prepareStatement(insert_subject,Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1,sub);
				int rowAffected = stmt.executeUpdate();
				if(rowAffected==1) {
					rs = stmt.getGeneratedKeys();
		            if(rs.next())id = rs.getString(1);
					JSONObject subObject = (JSONObject) quiz.get(sub);
					ArrayList<String> que = set(subObject);
					for(String q : que) {
						JSONObject quetions = (JSONObject) subObject.get(q);
						JSONArray options = (JSONArray) quetions.get("options");
						Iterator<String> iterator = options.iterator();
						stmt = con.prepareStatement(insert_subject_data);
						int i=1;
						stmt.setString(i++,id);
						stmt.setString(i++,(String)quetions.get("question").toString());
						stmt.setString(i++,(String)quetions.get("answer").toString());
						while(iterator.hasNext()) stmt.setString(i++,iterator.next());
						rowAffected += stmt.executeUpdate();
					}
				}
				rowAffect += rowAffected;
			}
		}
		
		// handle predicted Exception and return error message
		
		catch (FileNotFoundException e){return e.toString();}
		catch (IOException e){return e.toString();}
		catch (ParseException e){return e.toString();}
		catch (SQLException e){return e.toString();}
		catch (Exception e) {return e.toString();}
		
		// set success message and return 
		
		String Message = "Success..\nnumber of affected row in database = "+Integer.toString(rowAffect); 
		return Message;
	}
	
	
	
	
	// return the set of Child key in ArrayList
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> set(JSONObject object)throws Exception{
		ArrayList<String> ar = new ArrayList<String>();
		Set<Map.Entry<String,JSONObject>> entries = object.entrySet();
		for(Map.Entry<String,JSONObject> entry: entries) ar.add(entry.getKey().toString());
		return ar;
	}
	
	
	// return the DataSource of database
	
	public synchronized static  DataSource connect() throws Exception{
		db=new MysqlDataSource();
		db.setUrl("jdbc:mysql://remotemysql.com:3306/X7ekJTa09T?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
		db.setMaxReconnects(5);
		db.setUser("X7ekJTa09T");
		db.setPassword("non0ebkKrO");
		return db;
	}
}



// interface of query

interface query {
	String insert_subject = "INSERT INTO quiz (SUBJECT) VALUES(?)";
    String insert_subject_data = "insert into SUBJECT (fid,quetion,answer,option1,option2,option3,option4) values(?,?,?,?,?,?,?)";
}
