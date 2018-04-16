package edu.wpi.cs3733d18.teamS.pathfind;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.TwiML;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class InteractivePhone {
    private String account_sid = "AC61003cbc0ab62fb17514a23dd877954d";
    private String auth_token = "93baca692310f9af4c1e13a730071a3d";
    private TwiML twiml;

    public InteractivePhone(ArrayList<String> dir_steps){
        Twilio.init(account_sid, auth_token);

        VoiceResponse.Builder twiml_builder = new VoiceResponse.Builder();

        for (String step : dir_steps) {
            twiml_builder.say(new Say.Builder(step).build());
        }

        twiml = twiml_builder.build();
    }

    public void callDirections(String to_phone){
        try {
            Call call = Call.creator(
                    new PhoneNumber(to_phone),
                    new PhoneNumber("+18329475093"),
                    new URI(twiml.toUrl().toString())
            ).create();
        } catch (URISyntaxException e){
            e.printStackTrace();
        }
    }

}
