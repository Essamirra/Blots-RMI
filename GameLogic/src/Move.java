import java.io.Serializable;

/**
 * Created by Proinina Maria
 */
public class Move implements Serializable {
    int x;
    int y;
    Player who;
    ECellState newState;

}
