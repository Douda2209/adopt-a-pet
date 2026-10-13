package de.douda2209.adopt_a_pet.user;

import de.douda2209.adopt_a_pet.TestcontainersConfiguration;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional   // every test runs in a transaction that is rolled back afterwards
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private EntityManager em;

    private User newUser(String email) {
        return new User(email, "hash123", Role.ADOPTER);
    }

    @Test
    void save_newUser_assignsId() {
        User user = newUser("anna@example.com");
        assertNull(user.getId());

        User saved = repository.saveAndFlush(user);

        assertNotNull(saved.getId(), "the database should assign an id"); // because of the @GeneratedValue(strategy= GenerationType.IDENTITY)
    }

    @Test
    void save_user_roundTripKeepsFields() {
        User saved = repository.saveAndFlush(new User("anna@example.com", "hash123", Role.SHELTER));
        em.clear();   // forget the object, so the next read really hits the database

        User loaded = repository.findById(saved.getId()).orElseThrow();

        assertEquals("anna@example.com", loaded.getEmail());    
        assertEquals("hash123", loaded.getPasswordHash());
        assertEquals(Role.SHELTER, loaded.getRole());
        assertNotNull(loaded.getCreatedAt());
    }

    @Test
    void findByEmail_differentCase_findsUser() {
        repository.saveAndFlush(newUser("Anna@Example.com"));

        assertTrue(repository.findByEmail("anna@example.com").isPresent());
    }

    @Test
    void save_sameEmailDifferentCase_violatesUniqueIndex() {
        repository.saveAndFlush(newUser("Anna@Example.com"));

        assertThrows(DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(newUser("anna@example.com")),
                "the database must reject a duplicate email, whatever the case");
    }

    @Test
    void equals_realEntityAndProxy_areEqual() {
        Long id = repository.saveAndFlush(newUser("anna@example.com")).getId(); // saves the user and sends SQL code

        em.clear();
        User real = repository.findById(id).orElseThrow(); // look up by primary key // orElseThrow: opens the box and fails if empty
        em.clear(); // ereases the notepad that has the objects hibernate loaded (like a cache)
        User proxy = em.getReference(User.class, id);   // a placeholder subclass (User$HibernateProxy$...), loaded lazily, not fully loaded, no id
        // the next request (getEmail() ) goes through the user proxy and forwards it to the real user (real sql query)

        assertNotEquals(User.class, proxy.getClass(), "this should be a Hibernate proxy"); // proxy class is a subclass of User, not a plain User
        assertTrue(proxy.equals(real), "proxy.equals(real)"); // the equals() methd is evoked, so the proxy loads the full User
        assertTrue(real.equals(proxy), "real.equals(proxy)"); // same as above
    }
}