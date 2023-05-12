package test;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.junit.jupiter.api.Test;

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
    void shouldCalculateSum() {
        var sum = SERVICE.path("/sum").request().get(Integer.class);

        then(sum).isEqualTo(5);
    }
}
