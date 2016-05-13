package ua.hypson.jdbclab.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

import ua.hypson.jdbclab.dao.interfaces.AbstractJdbcDao;

public class ConnectionFactory extends AbstractJdbcDao {

  private static BasicDataSource dataSource;
  private static Properties properties;
  private static String propertiesPath;
  private static ConnectionFactory factory;

  public static void setPropertiesPath(String path) {
    propertiesPath = path;
  }

  public static ConnectionFactory getFactory() {
    if (factory == null) {
      factory = new ConnectionFactory();
    }
    return factory;
  }

  public static Properties loadProperties() {

    try (InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(propertiesPath)),
        StandardCharsets.UTF_8)) {
      Properties prop = new Properties();
      prop.load(reader);
      return prop;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void initializeDataSource() {
    properties = loadProperties();
    dataSource = new BasicDataSource();
    dataSource.setDriverClassName(properties.getProperty("driver"));
    dataSource.setUsername(properties.getProperty("username"));
    dataSource.setPassword(properties.getProperty("password"));
    dataSource.setUrl(properties.getProperty("url"));
  }

  @Override
  public Connection createConnection() {
    if (dataSource == null) {
      initializeDataSource();
    }
    try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
