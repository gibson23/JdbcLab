package ua.hypson.jdbclab.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ua.hypson.jdbclab.dao.interfaces.RoleDao;
import ua.hypson.jdbclab.entity.Role;
import ua.hypson.jdbclab.factory.ConnectionFactory;

public class JdbcRoleDao implements RoleDao {

  private ConnectionFactory factory;

  public JdbcRoleDao() {
    factory = ConnectionFactory.getFactory();
  }

  protected Boolean checkRole(Role role) {
    return checkIfExists(role);
  }

  private Boolean checkIfExists(Role role) {

    try (Connection connection = factory.createConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM ROLE WHERE PK_ROLE_ID = ?")) {
      statement.setLong(1, role.getId());
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

  private void checkNullRole(Role role) {
    if (null == role) {
      throw new RuntimeException("Null role is unacceptable");
    }
  }

  @Override
  public void create(Role role) {
    checkNullRole(role);
    if (checkIfExists(role)) {
      throw new RuntimeException("There already is role with id" + role.getId());
    }
    try (Connection connection = factory.createConnection();
        PreparedStatement statement = connection
            .prepareStatement("INSERT INTO ROLE (PK_ROLE_ID, NAME) VALUES (?, ?)")) {
      statement.setLong(1, role.getId());
      statement.setString(2, role.getName());
      statement.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(Role role) {
    checkNullRole(role);
    if (!checkIfExists(role)) {
      throw new RuntimeException("There is no role with id" + role.getId());
    }
    try (Connection connection = factory.createConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE ROLE SET NAME = ? WHERE PK_ROLE_ID = ?")) {
      statement.setString(1, role.getName());
      statement.setLong(2, role.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(Role role) {
    checkNullRole(role);
    if (!checkIfExists(role)) {
      throw new RuntimeException("There is no role with id" + role.getId());
    }
    try (Connection connection = factory.createConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM ROLE WHERE PK_ROLE_ID = ?")) {
      statement.setLong(1, role.getId());
      statement.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Role findByName(String name) {
    try (Connection connection = factory.createConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM ROLE WHERE NAME = ?")) {
      statement.setString(1, name);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          Role role = new Role();
          role.setId(rs.getLong("PK_ROLE_ID"));
          role.setName(rs.getString("NAME"));
          return role;
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Role findById(Long id) {
    try (Connection connection = factory.createConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM ROLE WHERE PK_ROLE_ID = ?")) {
      statement.setLong(1, id);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          Role role = new Role();
          role.setId(rs.getLong("PK_ROLE_ID"));
          role.setName(rs.getString("NAME"));
          return role;
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
