package summer2019.exercise6;

import java.util.Date;

/**
 * Created by zlatko on 12.07.19.
 */
public class Employee {
    private String firstName;
    private String lastName;
    private int salary;
    private Date startdate;

    private final int id;

    public Employee(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("ID not valid");
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(final int salary) {
        this.salary = salary;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(final Date startdate) {
        this.startdate = startdate;
    }
    public int getId() {
        return id;
    }
}
