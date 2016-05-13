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

  protected static interface Callback {
    Object makeSqlQuery(Connection connection, PreparedStatement statement) throws SQLException;
  }

  protected final Object executeCallback(Callback call) {
    Connection connection = factory.createConnection();
    PreparedStatement statement = null;
    try {
      return call.makeSqlQuery(connection, statement);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  protected Boolean checkRole(Role role) {
    return checkIfExists(role);
  }

  private Boolean checkIfExists(Role role) {
    return (Boolean) executeCallback(new Callback() {
      @Override
      public Object makeSqlQuery(Connection connection, PreparedStatement statement) throws SQLException {
        statement = connection.prepareStatement("SELECT * FROM ROLE WHERE PK_ROLE_ID = ?");
        statement.setLong(1, role.getId());

        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
          return true;
        }
        return false;
      }
    });
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
    executeCallback(new Callback() {
      @Override
      public Object makeSqlQuery(Connection connection, PreparedStatement statement) throws SQLException {
        statement = connection.prepareStatement("INSERT INTO ROLE (PK_ROLE_ID, NAME) VALUES (?, ?)");
        statement.setLong(1, role.getId());
        statement.setString(2, role.getName());
        statement.execute();
        return null;
      }
    });
  }

  @Override
  public void update(Role role) {
    checkNullRole(role);
    if (!checkIfExists(role)) {
      throw new RuntimeException("There is no role with id" + role.getId());
    }
    executeCallback(new Callback() {
      @Override
      public Object makeSqlQuery(Connection connection, PreparedStatement statement) throws SQLException {
        statement = connection.prepareStatement("UPDATE ROLE SET NAME = ? WHERE PK_ROLE_ID = ?");
        statement.setString(1, role.getName());
        statement.setLong(2, role.getId());
        statement.executeUpdate();
        return null;
      }
    });
  }

  @Override
  public void remove(Role role) {
    checkNullRole(role);
    if (!checkIfExists(role)) {
      throw new RuntimeException("There is no role with id" + role.getId());
    }
    executeCallback(new Callback() {
      @Override
      public Object makeSqlQuery(Connection connection, PreparedStatement statement) throws SQLException {
        statement = connection.prepareStatement("DELETE FROM ROLE WHERE PK_ROLE_ID = ?");
        statement.setLong(1, role.getId());
        statement.execute();
        return null;
      }
    });
  }

  @Override
  public Role findByName(String name) {
    return (Role) executeCallback(new Callback() {
      @Override
      public Object makeSqlQuery(Connection connection, PreparedStatement statement) throws SQLException {
        statement = connection.prepareStatement("SELECT * FROM ROLE WHERE NAME = ?");
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
          Role role = new Role();
          role.setId(rs.getLong("PK_ROLE_ID"));
          role.setName(rs.getString("NAME"));
          return role;
        }
        return null;
      }
    });
  }
}
