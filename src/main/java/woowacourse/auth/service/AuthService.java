package woowacourse.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.auth.exception.InvalidTokenException;
import woowacourse.auth.support.JwtTokenProvider;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.domain.Customer;

@Service
@Transactional
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomerDao customerDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, CustomerDao customerDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerDao = customerDao;
    }

    public TokenResponse createToken(TokenRequest request) {
        final Customer customer = customerDao.findByEmail(request.getEmail());
        customer.checkPassword(request.getPassword());

        final String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public String findCustomerByToken(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            return jwtTokenProvider.getPayload(token);
        }
        throw new InvalidTokenException("유효하지 않은 토큰입니다.");
    }
}
