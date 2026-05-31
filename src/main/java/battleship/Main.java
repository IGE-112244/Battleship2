package battleship;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.GraphicsEnvironment;

public class Main {
	private static final Logger log = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
		// Detetar ambiente headless (Docker/CI)
		boolean isHeadless = GraphicsEnvironment.isHeadless() ||
				System.getenv("DOCKER_ENV") != null;

		if (isHeadless) {
			System.setProperty("java.awt.headless", "true");
			System.out.println("[INFO] Modo headless ativado " +
					"(Docker/CI) — visuais desativados.");
		}

		log.info("***  Battleship  ***");
		Tasks.menu();
	}
}