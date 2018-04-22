package edu.wpi.cs3733d18.teamS.service;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Task;

public class EpicWrapper {

    private FhirContext fhir_ctx = FhirContext.forDstu3();

    public void createEpicService(String description){
        Task hl7_task = new Task();
        hl7_task.setStatus(Task.TaskStatus.REQUESTED);
        hl7_task.setDescription(description);
        hl7_task.setRequester(new Reference());
    }
/*
    public ServiceRequest getEpicServiceRequest(){
        /*return ServiceRequest.createService("Test", "this is a test",
                , "Some guy", "Bathrrom", "Some fullfiller");

    }
*/
}
