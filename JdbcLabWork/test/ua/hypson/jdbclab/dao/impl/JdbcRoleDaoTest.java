package ua.hypson.jdbclab.dao.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.junit.Test;

import ua.hypson.jdbclab.dao.interfaces.RoleDao;
import ua.hypson.jdbclab.entity.Role;
import ua.hypson.jdbclab.factory.ConnectionFactory;

public class JdbcRoleDaoTest extends DBTestCase {
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

  public JdbcRoleDaoTest(String name) {
    super(name);
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
  public void testRoleDao() throws Exception {
    Role extractedRole = roleDao.findByName("admin");
    assertEquals(1L, extractedRole.getId().longValue());

    extractedRole.setName("president");
    roleDao.update(extractedRole);
    extractedRole = roleDao.findByName("president");
    assertEquals(1L, extractedRole.getId().longValue());
  }

}
