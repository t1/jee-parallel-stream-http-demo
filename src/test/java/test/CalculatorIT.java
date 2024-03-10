package test;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.BDDAssertions.then;

class CalculatorIT {
    private static final Client HTTP = ClientBuilder.newClient();
    private static final WebTarget SERVICE = HTTP.target("http://localhost:8080/");

    @Test
    void shouldAddThreeAndFive() {
        var sum = SERVICE.path("/3plus5").request().get(Integer.class);

        then(sum).isEqualTo(8);
    }

    @Test
    void shouldCalculateSum2() {
        var start = Instant.now();
        var sum = SERVICE.path("/sum2").request().get(Integer.class);
        var duration = Duration.between(start, Instant.now());

        then(sum).isEqualTo(5);
        then(duration).isLessThan(Duration.of(1, SECONDS));
    }

    @Test
    void shouldCalculateSum15() {
        var start = Instant.now();
        var sum = SERVICE.path("/sum15").request().get(Integer.class);
        var duration = Duration.between(start, Instant.now());

        then(sum).isEqualTo(135);
        then(duration).isLessThan(Duration.of(5, SECONDS));
    }
}
