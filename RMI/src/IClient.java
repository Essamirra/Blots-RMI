import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Proinina Maria
 */
public interface IClient extends Remote {


    void receiveEnemyTurn(List<Move> moves)throws RemoteException;
    void winGame()throws RemoteException;
    void looseGame()throws RemoteException;
    void startGame(Game game)throws RemoteException;
    void setPlayer(Player p)throws RemoteException;

}
