package com.example.campusdiscovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Event {
    private String title;
    private final User host;
    private String description;
    private String location;
    private String time;
    private HashMap<User, RSVPStatus> rsvpList;
    private boolean inviteOnly;
    private List<User> invitees;
    private int capacity = 100; // Arbitrary number as they don't specify in the instructions.
    private int attendingCount = 0;

    public Event(String title, User host, String description, String location, String time) {
        this.title = title;
        this.host = host;
        this.description = description;
        this.location = location;
        this.time = time;
        this.rsvpList = new HashMap<>();
        this.invitees = new ArrayList<>();
    }

    public boolean RSVP(RSVPStatus response, User user) {
        if (inviteOnly && !invitees.contains(user)) {
            return false;
        }
        if (!rsvpList.containsKey(user) || rsvpList.get(user) == RSVPStatus.WILL_NOT_ATTEND) { // increment if not there before or if there but not will attend
            if (response != RSVPStatus.WILL_NOT_ATTEND) { // only increment if not (will not attend)
                if (attendingCount + 1 > capacity) { // cannot be accommodated.
                    return false;
                }
                attendingCount += 1;
            }
        }
        if (rsvpList.containsKey(user) && rsvpList.get(user) != RSVPStatus.WILL_NOT_ATTEND) { // Decrement if change to will not attend from any other state
            if (response == RSVPStatus.WILL_NOT_ATTEND) {
                attendingCount -= 1;
            }
        }
        rsvpList.put(user, response);
        return true;
    }

    public boolean removeRSVP(User ofUser, User byUser) {
        if ((ofUser != byUser && byUser != host && byUser.getUserType() != UserType.Administrator) || !rsvpList.containsKey(ofUser)) { // (not curr user and not host and not admin) or not even in the list
            return false;
        }
        RSVPStatus status = rsvpList.remove(ofUser);
        if (status != RSVPStatus.WILL_NOT_ATTEND) {
            attendingCount -= 1;
        }
        return true;
    }

    public boolean setInviteOnly(Boolean value, User user) {
        if (user != host && user.getUserType() != UserType.Administrator) {
            return false;
        }
        this.inviteOnly = value;
        return true;
    }

    /**
     * Invites user to event
     * @param user User to be invited
     * @param byUser The user that wants to make an invitation
     * @return Whether the operation was successful
     */
    public boolean invite(User user, User byUser) {
        if ((byUser != host && byUser.getUserType() != UserType.Administrator) || invitees.contains(user)) {
            return false;
        }
        invitees.add(user);
        return true;
    }


    public boolean cancelInvitation(User user, User byUser) {
        if ((byUser != host && byUser.getUserType() != UserType.Administrator) || !invitees.contains(user)) {
            return false;
        }
        invitees.remove(user);
        return true;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean setTitle(String title, User user) {
        if (user != host && user.getUserType() != UserType.Administrator) {
            return false;
        }
        this.title = title;
        return true;
    }

    public boolean setDescription(String description, User user) {
        if (user != host && user.getUserType() != UserType.Administrator) {
            return false;
        }
        this.description = description;
        return true;
    }

    public boolean setLocation(String location, User user) {
        if (user != host && user.getUserType() != UserType.Administrator) {
            return false;
        }
        this.location = location;
        return true;
    }

    public boolean setTime(String time, User user) {
        if (user != host && user.getUserType() != UserType.Administrator) {
            return false;
        }
        this.time = time;
        return true;
    }

    public HashMap<User, RSVPStatus> getRSVPList() {
        return rsvpList;
    }

    public List<User> getInvitees() {
        return invitees;
    }

    public int getAttendingCount() {
        return attendingCount;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isInviteOnly() {
        return inviteOnly;
    }

    public String getTitle() {
        return title;
    }

    public User getHost() {
        return host;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }
}
