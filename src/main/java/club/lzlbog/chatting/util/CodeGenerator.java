package club.lzlbog.chatting.util;

import java.util.Random;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-31 13:04
 **/
public class CodeGenerator {
    private static final int CODE_LENGTH = 6;
    public static String generateCode() {
        double num = new Random().nextDouble();
        String code = String.valueOf((int)(num * 1000000));
        if (code.length() < CODE_LENGTH) {
            code = "0" + code;
        }
        return code;
    }

    public static void main(String[] args) {
        String code = generateCode();
        System.out.println(code);

    }
}
