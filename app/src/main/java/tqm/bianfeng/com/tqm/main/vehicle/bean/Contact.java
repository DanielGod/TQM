package tqm.bianfeng.com.tqm.main.vehicle.bean;

/**
 * Created by 王九东 on 2017/7/24.
 */
public class Contact {

    /**
     * contactNumber : 18615250685
     * contact : 121
     */
    private int userId;
    private String contactNumber;
    private String contact;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "userId=" + userId +
                ", contactNumber='" + contactNumber + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
