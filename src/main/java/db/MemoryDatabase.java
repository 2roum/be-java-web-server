package db;

import model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryDatabase implements Database {
    private final static Map<String, User> users = new ConcurrentHashMap<>();
    private static Database database;

    private MemoryDatabase() {
    }

    public static Database getInstance() {
        if (database == null) database = new MemoryDatabase();
        return database;
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User findUserById(String userId) {
        return users.get(userId);
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
