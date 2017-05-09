package de.hdm_stuttgart.se2.softwareProject.mediathek.lists;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hdm_stuttgart.se2.softwareProject.mediathek.interfaces.IMedia;
import de.hdm_stuttgart.se2.softwareProject.mediathek.interfaces.IMedialist;

/**
 * 
 * @author ll040
 *
 * Audiolist implementiert das Interface IMedialist. Die Klasse
 * enthält ein Namesattribut und eine Map mit File-Objekten als Key und 
 * Audio-Objekten vom Typ IMedia als Wert.
 * Über die Methoden der Klasse, können Audio-Objekte hinzugefügt, ge-
 * löscht und ausgelesen werden. Zudem kann der Inhalt ausgegeben bzw.
 * aufgelistet werden. Der Name der Liste kann nachträglich geändert 
 * werden
 * 
 */
class Audiolist implements IMedialist {
	
	private static Logger log = LogManager.getLogger(Audiolist.class);
	
	private Map<File, IMedia> content;
	private String name;

	public Audiolist(String name) {
		this.content = new HashMap<File, IMedia>();
		this.name = name;
	}

	@Override
	public Map<File, IMedia> getContent() {
		return content;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void removeMedia(IMedia m) {
		this.content.remove(m.getFile());	
	}

	@Override
	public void printList() {
		for (Entry<File, IMedia> m : this.content.entrySet()) {
			m.getValue().getDetails();
		}
		log.debug("Inhalte der Audioliste " + this.name + " wurden komplett ausgegeben");
	}

	@Override
	public void setName(String name) {
		this.name = name;
		log.debug("Name der Audioliste auf " + this.name + " geändert");
	}

	@Override
	public void addMedia(IMedia media) {
		this.content.put(media.getFile(), media);
		log.debug(media.getTitle() + " wurde zur Liste hinzugefügt");
	}
}
