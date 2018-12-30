import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

/**
 * Created by Proinina Maria
 */
public class IClientModelImpl implements IClientModel, IClient {
    private List<IModelListener> listeners;
    private Game game;
    private Field tempField;
    private Player myPlayer;
    private IServer server;
    private UUID myName;
    private List<Move> currentMove = new LinkedList<>();


    public IClientModelImpl(Presenter presenter) {
        myName = randomUUID();
        listeners = new LinkedList<>();
        listeners.add(presenter);


    }

    @Override
    public void subscribe(IModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void connect() {
        Registry registry;
        IClient stub;
        try {
            stub = (IClient) UnicastRemoteObject.exportObject(this, 0);
            registry = LocateRegistry.getRegistry(1098);
            registry.rebind(myName.toString(), stub);
            server = (IServer) registry.lookup("Server");
            server.connect(myName);
            listeners.forEach(IModelListener::connected);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Player getPlayer() {
        return myPlayer;
    }

    @Override
    public Field getField() {
        return game.getField();
    }

    @Override
    public int getRemainigKlops() {
        return game.getRemainingKlops();
    }

    @Override
    public boolean checkMyMove(Move move) {
        if(game.getField().getCell(move.x,move.y).isAvailable())
            return true;
        return false;
    }

    @Override
    public void setMove(Move move) {
        currentMove.add(move);
        game.makeTurn(move.x, move.y);


        if(checkGameEnd())
            try {
                setMoves();
                server.playerDefeated(myName);
                return;


            } catch (RemoteException e) {
                e.printStackTrace();
            }
        listeners.forEach(IModelListener::fieldUpdated);
        if(game.getRemainingKlops() == 0)
        {
            setMoves();
            game.switchTurn();
            listeners.forEach(IModelListener::notYourMove);

        }


    }

    private boolean checkGameEnd() {
        return game.updateCellsAvailable() == 0 && getRemainigKlops() > 0;
    }

    @Override
    public List<FieldCell> getAvailableCell() {
        return game.markAvailableCells(myPlayer.getPlayerBase());
    }

    @Override
    public void startGame() {
        try {
            server.startGame();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void setMoves() {
        try {
            server.applyClientMove(currentMove, myName);
            currentMove.clear();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void receiveEnemyTurn(List<Move> moves) throws RemoteException {
        for (Move m:moves
                ) {
            game.makeTurn(m.x, m.y);
        }

        game.switchTurn();



        listeners.forEach(IModelListener::enemyMoved);
        if(checkGameEnd())
            try {
                listeners.forEach(IModelListener::fieldUpdated);
                server.playerDefeated(myName);



            } catch (RemoteException e) {
                e.printStackTrace();
            }
            else
        {
            listeners.forEach(IModelListener::fieldUpdated);
            listeners.forEach(IModelListener::takeMove);
        }




    }

    @Override
    public void winGame() throws RemoteException {
        listeners.forEach(IModelListener::youWin);

    }

    @Override
    public void looseGame() throws RemoteException {
        listeners.forEach(IModelListener::youLose);

    }

    @Override
    public void startGame(Game game) throws RemoteException {
        System.out.printf("I wanna start this");
        this.game = game;
        listeners.forEach((listener) -> listener.gameStarted());
      if (game.getCurrentPlayer().getName().equals(myPlayer.getName())) {
          game.updateCellsAvailable();
          listeners.forEach(IModelListener::takeMove);

      }
       else
           listeners.forEach(IModelListener::notYourMove);
    }

    @Override
    public void setPlayer(Player p) throws RemoteException {
        myPlayer = p;
    }


}
