package de.douda2209.adopt_a_pet.user;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id = null;

    @Column(nullable=false, length=254)
    private String email;

    @Column(name = "password_hash", nullable=false)
    private String passwordHash; //removed final because it must be assigned at every constructor, but protection is not lost bc of @Column(updatable = false)

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private Role role;

    @Column(name = "created_at", nullable=false, updatable=false)
    Instant createdAt; // removed final because it must be assigned at every constructor, but protection is not lost bc of @Column(updatable = false)

    // default constructor for JPA hibernate:
    protected User() {}

    public User(String email, String passwordHash, Role role) {


        validateEmail(email);
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("password hash is null");
        }
        if (role == null) {
            throw new IllegalArgumentException("role is null");
        }

        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = Instant.now();
    }


    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void changeEmail(String newEmail) {
        validateEmail(newEmail);
        this.email = newEmail;

    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isShelter() {
        return role == Role.SHELTER;
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank() || email.indexOf('@') == -1) {
            throw new IllegalArgumentException("email must not be null or blank");
        }
    }

    public void setId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("id is already set");
        }
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false; // in order to accept proxy // cast o to user and declares new variable  user of type User
        //if (o == null || this.getClass() != o.getClass()) return false; // better than isinstanceof() which compares if they're exact (false if someone is a subclass)
        //User user = (User) o; //casting o to User
        if (this.getId() == null || user.getId() == null) return false;
        return this.getId().equals(user.getId());

    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    // Two users are equal if they have the same id. A user without an id is only
    // equal to itself, because without an id there is no evidence that two different
    // objects are the same person. The hashCode does not use the id, because the id
    // changes when the user is saved; if it did, a HashSet would look in the wrong
    // locker and fail to find a user that is still inside it. The downside is that
    // all users share one locker, so lookups compare users one by one, which is
    // slower for very large collections but acceptable here.

}
