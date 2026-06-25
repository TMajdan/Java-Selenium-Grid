package java.api.requestmodel.accounts;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class DictionaryClient {

    private final RequestModelPOC requestModel = new RequestModelPOC();

    public ValidatableResponse getSurveyDictionaries(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/dictionaries/survey");
    }

    public ValidatableResponse getStreetTypeDictionaries(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/dictionaries/street-type");
    }

    public ValidatableResponse getPromotionsDictionaries(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/dictionaries/promotions");
    }

    public ValidatableResponse getDebitCardsDictionaries(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/dictionaries/debit-cards");
    }

    public ValidatableResponse getCountriesDictionaries(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/dictionaries/countries");
    }

    public ValidatableResponse getAgreementsDictionaries(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/dictionaries/agreements");
    }
}
