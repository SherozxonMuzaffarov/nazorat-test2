package com.sms.payload;

import com.sms.model.VagonMalumot;
import com.sms.model.VagonModel;

public class ApiResponse {

    private String message;
    private VagonMalumot vagonMalumot;
    private boolean success;
    private VagonModel vagonModel;

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ApiResponse(VagonMalumot vagonMalumot, boolean success) {
        this.vagonMalumot = vagonMalumot;
        this.success = success;
    }

    public ApiResponse(VagonModel vagonModel, boolean success ) {
        this.success = success;
        this.vagonModel = vagonModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getObject() {
        return vagonMalumot;
    }

    public void setObject(VagonMalumot vagonMalumot) {
        this.vagonMalumot = vagonMalumot;
    }

    public VagonMalumot getVagonMalumot() {
        return vagonMalumot;
    }

    public void setVagonMalumot(VagonMalumot vagonMalumot) {
        this.vagonMalumot = vagonMalumot;
    }

    public VagonModel getVagonModel() {
        return vagonModel;
    }

    public void setVagonModel(VagonModel vagonModel) {
        this.vagonModel = vagonModel;
    }
}
