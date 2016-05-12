package ua.hypson.jdbclab.entity;

import java.util.Date;

public class User {

  private Long id;
  private String login;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private Date birthday;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(final Date birthday) {
    this.birthday = (Date) birthday.clone();
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", login=" + login + ", password=" + password + ", email=" + email + ", firstName="
        + firstName + ", lastName=" + lastName + ", birthday=" + birthday + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((birthday == null) ? 0 : birthday.hashCode());
    result = (prime * result) + ((email == null) ? 0 : email.hashCode());
    result = (prime * result) + ((firstName == null) ? 0 : firstName.hashCode());
    result = (prime * result) + ((id == null) ? 0 : id.hashCode());
    result = (prime * result) + ((lastName == null) ? 0 : lastName.hashCode());
    result = (prime * result) + ((login == null) ? 0 : login.hashCode());
    result = (prime * result) + ((password == null) ? 0 : password.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    User other = (User) obj;
    if (birthday == null) {
      if (other.birthday != null) {
        return false;
      }
    } else if (!birthday.equals(other.birthday)) {
      return false;
    }
    if (email == null) {
      if (other.email != null) {
        return false;
      }
    } else if (!email.equals(other.email)) {
      return false;
    }
    if (firstName == null) {
      if (other.firstName != null) {
        return false;
      }
    } else if (!firstName.equals(other.firstName)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (lastName == null) {
      if (other.lastName != null) {
        return false;
      }
    } else if (!lastName.equals(other.lastName)) {
      return false;
    }
    if (login == null) {
      if (other.login != null) {
        return false;
      }
    } else if (!login.equals(other.login)) {
      return false;
    }
    if (password == null) {
      if (other.password != null) {
        return false;
      }
    } else if (!password.equals(other.password)) {
      return false;
    }
    return true;
  }

}
