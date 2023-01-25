package project.data;

import java.time.LocalDateTime;

/**
 * Contains all the properties of a division, also provides getters and setters for each.
 */
public class Division {

    private int divisionId;
    private String divisionName;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdateTime;
    private String lastUpdateBy;
    private int countryId;

    /**
     * @return division ID
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * @param divisionId - int
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * @return name of division
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * @param divisionName - String
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * @return datetime (UTC) division object was created
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate - LocalDateTime
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
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
     * @return the time (UTC) the division object was last updated
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

    /**
     * @return country ID
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * @param countryId - int
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
