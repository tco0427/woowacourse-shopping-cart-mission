package woowacourse.shoppingcart.controller;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woowacourse.auth.support.AuthenticationPrincipal;
import woowacourse.shoppingcart.dto.customer.request.ChangePasswordRequest;
import woowacourse.shoppingcart.dto.customer.request.CustomerDeletionRequest;
import woowacourse.shoppingcart.dto.customer.request.CustomerRequest;
import woowacourse.shoppingcart.dto.customer.response.CustomerResponse;
import woowacourse.shoppingcart.dto.customer.request.CustomerUpdateRequest;
import woowacourse.shoppingcart.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CustomerRequest request) {
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
            @AuthenticationPrincipal String email, @Valid @RequestBody CustomerUpdateRequest request) {
        final CustomerResponse response = customerService.update(email, request.getUsername());

        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/me", params = "target=password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal String email, @Valid @RequestBody ChangePasswordRequest request) {
        customerService.changePassword(email, request);

        return ResponseEntity.ok().location(URI.create("/login")).build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String email, @Valid @RequestBody CustomerDeletionRequest request) {
        customerService.delete(email, request.getPassword());

        return ResponseEntity.noContent().location(URI.create("/")).build();
    }
}
