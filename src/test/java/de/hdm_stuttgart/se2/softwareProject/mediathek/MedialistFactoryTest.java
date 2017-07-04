package de.hdm_stuttgart.se2.softwareProject.mediathek;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.hdm_stuttgart.se2.softwareProject.mediathek.exceptions.InvalidTypeException;
import de.hdm_stuttgart.se2.softwareProject.mediathek.interfaces.IMedialist;
import de.hdm_stuttgart.se2.softwareProject.mediathek.lists.ListFactory;

public class MedialistFactoryTest {

	@Test
	public void testMovielist() throws InvalidTypeException {
		IMedialist list = ListFactory.getInstance("video", "testListe");
		assertEquals("Movielist", list.getClass().getSimpleName());
	}
	
	@Test
	public void testAudiolist() throws InvalidTypeException {
		IMedialist list = ListFactory.getInstance("audio", "testListe");
		assertEquals("Audiolist", list.getClass().getSimpleName());
	}
	
	@Test
	public void testBookList() throws InvalidTypeException {
		IMedialist list = ListFactory.getInstance("book", "testListe");
		assertEquals("Booklist", list.getClass().getSimpleName());
	}
	
	@Test(expected=InvalidTypeException.class)
	public void testMedialistNegative() throws InvalidTypeException {
		ListFactory.getInstance("falseType", "testListe");
	}

}
