package ua.hypson.jdbclab.factory;

import java.util.Date;

import ua.hypson.jdbclab.entity.User;

public class UserFactory {

  public static User createUser(Long id, String login, String password, String email, String firstName, String lastName,
      Date birthday) {
    User user = new User();
    user.setId(id);
    user.setLogin(login);
    user.setPassword(password);
    user.setEmail(email);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setBirthday(birthday);
    return user;
  }

}
