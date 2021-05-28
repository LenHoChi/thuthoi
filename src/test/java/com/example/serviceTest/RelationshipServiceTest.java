package com.example.serviceTest;

import com.example.dto.RelationshipDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResouceNotFoundException;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;
import com.example.model.User;
import com.example.repository.RelationshipRepository;
import com.example.repository.UserRepository;
import com.example.service.impl.RelationshipServiceImpl;
import com.example.utils.convert.RelationshipConvert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RelationshipServiceTest {
    @TestConfiguration
    public static class RelationshipTestConfig{
        @Bean
        RelationshipServiceImpl relationshipService(){
            return new RelationshipServiceImpl();
        }
    }
    @MockBean
    private RelationshipRepository relationshipRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private RelationshipServiceImpl relationshipService;

    private RelationshipConvert relationshipConvert;

    //test findAllRelationships()
    @Test
    public void testGetRelationships() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);

        RelationshipPK relationshipPK1 = new RelationshipPK("jason@gmail.com", "david@gmail.com");
        Relationship relationship1 = new Relationship(relationshipPK1, true, false, false);

        List<Relationship> relationshipList = Arrays.asList(relationship, relationship1);
        when(relationshipRepository.findAll()).thenReturn(relationshipList);
        List<RelationshipDTO> relationshipDTOList = relationshipService.findAllRelationships();
        assertEquals(relationshipList.size(), relationshipDTOList.size());
    }
    //test findRelationshipById()
    //tc1 happy case
    @Test
    public void testGetRelationshipById() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
        Optional<RelationshipDTO> relationshipDTO = relationshipService.findRelationshipById(relationshipPK);
        assertEquals(relationship.getRelationshipPK().getUserEmail(), relationshipDTO.get().getRelationshipPK().getUserEmail());
    }
    //tc2 not found
    @Test
    public void testGetRelationshipByIdNotFound() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipService.findRelationshipById(relationshipPK));
        assertEquals("Error not found", exception.getMessage());
    }
    //test for beFriends()
    //tc1 happy case
    @Test
    public void testBeFriends() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);

        User user1 = new User("jason@gmail.com");
        User user2 = new User("kati@gmail.com");
        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);

        when(relationshipRepository.save(Mockito.any(Relationship.class))).thenReturn(relationship);
        Boolean result = relationshipService.beFriends(user1.getEmail(), user2.getEmail());
        assertTrue(result);
    }
    //tc2 two emails the same
    @Test
    public void testBeFriendsSameEmail() throws Exception {
        User user1 = new User("jason@gmail.com");
        User user2 = new User("jason@gmail.com");
        when(userRepository.existsById(user1.getEmail())).thenReturn(true);
        Throwable exception = assertThrows(BadRequestException.class, () -> relationshipService.beFriends(user1.getEmail(), user2.getEmail()));
        assertEquals("Two emails are same", exception.getMessage());

    }
    //tc3 exists already relationship
    @Test
    public void testBeFriendsAlready() throws Exception {
        User user1 = new User("jason@gmail.com");
        User user2 = new User("kati@gmail.com");
        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);

        when(relationshipRepository.existsById(new RelationshipPK(user1.getEmail(), user2.getEmail()))).thenReturn(true);
        Throwable exception = assertThrows(Exception.class, () -> relationshipService.beFriends(user1.getEmail(), user2.getEmail()));
        assertEquals("Error cause this relationship exists", exception.getMessage());
    }
    //tc4 email not exists in table
    @Test
    public void testBeFriendsNotExists() throws Exception {
        User user1 = new User("jason@gmail.com");
        User user2 = new User("kati@gmail.com");
        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);

        when(userRepository.existsById(user1.getEmail())).thenReturn(false);
        when(userRepository.existsById(user2.getEmail())).thenReturn(false);
        Throwable exception = assertThrows(Exception.class, () -> relationshipService.beFriends(user1.getEmail(), user2.getEmail()));
        assertEquals("Error not exists email in user table", exception.getMessage());
    }
    //test for getRelationship()
    //tc1  exists relationship
    @Test
    public void testGetRelationship() throws Exception{
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");
        Relationship relationship = new Relationship(relationshipPK, false, false, false);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));

        Relationship relationshipResult = relationshipService.getRelationship(relationshipPK);
        assertTrue(relationshipResult.getAreFriends());
        assertFalse(relationshipResult.getIsBlock());
        assertFalse(relationshipResult.getIsSubscriber());
        assertEquals(relationshipResult.getRelationshipPK(), relationshipPK);
    }
    //tc2 not exists relationship
    @Test
    public void testGetRelationshipIsEmpty() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");

        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.empty());
        Relationship relationshipResult = relationshipService.getRelationship(relationshipPK);
        assertTrue(relationshipResult.getAreFriends());
        assertFalse(relationshipResult.getIsBlock());
        assertFalse(relationshipResult.getIsSubscriber());
        assertEquals(relationshipResult.getRelationshipPK(), relationshipPK);
    }
    //tc3 this relationship isBlock
    @Test
    public void testGetRelationshipTrueBlock() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");

        Relationship relationship = new Relationship(relationshipPK, false, false, true);

        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
        Throwable exception = assertThrows(Exception.class, () -> relationshipService.getRelationship(relationshipPK));
        assertEquals("Error cause this relationship is block", exception.getMessage());
    }
    //tc4 this relation ship isn't Block, not friend
    @Test
    public void testGetRelationshipFalseBlock() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");

        Relationship relationship = new Relationship(relationshipPK, false, false, false);

        when(relationshipRepository.existsById(relationshipPK)).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));

        Relationship relationship1 = relationshipService.getRelationship(relationshipPK);
        assertTrue(relationship1.getAreFriends());
        assertFalse(relationship1.getIsBlock());
        assertFalse(relationship1.getIsSubscriber());
        assertEquals(relationship1.getRelationshipPK(), relationshipPK);
    }
    //test for findListFriends()
    //tc1 happy case
    @Test
    public void testFindFriendsList() throws Exception {
        List<String> lstFriend = new ArrayList<>();
        lstFriend.add("jason@gmail.com");

        User user = new User("kati@gmail.com");

        when(relationshipRepository.findFriendList(user.getEmail())).thenReturn(lstFriend);
        when(userRepository.existsById(user.getEmail())).thenReturn(true);
        List<String> lstTest = relationshipService.findFriendsList(user.getEmail());

        assertEquals(lstFriend.size(), lstTest.size());
    }
    //tc2 not found email in table
    @Test
    public void testGetFriendsListError() throws Exception{
        User user = new User("jason@gmail.com");
        when(userRepository.existsById(user.getEmail())).thenReturn(false);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipService.findFriendsList(user.getEmail()));
        assertEquals("Not found any email matched", exception.getMessage());
    }
    //test for findCommonFriendsList()
    //tc1 happy case
    @Test
    public void testGetCommonFriendsList() throws Exception {
        List<String> lstCommonFriend = new ArrayList<>();
        lstCommonFriend.add("jason@gmail.com");
        lstCommonFriend.add("kati@gmail.com");
        List<String> lstTest = new ArrayList<>();
        when(relationshipRepository.findFriendList(Mockito.anyString())).thenReturn(lstCommonFriend);
        when(userRepository.existsById(Mockito.anyString())).thenReturn(true);
        lstTest = relationshipService.findCommonFriendsList(lstCommonFriend);

        assertEquals(lstCommonFriend.size(), lstTest.size());
    }
    //tc2 two emails the same
    @Test
    public void testGetCommonFriendsListErrorEmailSame() throws Exception {
        List<String> lstCommonFriend = new ArrayList<>();
        lstCommonFriend.add("jason@gmail.com");
        lstCommonFriend.add("jason@gmail.com");

        Throwable exception = assertThrows(BadRequestException.class, () -> relationshipService.findCommonFriendsList(lstCommonFriend));
        assertEquals("Two emails are same", exception.getMessage());
    }
    //tc3 email not found in table
    @Test
    public void testGetCommonFriendsListErrorEmailMatch() throws Exception {
        List<String> lstCommonFriend = new ArrayList<>();
        lstCommonFriend.add("jason@gmail.com");
        lstCommonFriend.add("kati@gmail.com");

        when(userRepository.existsById(Mockito.anyString())).thenReturn(false);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipService.findCommonFriendsList(lstCommonFriend));
        assertEquals("Not found any email matched", exception.getMessage());
    }
    @Test
    public void testGetCommonFriendList() throws Exception{
        List<String> lstCommonFriend = new ArrayList<>();
        lstCommonFriend.add("jason@gmail.com");
        lstCommonFriend.add("kati@gmail.com");
        when(relationshipRepository.findFriendList(Mockito.anyString())).thenReturn(lstCommonFriend);
        List<String> lstTest = new ArrayList<>();
        lstTest = relationshipService.getCommonFriendList(lstCommonFriend);
        assertEquals(lstCommonFriend.size(), lstTest.size());
    }
    //test for beSubscriber()
    //tc1 two emails are same
    @Test
    public void testBeSubscriberSameEmail() throws Exception{
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "jason@gmail.com");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        Throwable exception = assertThrows(BadRequestException.class, () -> relationshipService.beSubscriber(relationshipPK.getUserEmail(), relationshipPK.getFriendEmail()));
        assertEquals("Two emails are same", exception.getMessage());
    }
    //tc2 email not exists in table
    @Test
    public void testBeSubscriberNotExists() throws Exception{
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(false);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipService.beSubscriber(relationshipPK.getUserEmail(), relationshipPK.getFriendEmail()));
        assertEquals("Not found any email matched", exception.getMessage());
    }
    //tc3 not exists this relationship
    @Test
    public void testBeSubscriberNotFriends() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.empty());
        Relationship result = relationshipService.beSubscriber(relationshipPK.getUserEmail(), relationshipPK.getFriendEmail());
        assertTrue(result.getIsSubscriber());
        assertFalse(result.getAreFriends());
    }
    //tc4 exists this relationship
    @Test
    public void testBeSubscriberFriends() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
        Relationship result = relationshipService.beSubscriber(relationshipPK.getUserEmail(), relationshipPK.getFriendEmail());
        assertTrue(result.getIsSubscriber());
    }
    //test for toBlock()
    //tc1 two emails are same
    @Test
    public void testToBlockSameEmail() throws Exception{
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "jason@gmail.com");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        Throwable exception = assertThrows(BadRequestException.class, () -> relationshipService.toBlock(relationshipPK.getUserEmail(), relationshipPK.getFriendEmail()));
        assertEquals("Two emails are same", exception.getMessage());
    }
    //tc2 email not exists in table
    @Test
    public void testToBlockNotExists() throws Exception{
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(false);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipService.toBlock(relationshipPK.getUserEmail(), relationshipPK.getFriendEmail()));
        assertEquals("Not found any email matched", exception.getMessage());
    }
    //tc3 not exists relationship
    @Test
    public void testToBlockNotRelationship() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.empty());
        Relationship result = relationshipService.toBlock(relationshipPK.getUserEmail(), relationshipPK.getFriendEmail());
        assertTrue(result.getIsBlock());
    }
    //tc4 have relationship,  not friends
    @Test
    public void testToBlockHaveRelationshipNotFriends() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");
        Relationship relationship = new Relationship(relationshipPK, false, false, false);

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
        Relationship result = relationshipService.toBlock(relationshipPK.getUserEmail(), relationshipPK.getFriendEmail());
        assertTrue(result.getIsBlock());
    }
    //tc5 have relationship, are friends
    @Test
    public void testToBlockHaveRelationshipWereFriends() throws Exception {
        RelationshipPK relationshipPK = new RelationshipPK("jason@gmail.com", "kati@gmail.com");
        Relationship relationship = new Relationship(relationshipPK, true, false, false);

        when(userRepository.existsById(Mockito.any(String.class))).thenReturn(true);
        when(relationshipRepository.findById(relationshipPK)).thenReturn(Optional.of(relationship));
        Relationship result = relationshipService.toBlock(relationshipPK.getUserEmail(), relationshipPK.getFriendEmail());
        assertFalse(result.getIsSubscriber());
    }
    //test for findReceiveUpdateList()
    //tc1 have email
    @Test
    public void testGetReceiveUpdateListHaveEmail() throws Exception {
        List<String> lstTemp = new ArrayList<>();
        lstTemp.add("jason@gmail.com");
        lstTemp.add("kati@gmail.com");
        String text = "please sent this email with David@gmail.com Regards";
        User user1 = new User("Mike@gmail.com");
        when(relationshipRepository.findReceiveUpdatesList(user1.getEmail())).thenReturn(lstTemp);
        when(userRepository.existsById(user1.getEmail())).thenReturn(true);
        List<String> lstTest = relationshipService.findReceiveUpdateList(user1.getEmail(),text);
        assertEquals(3, lstTest.size());
    }
    //tc2 haven't email
    @Test
    public void testGetReceiveUpdateListNotEmail() throws Exception {
        List<String> lstTemp = new ArrayList<>();
        lstTemp.add("jason@gmail.com");
        lstTemp.add("kati@gmail.com");
        String text = "this is content for text";
        User user1 = new User("Mike@gmail.com");
        when(relationshipRepository.findReceiveUpdatesList(user1.getEmail())).thenReturn(lstTemp);
        when(userRepository.existsById(user1.getEmail())).thenReturn(true);
        List<String> lstTest = relationshipService.findReceiveUpdateList(user1.getEmail(),text);
        assertEquals(2, lstTest.size());
    }
    //tc3 not exists email in table
    @Test
    public void testGetReceiveUpdateListError() throws Exception {
        List<String> lstTemp = new ArrayList<>();
        lstTemp.add("jason@gmail.com");
        lstTemp.add("kati@gmail.com");
        String text = "this is content for text";
        User user1 = new User("Mike@gmail.com");

        when(relationshipRepository.findReceiveUpdatesList(user1.getEmail())).thenReturn(lstTemp);
        when(userRepository.existsById(user1.getEmail())).thenReturn(false);
        Throwable exception = assertThrows(ResouceNotFoundException.class, () -> relationshipService.findReceiveUpdateList(user1.getEmail(),text));
        assertEquals("Not found any email matched", exception.getMessage());
    }
}
