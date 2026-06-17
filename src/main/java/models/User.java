package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data model representing a user in the system under test.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    /**
     * Creates a default admin user for testing.
     */
    public static User defaultAdminUser() {
        return User.builder()
                .username("admin")
                .password("admin123")
                .email("admin@example.com")
                .firstName("Admin")
                .lastName("User")
                .build();
    }

    /**
     * Creates a default regular user for testing.
     */
    public static User defaultRegularUser() {
        return User.builder()
                .username("user")
                .password("user123")
                .email("user@example.com")
                .firstName("Regular")
                .lastName("User")
                .build();
    }
}
