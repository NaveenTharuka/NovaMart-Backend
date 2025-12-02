package com.nm.novamart.Controller;

import com.nm.novamart.Dto.OrderResponseDto;
import com.nm.novamart.Dto.PlaceOrderBuyNowDto;
import com.nm.novamart.Service.OrderServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("api/order")
public class OrderController {

    private final OrderServiceImpl orderService;

    @GetMapping("{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable String orderId) {
        OrderResponseDto responseDto = orderService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> buyNow(@RequestBody PlaceOrderBuyNowDto placeOrderBuyNowDto) {
        OrderResponseDto response = orderService.placeOrderBuyNow(placeOrderBuyNowDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
