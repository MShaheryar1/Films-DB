package Controllers;
//REstCont
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import Database.FilmDAO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import Model.Film;

@WebServlet("/Films-api")
public class FilmsApiController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public FilmsApiController() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		FilmDAO dao = new FilmDAO();
		ArrayList<Film> allFilms = dao.getAllFilms();
		
		FilmList cl = new FilmList(allFilms);
		StringWriter sw = new StringWriter();
	
		
		  String format =request.getHeader("Accept");
		  JAXBContext context;
	
		System.out.println(format);
		//XML
		if("application/xml".equals(format)) {
			
			try {
				context = JAXBContext.newInstance(FilmList.class);
				
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
				Boolean.TRUE);
				
				m.marshal(cl, sw);
				out.print(sw.toString());
				
			} catch (JAXBException e) 
			{
				
				e.printStackTrace();
			}
			//String
		} else if ("text/plain".equals(format)) {
			
		String data = "";
		
		for (Film f: allFilms) {
			data += "%" +f.getId()+ "#" +f.getTitle()+"#"+ f.getYear()+"#"+f.getDirector()+"#"+f.getStars()+"#"+f.getReview();		
		}
		out.print(data);
		//JSON
	} else if ("application/json".equals(format)){
			response.setContentType("application/json");
			
			Gson gson  = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(allFilms);
			out.print(json);
		
		}
}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 String dataFormat = request.getHeader("Content-Type");
		 
		 PrintWriter out = response.getWriter();
		 String data = request.getReader().lines().reduce("", (accumulator ,actual) -> accumulator + actual);
		 System.out.println(data);
		 FilmDAO dao = new FilmDAO();
		 //XML
			if("application/xml".equals(dataFormat)) {
				JAXBContext jaxbContext;
			try {
		jaxbContext  = JAXBContext.newInstance(Film.class);
		jakarta.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	
		Film c = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
	 	
		dao.insertFilm(c);
		out.print("XML Film Added");
			} catch (SQLException e) {
				e.printStackTrace();
				
		} catch (JAXBException e) {
			
				e.printStackTrace();
			}
			}
			//Json
	
	else if("application/json".equals(dataFormat)) {
		try {
			Gson gson = new Gson();
			Film c = gson.fromJson(data, Film.class);
			
			dao.insertFilm(c);
			out.print("JSON Film Inserted");
		} catch (SQLException| JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
			//String
	else if (("plain/text".equals(dataFormat)) ) {
		response.setContentType("text/plain");
		
	
		String title= request.getParameter("Title");
		int year=(Integer.parseInt("Year"));
		String director= request.getParameter("Director");
		String stars= request.getParameter("Stars");
		String review= request.getParameter("Review");
		
		Film film = new Film();
		
		Film f = new Film();
		f.setTitle(title);
		f.setYear(year);
		f.setDirector(director);
		f.setStars(stars);
		f.setReview(review);
		
		
		try {
			dao.insertFilm(film);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	}
	
 @Override
protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 String dataFormat = request.getHeader("Content-Type");
	 
	 PrintWriter out = response.getWriter();
	 String data = request.getReader().lines().reduce("", (accumulator ,actual) -> accumulator + actual);
	 
	 FilmDAO dao = new FilmDAO();
	 //XML
	 
		if("application/xml".equals(dataFormat)) {
			JAXBContext jaxbContext;
		try {
	jaxbContext  = JAXBContext.newInstance(Film.class);
	jakarta.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	Film c = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
 	
	dao.EditFilms(c);
	out.print("XML Film Edited");
		} catch (SQLException e) {
			e.printStackTrace();
			
			out.print("header and data format not matching");
	} catch (JAXBException e) {
		
			e.printStackTrace();
		}
		}
//Json
else if("application/json".equals(dataFormat)) {
	try {
		Gson gson = new Gson();
		Film c = gson.fromJson(data, Film.class);
		
		dao.EditFilms(c);
		out.print("JSON Film Edited");
	} catch (SQLException| JsonSyntaxException e) {
		
		e.printStackTrace();
		out.print("header and data format not matching");
	}
		//String
	}
else if(dataFormat.equals("text/plain")) {
	int id = Integer.parseInt(request.getParameter("id"));
	String title =request.getParameter("title");
	int year = Integer.parseInt(request.getParameter("year"));
	String director =request.getParameter("director");
	String stars =request.getParameter("stars");
	String review =request.getParameter("rev");

	Film film = new Film (id, title, year, director, stars, review);
	try { dao.EditFilms(film);
	out.print("String Film Edited");
}
catch (SQLException e) {
		e.printStackTrace();
	}

}
}

 @Override
protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	 String dataFormat = req.getHeader("Content-Type");
	 
	 PrintWriter out = resp.getWriter();
	 String data = req.getReader().lines().reduce("", (accumulator ,actual) -> accumulator + actual);
	 
	 FilmDAO dao = new FilmDAO();
	 //XML
	 
		if("application/xml".equals(dataFormat)) {
			JAXBContext jaxbContext;
		try {
	jaxbContext  = JAXBContext.newInstance(Film.class);
	Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	Film c = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
 	
	dao.DeleteFilms(c);
	out.print("XML Film Deleted");
		} catch (SQLException e) {
			e.printStackTrace();
			
	} catch (JAXBException e) {
		
			e.printStackTrace();
		}
		}
//Json
else if("application/json".equals(dataFormat)) {
	try {
		Gson gson = new Gson();
		Film c = gson.fromJson(data, Film.class);
		
		dao.DeleteFilms(c);
		out.print("JSON Film Deleted");
	} catch (SQLException| JsonSyntaxException e) {
		
		e.printStackTrace();

	}
}
		//String
	else if ( dataFormat.equals("text/plain")) {
		int id = Integer.parseInt("id");
		Film Delete = dao.getFilmByID(id);
		try {
			dao.DeleteFilms(Delete);
			out.write("String Film Deleted");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
 }
}
//This is a RESTful web service that serves as an API for database.
//When user sends a GET request to this servlet, it retrieves all films from the database using the FilmDAO class.
//and returns the data to the user in XML, String, or JSON, using the accept header.
//The servlet uses the FilmDAO class to access the database and perform CRUD operations on the films table. 
//The doGet method handles the GET requests.
//the doPost method handles the POST requests.
//the doDelete method handles the DELETE requests.
//and the doPut method handles the PUT requests. 

//The servlet uses the JAXB library to marshal and unmarshal the data between Java objects and XML.
//It uses the Gson library to convert the data between Java objects and JSON. 
//It uses a FilmList class to store a list of films,
//which is marshaled and unmarshaled as an XML element containing multiple Film elements.
//The Film class represents a film and contains its fields: id, title, year, director, stars, and review.



