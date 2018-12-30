import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Proinina Maria
 */
public class Presenter implements IModelListener {
    IView v;
    IClientModel m;
    public Presenter(IView v)
    {
        this.v = v;
        m = new IClientModelImpl(this);
        v.setConnected(false);
    }

    @Override
    public void gameStarted() {
        System.out.printf(m.getPlayer().getName());
        v.setPlayerName(m.getPlayer().getName());
        v.setField(m.getField());
    }
    public void makeMove(Move move)
    {
       if(m.checkMyMove(move))
           m.setMove(move);
    }

    public void startGame()
    {
        m.connect();
        m.startGame();

    }
    @Override
    public void takeMove() {
        v.enableControls();
        v.setTurn(true);

    }

    @Override
    public void enemyMoved() {
        v.updateField(m.getField());

    }

    @Override
    public void youWin() {
        v.disableControls();
        v.showWin(true);


    }

    @Override
    public void youLose() {
        v.disableControls();
        v.showWin(false);
    }

    @Override
    public void notYourMove() {
        v.disableControls();
        v.setTurn(false);

    }

    @Override
    public void fieldUpdated() {
        v.updateField(m.getField());
    }

    @Override
    public void connected() {
        v.setConnected(true);
    }
}
