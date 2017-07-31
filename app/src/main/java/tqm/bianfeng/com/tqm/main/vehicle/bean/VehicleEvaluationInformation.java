package tqm.bianfeng.com.tqm.main.vehicle.bean;

import java.util.List;

/**
 * Created by 王九东 on 2017/7/12.
 */

public class VehicleEvaluationInformation {
    private String id;

    private String plateNumber;//车牌号

    private Integer carstatus;//车况:较差3;一般2;优秀1

    private Integer purpose; //车辆用途:1自用;2公务商用;3营运

    private Integer provinceId;//省份标识Id

    private String provinceName;//

    private Integer cityId;//城市标识ID

    private String cityName;//

    private Integer carId; //车型标识ID

    private String carName;//

    private Integer useddate;//待估车辆的启用年份

    private String useddateMonth;//待估车辆的启用月份

    private String mileage;//待估车辆的公里数(万)

    private String price;//待估车辆在购买价(万)

    private String csPurPrice;//车商收购价(万)

    private String grDealPrice;//个人交易价(万)

    private String csRetailPrice;//车商零售价(万)

    private String proposer;//申请人

    private String contact;//联系方式

    private Integer userId;//用户Id

    private List<Contact> addressBook;

    public List<Contact> getAddressBook() {
        return addressBook;
    }

    public void setAddressBook(List<Contact> addressBook) {
        this.addressBook = addressBook;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Integer getCarstatus() {
        return carstatus;
    }

    public void setCarstatus(Integer carstatus) {
        this.carstatus = carstatus;
    }

    public Integer getPurpose() {
        return purpose;
    }

    public void setPurpose(Integer purpose) {
        this.purpose = purpose;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Integer getUseddate() {
        return useddate;
    }

    public void setUseddate(Integer useddate) {
        this.useddate = useddate;
    }

    public String getUseddateMonth() {
        return useddateMonth;
    }

    public void setUseddateMonth(String useddateMonth) {
        this.useddateMonth = useddateMonth;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCsPurPrice() {
        return csPurPrice;
    }

    public void setCsPurPrice(String csPurPrice) {
        this.csPurPrice = csPurPrice;
    }

    public String getGrDealPrice() {
        return grDealPrice;
    }

    public void setGrDealPrice(String grDealPrice) {
        this.grDealPrice = grDealPrice;
    }

    public String getCsRetailPrice() {
        return csRetailPrice;
    }

    public void setCsRetailPrice(String csRetailPrice) {
        this.csRetailPrice = csRetailPrice;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "VehicleEvaluationInformation{" +
                "id='" + id + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", carstatus=" + carstatus +
                ", purpose=" + purpose +
                ", provinceId=" + provinceId +
                ", provinceName='" + provinceName + '\'' +
                ", cityId=" + cityId +
                ", cityName='" + cityName + '\'' +
                ", carId=" + carId +
                ", carName='" + carName + '\'' +
                ", useddate=" + useddate +
                ", useddateMonth='" + useddateMonth + '\'' +
                ", mileage='" + mileage + '\'' +
                ", price='" + price + '\'' +
                ", csPurPrice='" + csPurPrice + '\'' +
                ", grDealPrice='" + grDealPrice + '\'' +
                ", csRetailPrice='" + csRetailPrice + '\'' +
                ", proposer='" + proposer + '\'' +
                ", contact='" + contact + '\'' +
                ", userId=" + userId +
                '}';
    }
}
