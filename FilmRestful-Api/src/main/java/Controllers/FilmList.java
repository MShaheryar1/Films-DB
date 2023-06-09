package Controllers;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import Model.Film;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Films")

public class FilmList {
	@XmlElement(name = "Films")
	private List<Film> FilmList;


	public FilmList() {
	}

	public FilmList(List<Film> FilmList) {
		this.FilmList = FilmList;
	}

	public List<Film> getFilmList() {
		return FilmList;
	}

	public void setFilmList(List<Film> FilmList) {
		this.FilmList = FilmList;
	}
}