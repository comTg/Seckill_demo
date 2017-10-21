package top.vetoer.entity;

public class User {
    private long userId;
    private long phone;
    private String name;
    private String password;

    public User() {
    }

    public User(long userId, long phone, String name, String password) {

        this.userId = userId;
        this.phone = phone;
        this.name = name;
        this.password = password;
    }

    public long getUserId() {

        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", phone=" + phone +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
