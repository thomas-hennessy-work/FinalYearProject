package tom.sros;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LogInControllerTest {

	@Test
	@DisplayName("LogInController")
	void logInController() {
		LogInController gatherInput = new LogInController();
		assertEquals("Tom", gatherInput.logInCheck());
	}
}