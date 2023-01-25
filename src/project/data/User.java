package project.data;

import java.time.LocalDateTime;

/**
 * Contains all properties of a user, also provides getters and setters for each.
 */
public class User {

    private int userId;
    private String username;
    private String password;
    private LocalDateTime createTime;
    private String createdBy;
    private LocalDateTime lastUpdateTime;
    private String lastUpdateBy;

    /**
     * @return user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId - int
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return user name
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username - String
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password associated with the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password - String
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return datetime (UTC) user was created
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime - LocalDateTime
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the author's username
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy - String
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the time (UTC) the user object was last updated
     */
    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime - LocalDateTime
     */
    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return the username of the last user who updated the object
     */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /**
     * @param lastUpdateBy - String
     */
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
