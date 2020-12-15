package it.unibo.canteen.cache;

import it.unibo.canteen.cache.redis.RedisCache;
import it.unibo.canteen.model.Reservation;
import it.unibo.canteen.model.Room;
import it.unibo.canteen.model.Seat;
import it.unibo.canteen.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CacheFactory {
    @Value("${redis.cache.namespace}")
    private String namespace;

    @Bean
    public Cache<User> userCache() {
        return new RedisCache<>(namespace + "USER", User.class);
    }

    @Bean
    public Cache<Room> roomCache() {
        return new RedisCache<>(namespace + "ROOM", Room.class);
    }

    @Bean
    public Cache<Seat> seatCache() {
        return new RedisCache<>(namespace + "SEAT", Seat.class);
    }

    @Bean
    public Cache<Reservation> reservationCache() {
        return new RedisCache<>(namespace + "RESERVATION", Reservation.class);
    }
}
