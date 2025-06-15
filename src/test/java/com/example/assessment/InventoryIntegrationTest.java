//package com.example.assessment;
//
//import com.example.assessment.dto.ReserveInventoryRequest;
//import com.example.assessment.dto.UpdateInventoryRequest;
//import com.example.assessment.entity.Inventory;
//import com.example.assessment.entity.Item;
//import com.example.assessment.entity.Reservation;
//import com.example.assessment.repository.InventoryRepository;
//import com.example.assessment.repository.ItemRepository;
//import com.example.assessment.repository.ReservationRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//
//import static org.hamcrest.core.StringContains.containsString;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ActiveProfiles("test")
//@SpringBootTest
//@AutoConfigureMockMvc
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class InventoryIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private InventoryRepository inventoryRepository;
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @Autowired
//    private ItemRepository itemRepository;
//
//    @BeforeEach
//    public void setup() {
//        reservationRepository.deleteAll();
//        inventoryRepository.deleteAll();
//        itemRepository.deleteAll();
//    }
//
//    @Test
//    public void testAddSupplyAndGetAvailability() throws Exception {
//        // 1. Add a new item
//        Item item = itemRepository.save(Item.builder()
//                .name("Test Item")
//                .sku("SKU-001")
//                .build());
//
//        // 2. Add inventory for the item
//        UpdateInventoryRequest req = new UpdateInventoryRequest(item.getItemId(), 100);
//        mockMvc.perform(post("/inventory/supply")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk());
//
//        // 3. Query availability
//        mockMvc.perform(get("/inventory/availability/" + item.getItemId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.availableQuantity").value(100));
//    }
//
//    @Test
//    public void testReserveStockSuccess() throws Exception {
//        Item item = itemRepository.save(Item.builder()
//                .name("Test Item")
//                .sku("SKU-001")
//                .build());
//
//        inventoryRepository.save(new Inventory(null, item, 50, 0, LocalDateTime.now()));
//
//        ReserveInventoryRequest req = new ReserveInventoryRequest(item.getItemId(), 10);
//
//        mockMvc.perform(post("/inventory/reserve")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk());
//
//        // Check updated reserved quantity
//        Inventory inv = inventoryRepository.findByItem(item).orElseThrow();
//        assertEquals(10, inv.getReservedQuantity());
//
//        // Check availability endpoint reflects it
//        mockMvc.perform(get("/inventory/availability/" + item.getItemId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.availableQuantity").value(40));
//    }
//
//    @Test
//    public void testReserveFailsIfInsufficient() throws Exception {
//        Item item = itemRepository.save(
//                Item.builder()
//                        .name("Test Item 3")
//                        .sku("SKU-003")
//                        .build());
//        inventoryRepository.save(new Inventory(null, item, 20, 15, LocalDateTime.now())); // only 5 available
//
//        ReserveInventoryRequest req = new ReserveInventoryRequest(item.getItemId(), 10);
//
//        mockMvc.perform(post("/inventory/reserve")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(containsString("Not enough stock available")));
//    }
//
//    @Test
//    public void testCancelReservation() throws Exception {
//        Item item = itemRepository.save(Item.builder()
//                .name("Test Item 4")
//                .sku("SKU-004")
//                .build());
//        inventoryRepository.save(new Inventory(null, item, 100, 30, LocalDateTime.now()));
//        Reservation res = reservationRepository.save(
//                new Reservation(null, item, 30, Reservation.ReservationStatus.RESERVED, LocalDateTime.now())
//        );
//
//        mockMvc.perform(post("/inventory/cancel/" + res.getReservationId()))
//                .andExpect(status().isOk());
//
//        // Check inventory reserved updated
//        Inventory inv = inventoryRepository.findByItem(item).orElseThrow();
//        assertEquals(0, inv.getReservedQuantity());
//
//        // Check reservation marked as cancelled
//        Reservation updated = reservationRepository.findById(res.getReservationId()).orElseThrow();
//        assertEquals(Reservation.ReservationStatus.CANCELLED, updated.getStatus());
//    }
//
//}
//
