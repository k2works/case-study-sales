package com.example.sms.infrastructure.datasource.user;

public class Usr {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system.usr.user_id
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system.usr.first_name
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String firstName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system.usr.last_name
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String lastName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system.usr.password
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String password;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system.usr.role_name
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String roleName;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column system.usr.user_id
     *
     * @return the value of system.usr.user_id
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column system.usr.user_id
     *
     * @param userId the value for system.usr.user_id
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column system.usr.first_name
     *
     * @return the value of system.usr.first_name
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column system.usr.first_name
     *
     * @param firstName the value for system.usr.first_name
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName == null ? null : firstName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column system.usr.last_name
     *
     * @return the value of system.usr.last_name
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column system.usr.last_name
     *
     * @param lastName the value for system.usr.last_name
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void setLastName(String lastName) {
        this.lastName = lastName == null ? null : lastName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column system.usr.password
     *
     * @return the value of system.usr.password
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column system.usr.password
     *
     * @param password the value for system.usr.password
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column system.usr.role_name
     *
     * @return the value of system.usr.role_name
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column system.usr.role_name
     *
     * @param roleName the value for system.usr.role_name
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }
}