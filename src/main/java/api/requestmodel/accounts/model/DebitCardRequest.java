package api.requestmodel.accounts.model;

import com.github.javafaker.Faker;
import general.interfaces.JsonObject;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class DebitCardRequest implements JsonObject {

    String key;

    private static final Faker FAKER = new Faker();

    public static DebitCardRequestBuilder from(String cardKey) {
        return new DebitCardRequestBuilder()
                .key(cardKey != null && !cardKey.isBlank() ? cardKey : FAKER.lorem().word());
    }
}