package de.douda2209.adopt_a_pet.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;


public class UserTest {

    // a valid user that will be needed in each test
    private User validUser() {
        return new User("spiceman@example.com", "Hashcode123", Role.ADOPTER);
    }

    private User userWihRole(Role role) {
        return new User("spiceman@example.com", "Hashcode123", role);
    }

    @Test
    public void constructor_validInput_setsFields() {
        String passwordHash = "Hashcode123";
        String email = "spiceman@example.com";
        Role role = Role.ADOPTER;
        User user = new User(email, passwordHash, role);

        assertEquals(email, user.getEmail(), "Email should be stored as given");
        assertEquals(passwordHash, user.getPasswordHash(), "Hashcode  should be stored as given");
        assertEquals(role, user.getRole(),  "Role  should be stored as given");

    }

    @Test
    public void constructor_validInput_leavesIdNull() {
        assertNull(validUser().getId(), "Id should be null at the beginning");
    }

    @Test
    public void constructor_validInput_setsCreatedAt(){
        Instant before = Instant.now();
        // create a user:
        User user =  validUser();

        // Instant after:
        Instant after = Instant.now();
        assertFalse(user.getCreatedAt().isBefore(before), "createdAt should not be earlier than the moment before creation");
        assertFalse(user.getCreatedAt().isAfter(after), "createdAt should not be later than the moment after creation");

    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "abc"})
    public void constructor_invalidEmail_throws(String email){
        assertThrows(IllegalArgumentException.class, () -> new User(email, "Hashcode123", Role.ADOPTER), "email '" + email + "' should be rejected");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    public void constructor_invalidPasswordHash_throws(String passwordHash){
        assertThrows(IllegalArgumentException.class, () -> new User("spiceman@example.com", passwordHash, Role.ADOPTER), "passwordHash '" + passwordHash + "' should be rejected");

    }

    @Test
    public void constructor_nullRole_throws(){
        assertThrows(IllegalArgumentException.class, () -> new User("spiceman@example.com", "abc", null), "Null role should be rejected");
    }

    @Test
    public void changeEmail_validEmail_updatesEmail(){

        String newEmail = "newEmail@example.com";
        User user = validUser();

        user.changeEmail(newEmail);

        assertEquals(newEmail, user.getEmail(), "new email should equal to " + newEmail);


    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "abc"})
    public void changeEmail_invalidEmail_throws(String newEmail){

        User user = validUser();

        assertThrows(IllegalArgumentException.class, () -> user.changeEmail(newEmail),"invalid new email " + newEmail + " should be rejected");

    }
    @Test
    public void changeEmail_invalidEmail_keepsOldEmail(){

        User user = validUser();
        String oldEmail = user.getEmail();
        String newInvalidEmail = "abc";

        assertThrows(IllegalArgumentException.class, () -> user.changeEmail(newInvalidEmail), "new email" + newInvalidEmail + " should be rejected" );
        assertEquals(oldEmail, user.getEmail(), "user email should be the old email after rejection" + oldEmail);
    }

    @Test
    public void setId_firstTime_setsId(){

        User user = validUser();
        Long idToSet = 12345L;

        user.setId(idToSet);

        assertEquals(idToSet, user.getId(), "id should be the same as the first time set");


    }
    @Test
    public void setId_calledTwice_throwsIllegalState() {

        User user = validUser();
        Long idToSet = 12345L;

        user.setId(idToSet);
        assertThrows(IllegalStateException.class, () -> user.setId(idToSet), "SetId called twice should be rejected");


    }

    @Test
    public void setId_null_throwsIllegalArgument() {

        User user = validUser();

        assertThrows(IllegalArgumentException.class, () -> user.setId(null), "setId should throw exception when it gets null as parameter");

    }

    @ParameterizedTest
    @EnumSource(names = "ADMIN")
    public void isAdmin(Role role){

        User user = userWihRole(role);

        assertTrue(user.isAdmin(), "user should be admin");
    }

    @ParameterizedTest
    @EnumSource(names = "ADMIN", mode = EnumSource.Mode.EXCLUDE)
    public  void isAdmin_otherRole_returnsFalse(Role role) {

        User user = userWihRole(role);

        assertFalse(user.isAdmin(), "user should not be admin");

    }

    @ParameterizedTest
    @EnumSource(names = "SHELTER")
    public void isShelter(Role role){

        User user = userWihRole(role);

        assertTrue(user.isShelter(), "user should be shelter");
    }

    @ParameterizedTest
    @EnumSource(names = "SHELTER", mode = EnumSource.Mode.EXCLUDE)
    public  void isShelter_otherRole_returnsFalse(Role role) {

        User user = userWihRole(role);

        assertFalse(user.isShelter(), "user should not be shelter");

    }

    @Test
    public  void equals_sameObjectWithoutId_returnsTrue(){

        User user = validUser();

        assertTrue(user.equals(user), "unsaved user should equals to itself");

    }

    @Test
    public void equals_twoUnsavedUsers_returnsFalse() {

        User user_one = validUser();
        User user_two = validUser();

        assertFalse(user_one.equals(user_two), "two different unsaved should not be equal ");
    }

    @Test
    public void equals_sameId_returnsTrue() {

        User user_one = validUser();
        User user_two = validUser();

        user_one.setId(1L);
        user_two.setId(1L);

        assertTrue(user_one.equals(user_two), "users with the same id should be equal");
        assertTrue(user_two.equals(user_one), "equals should be symmetric ");
        assertEquals(user_one.hashCode(), user_two.hashCode(), "equal users should have the same hashcode");
    }

    @Test
    public void equals_sameIdDifferentData_returnsTrue() {
        User a = validUser();
        User b = new User("other@example.com", "otherHash", Role.SHELTER);

        a.setId(1L);
        b.setId(1L);  // if we use == instead of equals in User 1L works but
        // for 1000L it fails since Long valueOf keeps cache of objects from -128 to 127
        // 1L for both users-> inside that cache of objects
        // 1000L outside that cache -> each user gets its own Long object and == compares the objects

        assertTrue(a.equals(b), "identity is the id only, other fields don't matter");
    }

    @Test
    public void equals_differentIds_returnsFalse() {
        User a = validUser();
        User b = validUser();
        a.setId(1L);
        b.setId(2L);

        assertFalse(a.equals(b), "users with different ids should not be equal");
    }

    @Test
    public void equals_null_returnsFalse() {
        User user = validUser();

        assertFalse(user.equals(null), "a user should never equal null");
    }

    @Test
    public void equals_differentType_returnsFalse() {
        User user = validUser();

        assertFalse(user.equals("not a user"), "a user should not equal an object of another type");
    }

    //hashCode and HashSet

    @Test
    public void hashCode_afterSetId_isUnchanged() {
        User user = validUser();
        int before = user.hashCode();

        user.setId(5L);

        assertEquals(before, user.hashCode(), "hashCode must not change when the id is assigned");
    }

    @Test
    public void hashSet_unsavedUserAfterSetId_isStillFound() {
        Set<User> set = new HashSet<>();
        User user = validUser();
        set.add(user);

        user.setId(5L);

        assertTrue(set.contains(user), "the user should still be found after being saved");
    }

    @Test
    public void hashSet_twoDifferentUnsavedUsers_hasSizeTwo() {
        Set<User> set = new HashSet<>();
        set.add(validUser());
        set.add(validUser());

        assertEquals(2, set.size(), "two different unsaved users should both be kept");
    }

    @Test
    public void hashSet_sameUnsavedUserTwice_hasSizeOne() {
        Set<User> set = new HashSet<>();
        User user = validUser();
        set.add(user);
        set.add(user);

        assertEquals(1, set.size(), "the same object added twice should be stored once");
    }






}