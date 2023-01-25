package project.data;

import java.time.LocalDateTime;

/**
 * Contains all the properties of a customer, also provides getters and setters for each.
 */
public class Customer {

    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdateTime;
    private String lastUpdateBy;
    private int countryId;
    private int divisionId;

    /**
     * @return customer ID
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
     * @return name of customer
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName - String
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return address of the customer
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address - String
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return postal code of the customer
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode - String
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return customer's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone - String
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the datetime (UTC) the customer object was created
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
     * @return the time (UTC) the customer object was last updated
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
     * @return the username of the person who last updated the object
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
     * @return country ID of the customer
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
     * @return division ID of the customer
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
}
