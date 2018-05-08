package com.example.raj.myapplication;

import java.util.List;

public class CaptionItem {

    private Output output;
    private Integer jobId;

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

}

class Output {

    private List<Caption> captions = null;

    public List<Caption> getCaptions() {
        return captions;
    }

    public void setCaptions(List<Caption> captions) {
        this.captions = captions;
    }

}

