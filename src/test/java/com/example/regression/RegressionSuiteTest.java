package com.example.regression;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        com.example.service.UserServiceTest.class,
        com.example.repository.UserRepositoryTest.class,
        com.example.controller.UserControllerIntegrationTest.class,
        com.example.ui.UserFlowUiTest.class
})
public class RegressionSuiteTest {
}