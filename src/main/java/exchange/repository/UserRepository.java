package exchange.repository;

import java.util.List;

public interface UserRepository {
    public void save(String userId);
    public List<String> getAllUser();
}
