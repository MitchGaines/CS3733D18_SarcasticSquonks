package edu.wpi.cs3733d18.teamS.pathfind;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.twiml.TwiML;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Pause;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;
import edu.wpi.cs3733d18.teamS.internationalization.AllText;

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

    public InteractivePhone(ArrayList<String> dir_steps){
        Twilio.init(account_sid, auth_token);

        VoiceResponse.Builder twiml_builder = new VoiceResponse.Builder();

        for(String step : dir_steps) {
            Say line = null;

            if (AllText.getLanguage().equals("es")) {
                line = new Say.Builder(step).language(Say.Language.ES_ES).loop(1).voice(Say.Voice.WOMAN).build();
            } else if(AllText.getLanguage().equals("ru")) {
                line = new Say.Builder(step).language(Say.Language.RU_RU).loop(1).voice(Say.Voice.ALICE).build();
            } else {
                line = new Say.Builder(step).language(Say.Language.EN_US).loop(1).voice(Say.Voice.MAN).build();
            }

            Pause pause = new Pause.Builder().length(3).build();

            twiml_builder.pause(pause).say(line);

        }

        twiml = twiml_builder.build();
    }

    public void callDirections(String to_phone){
        try {

            String host = "159.203.189.146";
            //String host = "127.0.0.1";
            int port = 3000;
            String protocol = "http";
            String path = "/twiml/";

            path += twiml.toUrl() + ".xml";

            URL url = new URL(protocol, host, port, path);
            Call call = Call.creator(
                    new PhoneNumber(to_phone),
                    new PhoneNumber(from_phone),
                    new URI(url.toString())
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


    /*
    private String account_sid = "AC61003cbc0ab62fb17514a23dd877954d";
    private String auth_token = "93baca692310f9af4c1e13a730071a3d";
    private String from_phone = "+18329475093";
    private TwiML twiml;
    private URL root_endpoint;
    private ArrayList<URL> url_list = new ArrayList<>();
    private String host = "159.203.189.146";
    private int port = 3000;
    private String protocol = "http";
    private String path = "/twiml/";
    private int c;


    public InteractivePhone(ArrayList<String> dir_steps){
        Twilio.init(account_sid, auth_token);
        c = 0;
        root_endpoint = createRootEndpoint(dir_steps);
    }

    private String generateString(){
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int count = 10;
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


    private URL createRootEndpoint(ArrayList<String> steps){
        VoiceResponse.Builder twiml_builder = new VoiceResponse.Builder();

        HttpClient client = new DefaultHttpClient();
        HttpPost post = null;
        String key = generateString();
        URL endpoint = null;
        try {
            endpoint = new URL(protocol, host, port, (path + "new_xml/" + key));
            post = new HttpPost(String.valueOf(endpoint));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        post.setHeader("User-Agent", USER_AGENT);
        List<NameValuePair> url_params = new ArrayList<NameValuePair>();

        twiml_builder = twiml_builder.gather(new Gather
                    .Builder()
                    .numDigits(1)
                    .say(new Say
                            .Builder(steps.get(c)
                            + " Press 1 for next step. ")
                            .voice(Say.Voice.MAN)
                            .build())
                    .build());

        try {
            c++;
            if(c >= steps.size()){
                url_params.add(new BasicNameValuePair("xml", twiml_builder.build().toString()));
                post.setEntity(new UrlEncodedFormEntity(url_params));
                client.execute(post);

                return endpoint;
            } else {
                twiml_builder.redirect(new Redirect
                        .Builder(createRootEndpoint(steps)
                        .toURI()).build()).build();

                url_params.add(new BasicNameValuePair("xml", twiml_builder.build().toString()));
                post.setEntity(new UrlEncodedFormEntity(url_params));
                client.execute(post);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return endpoint;
    }

    public void callDirections(String to_phone){
        try {
            System.out.println(root_endpoint);
            Call call = Call.creator(
                    new PhoneNumber(to_phone),
                    new PhoneNumber(from_phone),
                    new URI(root_endpoint.toString())
            ).create();

        } catch (URISyntaxException e){
            e.printStackTrace();
        }
    }
    */

}
