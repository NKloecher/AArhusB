package model;

import exceptions.DiscountParseException;

import java.io.Serializable;

public class Discount implements Serializable {
    private String discount;

    public String getValue() {
        return discount;
    }

    public void setDiscount(String discount) throws DiscountParseException {
        if (discount.isEmpty() || discount == null) {
            this.discount = null;
        }
        else if (discount.matches("^[\\d,]+%|-[\\d,]+|[\\d,]+$")) {
            this.discount = discount;
        }
        else {
            throw new DiscountParseException("Ugyldig rabat " + discount);
        }

    }

    public double getPrice(double total) throws DiscountParseException {
        if (discount == null) {
            return total;
        }

        double newTotal;
        double discountAmount;
        if (discount.startsWith("-")) {

            discountAmount = Double.parseDouble(discount.substring(1, discount.length()));
            if (discountAmount > total) {
                throw new DiscountParseException(
                    discountAmount + " rabat er h\u00F8jere end " + total);
            }
            newTotal = total - discountAmount;
        }
        else if (discount.endsWith("%")) {
            discountAmount = Double.parseDouble(discount.substring(0, discount.length() - 1)) / 100;
            if (discountAmount > 1) {
                throw new DiscountParseException(discountAmount * 100 + "% er mere end 100% rabat");
            }
            newTotal = total * (1 - discountAmount);
        }
        else {
            newTotal = Double.parseDouble(discount);
        }
        return newTotal;
    }
}
