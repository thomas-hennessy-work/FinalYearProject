package tom.sros.logIn;

import tom.sros.logIn.LogInController;
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