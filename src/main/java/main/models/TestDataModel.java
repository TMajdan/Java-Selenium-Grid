package main.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic test data model that can hold various test data scenarios.
 * Used for data-driven testing with TestNG DataProvider.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TestDataModel {

    private String testCaseId;
    private String testCaseName;
    private String testDataKey;
    private String testDataValue;
    private String expectedResult;
    private boolean isNegativeTestCase;
    private String description;
}
