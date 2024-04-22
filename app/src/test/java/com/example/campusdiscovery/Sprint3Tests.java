package com.example.campusdiscovery;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Sprint3Tests {
    @Test
    public void testDatabaseFindUser() {
        User user1 = new User("user1", "pass", UserType.Student);
        Database.getDB().addUser(user1);

        assertEquals(Database.getDB().findUserByUsername("user1"), user1);
    }

    @Test
    public void testDatabaseFindUserFail() {
        User user1 = new User("user1", "pass", UserType.Student);
        Database.getDB().addUser(user1);

        assertEquals(Database.getDB().findUserByUsername("user2"), null);
    }
    @Test
    public void testRSVPWhileUninvited() {
        User host = new User("host", "pass", UserType.Organizer);
        Event event = new Event("Test Event", host, "This is a test event.", "Test Location", "Test Time");

        event.setInviteOnly(true, host);

        User user1 = new User("user1", "pass", UserType.Student);
        assertFalse(event.RSVP(RSVPStatus.WILL_ATTEND, user1));
    }

    @Test
    public void testRSVPWhileInvited() {
        User host = new User("host", "pass", UserType.Organizer);
        Event event = new Event("Test Event", host, "This is a test event.", "Test Location", "Test Time");
        assertTrue(event.setInviteOnly(true, host));

        User user1 = new User("user1", "pass", UserType.Student);
        event.invite(user1, host);
        assertTrue(event.RSVP(RSVPStatus.WILL_ATTEND, user1));
    }

    @Test
    public void testDisableInviteOnly() {
        User host = new User("host", "pass", UserType.Organizer);
        Event event = new Event("Test Event", host, "This is a test event.", "Test Location", "Test Time");

        User user1 = new User("user1", "pass", UserType.Student);

        assertTrue(event.setInviteOnly(true, host));
        assertFalse(event.RSVP(RSVPStatus.WILL_ATTEND, user1));

        assertTrue(event.setInviteOnly(false, host));
        assertTrue(event.RSVP(RSVPStatus.WILL_ATTEND, user1));
    }
    @Test
    public void testFullCapacity() {
        User host = new User("host", "pass", UserType.Organizer);
        Event event = new Event("Test Event", host, "This is a test event.", "Test Location", "Test Time");

        User user1 = new User("user1", "pass", UserType.Student);
        event.RSVP(RSVPStatus.WILL_ATTEND, user1);
        event.setCapacity(1);

        User user2 = new User("user2", "pass", UserType.Student);
        assertFalse(event.RSVP(RSVPStatus.WILL_ATTEND, user2));
    }
    @Test
    public void testDoubleRSVPMatch() {
        User host = new User("host", "pass", UserType.Organizer);
        Event event = new Event("Test Event", host, "This is a test event.", "Test Location", "Test Time");

        User user1 = new User("user1", "pass", UserType.Student);
        event.RSVP(RSVPStatus.WILL_ATTEND, user1);
        assertEquals(event.getRSVPList().get(user1), RSVPStatus.WILL_ATTEND);

        event.RSVP(RSVPStatus.WILL_NOT_ATTEND, user1);
        assertEquals(event.getRSVPList().get(user1), RSVPStatus.WILL_NOT_ATTEND);
    }
    @Test
    public void testUNRSVPListMatch() {
        User host = new User("host", "pass", UserType.Organizer);
        Event event = new Event("Test Event", host, "This is a test event.", "Test Location", "Test Time");

        User user1 = new User("user1", "pass", UserType.Student);
        event.RSVP(RSVPStatus.WILL_ATTEND, user1);
        assertEquals(event.getRSVPList().get(user1), RSVPStatus.WILL_ATTEND);

        event.removeRSVP(user1, user1);
        assertEquals(event.getRSVPList().get(user1), null);
    }

    @Test
    public void testRSVPAttendanceCount() {
        Database db = Database.getDB();
        User user = db.login("ssa", "ssa");
        Event event = new Event("Event title", user, "Description", "Location", "Time");
        db.createEvent(event);

        boolean rsvpSuccess = event.RSVP(RSVPStatus.WILL_ATTEND, user);
        assertTrue(rsvpSuccess);
        assertEquals(1, event.getAttendingCount());

        rsvpSuccess = event.RSVP(RSVPStatus.MAYBE, user);
        assertTrue(rsvpSuccess);
        assertEquals(1, event.getAttendingCount());

        rsvpSuccess = event.RSVP(RSVPStatus.WILL_NOT_ATTEND, user);
        assertTrue(rsvpSuccess);
        assertEquals(0, event.getAttendingCount());
    }
    @Test
    public void testAttendanceCount2() {
        User host = new User("host", "pass", UserType.Organizer);
        Event event = new Event("Test Event", host, "This is a test event.", "Test Location", "Test Time");

        User user1 = new User("user1", "pass", UserType.Student);
        event.RSVP(RSVPStatus.WILL_ATTEND, user1);
        assertEquals(event.getAttendingCount(), 1);

        User user2 = new User("user2", "pass", UserType.Student);
        event.RSVP(RSVPStatus.WILL_NOT_ATTEND, user2);
        assertEquals(event.getAttendingCount(), 1);

        User user3 = new User("user3", "pass", UserType.Student);
        event.RSVP(RSVPStatus.MAYBE, user3);
        assertEquals(event.getAttendingCount(), 2);

        User user4 = new User("user4", "pass", UserType.Student);
        event.RSVP(RSVPStatus.IM_YOUR_NEMESIS, user4);
        assertEquals(event.getAttendingCount(), 3);
    }
    @Test
    public void testDoubleRSVP() {
        Database db = Database.getDB();
        User user = db.login("ssa", "ssa");
        Event event = new Event("Event title", user, "Description", "Location", "Time");
        db.createEvent(event);

        boolean rsvpSuccess = event.RSVP(RSVPStatus.WILL_ATTEND, user);
        assertTrue(rsvpSuccess);
        assertEquals(1, event.getAttendingCount());

        rsvpSuccess = event.RSVP(RSVPStatus.WILL_ATTEND, user);
        assertTrue(rsvpSuccess);
        assertEquals(1, event.getAttendingCount());
    }
    @Test
    public void testDoubleRSVP2() {
        User host = new User("host", "pass", UserType.Organizer);
        Event event = new Event("Test Event", host, "This is a test event.", "Test Location", "Test Time");

        User user1 = new User("user1", "pass", UserType.Student);
        event.RSVP(RSVPStatus.WILL_ATTEND, user1);
        assertEquals(event.getAttendingCount(), 1);

        event.RSVP(RSVPStatus.WILL_NOT_ATTEND, user1);
        assertEquals(event.getAttendingCount(), 0);
    }
    @Test
    public void testUNRSVP2() {
        User host = new User("host", "pass", UserType.Organizer);
        Event event = new Event("Test Event", host, "This is a test event.", "Test Location", "Test Time");

        User user1 = new User("user1", "pass", UserType.Student);
        event.RSVP(RSVPStatus.WILL_ATTEND, user1);
        assertEquals(event.getAttendingCount(), 1);

        event.removeRSVP(user1, user1);
        assertEquals(event.getAttendingCount(), 0);
    }
    @Test
    public void testRSVPListMatch() {
        User host = new User("host", "pass", UserType.Organizer);
        Event event = new Event("Test Event", host, "This is a test event.", "Test Location", "Test Time");

        User user1 = new User("user1", "pass", UserType.Student);
        event.RSVP(RSVPStatus.WILL_ATTEND, user1);

        Map<User, RSVPStatus> expectedList = new HashMap<>();
        expectedList.put(user1, RSVPStatus.WILL_ATTEND);
        assertEquals(event.getRSVPList(), expectedList);
    }
    @Test
    public void testUNRSVP() {
        Database db = Database.getDB();
        User user = db.login("ssa", "ssa");
        Event event = new Event("Event title", user, "Description", "Location", "Time");
        db.createEvent(event);

        boolean unrsvpSuccess = event.removeRSVP(user, user);
        assertFalse(unrsvpSuccess);
        assertEquals(0, event.getAttendingCount());

        event.RSVP(RSVPStatus.WILL_NOT_ATTEND, user);
        unrsvpSuccess = event.removeRSVP(user, user);
        assertTrue(unrsvpSuccess);
        assertEquals(0, event.getAttendingCount());

        event.RSVP(RSVPStatus.WILL_ATTEND, user);
        unrsvpSuccess = event.removeRSVP(user, user);
        assertTrue(unrsvpSuccess);
        assertEquals(0, event.getAttendingCount());
    }
    @Test
    public void testRSVPList() {
        Database db = Database.getDB();
        User user = db.login("ssa", "ssa");
        Event event = new Event("Event title", user, "Description", "Location", "Time");
        db.createEvent(event);

        boolean rsvpSuccess = event.RSVP(RSVPStatus.WILL_ATTEND, user);
        assertTrue(rsvpSuccess);

        HashMap<User, RSVPStatus> rsvpList = event.getRSVPList();
        assertTrue(rsvpList.containsKey(user));
        assertEquals(RSVPStatus.WILL_ATTEND, rsvpList.get(user));
        rsvpSuccess = event.RSVP(RSVPStatus.MAYBE, user);
        assertTrue(rsvpSuccess);

        rsvpList = event.getRSVPList();
        assertTrue(rsvpList.containsKey(user));
        assertEquals(RSVPStatus.MAYBE, rsvpList.get(user));
    }

}
