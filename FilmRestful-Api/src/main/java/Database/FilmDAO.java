package Database;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Film;


import java.sql.*;
//API

public class FilmDAO {
	
	Film oneFilm = null;
	Connection conn = null;
    Statement stmt = null;
	String user = "shaherym";
    String password = "hergtylO9";
    // Note none default port used, 6306 not 3306
    String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+user;

	public FilmDAO() {}

	
	private void openConnection(){
		// loading jdbc driver for mysql
		try{
		    Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(Exception e) { System.out.println(e); }

		// connecting to database
		try{
			// connection string for demos database, username demos, password demos
 			conn = DriverManager.getConnection(url, user, password);
		    stmt = conn.createStatement();
		} catch(SQLException se) { System.out.println(se); }	   
    }
	private void closeConnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Film getNextFilm(ResultSet rs){
    	Film thisFilm=null;
		try {
			thisFilm = new Film(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getInt("year"),
					rs.getString("director"),
					rs.getString("stars"),
					rs.getString("review"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return thisFilm;		
	}
	
	
	
   public ArrayList<Film> getAllFilms(){
	   
		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();
		
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films";
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	allFilms.add(oneFilm);
		   }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allFilms;
   }

   public Film getFilmByID(int id){
	   
		openConnection();
		oneFilm=null;
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films where id="+id;
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return oneFilm;
   }
   
   public boolean insertFilm(Film  f) throws SQLException {

		boolean b = false;
		openConnection();
		try {
			String sql = "insert into films (title, year, director, stars, review) "
					+ "values ('" + f.getTitle() + "', "+ f.getYear() + ", '" + f.getDirector() + "', '" + f.getStars() + "', '" +f.getReview() + "');";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			closeConnection();
			b = true;
		} catch (SQLException s) {
			s.printStackTrace();
			throw new SQLException("Film Not Added");
		}
		return b;
	}
   
   public boolean DeleteFilms(Film f) throws SQLException {
		boolean b = false;
		openConnection();
		try {
		
		String sql = "delete from films WHERE id = " + f.getId()  + ";";
		System.out.println(sql);
		stmt.executeUpdate(sql);
		closeConnection();
		b = true;
	} catch (SQLException s1) {
	
		throw new SQLException("Film Deleted");
	}
		return b;
	}
   public boolean EditFilms(Film f) throws SQLException {
 		boolean b = false;
 		openConnection();
 		try {
 		
 		String sql = "UPDATE films SET title='" + f.getTitle()+"', review = '"+f.getReview()+"', year =  "+f.getYear()+", director = '"+ f.getDirector() +"', stars = '"+ f.getStars()+"' WHERE id = " + f.getId()  +  ";";
 		System.out.println(sql);
 		stmt.executeUpdate(sql);
 		closeConnection();
 		b = true;
 	} catch (SQLException s1) {
 		s1.printStackTrace();
 	
 		throw new SQLException("Film Updated");
 	}
 		return b;
 	}
}

