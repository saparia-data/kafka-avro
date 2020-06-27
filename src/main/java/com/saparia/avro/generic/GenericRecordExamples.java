package com.saparia.avro.generic;

import com.example.Customer;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.*;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.File;
import java.io.IOException;

/**
 * Created by Shree on 24-06-2020.
 */
public class GenericRecordExamples {
    public static void main(String[] args) {

        //step-0 define schema
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse("{\n" +
                "     \"type\": \"record\",\n" +
                "     \"namespace\": \"com.example\",\n" +
                "     \"name\": \"Customer\",\n" +
                "     \"fields\": [\n" +
                "       { \"name\": \"first_name\", \"type\": \"string\", \"doc\": \"First Name of Customer\" },\n" +
                "       { \"name\": \"last_name\", \"type\": \"string\", \"doc\": \"Last Name of Customer\" },\n" +
                "       { \"name\": \"age\", \"type\": \"int\", \"doc\": \"Age at the time of registration\" },\n" +
                "       { \"name\": \"height\", \"type\": \"float\", \"doc\": \"Height at the time of registration in cm\" },\n" +
                "       { \"name\": \"weight\", \"type\": \"float\", \"doc\": \"Weight at the time of registration in kg\" },\n" +
                "       { \"name\": \"automated_email\", \"type\": \"boolean\", \"default\": true, \"doc\": \"Field indicating if the user is enrolled in marketing emails\" }\n" +
                "     ]\n" +
                "}");

        // we build our first customer generic record
        GenericRecordBuilder customerBuilder = new GenericRecordBuilder(schema);
        customerBuilder.set("first_name", "John");
        customerBuilder.set("last_name", "Doe");
        customerBuilder.set("age", 26);
        customerBuilder.set("height", 175f);
        customerBuilder.set("weight", 70.5f);
        customerBuilder.set("automated_email", false);
        GenericData.Record myCustomer = customerBuilder.build();
        System.out.println(myCustomer);

        // we build our second customer generic record which has defaults i.e. automated_email
        GenericRecordBuilder customerBuilderWithDefault = new GenericRecordBuilder(schema);
        customerBuilderWithDefault.set("first_name", "John");
        customerBuilderWithDefault.set("last_name", "Doe");
        customerBuilderWithDefault.set("age", 26);
        customerBuilderWithDefault.set("height", 175f);
        customerBuilderWithDefault.set("weight", 70.5f);
        GenericData.Record customerWithDefault = customerBuilderWithDefault.build();
        System.out.println(customerWithDefault);

        //write generic record to a file in avro format
        final DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        try (DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter)) {
            dataFileWriter.create(myCustomer.getSchema(), new File("customer-generic.avro"));
            dataFileWriter.append(myCustomer);
            System.out.println("Written customer-generic.avro");
            dataFileWriter.close();
        } catch (IOException e) {
            System.out.println("Couldn't write file");
            e.printStackTrace();
        }

        // reading avro file
        final File file = new File("customer-generic.avro");
        final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
        GenericRecord customerRead;
        try (DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader)){
            customerRead = dataFileReader.next();
            System.out.println("Successfully read avro file");
            System.out.println(customerRead.toString());

            // get the data from the generic record
            System.out.println("First name: " + customerRead.get("first_name"));

            // read a non existent field
            System.out.println("Non existent field: " + customerRead.get("not_here"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        // note: we can read our other customer generated using the generic method!
        // end of the day, no matter the method, Avro is Avro!
        final File fileGeneric = new File("customer-generic.avro");
        final DatumReader<Customer> datumReaderGeneric = new SpecificDatumReader<>(Customer.class);
        final DataFileReader<Customer> dataFileReaderGeneric;
        try {
            System.out.println("Reading our specific record");
            dataFileReaderGeneric = new DataFileReader<>(fileGeneric, datumReaderGeneric);
            while (dataFileReaderGeneric.hasNext()) {
                Customer readCustomer = dataFileReaderGeneric.next();
                System.out.println(readCustomer.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
