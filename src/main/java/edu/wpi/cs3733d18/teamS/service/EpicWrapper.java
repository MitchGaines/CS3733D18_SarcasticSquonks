package edu.wpi.cs3733d18.teamS.service;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class EpicWrapper {

    private FhirContext fhir_ctx = FhirContext.forDstu3();

    public void createEpicService(ServiceRequest sr){
        StringBuilder description = new StringBuilder();
        Task hl7_task = new Task();
        byte[] hl7_task_b = new byte[10];

        description.append("At ")
                .append(sr.requestedDate)
                .append(", ")
                .append(sr.getRequester())
                .append(" requested ")
                .append(sr.getTitle())
                .append(" in ")
                .append(sr.getLocation().getLongName())
                .append(". ")
                .append(sr.getDescription());

        hl7_task.setStatus(Task.TaskStatus.REQUESTED);
        hl7_task.setDescription(description);

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

        /*return ServiceRequest.createService("Test", "this is a test",
                , "Some guy", "Bathrrom", "Some fullfiller");
        return hl7_task_b;
    }
*/
}
