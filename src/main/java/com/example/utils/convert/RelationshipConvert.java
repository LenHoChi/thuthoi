package com.example.utils.convert;

import com.example.dto.RelationshipDTO;
import com.example.model.Relationship;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RelationshipConvert {
    static ModelMapper modelMapper = new ModelMapper();
    public static RelationshipDTO convertModelToDTO(Relationship relationship){
        RelationshipDTO relationshipDTO = modelMapper.map(relationship, RelationshipDTO.class);
        return relationshipDTO;
    }
    public static List<RelationshipDTO> convertListModelToListDTO(List<Relationship> list){
        return list.stream().map(ele -> modelMapper.map(ele, RelationshipDTO.class)).collect(Collectors.toList());
    }
}
