package summer2019.exercise6;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Date;

public class MyTableModel extends AbstractTableModel {

    private final ArrayList<Employee> data = new ArrayList<>();

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    public int getEmployeeId(int SelectedRow) {
        Employee employee = data.get(SelectedRow);
        return employee.getId();
    }

    @Override
    public String getColumnName(final int column) {
        switch (column) {
            case 0:
                return "First name";
            case 1:
                return "Last name";
            case 2:
                return "Salary";
            case 3:
                return "Start date";
            default:
                throw new IndexOutOfBoundsException("Unknown column " + column);
        }
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        switch (columnIndex) {
            case 0:
                return data.get(rowIndex).getFirstName();
            case 1:
                return data.get(rowIndex).getLastName();
            case 2:
                return data.get(rowIndex).getSalary();
            case 3:
                return data.get(rowIndex).getStartdate();
            default:
                throw new IndexOutOfBoundsException("Unknown column " + columnIndex);
        }
    }

    public void add(int id,String firstName, String lastName , int salary, Date starDate) {
        final Employee employee = new Employee(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setSalary(salary);
        employee.setStartdate(starDate);
        data.add(employee);

        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    public void set(String firstName, String lastName ,int salary,Date starDate,int SelectedRow){
        final Employee employee = data.get(SelectedRow);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setSalary(salary);
        employee.setStartdate(starDate);
        fireTableRowsUpdated(SelectedRow,SelectedRow);
    }
    public void delete(int SelectedRow){
        data.remove(SelectedRow);
        fireTableRowsDeleted(SelectedRow,SelectedRow);
    }
}
