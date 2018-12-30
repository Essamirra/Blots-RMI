import java.util.List;

/**
 * Created by Proinina Maria
 */
public interface IClientModel {

    void subscribe(IModelListener listener);
    void connect();

    Player getPlayer();

    Field getField();

    int getRemainigKlops();

    boolean checkMyMove(Move move);

    void setMove(Move move);

    List<FieldCell> getAvailableCell();
    void startGame();
}
