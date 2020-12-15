package it.unibo.canteen.cache;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface Cache<T> {
    Optional<List<T>> get(String id) throws IOException;
    Optional<T> getAny(String id) throws IOException;
    void put(String id, List<T> value) throws IOException;
    void put(String id, T value) throws IOException;
    boolean delete(String id) throws IOException;
}
