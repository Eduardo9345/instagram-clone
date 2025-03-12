package br.edu.ifpb.instagram.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import br.edu.ifpb.instagram.model.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private UserEntity user;
    private UserEntity user2;

    @BeforeEach
    public void setUp() {

        user = new UserEntity();
        user.setUsername("testUser");
        user.setFullName("Test User");
        user.setEmail("testuser@example.com");
        user.setEncryptedPassword("testPassword");

        user2 = new UserEntity();
        user2.setUsername("testUser2");
        user2.setFullName("Test User 2");
        user2.setEmail("testuser2@example.com");
        user2.setEncryptedPassword("password2");
    }


    @Test
    public void testFindById() {

        userRepository.save(user);


        Optional<UserEntity> foundUser = userRepository.findById(user.getId());


        assertEquals(user.getUsername(), foundUser.get().getUsername());
    }

    @Test
    public void testUpdateUser() {

        UserEntity savedUser = userRepository.save(user);


        savedUser.setFullName("Updated User");
        userRepository.save(savedUser);

        Optional<UserEntity> updatedUser = userRepository.findById(savedUser.getId());

        assertEquals("Updated User", updatedUser.get().getFullName());
    }

    @Test
    public void testDeleteUser() {

        UserEntity savedUser = userRepository.save(user);


        userRepository.delete(savedUser);

        Optional<UserEntity> deletedUser = userRepository.findById(savedUser.getId());
        assertEquals(Optional.empty(), deletedUser);
    }

    @Test
    public void testFindByUsername() {

        userRepository.save(user);


        Optional<UserEntity> foundUser = userRepository.findByUsername(user.getUsername());


        assertEquals(user.getUsername(), foundUser.get().getUsername());
    }

    @Test
    public void testFindAll() {
        userRepository.save(user);
        userRepository.save(user2);

        List<UserEntity> users = userRepository.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    public void testUpdatePartialUser() {
        UserEntity savedUser = userRepository.save(user);

        String newFullName = "Josias C";
        String newEmail = "josias@gmail.com";

        int updatedRows = userRepository.updatePartialUser(
                newFullName,
                newEmail,
                null,
                null,
                savedUser.getId()
        );

        entityManager.flush();
        entityManager.clear();

        Optional<UserEntity> updatedUser = userRepository.findById(savedUser.getId());
        assertTrue(updatedUser.isPresent());

        assertEquals(newFullName, updatedUser.get().getFullName());
        assertEquals(newEmail, updatedUser.get().getEmail());

        assertEquals(savedUser.getUsername(), updatedUser.get().getUsername());
        assertEquals(savedUser.getEncryptedPassword(), updatedUser.get().getEncryptedPassword());
    }

    @Test
    void testCreateUser() {
        // Criando um novo usuario
        UserEntity user = new UserEntity();
        user.setFullName("Usuario Teste");
        user.setEmail("teste@teste.com");
        user.setUsername("testeUser");
        user.setEncryptedPassword("senhaTeste");

        // Salvando no banco de dados
        UserEntity savedUser = userRepository.save(user);

        // Verificando se o usuario foi salvo corretamente
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        // Buscando usuario salvo
        Optional<UserEntity> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testeUser");
    }
}
