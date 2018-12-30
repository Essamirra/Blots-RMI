import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Proinina Maria
 */
public interface IServer extends Remote {
    void connect(UUID playerName)  throws RemoteException;

    void startGame()throws RemoteException;

    void applyClientMove(List<Move> moves, UUID playerName)  throws RemoteException;
    void playerDefeated(UUID playerName) throws RemoteException;;
}
