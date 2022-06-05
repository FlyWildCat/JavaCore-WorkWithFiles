import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String inCsv = "data.csv";
        String inXml = "data.xml";
        String inJson = "data.json";
        String outOnCsv = "dataOnCsv.json";
        String outOnXml = "dataOnXml.json";

        List<Employee> listCSV = parseCSV(columnMapping, inCsv);
        List<Employee> listXML = parseXML(inXml);

        String csvJson = listToJson(listCSV);
        writeString(csvJson,outOnCsv);
        System.out.println("Данные из файла: " + inCsv + " записаны в файл: " + outOnCsv);

        String xmlJson = listToJson(listXML);
        writeString(xmlJson, outOnXml);
        System.out.println("Данные из файла: " + inXml + " записаны в файл: " + outOnXml);

        String json = readString(inJson);
        List<Employee> list = jsonToList(json);
        System.out.println("Данные из файла: " + inJson + " записаны в список:");
        for (Employee e : list) {
            System.out.println(e);
        }

    }

    private static List<Employee> jsonToList(String json) {
        List<Employee> employees = new ArrayList<>();
        JSONParser parser = new JSONParser();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            JSONArray jsonArray = (JSONArray) parser.parse(json);
            for (Object s : jsonArray) {
                Employee employee = gson.fromJson(String.valueOf(s), Employee.class);
                employees.add(employee);
            }
            return employees;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String readString(String inJson) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inJson))) {
            return reader.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Employee> parseXML(String inXml) {
        List<Employee> employees = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inXml);
            NodeList nl = doc.getElementsByTagName("employee");
            for (int i = 0; i < nl.getLength(); i++) {
                employees.add(read(nl.item(i).getChildNodes()));
            }
            return employees;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Employee read(NodeList nl) {
        Employee employee = new Employee();
         for (int j = 0; j < nl.getLength(); j++) {
             Node cn = nl.item(j);
             if (Node.ELEMENT_NODE == cn.getNodeType()) {
                 String scn = cn.getTextContent();
                 String name = cn.getNodeName();
                 switch (name) {
                     case "id": {
                         employee.id = Long.parseLong(scn);
                     } break;
                     case "firstName": {
                         employee.firstName = scn;
                     } break;
                     case "lastName": {
                         employee.lastName = scn;
                     } break;
                     case "country": {
                         employee.country = scn;
                     } break;
                     case "age": {
                         employee.age = Integer.parseInt(scn);
                     } break;
                 }
             }
         }
         return employee;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();

        return gson.toJson(list, listType);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {

        try(CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader).withMappingStrategy(strategy).build();

            return csv.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeString(String jsonString, String fileName) {

        try(FileWriter fw = new FileWriter(fileName)) {
            fw.write(jsonString);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
