package ua.hypson.jdbclab.dao.interfaces;

import ua.hypson.jdbclab.entity.Role;

public interface RoleDao {

  public void create(Role role);

  public void update(Role role);

  public void remove(Role role);

  public Role findByName(String name);

}
