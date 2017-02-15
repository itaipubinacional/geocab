package br.com.geocab.application.configuration;

import br.com.geocab.infrastructure.jpa2.springdata.JpaRepositoryFactoryBean;
import com.jolbox.bonecp.BoneCPDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by lcvmelo on 14/02/2017.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "br.com.geocab.domain.repository",
        repositoryFactoryBeanClass = JpaRepositoryFactoryBean.class)
@PropertySource("classpath:db.properties")
public class DbConfig
{
    @Value("${jdbc.driverClassName}")
    private String driverClass;

    @Value("${jdbc.jdbcUrl}")
    private String jdbcUrl;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Value("${hibernate.hbm2ddl}")
    private String hbm2ddl;

    @Value("${hibernate.showSql}")
    private boolean showSql;

    @Value("${hibernate.formatSql}")
    private boolean formatSql;

    @Value("${hibernate.default_schema}")
    private String defaultSchema;

    @Value("${hibernate.envers.default_schema}")
    private String enversDefaultSchema;

    @Bean
    public DataSource dataSource() {
        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(this.driverClass);
        dataSource.setJdbcUrl(this.jdbcUrl);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);

        dataSource.setConnectionTestStatement("SELECT NOW()");
        dataSource.setIdleConnectionTestPeriodInMinutes(60);
        dataSource.setIdleMaxAgeInMinutes(240);
        dataSource.setMaxConnectionsPerPartition(30);
        dataSource.setMinConnectionsPerPartition(10);
        dataSource.setPartitionCount(3);
        dataSource.setAcquireIncrement(10);
        dataSource.setStatementsCacheSize(50);

        // adiciona um proxy para lazy loading
        LazyConnectionDataSourceProxy proxy = new LazyConnectionDataSourceProxy();
        proxy.setTargetDataSource(dataSource);
        return proxy;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setPersistenceUnitName("br.com.geocab");
        factory.setPackagesToScan("br.com.geocab.domain.entity");
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factory.setDataSource(dataSource);

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", this.hbm2ddl);
        properties.setProperty("hibernate.format_sql", String.valueOf(this.formatSql));
        properties.setProperty("hibernate.show_sql", String.valueOf(this.showSql));
        properties.setProperty("hibernate.dialect", "org.hibernate.spatial.dialect.postgis.PostgisDialect");
        properties.setProperty("hibernate.default_schema", this.defaultSchema);
        properties.setProperty("hibernate.ejb.naming_strategy", "br.com.geocab.infrastructure.jpa2.hibernate.NamingStrategy");

        properties.setProperty("org.hibernate.envers.audit_table_suffix", "_audited");
        properties.setProperty("org.hibernate.envers.revision_field_name", "revision");
        properties.setProperty("org.hibernate.envers.revision_type_field_name", "revision_type");
        properties.setProperty("org.hibernate.envers.default_schema", this.enversDefaultSchema);

        factory.setJpaProperties(properties);

        /*
         * Precisa chamar este método ou o spring ficará dando NullPointerException ao
         * tentar instanciar um repositório na chamada do getObject. Outra solução é retornar o objeto do tipo
         * LocalContainerEntityManagerFactoryBean em vez do retornado atualmente na definição
         * do bean.
         *
         * Ref:
         * - http://stackoverflow.com/questions/23565175/java-lang-nullpointerexception-upon-entitymanager-injection-in-spring-repository
         */
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
