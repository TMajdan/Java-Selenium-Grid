package tests.api;

import api.logger.TestLifecycleLogger;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;

@Slf4j
@Listeners({TestLifecycleLogger.class})
public class SimpleApiTest {
}