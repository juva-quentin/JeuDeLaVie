package org.quentin.gameoflife.config;

import static org.assertj.core.api.Assertions.assertThat;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataSourceConfigTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void dataSourceShouldBeConfigured() {
        assertThat(dataSource).isNotNull();
    }
}
