package com.example.assessment.mapper;

import com.example.assessment.dto.InventoryDto;
import com.example.assessment.dto.UpdateInventoryRequest;
import com.example.assessment.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "inventoryId", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    @Mapping(target = "item", ignore = true)
    Inventory toEntity(UpdateInventoryRequest req);

    @Mapping(source = "item.itemId", target = "itemId")
    InventoryDto toDto(Inventory inv);

//    // helper – MapStruct calls this during mapping
//    @Named("itemIdToItem")
//    default Item mapItem(Long itemId) {
//        // you can inject the repository with @Context or Spring @Autowired
//        return itemRepository.findById(itemId)
//                .orElseThrow(() -> new ChangeSetPersister.NotFoundException("Item"));
//    }
}
