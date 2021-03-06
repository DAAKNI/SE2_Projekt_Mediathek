package de.hdm_stuttgart.se2.softwareProject.mediathek.models;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hdm_stuttgart.se2.softwareProject.mediathek.interfaces.IMedia;

/**
 * 
 * Implementiert das IMedia Interface. Erweiterung um medienspezifische
 * Attribute: duration, interpret, genre, info, ranking und releaseDate.
 * Die spezifischen Attribute werden anhand des Constructors gesetzt.
 * 
 * @author ll040
 * 
 */
class Audio extends Media implements IMedia {

	private static Logger log = LogManager.getLogger(Audio.class);
	
	private long duration;
	private String interpret;
	private String genre;
	private String info;
	private int ranking;
	private String releaseDate;
	
	/**
	 * 
	 * @param typ			Typ des Mediums
	 * @param title			Songtitel
	 * @param favorite		Toggle für Favoriten-Liste
	 * @param file			Pfad der Musik-Datei
	 * @param visible		Visibilität für die getDetails()-Methode
	 * @param duration		Dauer
	 * @param interpret		Künstler/Band
	 * @param genre			Musik-Genre
	 * @param releaseDate	Erscheinungsdatum
	 * @param info			benutzerspezifische Informationen
	 */
	public Audio(String typ, String title, File file, long duration, String interpret,
			String genre, String releaseDate, String info, boolean favorite, boolean visible, String ranking) {
		super(typ, title, favorite, file, visible);
		this.duration = duration;
		this.interpret = interpret;
		this.genre = genre;
		this.releaseDate = releaseDate;
		this.info = info;
		try {
			int rankInt = Integer.parseInt(ranking);
			if (rankInt > 0 && rankInt < 6) {
				this.ranking = rankInt;
			} else {
				this.ranking = 0;
			}
		} catch (NumberFormatException e) {
			log.catching(e);
			log.error("Ungültige Bewertung in Metadaten von " + title);
			this.ranking = 0;
		}
	}


	
	/**
	 * Auslesen der Metadaten, sofern visible == true
	 */
	@Override
	public void getDetails() {
		if (this.visible == true) {
			System.out.println(this.title);
			if (this.duration != 0) {
				System.out.format("Dauer: %d:%02d Minuten\n", this.duration / 60000, (this.duration % 60000) / 1000 );
			}
			if (this.releaseDate != null && !(this.releaseDate.equals("")) && !(this.releaseDate.equals("0"))) {
				System.out.println("Erscheinungsdatum: " + this.releaseDate);
			}
			if (this.interpret != null && !(this.interpret.equals(""))) {
				System.out.println("Interpret: " + this.interpret);
			}
			if (this.genre != null && !(this.genre.equals(""))) {
				System.out.println("Genre: " + this.genre);
			}
			if (this.info != null && !(this.info.equals(""))) {
				System.out.println("Beschreibung: " + this.info);
			}
			if (this.info != null && !(this.info.equals(""))) {
				System.out.println("Bewertung: " + this.ranking + "/5");
			}
		} else {
			log.debug("Metadaten des Medium " + this.getFile() + " nicht angezeigt, da attribut visible == false");
		}
	}

	@Override
	public void setDate(String date) {
		this.releaseDate = date;
		log.debug("Erscheinungsdatum des Mediums " + this.getFile() + " auf " + this.releaseDate + " geändert");
	}

	@Override
	public void setRegisseur(String regisseur) {
		this.interpret = regisseur;
		log.debug("Interpret des Mediums " + this.getFile() + " auf " + this.interpret + " geändert");
	}

	@Override
	public void setGenre(String genre) {
		this.genre = genre;
		log.debug("Genre des Mediums " + this.getFile() + " auf " + this.genre + " geändert");
	}

	@Override
	public void setInfo(String info) {
		this.info = info;
		log.debug("Beschreibung des Mediums " + this.getFile() + " geändert");
	}


	@Override
	public void setRanking(int ranking) {
		this.ranking = ranking;
		log.debug("Bewertung des Mediums " + this.getFile() + " auf " + this.ranking + " geändert");
	}

	@Override
	public boolean getFavorite() {
		return this.favorite;
	}

	@Override
	public String getRanking() {
		return Integer.toString(this.ranking);
	}

	@Override
	public String getDuration() {
		if (this.duration != 0) {
			return this.duration / 60000 + ":" + (this.duration % 60000) / 1000 + " min.";
		}
		return "0";
	}

	@Override
	public String getDate() {
		return this.releaseDate;
	}

	@Override
	public String getArtist() {
		return this.interpret;
	}

	@Override
	public String getGenre() {
		return this.genre;
	}

	@Override
	public boolean getVisiblity() {
		return this.visible;
	}
}
