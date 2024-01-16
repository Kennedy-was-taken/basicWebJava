package com.myProgress.basicWeb.Config.Databases;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "userEntityManagerFactory", //ref to the entity manager
    transactionManagerRef = "userTransactionManager", //ref to the Transaction manager
    basePackages = { "com.myProgress.basicWeb.Repository.Db1" } //Repository location
)
public class UserDBDatasourceConfig {

    @Autowired
    Environment env;
    
    //Datasource Properties
    @Bean(name = "userProperties")
    @Primary
    @ConfigurationProperties("spring.datasource.db1")
    //automatically loads database related properties
    public DataSourceProperties dataSourceProperties(){
    
        return new DataSourceProperties();
    }

    //Datasource
    @Bean(name = "userDatasource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource dataSource(@Qualifier("userProperties") DataSourceProperties dataSourceProperties){

        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
    
    //EntityManager
    @Bean(name = "userEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
        (EntityManagerFactoryBuilder builder, @Qualifier("userDatasource") DataSource dataSource){

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        properties.put("spring.jpa.show-sql", "true");
        properties.put("spring.jpa.properties.hibernate.format_sql", "true");

        return builder.dataSource(dataSource)
                .packages("com.myProgress.basicWeb.Entities.Db1")
                .persistenceUnit("db1")
                .properties(properties)
                .build();
    }

    //TransactionManager
    @Bean(name = "userTransactionManager")
    @Primary
    @ConfigurationProperties("spring.jpa")
    public PlatformTransactionManager transactionManager
        (@Qualifier("userEntityManagerFactory") EntityManagerFactory entityManagerFactory){
            
        return new JpaTransactionManager(entityManagerFactory);
    }
}
