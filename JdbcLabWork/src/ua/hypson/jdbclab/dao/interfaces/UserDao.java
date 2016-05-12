package ua.hypson.jdbclab.dao.interfaces;

import java.util.List;

import ua.hypson.jdbclab.entity.Role;
import ua.hypson.jdbclab.entity.User;

public interface UserDao {

  public void create(User user);

  public void update(User user);

  public void remove(User user);

  public List<User> findAll();

  public User findByLogin(String login);

  public User findByEmail(String email);

  public void setUserRole(User user, Role role);

  public Role getUserRole(User user);

}
