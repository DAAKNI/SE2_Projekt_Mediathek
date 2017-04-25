package de.hdm_stuttgart.se2.softwareProject.mediathek.driver;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hdm_stuttgart.se2.softwareProject.mediathek.interfaces.IMedia;
import de.hdm_stuttgart.se2.softwareProject.mediathek.interfaces.IMedialist;
import de.hdm_stuttgart.se2.softwareProject.mediathek.lists.ListFactory;
import de.hdm_stuttgart.se2.softwareProject.mediathek.models.MediaFactory;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

public class MediaStorage {

	private static Logger log = LogManager.getLogger(MediaStorage.class);

	public static MediaMeta readMetaData(String path) {
		MediaPlayerFactory factory = new MediaPlayerFactory();
		MediaMeta meta = factory.getMediaMeta(path, true);
		return meta;
	}

	private static ArrayList<File> directories = new ArrayList<File>();
	private static ArrayList<File> files = new ArrayList<File>();

	public static void directoryList(File f) {
		log.info("Ordner " + f.toString() + " wird gescannt");
		File[] scannedMedia = f.listFiles();

		try {
			for (int i = 0; i < scannedMedia.length; i++) {
				if (scannedMedia[i].isDirectory()) {

					directories.add(scannedMedia[i]);
					directoryList(scannedMedia[i]);
				} else {
					log.debug(scannedMedia[i] + "wurde registriert");
					files.add(scannedMedia[i]);
				}
			}
		} catch(NullPointerException e) {
			throw new NullPointerException();
			// logging

		}
	}
	

	public static IMedialist[] mediaScan() {

		// HashMaps für Medien werden erzeugt
		IMedialist movies = ListFactory.getInstance("video", "scannedMovies");
		log.info("Liste für Videodateien erstellt");
		IMedialist audio = ListFactory.getInstance("audio", "scannedAudio");
		log.info("Liste für Audiodateien erstellt");
		IMedialist books = ListFactory.getInstance("book", "scannedBooks");
		log.info("Liste für Textdateien erstellt");

		// Aus jeder Datei des Arrays wird ein Objekt erstellt und der richtigen HashMap zugeordnet
		for (int i = 0; i < files.size(); i++) {
			String typ = null;
			MediaMeta meta = readMetaData(files.get(i).toString());
			log.info("Metadaten von " + files.get(i) + " werden gelesen");
			
			if (files.get(i).getName().toLowerCase().matches("^.*\\.(avi|mp4|wmv|mdk|mkv|mpeg|mpg)$")) {
				typ = "video";
				IMedia temp = MediaFactory.getInstance(typ, meta.getTitle(), false, files.get(i),
						true, meta.getLength(), meta.getDate(), meta.getArtist(), meta.getGenre(), meta.getDescription());
				movies.getContent().put(files.get(i), temp);
				
			} else if (files.get(i).getName().toLowerCase().matches("^.*\\.(mp3||wav|wma|aac|ogg)$")) {
				typ = "audio";
				IMedia temp = MediaFactory.getInstance(typ, meta.getTitle(), false, files.get(i),
						true, meta.getLength(), meta.getDate(), meta.getArtist(), meta.getGenre(), meta.getDescription());
				audio.getContent().put(files.get(i), temp);
			} /*else if (scannedMedia[i].getName().toLowerCase().matches("^.*\\.(doc|docx|pdf|html|txt)$")) {
				typ = "book";
				IMedia temp = MediaFactory.getInstance(typ, meta.getTitle(), false, scannedMedia[i], size, true);
				books.getContent().put(scannedMedia[i], temp);
			}*/ else {
				log.info("Dateityp nicht unterstützt. " + files.get(i) + " wurde nicht eingelesen.");
			}
			meta.release();
		}
		// Die drei Maps werden in ein Array geschrieben und zurückgegeben
		IMedialist[] allMedia = {movies, audio, books};
		return allMedia;
	}

