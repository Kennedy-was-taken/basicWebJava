package com.myProgress.basicWeb.Config.Databases;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "accountEntityManagerFactory", //ref to the entity manager
    transactionManagerRef = "accountTransactionManager", //ref to the Transaction manager
    basePackages = { "com.myProgress.basicWeb.Repository.Db2" } //Repository location
)
public class AccountDBDatasourceConfig {
    
    //Datasource Properties
    @Bean(name = "accountProperties")
    @ConfigurationProperties("spring.datasource.db2")
    //automatically loads database related properties
    public DataSourceProperties dataSourceProperties(){
        return new DataSourceProperties();
    }

    //Datasource
    @Bean(name = "accountDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public DataSource dataSource(@Qualifier("accountProperties") DataSourceProperties dataSourceProperties){

        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
    //EntityManager
    @Bean(name = "accountEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
        (EntityManagerFactoryBuilder builder, @Qualifier("accountDatasource") DataSource dataSource){

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        properties.put("spring.jpa.show-sql", "true");
        properties.put("spring.jpa.properties.hibernate.format_sql", "true");

        return builder.dataSource(dataSource)
                .packages("com.myProgress.basicWeb.Entities.Db2")
                .persistenceUnit("db2")
                .properties(properties)
                .build();
    }

    //TransactionManager
    @Bean(name = "accountTransactionManager")
    @ConfigurationProperties("spring.jpa")
    public PlatformTransactionManager transactionManager
        (@Qualifier("accountEntityManagerFactory") EntityManagerFactory entityManagerFactory){
            
        return new JpaTransactionManager(entityManagerFactory);
    }
}
