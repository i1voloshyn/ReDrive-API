package dev.ivanvoloshyn.redriveapi.user;

import dev.ivanvoloshyn.redriveapi.user.model.User;
import dev.ivanvoloshyn.redriveapi.user.model.UserResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryUserRepository implements UserRepository {
    private Map<String, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public boolean existsByEmail(String email) {
        return users.containsKey(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public UserResponse save(User user) {
        return null;
    }

}
