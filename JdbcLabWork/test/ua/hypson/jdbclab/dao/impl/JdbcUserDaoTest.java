package ua.hypson.jdbclab.dao.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.junit.Test;

import ua.hypson.jdbclab.dao.interfaces.RoleDao;
import ua.hypson.jdbclab.dao.interfaces.UserDao;
import ua.hypson.jdbclab.entity.Role;
import ua.hypson.jdbclab.entity.User;
import ua.hypson.jdbclab.factory.ConnectionFactory;

public class JdbcUserDaoTest extends DBTestCase {

  private UserDao userDao;
  private RoleDao roleDao;
  private Properties connectionProperties;

  @Override
  protected IDataSet getDataSet() throws Exception {

    return new FlatXmlDataSetBuilder().build(new File("resources/preset.xml"));
  }

  static {
    ConnectionFactory.setPropertiesPath("resources/test.properties");
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

  public JdbcUserDaoTest(String name) {
    super(name);
    userDao = new JdbcUserDao();
    roleDao = new JdbcRoleDao();
    connectionProperties = ConnectionFactory.loadProperties();
    System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
        connectionProperties.getProperty("driver"));
    System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
        connectionProperties.getProperty("url"));
    System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, connectionProperties.getProperty("username"));
    System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, connectionProperties.getProperty("password"));
  }

  @Override
  protected void setUpDatabaseConfig(DatabaseConfig config) {
    config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
  }

  @Test
  public void testUserDao() throws Exception {
    User extractedUser = userDao.findByEmail("igor@igor");
    assertEquals("Igorek", extractedUser.getFirstName());
    assertEquals(10L, extractedUser.getId().longValue());
    assertEquals("igor", extractedUser.getLogin());
    assertEquals("12345", extractedUser.getPassword());
    assertEquals("Nikolaev", extractedUser.getLastName());
    assertEquals("1961-02-20", extractedUser.getBirthday().toString());

    extractedUser = userDao.findByLogin("igor");
    assertEquals(10L, (long) extractedUser.getId());
    assertEquals("Igorek", extractedUser.getFirstName());
    @SuppressWarnings("deprecation")
    User addingUser = User.createUser(50L, "gri", "poiu", "grisha2000@mail.ru", "Grigoriy", "Skovoroda",
        new Date(1920, 7, 13));

    userDao.create(addingUser);

    User extractedCreatedUser = userDao.findByLogin("gri");
    assertEquals("Skovoroda", extractedCreatedUser.getLastName());

    addingUser.setFirstName("Svetlana");
    userDao.update(addingUser);
    extractedCreatedUser = userDao.findByLogin("gri");
    assertEquals("Svetlana", extractedCreatedUser.getFirstName());

    userDao.remove(addingUser);
    extractedCreatedUser = userDao.findByLogin("gri");
    assertNull(extractedCreatedUser);

    Role extractedRole = userDao.getUserRole(extractedUser);
    assertEquals("guest", extractedRole.getName());

  }

  @Test
  public void testRoleOfUser() throws Exception {
    User user = userDao.findByLogin("vasya");
    Role role = roleDao.findByName("admin");
    userDao.setUserRole(user, role);

    IDataSet databaseDataSet = getConnection().createDataSet();
    ITable actualTable = databaseDataSet.getTable("USER");

    IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("resources/afterset.xml"));
    ITable expectedTable = expectedDataSet.getTable("USER");

    Assertion.assertEquals(expectedTable, actualTable);
  }

}