	public static void deleteMedia(IMedia m) {
		Scanner s = new Scanner(System.in);

		System.out.println("Möchtest du das Medium " + m.getTitle() +  " von der Festplatte löschen? (Ja/Nein)\n");

		boolean validInput = false;
		while (validInput == false) {
			String input = s.nextLine();
			if (input.equals("Ja") || input.equals("ja")) {
				validInput = true;
				m.getFile().delete();
				log.info("Das Medium " + m.getTitle() + " wurde von der Festplatte gelöscht");
				m.removeMedia();
			} else if (input.equals("Nein") || input.equals("nein")) {
				validInput = true;
				m.removeMedia();
				log.info("Das Medium " + m.getTitle() + " wurde aus der Mediathek entfernt");
			} else {
				System.out.println("ungültige Eingabe - entweder für 'Ja' / 'ja' oder 'Nein' / 'nein' entscheiden");
			}
		}
		s.close();
	}

	public static void editMetaInformation(IMedia m, Scanner s) {
		MediaMeta meta = readMetaData(m.getFile().toString());

		if (m.getTyp().equals("video"))	{
			log.info("Video Metadaten werden bearbeitet");
			System.out.println("Wie lautet der Titel des Films?");
			meta.setTitle(s.nextLine());
			System.out.println("Wann wurde der Film veröffentlicht?");
			meta.setDate(s.nextLine());
			System.out.println("Wer ist Regisseur des Films?");
			meta.setArtist(s.nextLine());
			System.out.println("Welchem Genre gehört der Film an?");
			meta.setGenre(s.nextLine());
			System.out.println("Weitere Infos zum Film?");
			meta.setDescription(s.nextLine());
			System.out.println("Welche Bewertung bekommt der Film?");
			meta.setRating(s.nextLine());
		}
		if (m.getTyp().equals("audio"))	{
			log.info("Audio Metadaten werden bearbeitet");
			System.out.println("Wie lautet der Titel der Audiodatei?");
			meta.setTitle(s.nextLine());
			System.out.println("Wann wurde das Audio veröffentlicht?");
			meta.setDate(s.nextLine());
			System.out.println("Wer ist der Verfasser der Audios?");
			meta.setArtist(s.nextLine());
			System.out.println("Welchem Genre gehört das Audio an?");
			meta.setGenre(s.nextLine());
			System.out.println("Weitere Infos zum Audio?");
			meta.setDescription(s.nextLine());
			System.out.println("Welche Bewertung bekommt das Audio?");
			meta.setRating(s.nextLine());
		}
		System.out.println("Folgende Eingaben speichern? (Ja/Nein)");
		System.out.println(meta.getTitle());
		System.out.println(meta.getDate());
		System.out.println(meta.getArtist());
		System.out.println(meta.getGenre());
		System.out.println(meta.getDescription());
		System.out.println(meta.getRating());

		boolean validInput = false;
		while (validInput == false) {
			String input = s.nextLine();
			if (input.equals("Ja") || input.equals("ja")) {
				validInput = true;
				log.info("Metadaten für " + meta.toString() + " werden gespeichert");
				// Metainformationen werden in Datei gespeichert
				meta.save();

				// Metainformationen werden in Attribute des IMedia Objekts geschrieben
				m.setTitle(meta.getTitle());
				m.setDate(meta.getDate());
				m.setRegisseur(meta.getArtist());
				m.setGenre(meta.getGenre());
				m.setInfo(meta.getDescription());
				m.setRanking(meta.getRating());
				meta.release();
				log.info("Änderungen erfolgreich gespeichert.");
			} else if (input.equals("Nein") || input.equals("nein")) {
				validInput = true;
				log.info("Änderungen werden verworfen.");
				meta.release();
			} else {
				System.out.println("ungültige Eingabe - entweder für 'Ja' / 'ja' oder 'Nein' / 'nein' entscheiden");
			}
		}
	}
}
