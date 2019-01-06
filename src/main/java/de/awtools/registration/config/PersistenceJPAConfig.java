package de.awtools.registration.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource({"file:${user.home}/.register.properties"})
@EnableTransactionManagement
@ComponentScan("de.awtools.registration")
@EnableJpaRepositories(basePackages = { "de.awtools.registration" })
public class PersistenceJPAConfig {

    // TODO Move to another Configuration class?
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "de.awtools.registration" });

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        // dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        // dataSource.setUrl("jdbc:mysql://192.168.99.100:3307/school");
        dataSource.setUrl("jdbc:mariadb://192.168.99.100:3308/register");
        dataSource.setUsername("register");
        dataSource.setPassword("register");

        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("autocommit", "false");
        dataSource.setConnectionProperties(connectionProperties);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory emf) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect",
                "org.hibernate.dialect.MariaDB102Dialect");
        return properties;
    }

}
