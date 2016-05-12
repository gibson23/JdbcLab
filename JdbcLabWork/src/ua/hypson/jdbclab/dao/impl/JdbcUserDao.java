package ua.hypson.jdbclab.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ua.hypson.jdbclab.dao.interfaces.UserDao;
import ua.hypson.jdbclab.entity.Role;
import ua.hypson.jdbclab.entity.User;
import ua.hypson.jdbclab.factory.ConnectionFactory;
import ua.hypson.jdbclab.factory.UserFactory;

public class JdbcUserDao extends ConnectionFactory implements UserDao {

  private boolean checkIfExists(User user) {

    Connection connection = super.createConnection();

    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("SELECT * FROM USER WHERE PK_USER_ID = ?");
      statement.setLong(1, user.getId());

      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        statement.close();
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    return false;
  }

  @SuppressWarnings({ "deprecation" })
  private String dateToString(Date date) {
    return date.getYear() + "-" + date.getMonth() + "-" + date.getDate();
  }

  @Override
  public void create(User user) {
    if (null == user) {
      throw new RuntimeException("Null user is unacceptable");
    }
    if (checkIfExists(user)) {
      throw new RuntimeException("There already is user with id" + user.getId());
    }
    Connection connection = super.createConnection();
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("INSERT INTO user"
          + " (PK_USER_ID, LOGIN, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, BIRTHDAY) VALUES (?, ?, ?, ?, ?, ?, ?)");
      statement.setLong(1, user.getId());
      statement.setString(2, user.getLogin());
      statement.setString(3, user.getPassword());
      statement.setString(4, user.getEmail());
      statement.setString(5, user.getFirstName());
      statement.setString(6, user.getLastName());
      statement.setString(7, dateToString(user.getBirthday()));
      statement.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        statement.close();
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void update(User user) {
    if (null == user) {
      throw new RuntimeException("Null user is unacceptable");
    }
    if (!checkIfExists(user)) {
      throw new RuntimeException("There is no user with id" + user.getId());
    }
    Connection connection = super.createConnection();
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("UPDATE USER SET LOGIN = ?, PASSWORD = ?, EMAIL = ?,"
          + "FIRSTNAME = ?, LASTNAME = ?, BIRTHDAY = ? WHERE PK_USER_ID = ?");
      statement.setString(1, user.getLogin());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getEmail());
      statement.setString(4, user.getFirstName());
      statement.setString(5, user.getLastName());
      statement.setString(6, dateToString(user.getBirthday()));
      statement.setLong(7, user.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        statement.close();
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }

  }

  @Override
  public void remove(User user) {
    if (null == user) {
      throw new RuntimeException("Null user is unacceptable");
    }
    if (!checkIfExists(user)) {
      throw new RuntimeException("There is no user with id" + user.getId());
    }
    Connection connection = super.createConnection();
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("DELETE FROM USER WHERE PK_USER_ID = ?");
      statement.setLong(1, user.getId());
      statement.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        statement.close();
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }

  }

  @Override
  public List<User> findAll() {
    List<User> users = new ArrayList<>();
    Connection connection = super.createConnection();
    Statement statement = null;
    try {
      statement = connection.createStatement();

      ResultSet rs = statement.executeQuery("SELECT * FROM USER");
      while (rs.next()) {
        User user = UserFactory.createUser(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
            rs.getString(5), rs.getString(6), rs.getDate(7));
        users.add(user);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        statement.close();
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    return users;
  }

  @Override
  public User findByLogin(String login) {
    Connection connection = super.createConnection();
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("SELECT * FROM USER WHERE LOGIN = ?");
      statement.setString(1, login);
      ResultSet rs = statement.executeQuery();

      if (rs.next()) {
        User user = UserFactory.createUser(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
            rs.getString(5), rs.getString(6), rs.getDate(7));
        return user;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        statement.close();
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  @Override
  public User findByEmail(String email) {
    Connection connection = super.createConnection();
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("SELECT * FROM USER WHERE EMAIL = ?");
      statement.setString(1, email);
      ResultSet rs = statement.executeQuery();

      if (rs.next()) {
        User user = UserFactory.createUser(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
            rs.getString(5), rs.getString(6), rs.getDate(7));
        return user;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        statement.close();
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  @Override
  public void setUserRole(User user, Role role) {
    if (null == user) {
      throw new RuntimeException("Null user is unacceptable");
    }
    if (null == role) {
      throw new RuntimeException("Null role is unacceptable");
    }
    if (!checkIfExists(user)) {
      throw new RuntimeException("There is no user with id" + user.getId());
    }
    if (!(new JdbcRoleDao().checkRole(role))) {
      throw new RuntimeException("There is no role with id" + role.getId());
    }
    Connection connection = super.createConnection();
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement("UPDATE USER SET FK_ROLE_ID = ? WHERE PK_USER_ID = ?");
      statement.setLong(1, role.getId());
      statement.setLong(2, user.getId());
      statement.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        statement.close();
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public Role getUserRole(User user) {
    Long fk_role_id = null;
    Connection connection = super.createConnection();
    PreparedStatement statement2 = null;
    PreparedStatement statement = null;
    try {
      statement2 = connection.prepareStatement("SELECT FK_ROLE_ID FROM USER WHERE PK_USER_ID = ?");
      statement2.setLong(1, user.getId());
      ResultSet rs2 = statement2.executeQuery();
      if (rs2.next()) {
        fk_role_id = rs2.getLong(1);
      }
      statement = connection.prepareStatement("SELECT NAME FROM ROLE WHERE PK_ROLE_ID = ?");
      statement.setLong(1, fk_role_id);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        String roleName = rs.getString("NAME");
        Role role = new Role();
        role.setId(fk_role_id);
        role.setName(roleName);
        return role;
      }
      return null;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        statement2.close();
        statement.close();
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

}