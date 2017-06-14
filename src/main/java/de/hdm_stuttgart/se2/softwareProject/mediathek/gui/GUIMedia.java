package de.hdm_stuttgart.se2.softwareProject.mediathek.gui;

import java.awt.Dialog.ModalExclusionType;
import java.io.File;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.hdm_stuttgart.se2.softwareProject.mediathek.controller.Settings;
import de.hdm_stuttgart.se2.softwareProject.mediathek.driver.App;
import de.hdm_stuttgart.se2.softwareProject.mediathek.exceptions.InvalidInputException;
import de.hdm_stuttgart.se2.softwareProject.mediathek.interfaces.IMedia;
import de.hdm_stuttgart.se2.softwareProject.mediathek.interfaces.IMedialist;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Toggle;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

/**
 * Hilfsklasse für MOController
 * Auslagerung aller extra Methoden und Klassen
 *
 */
public class GUIMedia {
	
	private static Logger log = LogManager.getLogger(GUIMedia.class);

	private static GUIMedia instance;

	private final SimpleStringProperty title;
	private final SimpleLongProperty length;
	private final SimpleStringProperty date;
	private final SimpleStringProperty artist;
	private final SimpleStringProperty genre; 

	public GUIMedia(String title, Long length, String date, String artist, String genre) {
		this.title = new SimpleStringProperty(title);
		this.length = new SimpleLongProperty(length);
		this.date = new SimpleStringProperty(date);
		this.artist = new SimpleStringProperty(artist);
		this.genre = new SimpleStringProperty(genre);
	}

	public String getTitle() {
		return title.get();
	}

	public long getLength() {
		return length.get();
	}

	public String getDate() {
		return date.get();
	}

	public String getArtist() {
		return artist.get();
	}

	public String getGenre() {
		return genre.get();
	}


	public static GUIMedia getInstance() {
		return instance;
	}


	public static void setInstance(GUIMedia instance) {
		GUIMedia.instance = instance;
	}


	public static void playMovie(Settings s, String play_data, IMedialist movies, IMedialist audio) {

		new Thread(new Runnable() {
			public void run() {

				getInput(s, play_data, movies, audio).open();

			}
		}).start();

	}
	

	public static IMedia getInput(Settings s, String play_data, IMedialist movies, IMedialist audio) {

		for (Entry<File, IMedia> i : movies.getContent().entrySet()) {
			if (i.getValue().getTitle().equals((play_data))) {
				return i.getValue();
			}
		}
		for (Entry<File, IMedia> i : audio.getContent().entrySet()) {
			if (i.getValue().getTitle().equals(play_data)) {
				return i.getValue();
			}
		}
		throw new InvalidInputException();
	}
	
	
	public static MediaMeta readMetaData(File file) {
		MediaPlayerFactory factory = new MediaPlayerFactory();
		MediaMeta meta = factory.getMediaMeta(file.toString(), true);
		return meta;
	}
	
	public static void editMetaInformation(IMedia m,String save_titel,String save_year, String save_artist, String save_genre, String ranking, Toggle favo) throws ParseException {

		MediaMeta meta = readMetaData(m.getFile());
		log.info("Metadaten von " + m.getFile() + " werden bearbeitet");

		if (m.getTyp().equals("video"))	{
			do {
				meta.setTitle(save_titel);
			} while (meta.getTitle().isEmpty() || meta.getTitle().matches("^\\s*$"));
			meta.setDate(save_year);
			meta.setArtist(save_artist);
			meta.setGenre(save_genre);
			//meta.setRating(ranking);

			
			
			HashMap<String, Object> root = new HashMap<>();
			root.put("infos", meta);
			//root.put("ranking", ranking);
			root.put("visible", true);
			JSONObject metaInfos = new JSONObject(root);
			meta.setDescription(metaInfos.toString());
		}
		if (m.getTyp().equals("audio"))	{
			do {
				meta.setTitle(save_titel);
			} while (meta.getTitle().isEmpty() || meta.getTitle().matches("^\\s*$"));
			meta.setDate(save_year);
			meta.setArtist(save_artist);
			meta.setGenre(save_genre);
			//meta.setRating(ranking.toString());

			HashMap<String, Object> root = new HashMap<>();
			root.put("infos", meta);
			//root.put("ranking", ranking);
			root.put("visible", true);
			JSONObject metaInfos = new JSONObject(root);
			meta.setDescription(metaInfos.toString());
		}
		
		
		//JSONObject root = (JSONObject) new JSONParser().parse(meta.getDescription());

				// Metainformationen werden in Datei gespeichert
				
				meta.save();
			

				// Metainformationen werden in Attribute des IMedia Objekts geschrieben
//				m.setTitle(meta.getTitle());
//				m.setDate(meta.getDate());
//				m.setRegisseur(meta.getArtist());
//				m.setGenre(meta.getGenre());
//				m.setInfo((String) root.get("infos"));
//				m.setRanking(Integer.parseInt((String)root.get("ranking")));
//				m.setFavorite((Boolean)root.get("favorite"));
//				meta.release();
//				log.info("Änderungen erfolgreich gespeichert.");
			
		
	}

}
