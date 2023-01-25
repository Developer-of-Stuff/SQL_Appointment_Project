package project.data;

import java.time.LocalDateTime;

/**
 * Contains all properties of a country, also provides getters and setters for each.
 */
public class Country {

    private int countryId;
    private String countryName;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdateTime;
    private String lastUpdateBy;

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

    /**
     * @return name of country
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * @param countryName - String
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * @return datetime (UTC) country object was created
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
     * @return the time (UTC) the country object was last updated
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
