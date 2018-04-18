package edu.wpi.cs3733d18.teamS.pathfind;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.twiml.TwiML;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Redirect;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class InteractivePhone {
    private String account_sid = "AC61003cbc0ab62fb17514a23dd877954d";
    private String auth_token = "93baca692310f9af4c1e13a730071a3d";
    private String from_phone = "+18329475093";
    private TwiML twiml;
    private ArrayList<URL> url_list = new ArrayList<>();
    private String host = "159.203.189.146";
    private int port = 3000;
    private String protocol = "http";
    private String path = "/twiml/";
    private int c = 0;

    public InteractivePhone(ArrayList<String> dir_steps){
        Twilio.init(account_sid, auth_token);

        twiml = createTwiml(dir_steps);
    }

    private TwiML createTwiml(ArrayList<String> steps){

        VoiceResponse.Builder twiml_builder = new VoiceResponse.Builder();

        twiml_builder = twiml_builder.gather(new Gather
                    .Builder()
                    .numDigits(1)
                    .say(new Say
                            .Builder(steps.get(c)
                            + " Press 1 for next step. ")
                            .voice(Say.Voice.ALICE)
                            .build())
                    .build());

        try {
            c++;
            if(c >= steps.size()){
                twiml_builder.redirect(new Redirect
                        .Builder(new URL(protocol, host, port, path)
                        .toURI()).build()).build();
            } else {
                twiml_builder.redirect(new Redirect
                        .Builder(new URL(protocol, host, port, (path + createTwiml(steps).toUrl()))
                        .toURI()).build()).build();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return twiml_builder.build();

    }

    public void callDirections(String to_phone){
        try {
            System.out.println(new URL(protocol, host, port, (path + twiml.toUrl())).toURI().toString());
            Call call = Call.creator(
                    new PhoneNumber(to_phone),
                    new PhoneNumber(from_phone),
                    new URI(new URL(protocol, host, port, (path + twiml.toUrl())).toURI().toString())
            ).create();

        } catch (URISyntaxException e){
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void textDirections(String to_phone, URL url){
        Message msg = Message.creator(
                new PhoneNumber(to_phone),
                new PhoneNumber(from_phone),
                "Click this link for directions: " + url.toString()
        ).create();
    }
}
