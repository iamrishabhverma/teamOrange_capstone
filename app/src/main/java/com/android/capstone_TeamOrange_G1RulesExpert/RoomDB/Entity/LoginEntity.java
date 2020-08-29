package com.android.capstone_TeamOrange_G1RulesExpert.RoomDB.Entity;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//The Entity is tha table structure of our database
// One PrimaryKey is mandatory
@Entity
public class LoginEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "login_id")
    private String login_id;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "isAutoLoginEnabled")
    private boolean isAutoLoginEnabled;

    @ColumnInfo(name = "countryCode")
    private String countryCode;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAutoLoginEnabled() {
        return isAutoLoginEnabled;
    }

    public void setAutoLoginEnabled(boolean autoLoginEnabled) {
        isAutoLoginEnabled = autoLoginEnabled;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginEntity)) return false;

        LoginEntity that = (LoginEntity) o;

        if (getId() != that.getId()) return false;
        if (isAutoLoginEnabled() != that.isAutoLoginEnabled()) return false;
        if (getLogin_id() != null ? !getLogin_id().equals(that.getLogin_id()) : that.getLogin_id() != null)
            return false;
        if (getPassword() != null ? !getPassword().equals(that.getPassword()) : that.getPassword() != null)
            return false;
        return getCountryCode() != null ? getCountryCode().equals(that.getCountryCode()) : that.getCountryCode() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getLogin_id() != null ? getLogin_id().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = 31 * result + (isAutoLoginEnabled() ? 1 : 0);
        result = 31 * result + (getCountryCode() != null ? getCountryCode().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LoginEntity{" +
                "id=" + id +
                ", login_id='" + login_id + '\'' +
                ", password='" + password + '\'' +
                ", isAutoLoginEnabled=" + isAutoLoginEnabled +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
