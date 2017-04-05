package gui;

import java.util.regex.Pattern;

public class CustomerEvaluator {

    public boolean isValid(String name, String phone, String email) {
//        System.out.println("navn" + nameIsValid(name));
//        System.out.println("phone" + phoneIsValid(phone));
//        System.out.println("mail" + emailIsValid(email));
        return nameIsValid(name) && phoneIsValid(phone) && emailIsValid(email)
            && addresssValid();
    }

    public boolean nameIsValid(String s) {
        if (s != null && !s.isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean phoneIsValid(String s) {
        return Pattern.matches("\\d*", s);
    }

    public boolean emailIsValid(String s) {
        if (s.isEmpty()) {
            return true;
        }
        return Pattern.matches("^\\w+(\\.\\w+)?@\\w+(\\.\\w+)?$", s);
    }

    public boolean addresssValid() {
        return true; //Kan ikke lige validere en adresse
    }

}
