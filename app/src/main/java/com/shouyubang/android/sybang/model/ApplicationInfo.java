package com.shouyubang.android.sybang.model;

/**
 * Created by KunLiu on 2017/9/3.
 */
public class ApplicationInfo {


    private int id;
    private String userId;
    private Integer jobId;
    private String jobTitle;
    private int companyId;
    private String companyName;
    private String applyDate;
    private String process;
    private String adviserId;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getAdviserId() {
        return adviserId;
    }

    public void setAdviserId(String adviserId) {
        this.adviserId = adviserId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", jobId=" + jobId +
                ", jobTitle='" + jobTitle + '\'' +
                ", companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", applyDate=" + applyDate +
                ", process='" + process + '\'' +
                ", adviserId='" + adviserId + '\'' +
                ", status=" + status +
                '}';
    }
}
