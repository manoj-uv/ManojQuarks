package com.example.assessment.mapper;

import com.example.assessment.dto.CreateItemRequest;
import com.example.assessment.dto.ItemDto;
import com.example.assessment.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ItemMapper {

    ItemDto toDto(Item item);

    @Mapping(target = "itemId", ignore = true)
    Item toEntity(CreateItemRequest request);
}