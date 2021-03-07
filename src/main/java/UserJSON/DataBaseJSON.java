package UserJSON;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class DataBaseJSON
{
    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public User[] getUserList() {
        return userList;
    }

    public void setUserList(User[] userList) {
        this.userList = userList;
    }

    private long totalUsers;
    private String curDate;
    private User[] userList;
}
