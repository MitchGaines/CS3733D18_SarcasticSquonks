package edu.wpi.cs3733d18.teamS.epic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.wpi.cs3733d18.teamS.service.ServiceRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class EpicWrapper {

    private static org.apache.http.client.HttpClient http_client = HttpClientBuilder.create().build();

    public static void send2Epic(ServiceRequest sr){
        ObjectWriter json_map = new ObjectMapper().writer();
        String json = "";
        try {
            json = json_map.writeValueAsString(sr);
            System.out.println(json);
            StringEntity params = new StringEntity(json);

            HttpPost req = new HttpPost("http://159.203.189.146:3000/epic");
            req.addHeader("content-type", "application/x-www-form-urlencoded");
            req.setEntity(params);
            HttpResponse res = http_client.execute(req);
            System.out.println(res.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /*
    private static FhirContext fhir_ctx = FhirContext.forDstu3();


    public static byte[] createEpicService(ServiceRequest sr){
        StringBuilder description = new StringBuilder();
        Task hl7_task = new Task();
        byte[] hl7_task_b = new byte[10];

        if(sr.getLocation() != null) {
            description.append("At ")
                    .append(sr.getRequestedDate())
                    .append(", ")
                    .append(sr.getRequester())
                    .append(" requested ")
                    .append(sr.getTitle())
                    .append(" in ")
                    .append(sr.getLocation().getLongName())
                    .append(". ")
                    .append(sr.getDescription());
        }

        hl7_task.setStatus(Task.TaskStatus.REQUESTED);
        hl7_task.setDescription(description.toString());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try{
            out = new ObjectOutputStream(bos);
            out.writeObject(hl7_task);
            out.flush();
            hl7_task_b = bos.toByteArray();
        } catch(IOException ex) {
            System.out.println("Unable to make byte array out of HL7 Object");
        } finally {
            try{
                bos.close();
            } catch (IOException ex) {
                System.out.println("Unable to clse ByteArrayOutputStream");
            }
        }

        return hl7_task_b;
    }*/


}
