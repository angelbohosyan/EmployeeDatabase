package summer2019.exercise6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {
    MyTableModel m;
    Controller(MyTableModel model) {
        this.m = model;
    }

    public void insert(Date date,String firstNameInput,String lastNameInput,int salary) throws SQLException {
        SimpleDateFormat sqlFormat = new SimpleDateFormat(Constants.SERVER_DATE_PATTERN);
        String sqlDate = sqlFormat.format(date);
        Connection myConnection = DriverManager.getConnection(
                Constants.SERVER_URL, Constants.SERVER_USER, Constants.SERVER_PASSWORD);
        Statement myStatement = myConnection.createStatement();
        String query = "INSERT INTO public.\"Employees\"(first_name,last_name,salary,start_date) " +
                "VALUES('" + firstNameInput + "','" + lastNameInput + "','" + salary + "','" + sqlDate + "')";
        myStatement.execute(query);
    }

    public void update(int selectedRowNumber,Date date,String firstNameInput,String lastNameInput,int salary) throws SQLException {

            SimpleDateFormat sqlFormat = new SimpleDateFormat(Constants.SERVER_DATE_PATTERN);
            String sqlDate = sqlFormat.format(date);
            int selectedRowNumberId = m.getEmployeeId(selectedRowNumber);
            Connection myConnection = DriverManager.getConnection(
                    Constants.SERVER_URL, Constants.SERVER_USER, Constants.SERVER_PASSWORD);
            Statement updateStatement = myConnection.createStatement();
            String updateQuery = "update public.\"Employees\" \n" +
                    "set first_name='" + firstNameInput + "',\n" +
                    "last_name='" + lastNameInput + "',\n" +
                    "salary= " + salary + ",\n" +
                    "start_date= '" + sqlDate + "'\n" +
                    "where id=" + selectedRowNumberId;
            updateStatement.execute(updateQuery);
            m.fireTableRowsUpdated(selectedRowNumber,selectedRowNumber);
    }
    public void delete(int selectedRowNumber) throws SQLException {
        int selectedRowNumberId= m.getEmployeeId(selectedRowNumber);
        Connection myConnection = DriverManager.getConnection(
                Constants.SERVER_URL, Constants.SERVER_USER, Constants.SERVER_PASSWORD);
        Statement deleteStatement = myConnection.createStatement();
        String deleteQuery = "delete from employees \n" +
                "where id=" + selectedRowNumberId;
        deleteStatement.execute(deleteQuery);
    }
}
