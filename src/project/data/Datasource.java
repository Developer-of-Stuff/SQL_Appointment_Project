package project.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Hashtable;

/**
 * Responsible for all connections and queries to the MySQL database.
 */
public class Datasource {

    public static final String DB_NAME = "WJ06Y5u";
    public static final String USERNAME = "U06Y5u";
    public static final String PASSWORD = "53688902003";
    public static final String CONNECTION_STRING = "jdbc:mysql://wgudb.ucertify.com:3306/" + DB_NAME;

    public static final String TABLE_COUNTRIES = "countries";
    public static final int INDEX_COUNTRY_ID = 1;
    public static final int INDEX_COUNTRY_NAME = 2;
    public static final int INDEX_COUNTRY_CREATE_DATE = 3;
    public static final int INDEX_COUNTRY_CREATED_BY = 4;
    public static final int INDEX_COUNTRY_LAST_UPDATE_TIME = 5;
    public static final int INDEX_COUNTRY_LAST_UPDATE_BY = 6;

    public static final String TABLE_DIVISIONS = "first_level_divisions";
    public static final int INDEX_DIVISION_ID = 1;
    public static final int INDEX_DIVISION_NAME = 2;
    public static final int INDEX_DIVISION_CREATE_DATE = 3;
    public static final int INDEX_DIVISION_CREATED_BY = 4;
    public static final int INDEX_DIVISION_LAST_UPDATE_TIME = 5;
    public static final int INDEX_DIVISION_LAST_UPDATE_BY = 6;
    public static final int INDEX_DIVISION_COUNTRY_ID = 7;

    public static final String TABLE_CUSTOMERS = "customers";
    public static final int INDEX_CUSTOMER_ID = 1;
    public static final int INDEX_CUSTOMER_NAME = 2;
    public static final int INDEX_CUSTOMER_ADDRESS = 3;
    public static final int INDEX_CUSTOMER_POSTAL_CODE = 4;
    public static final int INDEX_CUSTOMER_PHONE = 5;
    public static final int INDEX_CUSTOMER_CREATE_DATE = 6;
    public static final int INDEX_CUSTOMER_CREATED_BY = 7;
    public static final int INDEX_CUSTOMER_LAST_UPDATE_TIME = 8;
    public static final int INDEX_CUSTOMER_LAST_UPDATE_BY = 9;
    public static final int INDEX_CUSTOMER_DIVISION_ID = 10;

    public static final String TABLE_APPOINTMENTS = "appointments";
    public static final int INDEX_APPOINTMENT_ID = 1;
    public static final int INDEX_APPOINTMENT_TITLE = 2;
    public static final int INDEX_APPOINTMENT_DESCRIPTION = 3;
    public static final int INDEX_APPOINTMENT_LOCATION = 4;
    public static final int INDEX_APPOINTMENT_TYPE = 5;
    public static final int INDEX_APPOINTMENT_START = 6;
    public static final int INDEX_APPOINTMENT_END = 7;
    public static final int INDEX_APPOINTMENT_CREATE_TIME = 8;
    public static final int INDEX_APPOINTMENT_CREATED_BY = 9;
    public static final int INDEX_APPOINTMENT_LAST_UPDATE_TIME = 10;
    public static final int INDEX_APPOINTMENT_LAST_UPDATE_BY = 11;
    public static final int INDEX_APPOINTMENT_CUSTOMER_ID = 12;
    public static final int INDEX_APPOINTMENT_USER_ID = 13;
    public static final int INDEX_APPOINTMENT_CONTACT_ID = 14;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_NAME = "User_Name";
    public static final String COLUMN_PASSWORD = "Password";
    public static final int INDEX_USER_ID = 1;
    public static final int INDEX_USER_NAME = 2;
    public static final int INDEX_USER_PASSWORD = 3;
    public static final int INDEX_USER_CREATE_TIME = 4;
    public static final int INDEX_USER_CREATED_BY = 5;
    public static final int INDEX_USER_LAST_UPDATE_TIME = 6;
    public static final int INDEX_USER_LAST_UPDATE_BY = 7;

