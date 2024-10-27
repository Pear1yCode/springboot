package com.mergyping.model.dto;

import org.springframework.stereotype.Component;

@Component
public class MbtiTester {
    private int CB;
    private int HG;
    private int SE;
    private int TM;

    public MbtiTester() {}

    public MbtiTester(int CB, int HG, int SE, int TM) {
        this.CB = CB;
        this.HG = HG;
        this.SE = SE;
        this.TM = TM;
    }

    public int getCB() {
        return CB;
    }

    public void setCB(int CB) {
        this.CB = CB;
    }

    public int getHG() {
        return HG;
    }

    public void setHG(int HG) {
        this.HG = HG;
    }

    public int getSE() {
        return SE;
    }

    public void setSE(int SE) {
        this.SE = SE;
    }

    public int getTM() {
        return TM;
    }

    public void setTM(int TM) {
        this.TM = TM;
    }

    @Override
    public String toString() {
        return "MbtiTester{" +
                "CB=" + CB +
                ", HG=" + HG +
                ", SE=" + SE +
                ", TM=" + TM +
                '}';
    }
}
