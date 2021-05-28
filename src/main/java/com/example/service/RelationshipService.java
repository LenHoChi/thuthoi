package com.example.service;

import com.example.dto.RelationshipDTO;
import com.example.exception.BadRequestException;
import com.example.model.Relationship;
import com.example.model.RelationshipPK;

import java.util.List;
import java.util.Optional;

public interface RelationshipService {
    Optional<RelationshipDTO> findRelationshipById(RelationshipPK relationshipPK) throws Exception;

    List<RelationshipDTO> findAllRelationships();

    Boolean beFriends(String userEmail, String friendEmail) throws Exception;

    List<String> findFriendsList(String email);

    List<String> findCommonFriendsList(List<String> emailList) throws Exception;

    Relationship beSubscriber(String email_requestor, String email_target) throws BadRequestException;

    Relationship toBlock(String email_requestor, String email_target) throws BadRequestException;

    List<String> findReceiveUpdateList(String email, String text);

    public boolean checkMail(final String emailCheck);
}
