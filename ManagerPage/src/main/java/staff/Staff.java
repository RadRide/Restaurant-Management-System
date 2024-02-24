package staff;

import java.text.DecimalFormat;

public class Staff {

    private String name, phoneNumber, dateJoined, shift, section;
    private int age;
    private Salary salary;
    private int id; // 1 for supervisors, 2 for waiter, 3 for cashier, 4 for kitchen
    private DecimalFormat formatter;
    private final String pattern = "#,##0.##";

    public Staff(String name, int age, Salary salary, String phoneNumber, String dateJoined, String shift, int id) {
        formatter = new DecimalFormat(pattern);
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.phoneNumber = phoneNumber;
        this.dateJoined = dateJoined;
        this.shift = shift;
        this.id = id;
    }

    public Staff(String name, String phoneNumber, String dateJoined, String shift, String section, int age, Salary salary, int id) {
        formatter = new DecimalFormat(pattern);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dateJoined = dateJoined;
        this.shift = shift;
        this.section = section;
        this.age = age;
        this.salary = salary;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public int getId() {return id;}

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setId(int id) {this.id = id;}

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }
}
