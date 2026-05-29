package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"aws.region=eu-west-1",
		"aws.s3.bucket-name=test-bucket",
		"aws.accessKeyId=test-access-key",
		"aws.secretKey=test-secret-key"
})
class VehicleInsuranceComplianceTrackerApplicationTests {

	@Test
	void contextLoads() {
	}

}
