package com.fastcampus.pass;

import com.fastcampus.pass.config.TestBatchConfig;
import com.fastcampus.pass.job.pass.ExpirePassesJobConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {ExpirePassesJobConfig.class, TestBatchConfig.class})
class PassBatchApplicationTests {

	@Test
	void contextLoads() {
	}

}
