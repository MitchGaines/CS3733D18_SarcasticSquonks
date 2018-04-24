package edu.wpi.cs3733d18.teamS.service;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Task;

public class EpicWrapper {

    private FhirContext fhir_ctx = FhirContext.forDstu3();

    public void createEpicService(ServiceRequest sr){
        StringBuilder description = new StringBuilder();
        Task hl7_task = new Task();

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
        hl7_task.setDescription(sr.getDescription());
    }
}
