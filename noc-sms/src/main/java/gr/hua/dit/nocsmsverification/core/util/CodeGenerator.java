package gr.hua.dit.nocsmsverification.core.util;

import java.util.Random;

public class CodeGenerator {

    public static String generate6DigitCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
