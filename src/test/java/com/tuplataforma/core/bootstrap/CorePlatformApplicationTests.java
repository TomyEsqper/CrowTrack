package com.tuplataforma.core.bootstrap;

import com.tuplataforma.core.infrastructure.persistence.repository.JpaTenantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class})
class CorePlatformApplicationTests {

    @MockBean
    private JpaTenantRepository jpaTenantRepository;

    @Test
    void contextLoads() {
    }

}
