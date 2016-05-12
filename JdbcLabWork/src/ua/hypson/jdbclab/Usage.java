package ua.hypson.jdbclab;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.dbunit.DatabaseUnitException;
import org.h2.tools.Server;

import ua.hypson.jdbclab.dao.impl.JdbcRoleDao;
import ua.hypson.jdbclab.dao.impl.JdbcUserDao;
import ua.hypson.jdbclab.dao.interfaces.RoleDao;
import ua.hypson.jdbclab.dao.interfaces.UserDao;
import ua.hypson.jdbclab.entity.Role;
import ua.hypson.jdbclab.entity.User;
import ua.hypson.jdbclab.factory.ConnectionFactory;
import ua.hypson.jdbclab.factory.UserFactory;

public class Usage {

  private static void createTables() throws DatabaseUnitException, FileNotFoundException, IOException {
    ConnectionFactory factory = new ConnectionFactory();
    Connection conn = factory.createConnection();
    Statement stmt;
    try {
      stmt = conn.createStatement();
      stmt.execute("DROP TABLE IF EXISTS ROLE");
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS ROLE " + "(PK_ROLE_ID BIGINT PRIMARY KEY, NAME VARCHAR(255) NOT NULL UNIQUE)");
      stmt.execute("DROP TABLE IF EXISTS USER");
      stmt.execute("CREATE TABLE IF NOT EXISTS USER "
          + "(PK_USER_ID BIGINT PRIMARY KEY, LOGIN VARCHAR(255) NOT NULL UNIQUE, PASSWORD VARCHAR(255) NOT NULL, EMAIL VARCHAR(255) NOT NULL UNIQUE,"
          + " FIRSTNAME VARCHAR(255), LASTNAME VARCHAR(255), BIRTHDAY DATE, FK_ROLE_ID BIGINT)");

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  public static void main(String[] args) throws FileNotFoundException, DatabaseUnitException, IOException {
    Server server = null;
    try {
      server = Server.createTcpServer().start();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    ConnectionFactory.setPropertiesPath("resources/h2.properties");
    createTables();

    RoleDao roleDao = new JdbcRoleDao();
    Role role = new Role();
    role.setId(20L);
    role.setName("pti4ka");
    roleDao.create(role);
    @SuppressWarnings("deprecation")
    Date date = new Date(1987, 5, 9);
    User user2 = UserFactory.createUser(20L, "pipka221", "kota", "lexus212@co.uk", "je", "Sus", date);

    UserDao userDao = new JdbcUserDao();
    userDao.create(user2);
    // List<User> list = userDao.findAll();
    // System.out.println(list);
    // }
    System.out.println(userDao.findByEmail("jesus@co.uk"));
    System.out.println(userDao.findAll());
    userDao.setUserRole(user2, role);
    System.out.println(userDao.getUserRole(user2));
    System.out.println(roleDao.findByName("pti4ka"));
    Role fetchedRole = roleDao.findByName("pti4ka");
    User fetchedUser = userDao.findByLogin("pipka11");
    System.out.println(fetchedRole);
    System.out.println(fetchedUser);
    server.stop();
  }
}
