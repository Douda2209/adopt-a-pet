package de.douda2209.adopt_a_pet.user;

import java.time.Instant;

public class User {
    private Long id = null;
    private String email;
    final private String passwordHash;
    private Role role;
    final Instant createdAt;

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
        if (o == null || this.getClass() != o.getClass()) return false; // better than isinstanceof() which compares if they're exact (false if someone is a subclass)
        User user = (User) o; //casting o to User
        if (this.id == null || user.id == null) return false;
        return this.id.equals(user.id);

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
