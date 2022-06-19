import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testReadStringNotNull() {
        String inJson = "data.json";

        String actual = Main.readString(inJson);

        assertNotNull(actual, actual);
    }

    @Test
    void testlistToJsonNotNull() {
        Employee e1 = new Employee(1,"John","Smith","USA",25);
        Employee e2 = new Employee(2,"Inav","Petrov","RU",23);
        List<Employee> list = new ArrayList<>();
        list.add(e1);
        list.add(e2);

        String actual = Main.listToJson(list);

        assertNotNull(actual,actual);
    }

    @Test
    void testJsonToListNotNull() {
        String json = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25}," +
                "{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";

        List<Employee> list = Main.jsonToList(json);

        assertNotNull(list);
    }
}