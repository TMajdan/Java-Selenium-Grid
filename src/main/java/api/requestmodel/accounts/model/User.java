package api.requestmodel.accounts.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class User {
    String nip;
    String phoneNumber;
    String password;
    String nik;
    String cifIndividual;
}