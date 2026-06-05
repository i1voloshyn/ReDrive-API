package dev.ivanvoloshyn.redriveapi.user;

import dev.ivanvoloshyn.redriveapi.user.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
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
    public User save(User user) {
        user.setId(idGenerator.getAndIncrement());
        users.put(user.getEmail(), user);

        return users.get(user.getEmail());
    }

}
