package woowacourse.shoppingcart.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woowacourse.auth.support.AuthenticationPrincipal;
import woowacourse.shoppingcart.dto.order.request.OrderRequest;
import woowacourse.shoppingcart.dto.order.response.OrderResponse;
import woowacourse.shoppingcart.service.OrderService;

@Validated
@RestController
@RequestMapping("/api/myorders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> saveOrder(@AuthenticationPrincipal String email, @RequestBody OrderRequest request) {
        final Long orderId = orderService.addOrder(email, request);

        return ResponseEntity.created(URI.create("/api/myorders/" + orderId)).build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findOrder(@AuthenticationPrincipal String email,
                                            @PathVariable final Long orderId) {
        final OrderResponse orderResponse = orderService.findOrderById(email, orderId);

        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findOrders(@AuthenticationPrincipal String email) {
        final List<OrderResponse> response = orderService.findOrdersByCustomer(email);

        return ResponseEntity.ok(response);
    }
}
