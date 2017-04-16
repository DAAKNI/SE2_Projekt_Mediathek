package de.hdm_stuttgart.se2.softwareProject.mediathek.driver;

import java.io.File;
import java.util.Scanner;

import de.hdm_stuttgart.se2.softwareProject.mediathek.interfaces.IMedia;
import de.hdm_stuttgart.se2.softwareProject.mediathek.interfaces.IMedialist;
import de.hdm_stuttgart.se2.softwareProject.mediathek.lists.ListFactory;
import de.hdm_stuttgart.se2.softwareProject.mediathek.models.MediaFactory;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

public class MediaStorage {

	public static MediaMeta readMetaData(String path) {
		MediaPlayerFactory factory = new MediaPlayerFactory();
		MediaMeta meta = factory.getMediaMeta(path, true);
		return meta;
	}

	public static IMedialist[] mediaScan(File f) {

		// Angegebener Ordner wird gescannt und alle Dateien in Array geschrieben
		File[] scannedMedia = f.listFiles();

		// HashMaps für Medien werden erzeugt
		IMedialist movies = ListFactory.getInstance("video", "scannedMovies");
		IMedialist audio = ListFactory.getInstance("audio", "scannedAudio");
		IMedialist books = ListFactory.getInstance("book", "scannedBooks");

		// Aus jeder Datei des Arrays wird ein Objekt erstellt und der richtigen HashMap zugeordnet
		for (int i = 0; i < scannedMedia.length; i++) {
			String typ = null;
			MediaMeta meta = readMetaData(scannedMedia[i].toString());

			if (scannedMedia[i].getName().toLowerCase().matches("^.*\\.(avi|mp4|wmv|mdk|mkv|mpeg|mpg)$")) {
				typ = "video";
				IMedia temp = MediaFactory.getInstance(typ, meta.getTitle(), false, scannedMedia[i],
						true, meta.getLength(), meta.getDate(), meta.getArtist(), meta.getGenre(), meta.getDescription());
				movies.getContent().put(scannedMedia[i], temp);
			} else if (scannedMedia[i].getName().toLowerCase().matches("^.*\\.(mp3||wav|wma|aac|ogg)$")) {
				typ = "audio";
				IMedia temp = MediaFactory.getInstance(typ, meta.getTitle(), false, scannedMedia[i],
						true, meta.getLength(), meta.getDate(), meta.getArtist(), meta.getGenre(), meta.getDescription());
				audio.getContent().put(scannedMedia[i], temp);
			} /*else if (scannedMedia[i].getName().toLowerCase().matches("^.*\\.(doc|docx|pdf|html)$")) {
				typ = "book";
				IMedia temp = MediaFactory.getInstance(typ, meta.getTitle(), false, scannedMedia[i], size, true);
				books.getContent().put(scannedMedia[i], temp);
			}*/ else {
				System.out.println("Info: Dateityp nicht unterstützt. " + scannedMedia[i] + " wurde nicht eingelesen.");
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
				System.out.println("Das Medium " + m.getTitle() + " wurde von der Festplatte gelöscht");
				m.getFile().delete();
				m.removeMedia();
			} else if (input.equals("Nein") || input.equals("nein")) {
				validInput = true;
				System.out.println("Das Medium " + m.getTitle() + " wurde aus der Mediathek entfernt");
				m.removeMedia();
			} else {
				System.out.println("ungültige Eingabe - entweder für 'Ja' / 'ja' oder 'Nein' / 'nein' entscheiden");
			}
		}
		s.close();
	}

	public static void editMetaInformation(IMedia m, Scanner s) {
		MediaMeta meta = readMetaData(m.getFile().toString());

		if (m.getTyp().equals("video"))	{
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
				System.out.println("Änderungen werden gespeichert...");
				// Metainformationen werden in Datei gespeichert
				meta.save();
				
				// Metainformationen werden in Attributen des IMedia Objekts geschrieben
				m.setTitle(meta.getTitle());
				m.setDate(meta.getDate());
				m.setRegisseur(meta.getArtist());
				m.setGenre(meta.getGenre());
				m.setInfo(meta.getDescription());
				m.setRanking(meta.getRating());
				meta.release();
				System.out.println("Änderungen erfolgreich gespeichert.");
			} else if (input.equals("Nein") || input.equals("nein")) {
				validInput = true;
				System.out.println("Änderungen werden verworfen.");
				meta.release();
			} else {
				System.out.println("ungültige Eingabe - entweder für 'Ja' / 'ja' oder 'Nein' / 'nein' entscheiden");
			}
		}
	}
}
