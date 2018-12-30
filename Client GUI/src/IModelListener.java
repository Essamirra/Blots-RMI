/**
 * Created by Proinina Maria
 */
public interface IModelListener {
    void gameStarted();
    void takeMove();
    void enemyMoved();
    void youWin();
    void youLose();
    void notYourMove();
    void fieldUpdated();
    void connected();
}
