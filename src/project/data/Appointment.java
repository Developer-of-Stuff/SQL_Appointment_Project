package project.data;

import java.time.LocalDateTime;

/**
 * Contains all properties of each appointment, and provides getters and setters for each.
 */
public class Appointment {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
    private String createdBy;
    private LocalDateTime lastUpdateTime;
    private String lastUpdateBy;
    private int customerId;
    private int userId;
    private int contactId;

    /**
     * @return appointment ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * @param appointmentId - int
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * @return title of appointment
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title - String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return description of appointment
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description - String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return location of appointment
     */
    public String getLocation() {
        return location;
    }

    /**
      * @param location - String
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return type of appointment
     */
    public String getType() {
        return type;
    }

    /**
     * @param type - String
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return start time (UTC) of appointment
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * @param startTime - LocalDateTime
     */
    public void setStartTime (LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @return end time (UTC) of appointment
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * @param endTime - LocalDateTime
     */
    public void setEndTime (LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * @return creation time (UTC) of appointment
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
     * @return author's username
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
     * @return time (UTC) of last update
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
     * @return username of the last user to update appointment
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

    /**
     * @return customer ID associated with appointment
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId - int
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return user ID associated with appointment
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
     * @return contact associated with appointment
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @param contactId - int
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
