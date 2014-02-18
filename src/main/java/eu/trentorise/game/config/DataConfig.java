package eu.trentorise.game.config;


import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 *
 * @author Luca Piras
 */
@Configuration
@EnableJpaRepositories(basePackages = "eu.trentorise.game")
@EnableTransactionManagement //this one activates transaction management for method annotated by the transactional annotation, the same of <tx:annotation-driven />
public class DataConfig {
     
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        BasicDataSource bds = new BasicDataSource();
        
        bds.setDriverClassName(dbDriverClassName);
        bds.setUrl(dbUrl);
        bds.setUsername(dbUsername);
        bds.setPassword(dbPassword);
        
        return bds;
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPackagesToScan("eu.trentorise.game");
        return lef;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(false);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
    
    
    @Value("#{properties.db_driverClassName}")
    protected String dbDriverClassName;
    @Value("#{properties.db_url}")
    protected String dbUrl;
    @Value("#{properties.db_username}")
    protected String dbUsername;
    @Value("#{properties.db_password}")
    protected String dbPassword;
}