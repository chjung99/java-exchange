package exchange.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryUserRepository implements UserRepository{
    Set<String> userSet = new TreeSet<>();
    @Override
    public void save(String userId) {
        userSet.add(userId);
    }

    @Override
    public List<String> getAllUser() {
        return new ArrayList<>(userSet);
    }
}
