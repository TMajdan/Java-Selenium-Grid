package api.requestmodel.accounts.model;

import com.github.javafaker.Faker;
import general.interfaces.JsonObject;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CompanyDataRequest implements JsonObject {

    String nip;

    private static final Faker FAKER = new Faker();

    public static CompanyDataRequestBuilder from(String nip) {
        return new CompanyDataRequestBuilder()
                .nip(nip != null && !nip.isBlank() ? nip : FAKER.regexify("\\d{10}"));
    }
}