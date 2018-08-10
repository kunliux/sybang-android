package com.shouyubang.android.sybang.model;

/**
 * Created by KunLiu on 2017/8/3.
 */
public class Job {

    private int id;
    private String jobTitle;
    private String type;
    private String education;
    private String disabilityType;
    private String company;
    private String city;
    private String district;
    private String workplace;
    private int minSalary;
    private int maxSalary;
    private int gender;
    private int minAge;
    private int maxAge;
    private String jobContent;
    private String requirement;
    private String workTime;
    private String salary;
    private String insurance;
    private String roomBoard;
    private String remark;
    private String contract;
    private String benefits;
    private int amount;
    private int collected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getDisabilityType() {
        return disabilityType;
    }

    public void setDisabilityType(String disabilityType) {
        this.disabilityType = disabilityType;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public int getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(int minSalary) {
        this.minSalary = minSalary;
    }

    public int getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getJobContent() {
        return jobContent;
    }

    public void setJobContent(String jobContent) {
        this.jobContent = jobContent;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getRoomBoard() {
        return roomBoard;
    }

    public void setRoomBoard(String roomBoard) {
        this.roomBoard = roomBoard;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCollected() {
        return collected;
    }

    public void setCollected(int collected) {
        this.collected = collected;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", jobTitle='" + jobTitle + '\'' +
                ", type='" + type + '\'' +
                ", education='" + education + '\'' +
                ", disabilityType='" + disabilityType + '\'' +
                ", company='" + company + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", workplace='" + workplace + '\'' +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary +
                ", gender='" + gender + '\'' +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                ", jobContent='" + jobContent + '\'' +
                ", requirement='" + requirement + '\'' +
                ", workTime='" + workTime + '\'' +
                ", salary='" + salary + '\'' +
                ", insurance='" + insurance + '\'' +
                ", roomBoard='" + roomBoard + '\'' +
                ", remark='" + remark + '\'' +
                ", contract='" + contract + '\'' +
                ", benefits='" + benefits + '\'' +
                ", amount=" + amount +
                '}';
    }
}

