package com.security.tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Security Test Suite")
@SelectClasses({
    PickupApiSecurityTest.class,
    BankInfoApiSecurityTest.class,
    ForgetPasswordApiSecurityTest.class
})
public class SecurityTestSuite {
    // This class is used as a test suite runner
}
