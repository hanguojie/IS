package com.tospur.is.vo;

import java.io.Serializable;

/**
 * Created by 32070 on 2016/1/8.
 */
public class DingTalkHrmResourceVo implements Serializable {

    private Long id;

    private String userId;

    private String mobile;

    private String name;

    private Integer showOrder;

    private Long dingtalkId;

    private String jobName;

    private String email;

    private String workcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDingtalkId() {
        return dingtalkId;
    }

    public void setDingtalkId(Long dingtalkId) {
        this.dingtalkId = dingtalkId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkcode() {
        return workcode;
    }

    public void setWorkcode(String workcode) {
        this.workcode = workcode;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
