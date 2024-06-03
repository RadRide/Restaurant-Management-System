package staff;

import dbConnection.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import org.controlsfx.control.ToggleSwitch;
import properties.Properties;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;

public class Staff {

    private String name, phoneNumber, dateJoined, shift, section;
    private int age, status;
    private Salary salary;
    private int id; // 1 for supervisors, 2 for waiter, 3 for cashier, 4 for kitchen
    private LocalDate dob;
    private DecimalFormat formatter;
    private ToggleSwitch statusButton;

    public Staff(String name, Salary salary, String phoneNumber, String dateJoined, String shift, int id, int status, LocalDate dob) {
        formatter = new DecimalFormat(Properties.PATTERN);
        this.name = name;
        this.dob = dob;
        setAge();
        this.salary = salary;
        this.phoneNumber = phoneNumber;
        this.dateJoined = dateJoined;
        this.shift = shift;
        this.id = id;
        this.status = status;
        setStatusButton();
    }

    public Staff(String name, String phoneNumber, String dateJoined, String shift, String section, Salary salary, int id, int status, LocalDate dob) {
        formatter = new DecimalFormat(Properties.PATTERN);
        this.name = name;
        this.dob = dob;
        setAge();
        this.phoneNumber = phoneNumber;
        this.dateJoined = dateJoined;
        this.shift = shift;
        this.section = section;
        this.salary = salary;
        this.id = id;
        this.status = status;
        setStatusButton();
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

    public void setAge() {
        this.age = Period.between(dob, LocalDate.now()).getYears();
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

    public LocalDate getDob() {
        return dob;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public ToggleSwitch getStatusButton() {
        return statusButton;
    }
    public void setStatusButton() {
        DBConnection connection = new DBConnection();

        statusButton = new ToggleSwitch("Disabled");
        statusButton.getStylesheets().add(getClass().getResource("/Styles/ManagerStyles.css").toString());
        statusButton.getStyleClass().add("toggleSwitch");
        statusButton.setAlignment(Pos.CENTER_RIGHT);

        if(status == Properties.STAFF_ACTIVE){
            statusButton.setSelected(true);
            statusButton.setText("Active");
        }

        statusButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1){
                    status = Properties.STAFF_ACTIVE;
                    statusButton.setText("Active");
                }else{
                    status = Properties.STAFF_DISABLED;
                    statusButton.setText("Disabled");
                }
                connection.editStaffStatus(id, status);
            }
        });
    }
}