    public static final String TABLE_CONTACTS = "contacts";
    public static final int INDEX_CONTACT_ID = 1;
    public static final int INDEX_CONTACT_NAME = 2;
    public static final int INDEX_CONTACT_EMAIL = 3;

    private static ObservableList<Customer> customerList;
    private static ObservableList<Country> countryList;
    private static ObservableList<Appointment> appointmentList;
    private static ObservableList<User> userList;
    private static ObservableList<Contact> contactList;
    private static final Hashtable<Integer, ObservableList<Division>> divisionListings = new Hashtable<>();

    private static Connection conn;
    private static User currentUser;

    /**
     * Opens the connection to the MySQL database.
     *
     * @return whether the connection was successfully established or not
     */
    public static boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
            System.out.println("Connection opened.");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Closes the connection to the MySQL database.
     */
    public static void close() {
        try {
            if (conn != null) {
                conn.close();
                currentUser = null;
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Loads the ObservableList userList with any users stored in the database for local access.
     */
    public static void loadUsers() {
        userList = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + TABLE_USERS;

        try (Statement statement = conn.createStatement(); ResultSet results = statement.executeQuery(query)) {
            while (results.next()) {
                User user = new User();
                user.setUserId(results.getInt(INDEX_USER_ID));
                user.setUsername(results.getString(INDEX_USER_NAME));
                user.setPassword(results.getString(INDEX_USER_PASSWORD));
                user.setCreateTime(results.getTimestamp(INDEX_USER_CREATE_TIME).toLocalDateTime());
                user.setCreatedBy(results.getString(INDEX_USER_CREATED_BY));
                user.setLastUpdateTime(results.getTimestamp(INDEX_USER_LAST_UPDATE_TIME).toLocalDateTime());
                user.setLastUpdateBy(results.getString(INDEX_USER_LAST_UPDATE_BY));

                userList.add(user);
            }
            System.out.println("Users loaded!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Takes a user's input and establishes whether those credentials fit a user stored in the database. If so, this will grant them access to the rest of the application.
     *
     * @param username - String : the input username
     * @param password - String : the input password
     * @return whether login was successful
     */
    public static boolean loginUser(String username, String password) {
        boolean loginSuccess = false;
        String loginQuery = "SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_NAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        try {
            PreparedStatement loginQueryStatement = conn.prepareStatement(loginQuery);
            loginQueryStatement.setString(1, username);
            loginQueryStatement.setString(2, password);
            ResultSet results = loginQueryStatement.executeQuery();

            if (results.isBeforeFirst()) {
                while (results.next()) {
                    System.out.println("User logged in!");
                    for (User user : userList) {
                        if (user.getUsername().equals(username)) {
                            currentUser = user;
                        }
                    }
                    loginSuccess = true;
                }
            }
            loginQueryStatement.close();
            results.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return loginSuccess;
    }

    /**
     * Retrieves the userList.
     *
     * @return the userList
     */
    public static ObservableList<User> getUserList() {
        return userList;
    }

    /**
     * Retrieves the current user of the application, if signed in.
     *
     * @return the current user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Retrieves a specific user, by ID, from the local userList. If the ID is not found, returns null.
     *
     * @param userId - int : the user ID in question
     * @return the retrieved user, or null
     */
    public static User getUser(int userId) {
        for (User user : userList) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    /**
     * Loads the ObservableList contactList with any contacts stored in the database for local access.
     */
    public static void loadContactList() {
        contactList = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + TABLE_CONTACTS;

        try (Statement statement = conn.createStatement(); ResultSet results = statement.executeQuery(query)) {
            while (results.next()) {
                Contact contact = new Contact();
                contact.setContactId(results.getInt(INDEX_CONTACT_ID));
                contact.setContactName(results.getString(INDEX_CONTACT_NAME));
                contact.setEmail(results.getString(INDEX_CONTACT_EMAIL));

                contactList.add(contact);
            }
            System.out.println("Contacts loaded!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves the contactList.
     *
     * @return the contactList
     */
    public static ObservableList<Contact> getContactList() {
        return contactList;
    }

    /**
     * Retrieves a specific contact, by ID, from the local contactList. If the ID is not found, returns null.
     *
     * @param contactId - int : the contact ID in question
     * @return the retrieved contact, or null
     */
    public static Contact getContact(int contactId) {
        for (Contact contact : contactList) {
            if (contact.getContactId() == contactId) {
                return contact;
            }
        }
        return null;
    }

    /**
     * Loads the ObservableList customerList with any customers stored in the database for local access.
     */
    public static void loadCustomerList() {
        customerList = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + TABLE_CUSTOMERS;

        try (Statement statement = conn.createStatement(); ResultSet results = statement.executeQuery(query)) {
            while (results.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(results.getInt(INDEX_CUSTOMER_ID));
                customer.setCustomerName(results.getString(INDEX_CUSTOMER_NAME));
                customer.setAddress(results.getString(INDEX_CUSTOMER_ADDRESS));
                customer.setPostalCode(results.getString(INDEX_CUSTOMER_POSTAL_CODE));
                customer.setPhone(results.getString(INDEX_CUSTOMER_PHONE));
                customer.setCreateDate(results.getTimestamp(INDEX_CUSTOMER_CREATE_DATE).toLocalDateTime());
                customer.setCreatedBy(results.getString(INDEX_CUSTOMER_CREATED_BY));
                customer.setLastUpdateTime(results.getTimestamp(INDEX_CUSTOMER_LAST_UPDATE_TIME).toLocalDateTime());
                customer.setLastUpdateBy(results.getString(INDEX_CUSTOMER_LAST_UPDATE_BY));
                customer.setDivisionId(results.getInt(INDEX_CUSTOMER_DIVISION_ID));

                Division customerDivision = getDivision(customer.getDivisionId());
                if (customerDivision != null) {
                    customer.setCountryId(customerDivision.getCountryId());
                }
                customerList.add(customer);
            }
            System.out.println("Customers loaded!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates a query to insert a new customer into the database, as well as adding that customer to the ObservableList customerList for local access.
     *
     * @param customer - Customer : the customer to be added
     */
    public static void addCustomer(Customer customer) {
        ZonedDateTime utcTime = DateConverter.convertSystemToUTC(LocalDateTime.now());
        String query = "INSERT INTO " + TABLE_CUSTOMERS + "(Customer_ID, Customer_Name, Address, Postal_Code, " +
                "Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement addCustomerQuery = conn.prepareStatement(query)) {
            addCustomerQuery.setInt(1, customer.getCustomerId());
            addCustomerQuery.setString(2, customer.getCustomerName());
            addCustomerQuery.setString(3, customer.getAddress());
            addCustomerQuery.setString(4, customer.getPostalCode());
            addCustomerQuery.setString(5, customer.getPhone());
            addCustomerQuery.setTimestamp(6, DateConverter.convertUTCToSQL(utcTime));
            addCustomerQuery.setString(7, currentUser.getUsername());
            addCustomerQuery.setTimestamp(8, DateConverter.convertUTCToSQL(utcTime));
            addCustomerQuery.setString(9, currentUser.getUsername());
            addCustomerQuery.setInt(10, customer.getDivisionId());
            customerList.add(customer);
            addCustomerQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Takes a customer record and updates it in the SQL database.
     *
     * @param customer - Customer : the customer to be updated
     */
    public static void updateCustomer(Customer customer) {
        ZonedDateTime utcTime = DateConverter.convertSystemToUTC(LocalDateTime.now());
        String query = "UPDATE " + TABLE_CUSTOMERS +
                " SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?" +
                ", Last_Update = ?, Last_Updated_By = ?, Division_ID = ?" +
                " WHERE Customer_ID = ?";

        try (PreparedStatement updateCustomerQuery = conn.prepareStatement(query)) {
            updateCustomerQuery.setString(1, customer.getCustomerName());
            updateCustomerQuery.setString(2, customer.getAddress());
            updateCustomerQuery.setString(3, customer.getPostalCode());
            updateCustomerQuery.setString(4, customer.getPhone());
            updateCustomerQuery.setTimestamp(5, DateConverter.convertUTCToSQL(utcTime));
            updateCustomerQuery.setString(6, currentUser.getUsername());
            updateCustomerQuery.setInt(7, customer.getDivisionId());
            updateCustomerQuery.setInt(8, customer.getCustomerId());
            for (int i = 0; i < customerList.size(); i++) {
                if (customerList.get(i).getCustomerId() == customer.getCustomerId()) {
                    customer.setLastUpdateTime(utcTime.toLocalDateTime());
                    customer.setLastUpdateBy(currentUser.getUsername());
                    customerList.set(i, customer);
                }
            }
            updateCustomerQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a customer record from the SQL database.
     *
     * @param customer - Customer : the customer to be deleted
     */
    public static void deleteCustomer(Customer customer) {
        String query = "DELETE FROM " + TABLE_CUSTOMERS + " WHERE Customer_ID = ?";
        try (PreparedStatement deleteCustomerQuery = conn.prepareStatement(query)) {
            deleteCustomerQuery.setInt(1, customer.getCustomerId());
            deleteCustomerQuery.executeUpdate();
            customerList.remove(customer);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves the customerList.
     *
     * @return the customerList
     */
    public static ObservableList<Customer> getCustomerList() {
        return customerList;
    }

    /**
     * Retrieves a specific customer, by ID, from the local customerList. If the ID is not found, returns null.
     *
     * @param customerId - int : customer ID
     * @return the customer in question
     */
    public static Customer getCustomer(int customerId) {
        for (Customer customer : customerList) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Loads the ObservableList appointmentList with any appointments stored in the database for local access.
     */
    public static void loadAppointmentList() {
        appointmentList = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + TABLE_APPOINTMENTS;

        try (Statement statement = conn.createStatement(); ResultSet results = statement.executeQuery(query)) {
            while (results.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(results.getInt(INDEX_APPOINTMENT_ID));
                appointment.setTitle(results.getString(INDEX_APPOINTMENT_TITLE));
                appointment.setDescription(results.getString(INDEX_APPOINTMENT_DESCRIPTION));
                appointment.setLocation(results.getString(INDEX_APPOINTMENT_LOCATION));
                appointment.setType(results.getString(INDEX_APPOINTMENT_TYPE));
                appointment.setStartTime(results.getTimestamp(INDEX_APPOINTMENT_START).toLocalDateTime());
                appointment.setEndTime(results.getTimestamp(INDEX_APPOINTMENT_END).toLocalDateTime());
                appointment.setCreateTime(results.getTimestamp(INDEX_APPOINTMENT_CREATE_TIME).toLocalDateTime());
                appointment.setCreatedBy(results.getString(INDEX_APPOINTMENT_CREATED_BY));
                appointment.setLastUpdateTime(results.getTimestamp(INDEX_APPOINTMENT_LAST_UPDATE_TIME).toLocalDateTime());
                appointment.setLastUpdateBy(results.getString(INDEX_APPOINTMENT_LAST_UPDATE_BY));
                appointment.setCustomerId(results.getInt(INDEX_APPOINTMENT_CUSTOMER_ID));
                appointment.setUserId(results.getInt(INDEX_APPOINTMENT_USER_ID));
                appointment.setContactId(results.getInt(INDEX_APPOINTMENT_CONTACT_ID));

                appointmentList.add(appointment);
            }
            System.out.println("Appointments loaded!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates a query to insert a new appointment into the database, as well as adding that appointment to the ObservableList appointmentList for local access.
     *
     * @param appointment - Appointment : the appointment to be added
     */
    public static void addAppointment(Appointment appointment) {
        ZonedDateTime utcTime = DateConverter.convertSystemToUTC(LocalDateTime.now());
        String query = "INSERT INTO " + TABLE_APPOINTMENTS + "(Appointment_ID, Title, Description, " +
                "Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, " +
                "Customer_ID, User_ID, Contact_ID) " +
                "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        try (PreparedStatement addAppointmentQuery = conn.prepareStatement(query)) {
            addAppointmentQuery.setInt(1, appointment.getAppointmentId());
            addAppointmentQuery.setString(2, appointment.getTitle());
            addAppointmentQuery.setString(3, appointment.getDescription());
            addAppointmentQuery.setString(4, appointment.getLocation());
            addAppointmentQuery.setString(5, appointment.getType());
            addAppointmentQuery.setTimestamp(6, DateConverter.convertUTCToSQL(ZonedDateTime.of(appointment.getStartTime(), ZoneId.of("UTC"))));
            addAppointmentQuery.setTimestamp(7, DateConverter.convertUTCToSQL(ZonedDateTime.of(appointment.getEndTime(), ZoneId.of("UTC"))));
            addAppointmentQuery.setTimestamp(8, DateConverter.convertUTCToSQL(utcTime));
            addAppointmentQuery.setString(9, currentUser.getUsername());
            addAppointmentQuery.setTimestamp(10, DateConverter.convertUTCToSQL(utcTime));
            addAppointmentQuery.setString(11, currentUser.getUsername());
            addAppointmentQuery.setInt(12, appointment.getCustomerId());
            addAppointmentQuery.setInt(13, appointment.getUserId());
            addAppointmentQuery.setInt(14, appointment.getContactId());
            appointmentList.add(appointment);
            addAppointmentQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Takes an appointment and updates it in the SQL database.
     *
     * @param appointment - Appointment : the appointment to be updated
     */
    public static void updateAppointment(Appointment appointment) {
        ZonedDateTime utcTime = DateConverter.convertSystemToUTC(LocalDateTime.now());
        String query = "UPDATE " + TABLE_APPOINTMENTS +
                " SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?" +
                ", End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?" +
                ", User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        
        try (PreparedStatement updateAppointmentQuery = conn.prepareStatement(query)) {
            updateAppointmentQuery.setString(1, appointment.getTitle());
            updateAppointmentQuery.setString(2, appointment.getDescription());
            updateAppointmentQuery.setString(3, appointment.getLocation());
            updateAppointmentQuery.setString(4, appointment.getType());
            updateAppointmentQuery.setTimestamp(5, DateConverter.convertUTCToSQL(ZonedDateTime.of(appointment.getStartTime(), ZoneId.of("UTC"))));
            updateAppointmentQuery.setTimestamp(6, DateConverter.convertUTCToSQL(ZonedDateTime.of(appointment.getEndTime(), ZoneId.of("UTC"))));
            updateAppointmentQuery.setTimestamp(7, DateConverter.convertUTCToSQL(utcTime));
            updateAppointmentQuery.setString(8, currentUser.getUsername());
            updateAppointmentQuery.setInt(9, appointment.getCustomerId());
            updateAppointmentQuery.setInt(10, appointment.getUserId());
            updateAppointmentQuery.setInt(11, appointment.getContactId());
            updateAppointmentQuery.setInt(12, appointment.getAppointmentId());
            for (int i = 0; i < appointmentList.size(); i++) {
                if (appointmentList.get(i).getAppointmentId() == appointment.getAppointmentId()) {
                    appointment.setLastUpdateTime(utcTime.toLocalDateTime());
                    appointment.setLastUpdateBy(currentUser.getUsername());
                    appointmentList.set(i, appointment);
                }
            }
            updateAppointmentQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes an appointment from the SQL database.
     *
     * @param appointment - Appointment : the appointment to be deleted
     */
    public static void deleteAppointment(Appointment appointment) {
        String query = "DELETE FROM " + TABLE_APPOINTMENTS + " WHERE Appointment_ID = ?";
        try (PreparedStatement deleteAppointmentQuery = conn.prepareStatement(query)) {
            deleteAppointmentQuery.setInt(1, appointment.getAppointmentId());
            deleteAppointmentQuery.executeUpdate();
            appointmentList.remove(appointment);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves the appointmentList.
     *
     * @return the appointmentList
     */
    public static ObservableList<Appointment> getAppointmentList() {
        return appointmentList;
    }

    /**
     * Loads the ObservableList countryList with any countries stored in the database for local access.
     */
    public static void loadCountryList() {
        countryList = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + TABLE_COUNTRIES;

        try (Statement statement = conn.createStatement(); ResultSet results = statement.executeQuery(query)) {
            while (results.next()) {
                Country country = new Country();
                country.setCountryId(results.getInt(INDEX_COUNTRY_ID));
                country.setCountryName(results.getString(INDEX_COUNTRY_NAME));
                country.setCreateDate(results.getTimestamp(INDEX_COUNTRY_CREATE_DATE).toLocalDateTime());
                country.setCreatedBy(results.getString(INDEX_COUNTRY_CREATED_BY));
                country.setLastUpdateTime(results.getTimestamp(INDEX_COUNTRY_LAST_UPDATE_TIME).toLocalDateTime());
                country.setLastUpdateBy(results.getString(INDEX_COUNTRY_LAST_UPDATE_BY));

                countryList.add(country);
            }
            System.out.println("Countries loaded!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves the countryList.
     *
     * @return the countryList
     */
    public static ObservableList<Country> getCountryList() {
        return countryList;
    }

    /**
     * Retrieves a specific country, by ID, from the local countryList. If the ID is not found, returns null.
     *
     * @param countryId - int : country ID
     * @return the country in question
     */
    public static Country getCountry(int countryId) {
        for (Country country : countryList) {
            if (country.getCountryId() == countryId) {
                return country;
            }
        }
        return null;
    }

    /**
     * Loads the HashTable divisionListings with lists of divisions by country for local access.
     */
    public static void loadDivisionListings() {
        String query = "SELECT * FROM " + TABLE_DIVISIONS;

        try (Statement statement = conn.createStatement(); ResultSet results = statement.executeQuery(query)) {
            while (results.next()) {
                ObservableList<Division> divisionList;
                Division division = new Division();
                division.setDivisionId(results.getInt(INDEX_DIVISION_ID));
                division.setDivisionName(results.getString(INDEX_DIVISION_NAME));
                division.setCreateDate(results.getTimestamp(INDEX_DIVISION_CREATE_DATE).toLocalDateTime());
                division.setCreatedBy(results.getString(INDEX_DIVISION_CREATED_BY));
                division.setLastUpdateTime(results.getTimestamp(INDEX_DIVISION_LAST_UPDATE_TIME).toLocalDateTime());
                division.setLastUpdateBy(results.getString(INDEX_DIVISION_LAST_UPDATE_BY));
                division.setCountryId(results.getInt(INDEX_DIVISION_COUNTRY_ID));

                if (divisionListings.get(division.getCountryId()) == null) {
                    divisionList = FXCollections.observableArrayList();
                    divisionListings.put(division.getCountryId(), divisionList);
                }

                divisionListings.get(division.getCountryId()).add(division);
            }
        } catch (SQLException e) {
            System.out.println("STUFF: " + e.getMessage());
        }
    }

    /**
     * Retrieves the divisionListings.
     *
     * @return the divisionListings
     */
    public static Hashtable<Integer, ObservableList<Division>> getDivisionListings() {
        return divisionListings;
    }

    /**
     * Retrieves a specific division, by ID, from the local divisionListings. If the ID is not found, returns null.
     *
     * @param divisionId - int : division ID
     * @return the division in question
     */
    public static Division getDivision(int divisionId) {
        for (ObservableList<Division> divisionList : divisionListings.values()) {
            for (Division division : divisionList) {
                if (division.getDivisionId() == divisionId) {
                    return division;
                }
            }
        }
        return null;
    }
}
