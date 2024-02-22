package org.api.springf1.repository;

import org.api.springf1.model.Driver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class DriverRepositoryTest {
    @Autowired
    DriverRepository driverRepository;

    Driver driver;

    @BeforeEach
    public void setUp(){
        driver = Driver.builder().id(1l).code("AAA").forename("Ayrton").surname("Senna").build();
    }

    @Test
    void shouldReturnSavedDriverWhenSave(){
        //Given
        //When
        Driver savedDriver = driverRepository.save(driver);

        //Then
        assertThat(savedDriver).isNotNull();
        assertThat(savedDriver.getId()).isGreaterThan(0);
    }

    @Test
    void shouldReturnMoreThanOneDriverWhenSaveTwoDrivers(){
        driverRepository.save(driver);
        driverRepository.save(Driver.builder().id(2l).code("BBB").forename("Michael").surname("Schumacher").build());

        List<Driver> drivers = driverRepository.findAll();

        assertThat(drivers).isNotNull();
        assertThat(drivers.size()).isGreaterThan(1);
        assertEquals(drivers.size(), 2);
    }

    @Test
    void shouldReturnDriverNotNullWhenFindByCode(){
        driverRepository.save(driver);
        assertNotNull(driverRepository.findByCodeIgnoreCase("AAA").get());
    }

    @Test
    void shouldReturnDriverNotNullWhenUpdateDriver(){
        driverRepository.save(driver);
        Driver actualizado = driverRepository.findByCodeIgnoreCase("AAA").get();
        actualizado.setForename("Lewis");
        driverRepository.save(actualizado);

        assertNotNull(driverRepository.findByCodeIgnoreCase("AAA"));
        assertEquals(driverRepository.findByCodeIgnoreCase("AAA").get().getForename(), "Lewis");
    }

    @Test
    void shouldReturnNullDriverWhenDelete(){
        driverRepository.save(driver);
        driverRepository.deleteByCodeIgnoreCase("AAA");
        Driver driver = driverRepository.findByCodeIgnoreCase("AAA").orElse(null);
        assertThat(driver).isNull();
    }

}