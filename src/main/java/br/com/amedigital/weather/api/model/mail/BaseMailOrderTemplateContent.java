package br.com.amedigital.weather.api.model.mail;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Abstract class to be implemented when the template must have dynamically information, like the Ame Logo.
 * Each value variable in the template must have an equivalent in the class/subclass.
 */
public abstract class BaseMailOrderTemplateContent extends BaseMailTemplateContent {

    /**
     * Total amount of the order
     */
    private BigDecimal totalAmount;

    /**
     * Order date
     */
    private LocalDate orderDate;

    /**
     * Name whom is going to receive the email
     */
    private String receiverName;

    /**
     * Payment info
     */
    public BaseMailOrderTemplateContent() {
    }

    public BaseMailOrderTemplateContent(String imagesHost, BigDecimal totalAmount, LocalDate orderDate, String receiverName) {
        super(imagesHost);
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.receiverName = receiverName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
