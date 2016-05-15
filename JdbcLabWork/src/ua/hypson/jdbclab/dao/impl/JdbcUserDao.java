package ua.hypson.jdbclab.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.hypson.jdbclab.dao.interfaces.RoleDao;
import ua.hypson.jdbclab.dao.interfaces.UserDao;
import ua.hypson.jdbclab.entity.User;
import ua.hypson.jdbclab.factory.ConnectionFactory;

public class JdbcUserDao implements UserDao {

  private ConnectionFactory factory;
  private RoleDao roleDao;

  public JdbcUserDao() {
    factory = ConnectionFactory.getFactory();
    roleDao = new JdbcRoleDao();
  }

  private void checkNullUser(User user) {
    if (null == user) {
      throw new RuntimeException("Null user is unacceptable");
    }
  }

  private Boolean checkIfExists(User user) {
    try (Connection connection = factory.createConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER WHERE PK_USER_ID = ?")) {
      statement.setLong(1, user.getId());
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return true;
        } else {
          return false;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void create(User user) {
    checkNullUser(user);
    if (checkIfExists(user)) {
      throw new RuntimeException("There already is user with id" + user.getId());
    }
    try (Connection connection = factory.createConnection();
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO USER" + " (PK_USER_ID, LOGIN, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, BIRTHDAY, FK_ROLE_ID)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
      statement.setLong(1, user.getId());
      statement.setString(2, user.getLogin());
      statement.setString(3, user.getPassword());
      statement.setString(4, user.getEmail());
      statement.setString(5, user.getFirstName());
      statement.setString(6, user.getLastName());
      statement.setString(7, user.getBirthday().toString());
      statement.setLong(8, user.getRole().getId());
      statement.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(User user) {
    checkNullUser(user);
    if (!checkIfExists(user)) {
      throw new RuntimeException("There is no user with id" + user.getId());
    }
    try (Connection connection = factory.createConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE USER SET LOGIN = ?, PASSWORD = ?, EMAIL = ?,"
            + "FIRSTNAME = ?, LASTNAME = ?, BIRTHDAY = ?, FK_ROLE_ID = ? WHERE PK_USER_ID = ?")) {
      statement.setString(1, user.getLogin());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getEmail());
      statement.setString(4, user.getFirstName());
      statement.setString(5, user.getLastName());
      statement.setString(6, user.getBirthday().toString());
      statement.setLong(7, user.getRole().getId());
      statement.setLong(8, user.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(User user) {
    checkNullUser(user);
    if (!checkIfExists(user)) {
      throw new RuntimeException("There is no user with id" + user.getId());
    }
    try (Connection connection = factory.createConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM USER WHERE PK_USER_ID = ?")) {
      statement.setLong(1, user.getId());
      statement.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<User> findAll() {
    List<User> users = new ArrayList<>();
    try (Connection connection = factory.createConnection(); Statement statement = connection.createStatement()) {
      try (ResultSet rs = statement.executeQuery("SELECT * FROM USER")) {
        while (rs.next()) {
          User user = User.createUser(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
              rs.getString(6), rs.getDate(7), roleDao.findById(rs.getLong(8)));
          users.add(user);
        }
        return users;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public User findByLogin(String login) {
    try (Connection connection = factory.createConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER WHERE LOGIN = ?")) {
      statement.setString(1, login);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          User user = User.createUser(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
              rs.getString(6), rs.getDate(7), roleDao.findById(rs.getLong(8)));
          return user;
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public User findByEmail(String email) {
    try (Connection connection = factory.createConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER WHERE EMAIL = ?")) {
      statement.setString(1, email);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          User user = User.createUser(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
              rs.getString(6), rs.getDate(7), roleDao.findById(rs.getLong(8)));
          return user;
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
