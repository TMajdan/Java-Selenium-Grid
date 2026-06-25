package api.requestmodel.accounts.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class AccountsModel {
    User user;
    String firstName;
    String lastName;
    String pesel;
    String id;
    String email;
    String dop;
    String doe;
    String debitCard;
    String smeAccountsChannel;
}