package com.hotelbooking.util;

import com.hotelbooking.models.User;
import java.time.LocalDateTime;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private LocalDateTime sessionStartTime;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.sessionStartTime = LocalDateTime.now();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return isLoggedIn() && "ADMIN".equals(currentUser.getRole());
    }

    public void clearSession() {
        this.currentUser = null;
        this.sessionStartTime = null;
    }

    public LocalDateTime getSessionStartTime() {
        return sessionStartTime;
    }
}