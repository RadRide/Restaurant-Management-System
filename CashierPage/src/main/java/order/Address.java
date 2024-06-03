package order;

public class Address {

    private int id;
    private String address, addressName;

    public Address(int id, String addressName, String address){
        this.address = address;
        this.addressName = addressName;
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressName() {
        return addressName;
    }
    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }
}
