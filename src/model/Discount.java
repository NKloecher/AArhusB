package model;

public class Discount {
    private String discount;

    public void setDiscount(String discount) throws Exception {
        if (discount.matches("^[\\d,]+%|-[\\d,]+|[\\d,]+$")) {
            this.discount = discount;
        }
        else {
            throw new Exception("Ugyldig rabat " + discount);
        }

    }

    public double getPrice(double total) throws Exception {
        double newTotal = 0;
        double discountAmount = 0;
        if (discount.startsWith("-")) {
            discountAmount = Double.parseDouble(discount.substring(1, discount.length()));
            if (discountAmount > total) {
                throw new Exception(discountAmount + " rabat er h\u00F8jere end " + total);
            }
            newTotal = total - discountAmount;
        }
        else if (discount.endsWith("%")) {
            discountAmount = Double.parseDouble(discount.substring(0, discount.length() - 1)) / 100;
            if (discountAmount > 1) {
                throw new Exception(discountAmount * 100 + "% er mere end 100% rabat");
            }
            newTotal = total * (1 - discountAmount);
        }
        else {
            newTotal = Double.parseDouble(discount);
        }
        return newTotal;
    }
}
