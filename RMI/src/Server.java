import java.awt.*;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.List;

/**
 * Created by Proinina Maria
 */
public class Server implements IServer {
  Dictionary<UUID,IClient> client = new Hashtable<>();
    UUID[] keys = new UUID[2];
    private Registry registry;
    private int wantToStart = 0;

    public Server() {
        IServer stub = null;
        try {
            stub = (IServer) UnicastRemoteObject.exportObject(this, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // Bind the remote object's stub in the registry
        registry = null;
        try {
            registry = LocateRegistry.createRegistry(1098);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            registry.rebind("Server", stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.printf("Server started\n");
    }

    public static void main(String[] args) {
        Server s = new Server();

    }

    @Override
    public void connect(UUID playerName) {
        try {
            if(client.size() < 2) {
                client.put(playerName, (IClient) registry.lookup(playerName.toString()));
                keys[client.size()-1] = playerName;
                System.out.printf("Client " + playerName.toString() + " connected\n");
            }


        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void startGame()
    {
        wantToStart++;
        if(wantToStart !=2) return;
        int rows = 19;
        int cols = 19;
        Player p1 = new Player("Player 1",2,2, Color.red);
        Player p2 = new Player("Player 2",rows-2,cols-2, Color.BLUE);
        try {

            client.get(keys[0]).setPlayer(p1);
            client.get(keys[1]).setPlayer(p2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

       List<Player> players = new LinkedList<>();
        players.add(p1);
        players.add(p2);

        Game g = new Game(rows, cols, players,2);
        g.getField().getCell(2,2).setOwner(p1);
        g.getField().getCell(rows-2,cols-2).setOwner(p2);
        try {
            client.get(keys[0]).startGame(g);
            client.get(keys[1]).startGame(g);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.printf("game started\n");
    }

    @Override
    public void applyClientMove(List<Move> moves, UUID playerName) throws RemoteException {
        System.out.printf("Get move from" + playerName.toString()+"\n");
        UUID whoNext = playerName;
        for (int i = 0; i < 2; i++) {
            if(!keys[i].equals(playerName))
                whoNext = keys[i];
        }
        client.get(whoNext).receiveEnemyTurn(moves);


    }

    @Override
    public void playerDefeated(UUID playerName) {
        UUID whoNext = playerName;
        for (int i = 0; i < 2; i++) {
            if(!keys[i].equals(playerName))
                whoNext = keys[i];
        }

        try {
            client.get(whoNext).winGame();
            client.get(playerName).looseGame();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}
