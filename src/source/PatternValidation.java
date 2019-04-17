package source;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

public class PatternValidation {

    public static boolean email(String emailid){
        //email RFC 5322 validation
        //https://howtodoinjava.com/regex/java-regex-validate-email-address/

        return emailid.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[A-Za-z]+[.][A-Za-z.]+");

    }

    public static boolean isnull(String samplestr){
        return (samplestr.isEmpty() || samplestr.equals(""));
    }

    public static boolean isNum(String samplestr){
        return samplestr.matches("\\d{10}");
    }

    public static boolean isnull(ComboBox box){
        if(box.getValue() == null){
            return true;
        }
        else if(box.getValue().toString() == null || box.getValue().toString().equals("")){
            return true;
        }
        else {
            return false;
        }
    }

}
