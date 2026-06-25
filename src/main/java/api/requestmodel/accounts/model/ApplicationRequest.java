package api.requestmodel.accounts.model;

import com.github.javafaker.Faker;
import general.interfaces.JsonObject;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ApplicationRequest implements JsonObject {

    String phonePrefix;
    String phoneNumber;
    String email;

    private static final Faker FAKER = new Faker();

    public static ApplicationRequestBuilder from(String phonePrefix, String phoneNumber) {
        return new ApplicationRequestBuilder()
                .phonePrefix(phonePrefix)
                .phoneNumber(phoneNumber)
                .email(FAKER.internet().emailAddress());
    }

    public static ApplicationRequestBuilder from(String phonePrefix, String phoneNumber, String email) {
        return new ApplicationRequestBuilder()
                .phonePrefix(phonePrefix)
                .phoneNumber(phoneNumber)
                .email(email);
    }
}