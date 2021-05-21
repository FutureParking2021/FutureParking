package com.example.future_parking.classes;

import java.util.Date;
import java.util.Map;

public class Operations {
    private OperationId operationId;
    private String type;
    private Parking parking;
    private Date createdTimestamp;
    private InvokedBy invokedBy;
    private Map<String,Object> operationAttributes;

    public Operations() {
    }

    public Operations(OperationId operationId, String type,
                             Parking parking, Date createTimestamp, InvokedBy invokedBy,
                             Map<String,Object> operationAttributes) {
        super();
        this.operationId = operationId;
        this.type = type;
        this.parking = parking;
        this.createdTimestamp = createTimestamp;
        this.invokedBy = invokedBy;
        this.operationAttributes = operationAttributes;
    }



    public OperationId getOperationId() {
        return operationId;
    }


    public void setOperationId(OperationId operationId) {
        this.operationId = operationId;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public Parking getItem() {
        return parking;
    }


    public void setItem(Parking parking) {
        this.parking = parking;
    }


    public InvokedBy getInvokedBy() {
        return invokedBy;
    }



    public void setInvokedBy(InvokedBy invokedBy) {
        this.invokedBy = invokedBy;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Map<String, Object> getOperationAttributes() {
        return operationAttributes;
    }

    public void setOperationAttributes(Map<String, Object> operationAttributes) {
        this.operationAttributes = operationAttributes;
    }


}
