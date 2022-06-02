package woowacourse.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.auth.exception.InvalidLoginException;
import woowacourse.auth.exception.InvalidTokenException;
import woowacourse.auth.support.JwtTokenProvider;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.exception.InvalidPasswordException;
import woowacourse.shoppingcart.exception.NotExistException;

@Service
@Transactional
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomerDao customerDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, CustomerDao customerDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerDao = customerDao;
    }

    @Transactional(readOnly = true)
    public TokenResponse createToken(TokenRequest request) {
        try {
            checkCustomer(request);
        } catch (NotExistException | InvalidPasswordException e) {
            throw new InvalidLoginException("Login Fail");
        }

        final String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    private void checkCustomer(TokenRequest request) {
        final Customer customer = customerDao.findByEmail(request.getEmail());
        customer.checkCorrectPassword(request.getPassword());
    }

    public String findCustomerByToken(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            return jwtTokenProvider.getPayload(token);
        }
        throw new InvalidTokenException("유효하지 않은 토큰입니다.");
    }
}
