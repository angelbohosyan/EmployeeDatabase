package summer2019.exercise6;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zlatko on 12.07.19.
 */
public class MainFrame extends JFrame {

    private final MyTableModel tableModel = new MyTableModel();
    private final JTextField firstNameInput = new JTextField();
    private final JTextField salaryInput = new JTextField();
    private final JTextField lastNameInput = new JTextField();
    private final JTextField startDateInput = new JTextField();

    private final JButton insert = new JButton("Insert");
    private final JButton update = new JButton("Update");
    private final JButton delete = new JButton("Delete");
    private final JButton clear = new JButton("Clear");
    private Controller controller = new Controller(tableModel);

    public MainFrame() throws HeadlessException {
        super("Exercise6");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JPanel buttons = new JPanel(new GridLayout(1, 4));
        buttons.add(insert);
        buttons.add(update);
        buttons.add(delete);
        buttons.add(clear);

        final JPanel topPanel = new JPanel(new BorderLayout());
        final JPanel userInputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        userInputPanel.add(new JLabel("First name"));
        userInputPanel.add(firstNameInput);
        userInputPanel.add(new JLabel("Salary"));
        userInputPanel.add(salaryInput);
        userInputPanel.add(new JLabel("Last Name"));
        userInputPanel.add(lastNameInput);
        userInputPanel.add(new JLabel("Start date"));
        userInputPanel.add(startDateInput);

        final JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setContentPane(contentPane);

        topPanel.add(userInputPanel, BorderLayout.CENTER);
        topPanel.add(buttons, BorderLayout.SOUTH);
        this.add(topPanel, BorderLayout.NORTH);
        final JTable table = new JTable(tableModel);
        this.add(new JScrollPane(table), BorderLayout.CENTER);

        pack();
        TableColumn DateTableColumn = table.getColumn(tableModel.getColumnName(3));
        DateTableColumn.setCellRenderer(new DateCellRenderer());

        try {
            Connection myConnection = DriverManager.getConnection(
                    Constants.SERVER_URL, Constants.SERVER_USER, Constants.SERVER_PASSWORD);

            Statement myStatement = myConnection.createStatement();
            String query = "SELECT * FROM public.\"Employees\"";

            ResultSet myResultSet = myStatement.executeQuery(query);

            while (myResultSet.next()) {
                SimpleDateFormat format = new SimpleDateFormat(Constants.TABLE_DATE_PATTERN);
                try {
                    tableModel.add(Integer.parseInt(myResultSet.getString(1)),
                            myResultSet.getString(2),
                            myResultSet.getString(3),
                            Integer.parseInt(myResultSet.getString(4)),
                            format.parse(myResultSet.getString(5)));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        insert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                insert();
            }
        });
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                clear();
            }
        });
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                update(table.getSelectedRow());
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                delete(table.getSelectedRow());
            }
        });
        checkDeleteAndUpdateButtonState(false);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged(table.getSelectedRow());
                checkDeleteAndUpdateButtonState(true);
            }
        });
    }

    public static void main(String[] args) {
        final MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }

    private void tableSelectionChanged(int selectedRow) {
        if (selectedRow >= 0) {
            firstNameInput.setText((String) tableModel.getValueAt(selectedRow, 0));
            lastNameInput.setText((String) tableModel.getValueAt(selectedRow, 1));
            salaryInput.setText(tableModel.getValueAt(selectedRow, 2).toString());
            SimpleDateFormat format = new SimpleDateFormat(Constants.TABLE_DATE_PATTERN);
            Date date = (Date) tableModel.getValueAt(selectedRow, 3);
            startDateInput.setText(format.format(date));
        }
    }

    private void insert() {
        SimpleDateFormat format = new SimpleDateFormat(Constants.TABLE_DATE_PATTERN);
        String errorInfo = "";
        int salary = 0;
        Date date = null;
        if (firstNameInput.getText().equals("")) errorInfo += "firstName can not be empty";
        if (lastNameInput.getText().equals("")) errorInfo += "lastName can not be empty";
        try {
            salary = Integer.parseInt(salaryInput.getText());
        } catch (NumberFormatException a) {
            errorInfo += "Salary must be a number and it must be > 0";
        }
        try {
            date = format.parse(startDateInput.getText());
        } catch (java.text.ParseException e) {
            errorInfo += "Date is not valid";
        }
        if (errorInfo.equals("")) {
            try {
                controller.insert(date, firstNameInput.getText(), lastNameInput.getText(), salary);
                Connection myConnection = DriverManager.getConnection(
                        Constants.SERVER_URL, Constants.SERVER_USER, Constants.SERVER_PASSWORD);
                Statement idStatement = myConnection.createStatement();
                String idQuery = "select id \n" +
                        "from public.\"Employees\" \n" +
                        "order by id desc\n" +
                        "fetch first 1 rows only";
                ResultSet myResultSet = idStatement.executeQuery(idQuery);
                myResultSet.next();
                int id = Integer.parseInt(myResultSet.getString(1));
                tableModel.add(id, firstNameInput.getText(), lastNameInput.getText(), salary, date);
            } catch (java.sql.SQLException e) {
                JOptionPane.showMessageDialog(this, "The operation stopped", "", JOptionPane.ERROR_MESSAGE);
            }
        } else
            JOptionPane.showMessageDialog(this, errorInfo, "wrong user input", JOptionPane.ERROR_MESSAGE);

        clear();
    }

    private void clear() {
        firstNameInput.setText("");
        lastNameInput.setText("");
        salaryInput.setText("");
        startDateInput.setText("");
    }

    private void update(int selectedRowNumber) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.TABLE_DATE_PATTERN);
        String errorInfo = "";
        int salary = 0;
        Date date = null;
        if (firstNameInput.getText().equals("")) errorInfo += "firstName can not be empty";
        if (lastNameInput.getText().equals("")) errorInfo += "lastName can not be empty";
        try {
            salary = Integer.parseInt(salaryInput.getText());
        } catch (NumberFormatException a) {
            errorInfo += "Salary must be a number and it must be > 0";
        }
        try {
            date = format.parse(startDateInput.getText());
        } catch (java.text.ParseException e) {
            errorInfo += "Date is not valid";
        }
        if (errorInfo.equals("")) {
            try {
                controller.update(selectedRowNumber, date, firstNameInput.getText(), lastNameInput.getText(), salary);
            } catch (java.sql.SQLException e) {
                JOptionPane.showMessageDialog(this, "The operation stopped", "", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            tableModel.set(firstNameInput.getText(), lastNameInput.getText(), salary, date, selectedRowNumber);
        } else
            JOptionPane.showMessageDialog(this, errorInfo, "wrong user input", JOptionPane.ERROR_MESSAGE);
        clear();
    }

    private void delete(int selectedRowNumber) {
        try {
            controller.delete(selectedRowNumber);
        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this, "The operation stopped", "", JOptionPane.ERROR_MESSAGE);
        }
        tableModel.delete(selectedRowNumber);
        clear();
    }

    private void checkDeleteAndUpdateButtonState(boolean flag) {
        update.setEnabled(flag);
        delete.setEnabled(flag);
    }

    class DateCellRenderer extends DefaultTableCellRenderer {
        @Override
        protected void setValue(Object value) {
            SimpleDateFormat format = new SimpleDateFormat(Constants.TABLE_DATE_PATTERN);
            super.setValue(format.format(value));
        }
    }
}
