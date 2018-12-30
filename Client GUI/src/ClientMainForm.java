import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Proinina Maria
 */
public class ClientMainForm implements IView {

    Presenter p;
    Field f = new Field(0,0);
    Move currentMove;
    public ClientMainForm() {
        p = new Presenter(this);



        gameButton.addActionListener(e -> {
            playerName.setText("Waiting for another player");
            gameButton.setEnabled(false);
            p.startGame();
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ClientMainForm");
        frame.setContentPane(new ClientMainForm().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }

    private JPanel rootPanel;
    private JTable table1;
    private JLabel connectLable;
    private JLabel playerName;
    private JButton gameButton;
    private JButton sendMoveButton;
    private JLabel turnLabel;

    @Override
    public void setConnected(boolean isConnected) {
        if(isConnected)
            connectLable.setText("Client connected");
        else
            connectLable.setText("Client disconnected");

    }

    @Override
    public void setPlayerName(String playerName) {
        this.playerName.setText(playerName);
    }

    @Override
    public void setField(Field f) {
        this.f = f;
        table1.setModel(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return f.getRows();
            }

            @Override
            public int getColumnCount() {
                return f.getCols();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return f.getCell(rowIndex,columnIndex).getState().toString();
            }
        });
        table1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if(f.getCell(row,col).isAvailable())
                    setBackground(Color.green);
                else if(f.getCell(row,col).getOwner() != null)
                    setBackground(f.getCell(row,col).getOwner().getC());
                else
                    setBackground(table1.getBackground());
                return this;
            }
        });
        sendMoveButton.addActionListener(e -> {
            int row = table1.getSelectedRow();
            int column = table1.getSelectedColumn();
            currentMove= new Move();
            currentMove.x = row;
            currentMove.y = column;
            p.makeMove(currentMove);
        });


        table1.repaint();

    }

    @Override
    public void updateField(Field f) {

        table1.repaint();

    }

    @Override
    public void showWin(boolean isWinner) {
        String message;
        if(isWinner)
            message = " You win";
        else
            message = "You loose";

        JOptionPane.showMessageDialog(new JFrame(), message);
    }

    @Override
    public void disableControls() {
        table1.setEnabled(false);
        sendMoveButton.setEnabled(false);
    }

    @Override
    public void enableControls() {
        table1.setEnabled(true);
        sendMoveButton.setEnabled(true);
    }

    @Override
    public void setTurn(boolean isMyTurn) {
        if(isMyTurn)
            turnLabel.setText("Your turn");
        else
            turnLabel.setText("Waiting for another player");
    }
}
