package com.example.campusdiscovery;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Database {
    private final HashMap<String, User> userList;
    private final List<Event> eventList;
    private final HashMap<String, GeoPoint> locationList;
    private static Database shared;

    public static Database getDB() {
        if (shared == null) {
            shared = new Database();
            hardCoded();
        }

        return shared;
    }

    private static void hardCoded() {
        shared.locationList.put("Scheller", new GeoPoint(33.77654, -84.38773));
        shared.locationList.put("Exhibition Hall", new GeoPoint(33.77483, -84.40184));
        shared.locationList.put("College of Computing", new GeoPoint(33.77742, -84.39736));
        shared.locationList.put("Klaus", new GeoPoint(33.77709, -84.39599));
        shared.locationList.put("CULC", new GeoPoint(33.77478, -84.39642));
        shared.locationList.put("Mason", new GeoPoint(33.77665, -84.39888));
        shared.locationList.put("Kendeda", new GeoPoint(33.77862, -84.39974));
        shared.locationList.put("Woodruff", new GeoPoint(33.77688, -84.40026));
        User u1 = new User("admin", "admin", UserType.Administrator);
        shared.addUser(u1);
        User u2 = new User("blake", "blake", UserType.Staff);
        shared.addUser(u2);
        User u3 = new User("tariq", "tariq", UserType.Student);
        shared.addUser(u3);
        User u4 = new User("ssa", "ssa", UserType.Organizer);
        shared.addUser(u4);
        User u5 = new User("u5", "u5", UserType.Student);
        shared.addUser(u5);
        User u6 = new User("u6", "u6", UserType.Student);
        shared.addUser(u6);
        for (int i = 0; i < 15; i++) {
            Event newEvent = new Event("CS 2340 Break " + i, u4, "Some sort of puzzle", getDB().locationList.keySet().stream().skip(new Random().nextInt(getDB().locationList.size())).findFirst().orElse(null), "17 Oct 2022 4:" + (i - (i%2)) + " pm");
            if (i % 2 == 0) {
                newEvent.RSVP(RSVPStatus.IM_YOUR_NEMESIS, u2);
                newEvent.RSVP(RSVPStatus.WILL_ATTEND, u3);
                newEvent.RSVP(RSVPStatus.WILL_ATTEND, u4);
                newEvent.RSVP(RSVPStatus.WILL_NOT_ATTEND, u5);
                newEvent.RSVP(RSVPStatus.MAYBE, u6);
            } else {
                newEvent.setInviteOnly(true, u1); // u1 has admin permissions so use that
                newEvent.invite(u3, u1); // Invite u3
                newEvent.RSVP(RSVPStatus.WILL_ATTEND, u3); // u3 RSVPs
                newEvent.RSVP(RSVPStatus.WILL_ATTEND, u5); // u5 shouldn't show in the RSVP list as it's not invited.
            }
            shared.createEvent(newEvent);
        }

    }

    private Database() {
        userList = new HashMap<>();
        eventList = new ArrayList<>();
        locationList = new HashMap<>();
    }

    public HashMap<String, GeoPoint> getLocationList() {
        return locationList;
    }

    public boolean addUser(User user) {
        userList.put(user.getName(), user);
        return true;
    }

    public User login(String username, String password) {
        if (!userList.containsKey(username) || !userList.get(username).authenticate(password)) {
            return null;
        }
        return userList.get(username);
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public boolean createEvent(Event event) {
        eventList.add(0, event);
        return true;
    }

    public boolean canEditEvent(Event event, User user) {
        if (!eventList.contains(event))
            return false;
        return (user.getUserType() == UserType.Administrator || event.getHost() == user);
    }

    public boolean deleteEvent(Event event, User user) {
        if (!eventList.contains(event) || !(user.getUserType() == UserType.Administrator || event.getHost() == user))
            return false;
        eventList.remove(event);
        return true;
    }

    public User findUserByUsername(String s) {
        if (userList.containsKey(s)) {
            return userList.get(s);
        }
        return null;
    }
}
