package com.example.controller;

import com.example.dto.RelationshipDTO;
import com.example.exception.BadRequestException;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;
import com.example.service.RelationshipService;
import com.example.model.request.RequestFriends;
import com.example.model.request.RequestFriendsList;
import com.example.model.request.RequestReciveUpdate;
import com.example.model.request.RequestSubcriber;
import com.example.model.response.ResponseFriends;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/relationship")
public class RelationshipController {
    @Autowired
    private RelationshipService relationshipService;
    @GetMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> findAllRelationships() {
        List<RelationshipDTO> listRelationship = relationshipService.findAllRelationships();
        if(listRelationship.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(relationshipService.findAllRelationships());
    }
    @PostMapping("/find-relationship-by-id")
    public ResponseEntity<?> findRelationshipById(@Valid @RequestBody RelationshipPK relationshipPK) throws Exception {
        return ResponseEntity.ok(relationshipService.findRelationshipById(relationshipPK));
    }

    //API to create a friend connection between two email addresses.
    @PostMapping("")
    public ResponseEntity<?> beFriends(@RequestBody @Valid RequestFriends requestFriends) throws Exception {
        boolean success = relationshipService.beFriends(requestFriends.getEmails().get(0), requestFriends.getEmails().get(1));
        ResponseFriends responseFriends = ResponseFriends.builder().success(success).build();
        return ResponseEntity.ok(responseFriends);
    }

    //API to retrieve the friends list for an email address.
    @PostMapping("/friends-list")
    public ResponseEntity<?> findFriendList(@Valid @RequestBody RequestFriendsList requestFriendsList) {
        List<String> lstFriend = relationshipService.findFriendsList(requestFriendsList.getEmail());
        ResponseFriends responseFriends = new ResponseFriends();
        responseFriends.setSuccess(true);
        responseFriends.setCount(lstFriend.size());
        responseFriends.setFriends(lstFriend);
        return ResponseEntity.ok(responseFriends);
    }

    //API to retrieve the common friends list between two email addresses.
    @PostMapping("/common-friends-list")
    public ResponseEntity<?> findCommonFriendsList(@Valid @RequestBody RequestFriends requestFriends) throws Exception {
        List<String> lstFriendCommon = relationshipService.findCommonFriendsList(requestFriends.getEmails());
        ResponseFriends responseFriends = new ResponseFriends();
        responseFriends.setSuccess(true);
        responseFriends.setCount(lstFriendCommon.size());
        responseFriends.setFriends(lstFriendCommon);
        return ResponseEntity.ok(responseFriends);
    }

    //API to subscribe to updates from an email address.
    @PostMapping("/be-subscriber")
    public ResponseEntity<?> beSubscriber(@Valid @RequestBody RequestSubcriber requestSubcriber) throws BadRequestException {
        Relationship success = relationshipService.beSubscriber(requestSubcriber.getRequestor(), requestSubcriber.getTarget());
        ResponseFriends responseFriends = new ResponseFriends();
        responseFriends.setSuccess(true);
        return ResponseEntity.ok(responseFriends);
    }

    //API to block updates from an email address.
    @PostMapping("/to-block")
    public ResponseEntity<?> toBlock(@Valid @RequestBody RequestSubcriber requestSubcriber) throws BadRequestException {
        Relationship success = relationshipService.toBlock(requestSubcriber.getRequestor(), requestSubcriber.getTarget());
        ResponseFriends responseFriends = new ResponseFriends();
        responseFriends.setSuccess(true);
        return ResponseEntity.ok(responseFriends);
    }

    //API to retrieve all email addresses that can receive updates from an email address.
    @PostMapping("/receive-update")
    public ResponseEntity<?> findReceiveUpdateList(@Valid @RequestBody RequestReciveUpdate requestReciveUpdate) {
        List<String> lstRecipient = relationshipService.findReceiveUpdateList(requestReciveUpdate.getSender(),requestReciveUpdate.getText());
        ResponseFriends responseFriends = new ResponseFriends();
        responseFriends.setSuccess(true);
        responseFriends.setRecipients(lstRecipient);
        return ResponseEntity.ok(responseFriends);
    }
}
