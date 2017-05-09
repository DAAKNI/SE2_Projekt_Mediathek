package de.hdm_stuttgart.se2.softwareProject.mediathek.models;

import java.io.File;
import de.hdm_stuttgart.se2.softwareProject.mediathek.interfaces.IMedia;

/**
 * 
 * @author ll040
 * 
 * Implementiert das IMedia Interface. Erweiterung um medienspezifische
 * Attribute: pages, author, info, releaseDate, genre und ranking.
 * Die spezifischen Attribute werden anhand des Constructors gesetzt.
 */
class Book extends Media implements IMedia {

	private int pages;
	private String author;
	private String info;
	private String releaseDate;
	private String genre;
	private String ranking;



	/**
	 * 
	 * @param typ		Typ des Mediums
	 * @param title		Titel des Buches
	 * @param favorite	Toggle für Favoriten-Liste
	 * @param file		Pfad der Buch-Datei
	 * @param visible	Visibilität für die getDetails()-Methode
	 */
	public Book(String typ, String title, boolean favorite, File file/*, int pages*/, boolean visible) {
		super(typ, title, favorite, file, visible);
		//this.pages = pages;
	}

	@Override
	/**
	 * Auslesen der Metadaten, sofern visible == true
	 */
	public void getDetails() {
		if (this.visible == true) {
			System.out.println(this.title);
			if (this.pages != 0) {
				System.out.println("Seitenzahl: " + this.pages);
			}
			if (this.releaseDate != null && !(this.releaseDate.equals(""))) {
				System.out.println("Erscheinungsdatum: " + this.releaseDate);
			}
			if (this.author != null && !(this.author.equals(""))) {
				System.out.println("Autor: " + this.author);
			}
			if (this.genre != null && !(this.genre.equals(""))) {
				System.out.println("Genre: " + this.genre);
			}
			if (this.info != null && !(this.info.equals(""))) {
				System.out.println("Klappentext: " + this.info);
			}
			System.out.println("Bewertung: " + this.ranking);
		}
	}

	@Override
	public void setDate(String date) {
		this.releaseDate = date;
	}


	@Override
	public void setRegisseur(String regisseur) {
		this.author = regisseur;
	}


	@Override
	public void setGenre(String genre) {
		this.genre = genre;
	}


	@Override
	public void setInfo(String info) {
		this.info = info;
	}


	@Override
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub
		
	}

}
