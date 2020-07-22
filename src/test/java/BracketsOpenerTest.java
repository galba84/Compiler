import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BracketsOpenerTest {

    BracketsOpener testInstance = new BracketsOpener();

    @Test
    void openBrackets() {
        testInstance.openBrackets("i:=22+((14-3)*4)/1;");
        System.out.println(testInstance.tempBuffer);
    }
}