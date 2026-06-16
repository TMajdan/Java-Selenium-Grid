package main.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import main.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Factory class for creating test data objects.
 * Provides predefined test users and dynamic data generation.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestDataFactory {

    /**
     * Creates a predefined standard user for testing.
     */
    public static User createStandardUser() {
        return User.defaultRegularUser();
    }

    /**
     * Creates a predefined admin user for testing.
     */
    public static User createAdminUser() {
        return User.defaultAdminUser();
    }

    /**
     * Creates a random user using Faker.
     */
    public static User createRandomUser() {
        return DataFakerUtils.generateRandomUser();
    }

    /**
     * Creates a list of random users.
     *
     * @param count the number of users to create
     * @return a list of random users
     */
    public static List<User> createRandomUsers(int count) {
        List<User> users = new ArrayList<>(count);
        IntStream.range(0, count).forEach(i -> users.add(createRandomUser()));
        return users;
    }

    /**
     * Creates a user with a custom email.
     *
     * @param email the email to use
     * @return a User with the specified email
     */
    public static User createUserWithEmail(String email) {
        User base = createRandomUser();
        return base.toBuilder().email(email).build();
    }

    /**
     * Creates a user with custom credentials.
     *
     * @param username the username
     * @param password the password
     * @return a User with the specified credentials
     */
    public static User createUserWithCredentials(String username, String password) {
        User base = createRandomUser();
        return base.toBuilder()
                .username(username)
                .password(password)
                .email(username + "@example.com")
                .build();
    }

    /**
     * Creates a user with an invalid email format.
     */
    public static User createUserWithInvalidEmail() {
        User base = createRandomUser();
        return base.toBuilder().email("invalid-email").build();
    }

    /**
     * Creates a user with a very long name (boundary testing).
     */
    public static User createUserWithLongName() {
        String longName = "A".repeat(256);
        User base = createRandomUser();
        return base.toBuilder().firstName(longName).lastName(longName).build();
    }
}
