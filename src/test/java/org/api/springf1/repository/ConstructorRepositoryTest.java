package org.api.springf1.repository;

import org.api.springf1.model.Constructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
class ConstructorRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.5");

    @Autowired
    ConstructorRepository constructorRepository;

    static Constructor constructor, constructor2;
    @BeforeAll
    public static void setUp(){
        constructor = Constructor.builder().id(1l).ref("FER").name("Ferrari").nationality("Italian").build();
        constructor2 = Constructor.builder().id(2l).ref("MER").name("Mercedes").nationality("German").build();
    }

    @Test
    @Order(1)
    void shouldReturnMoreThanOneConstructorWhenSaveTwoConstructors(){
        constructorRepository.save(constructor);
        constructorRepository.save(constructor2);

        assertThat(constructorRepository.findAll().size()).isGreaterThan(1);
    }

    @Test
    @Order(2)
    void shouldReturnConstructorNotNullWhenFindByRef(){
        constructorRepository.save(constructor);
        assertNotNull(constructorRepository.findByRefIgnoreCase("FER").get());
    }

    @Test
    @Order(3)
    void shouldReturnConstructorNotNullWhenUpdateConstructor(){
        constructorRepository.save(constructor);
        constructor.setRef("FOR");
        constructorRepository.save(constructor);

        assertNotNull(constructorRepository.findByRefIgnoreCase("FOR").get());

    }

    @Test
    @Order(4)
    void shouldReturnNullConstructorWhenDelete() {
        constructorRepository.delete(constructor);

        assertNull(constructorRepository.findByRefIgnoreCase("FOR").orElse(null));
    }

    }