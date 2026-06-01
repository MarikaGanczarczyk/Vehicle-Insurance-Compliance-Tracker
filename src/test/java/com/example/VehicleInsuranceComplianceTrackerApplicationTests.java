package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.cloud.aws.region.static=eu-west-1",
		"spring.cloud.aws.credentials.access-key=test-access-key",
		"spring.cloud.aws.credentials.secret-key=test-secret-key",
		"aws.s3.bucket-name=test-bucket",
		"spring.cloud.aws.sqs.enabled=true",
		"spring.cloud.aws.stack.auto=false"
})
class VehicleInsuranceComplianceTrackerApplicationTests {

	@Test
	void contextLoads() {
	}

}
