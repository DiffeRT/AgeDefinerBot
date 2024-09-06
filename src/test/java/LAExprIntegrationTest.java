import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LAExprIntegrationTest {

    @Test(description = "Check Age() function")
    public void testAgeMinis() {
        Instant now = Instant.now();

        String string = "01.06.1984";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date1 = LocalDate.parse(string, formatter);
        String string2 = "26.04.1986";
        LocalDate date2 = LocalDate.parse(string, formatter);
        Date dateMe = Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateBro = Date.from(date2.atStartOfDay(ZoneId.systemDefault()).toInstant());



    }
}
