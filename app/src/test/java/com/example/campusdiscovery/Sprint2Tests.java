package com.example.campusdiscovery;

import org.junit.Test;

import static org.junit.Assert.*;


public class Sprint2Tests {
    @Test
    public void testAdminCanEditAnyEvent() {
        Database db = Database.getDB();
        User admin = db.login("admin", "admin");
        User ssa = db.login("ssa", "ssa");
        Event event = new Event("Event", ssa, "Desc", "CULC", "Jan 21 2023");
        boolean success = event.setTitle("New title", admin);
        assertTrue(success);
        assertTrue(event.getTitle().equals( "New title"));
    }
    @Test
    public void testUserCanEditOwnEvent() {
        Database db = Database.getDB();
        User user = db.login("ssa", "ssa");
        Event event = new Event("Event title", user, "Description", "Location", "Time");
        db.createEvent(event);
        boolean editSuccess = event.setTitle("New title", user);
        assertTrue(editSuccess);
        assertTrue(event.getTitle() .equals( "New title"));
    }
    @Test
    public void testUserCanDeleteOwnEvent() {
        Database db = Database.getDB();
        User user = db.login("ssa", "ssa");
        Event event = new Event("Event title", user, "Description", "Location", "Time");
        db.createEvent(event);
        boolean deleteSuccess = db.deleteEvent(event, user);
        assertTrue(deleteSuccess);
        assertFalse(db.getEventList().contains(event));
    }
    @Test
    public void testAdminCanDeleteAnyEvent() {
        Database db = Database.getDB();
        User admin = db.login("admin", "admin");
        User ssa = db.login("ssa", "ssa");
        Event event = new Event("Event", ssa, "Desc", "CULC", "Jan 21 2023");
        db.createEvent(event);
        boolean success = db.deleteEvent(event, admin);
        assertTrue(success);;
        assertFalse(db.getEventList().contains(event));
    }
    @Test
    public void testEventIsAddedOnCreate() {
        Database db = Database.getDB();
        User user = db.login("ssa", "ssa");
        Event event = new Event("Event title", user, "Description", "Location", "Time");
        boolean eventExistsBefore = db.getEventList().contains(event);
        db.createEvent(event);
        boolean eventExistsAfterCreate = db.getEventList().contains(event);
        assertFalse(eventExistsBefore);
        assertTrue(eventExistsAfterCreate);
    }
    @Test
    public void testEventIsRemovedOnDelete() {
        Database db = Database.getDB();
        User user = db.login("ssa", "ssa");
        Event event = new Event("Event title", user, "Description", "Location", "Time");
        db.createEvent(event);
        boolean eventExistsBefore = db.getEventList().contains(event);
        db.deleteEvent(event, user);
        boolean eventExistsAfterDelete = db.getEventList().contains(event);
        assertTrue(eventExistsBefore);
        assertFalse(eventExistsAfterDelete);
    }
    @Test
    public void testAuthenticateWithWrongPassword() {
        Database db = Database.getDB();
        User user = db.login("ssa", "incorrect password");
        assertNull(user);
    }
    @Test
    public void getDB_isItTrulyASingleton() { // 12
        Database db1 = Database.getDB();
        Database db2 = Database.getDB();
        User u = new User("B", "B", UserType.Staff);
        Event newEvent = new Event("A", u, "C", "D", "E");
        db1.createEvent(newEvent);
        assertEquals(true, db2.getEventList().contains(newEvent));
    }

    @Test
    public void editing_unauthorized() { // 5
        User u2 = new User("B", "B", UserType.Staff);
        User u3 = new User("C", "C", UserType.Staff);
        String oldDesc = "old description";
        String oldTime = "2pm";
        String oldLocation = "scheller";
        String oldTitle = "title";
        Event newEvent = new Event(oldTitle, u2, oldDesc, oldLocation, oldTime);

        newEvent.setLocation("Zoom", u3);
        assertEquals(newEvent.getLocation(), oldLocation);

        newEvent.setTime("1pm", u3);
        assertEquals(newEvent.getTime(), oldTime);

        newEvent.setDescription("new description", u3);
        assertEquals(newEvent.getDescription(), oldDesc);

        newEvent.setTitle("new title", u3);
        assertEquals(newEvent.getTitle(), oldTitle);
    }

    @Test
    public void deleting_unauthorized() { // 6
        User u2 = new User("B", "B", UserType.Staff);
        User u3 = new User("C", "C", UserType.Staff);
        Event newEvent = new Event("CS 2340 Break", u2, "Some sort of puzzle", "Scheller", "17 Oct 2022 4:" + " pm");
        Database.getDB().createEvent(newEvent);
        assertEquals(false, Database.getDB().deleteEvent(newEvent, u3));
        assertEquals(true, Database.getDB().getEventList().contains(newEvent));
    }
}