package com.roozen.register.model.view;

import com.roozen.register.model.TenderRecord;

import java.text.DecimalFormat;
import java.util.Date;

public class TenderRecordView {

    private TenderRecord tenderRecord;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public TenderRecordView(TenderRecord tenderRecord) {
        this.tenderRecord = tenderRecord;
    }

    public String getAmountTendered() {
        return "$" + decimalFormat.format(this.tenderRecord.getAmountTendered());
    }

    public String getChangeGiven() {
        return "$" + decimalFormat.format(this.tenderRecord.getChangeGiven());
    }

    // TODO: Format date to desired string as well.
    public Date getTimestamp() {
        return this.tenderRecord.getTimestamp();
    }
}
