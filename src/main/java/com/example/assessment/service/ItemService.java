package com.example.assessment.service;

import com.example.assessment.dto.CreateItemRequest;
import com.example.assessment.dto.ItemDto;
import com.example.assessment.entity.Item;
import com.example.assessment.mapper.ItemMapper;
import com.example.assessment.repository.ItemRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public ItemDto createItem(@Valid CreateItemRequest request) {
        Item item = itemMapper.toEntity(request);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }
}
