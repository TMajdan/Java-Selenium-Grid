package general.requestmodel;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class temp {

    private final RequestModelPOC requestModel = new RequestModelPOC();

    public ValidatableResponse details(RequestSpecification rs, String messageId) {
        return requestModel.getRequest(rs, "/panel/communications/", messageId);
    }

    // ========== Application Controller ==========

    public ValidatableResponse createApplication(RequestSpecification rs, String requestBody) {
        return requestModel.postRequest(rs, requestBody, "/application");
    }

    public ValidatableResponse updateApplication(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId);
    }

    public ValidatableResponse getApplicationStatus(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/status");
    }

    public ValidatableResponse updateApplicationStatus(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/status");
    }

    // ========== Verifications Controller ==========

    public ValidatableResponse createUserDataVerification(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/user-data/verifications");
    }

    // ========== User Data Personal Controller ==========

    public ValidatableResponse getUserDataPersonal(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/user-data/personal");
    }

    public ValidatableResponse createUserDataPersonal(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/user-data/personal");
    }

    public ValidatableResponse createUserDataPersonalFolder(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/user-data/personal/folder");
    }

    public ValidatableResponse updateUserDataPersonalAdditional(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/user-data/personal/additional");
    }

    // ========== User Data Company Controller ==========

    public ValidatableResponse createUserDataCompanyFolder(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/user-data/company/folder");
    }

    public ValidatableResponse getUserDataCompany(RequestSpecification rs, String appId) {
        return requestModel.getRequest(rs, "/application/", appId + "/user-data/company");
    }

    public ValidatableResponse updateUserDataCompany(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/user-data/company");
    }

    // ========== Sign Controller ==========

    public ValidatableResponse signDocument(RequestSpecification rs, String requestBody, String appId, String type) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/documents/" + type + "/sign");
    }

    public ValidatableResponse confirmDocumentSign(RequestSpecification rs, String requestBody, String appId, String type) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/documents/" + type + "/sign");
    }

    public ValidatableResponse sendNewSignSms(RequestSpecification rs, String requestBody, String appId, String type) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/documents/" + type + "/sign/new-sms");
    }

    // ========== Contract Template Controller ==========

    public ValidatableResponse createContractTemplate(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.postRequest(rs, requestBody, "/application/" + appId + "/contract-template");
    }

    // ========== Products Controller ==========

    public ValidatableResponse updateDebitCardProduct(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/products/debit-card");
    }

    // ========== Survey Controller ==========

    public ValidatableResponse updateSurvey(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/participants/survey");
    }

    public ValidatableResponse updateSurveyAdditional(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/participants/survey/additional");
    }

    // ========== Agreements Controller ==========

    public ValidatableResponse updateAgreements(RequestSpecification rs, String requestBody, String appId) {
        return requestModel.patchRequest(rs, requestBody, "/application/" + appId + "/participants/agreements");
    }

    // ========== Health Check Controller ==========

    public ValidatableResponse healthcheck(RequestSpecification rs) {
        return requestModel.getRequest(rs, "/healthcheck", "");
    }

    // ========== Dictionary Controller ==========

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