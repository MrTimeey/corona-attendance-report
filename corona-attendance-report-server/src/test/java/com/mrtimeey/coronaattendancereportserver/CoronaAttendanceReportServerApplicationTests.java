package com.mrtimeey.coronaattendancereportserver;

import com.mrtimeey.coronaattendancereportserver.rest.development.DevelopmentService;
import com.mrtimeey.coronaattendancereportserver.rest.development.ProdDevelopmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(SpringProfiles.NOSECURITY)
class CoronaAttendanceReportServerApplicationTests {

	@Autowired
	private DevelopmentService developmentService;

	@Test
	void contextLoads() {
		assertThat(developmentService)
				.isNotNull()
				.isInstanceOf(ProdDevelopmentService.class);
	}

}
