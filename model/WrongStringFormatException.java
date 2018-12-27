package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

class WrongStringFormatException extends Exception{}
class test {
    public static void checkString() throws Exception {
        Pattern p = Pattern.compile("bulabula");
        if (!p.matcher("Hello world").matches()) {
            throw new WrongStringFormatException();
        }
    }

    public static void main(String[] args) {
        try{
            checkString();
        } catch (WrongStringFormatException i) {
            System.out.println("The string was not match");
        }
    }
}

