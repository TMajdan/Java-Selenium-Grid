package main.utils;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import main.models.User;

import java.util.Locale;

/**
 * Utility class for generating realistic test data using JavaFaker.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataFakerUtils {

    private static final Faker FAKER = new Faker(Locale.US);

    /**
     * Generates a random user with realistic data.
     *
     * @return a randomly generated User
     */
    public static User generateRandomUser() {
        return User.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .email(FAKER.internet().emailAddress())
                .username(FAKER.name().username())
                .password(FAKER.internet().password(8, 16, true, true, true))
                .phoneNumber(FAKER.phoneNumber().cellPhone())
                .address(FAKER.address().streetAddress())
                .city(FAKER.address().city())
                .state(FAKER.address().state())
                .zipCode(FAKER.address().zipCode())
                .country(FAKER.address().country())
                .build();
    }

    /**
     * Generates a random email address.
     *
     * @return a random email
     */
    public static String generateEmail() {
        return FAKER.internet().emailAddress();
    }

    /**
     * Generates a random username.
     *
     * @return a random username
     */
    public static String generateUsername() {
        return FAKER.name().username();
    }

    /**
     * Generates a random password with configurable strength.
     *
     * @param minLength minimum length
     * @param maxLength maximum length
     * @param includeUppercase include uppercase letters
     * @param includeSpecial include special characters
     * @return a random password
     */
    public static String generatePassword(int minLength, int maxLength,
                                          boolean includeUppercase, boolean includeSpecial) {
        return FAKER.internet().password(minLength, maxLength, includeUppercase, includeSpecial);
    }

    /**
     * Generates a random phone number.
     *
     * @return a random phone number
     */
    public static String generatePhoneNumber() {
        return FAKER.phoneNumber().cellPhone();
    }

    /**
     * Generates a random street address.
     *
     * @return a random address
     */
    public static String generateAddress() {
        return FAKER.address().streetAddress();
    }

    /**
     * Generates a random company name.
     *
     * @return a random company name
     */
    public static String generateCompanyName() {
        return FAKER.company().name();
    }

    /**
     * Generates a random credit card number.
     *
     * @return a random credit card number
     */
    public static String generateCreditCard() {
        return FAKER.finance().creditCard();
    }

    /**
     * Generates a random sentence (lorem ipsum).
     *
     * @param wordCount the number of words
     * @return a random sentence
     */
    public static String generateSentence(int wordCount) {
        return FAKER.lorem().sentence(wordCount);
    }

    /**
     * Generates a random paragraph.
     *
     * @param sentenceCount the number of sentences
     * @return a random paragraph
     */
    public static String generateParagraph(int sentenceCount) {
        return FAKER.lorem().paragraph(sentenceCount);
    }

    /**
     * Returns the underlying Faker instance for custom usage.
     *
     * @return the Faker instance
     */
    public static Faker getFaker() {
        return FAKER;
    }
}
