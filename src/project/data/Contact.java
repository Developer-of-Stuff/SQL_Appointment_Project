package project.data;

/**
 * Contains all properties of a contact, also provides getters and setters for each.
 */
public class Contact {

    private int contactId;
    private String contactName;
    private String email;

    /**
     * @return contact ID
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

    /**
     * @return name of contact
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName - String
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return email of contact
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email - String
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
