package ua.hypson.jdbclab.dao.interfaces;

import java.sql.Connection;

public abstract class AbstractJdbcDao {

  public abstract Connection createConnection();

}
