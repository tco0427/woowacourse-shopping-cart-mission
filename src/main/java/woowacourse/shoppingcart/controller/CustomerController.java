package woowacourse.shoppingcart.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woowacourse.auth.support.AuthenticationPrincipal;
import woowacourse.shoppingcart.dto.ChangePasswordRequest;
import woowacourse.shoppingcart.dto.CustomerRequest;
import woowacourse.shoppingcart.dto.CustomerResponse;
import woowacourse.shoppingcart.dto.CustomerUpdateRequest;
import woowacourse.shoppingcart.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CustomerRequest request) {
        customerService.save(request);

        return ResponseEntity.created(URI.create("/login")).build();
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> findCustomer(@AuthenticationPrincipal String email) {
        final CustomerResponse response = customerService.findByEmail(email);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/me", params = "target=generalInfo")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @AuthenticationPrincipal String email, @RequestBody CustomerUpdateRequest request) {
        final CustomerResponse response = customerService.update(email, request.getUsername());

        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/me", params = "target=password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal String email, @RequestBody ChangePasswordRequest request) {
        customerService.changePassword(email, request);

        return ResponseEntity.ok().location(URI.create("/login")).build();
    }
}
