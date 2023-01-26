package service.user;

import exception.DuplicateIdException;
import model.User;

import javax.naming.AuthenticationException;
import java.util.Collection;
import java.util.Map;

public interface UserService {
    void join(User user);

    User findUser(String userId);

    void login(String userId, String password) throws AuthenticationException;

    void createUser(Map<String, String> params) throws DuplicateIdException;

    Collection<User> findAllUsers();
}
