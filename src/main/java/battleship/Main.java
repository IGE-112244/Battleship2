/**
 * 
 */
package battleship;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Main.
 *
 * @author britoeabreu
 * @author adrianolopes
 * @author miguelgoulao
 */
public class Main
{
    private static final Logger log = LogManager.getLogger(Main.class);

    /**
	 * Main.
	 *
	 * @param args the args
	 */
	public static void main(String[] args)
    {
		log.info("***  Battleship  ***");

		Tasks.menu();
    }
}
