package manager;

import java.text.DecimalFormat;
import java.util.Date;

public class Staff {

    private String name, phoneNumber, dateJoined, shift, section, salary;
    private int age;
    private double salaryUSD, salaryLBP;
    private int id; // 0 for waiter, 1 for cashier, 2 for kitchen
    private DecimalFormat formatter;

    public Staff(String name, int age, double salaryUSD, double salaryLBP, String phoneNumber, String dateJoined, String shift, int id) {
        formatter = new DecimalFormat("#,###");
        this.name = name;
        this.age = age;
        this.salaryUSD = salaryUSD;
        this.salaryLBP = salaryLBP;
        this.phoneNumber = phoneNumber;
        this.dateJoined = dateJoined;
        this.shift = shift;
        this.id = id;
        setSalary();
    }

    public Staff(String name, String phoneNumber, String dateJoined, String shift, String section, int age, double salaryUSD, double salaryLBP, int id) {
        formatter = new DecimalFormat("#,###");
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dateJoined = dateJoined;
        this.shift = shift;
        this.section = section;
        this.age = age;
        this.salaryUSD = salaryUSD;
        this.salaryLBP = salaryLBP;
        this.id = id;
        setSalary();
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

    public String getSalary() {
        return salary;
    }

    public void setSalary() {
        this.salary = "$ " + formatter.format(salaryUSD) + "\nLBP " + formatter.format(salaryLBP);
    }

    public double getSalaryUSD() {
        return salaryUSD;
    }

    public void setSalaryUSD(double salaryUSD) {
        this.salaryUSD = salaryUSD;
    }

    public double getSalaryLBP() {
        return salaryLBP;
    }

    public void setSalaryLBP(double salaryLBP) {
        this.salaryLBP = salaryLBP;
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
}
