package ai;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.scene.layout.VBox;
import properties.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Communicator {

    public static void sendMessage(String message, String instructions, MFXProgressBar loadingBar, MFXButton sendButton, VBox chatBox){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MessageBox answerBox = new MessageBox("", "answerBox");
                    AIController.startLoading(loadingBar, sendButton, chatBox, answerBox);

                    URL obj = new URL(Properties.AI_URL);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");

                    // Build request body
                    String body = "{\n\"model\": \"mistral-7b-instruct-v0.2.Q5_K_M.gguf\"," +
                            "\n\"messages\": [" +
                            "\n{\n    \"role\": \"system\",\n    \"content\": \"" + instructions +"\"\n}," +
                            "\n{\n    \"role\": \"user\",\n    \"content\": \"" + message + "\"\n}\n],\n" +
                            "\"stream\": true\n," +
                            "\"n_keep\": -1," +
                            "\"cache_prompt\": true" +
                            "\n}";
                    con.setDoOutput(true);

                    OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                    writer.write(body);
                    writer.flush();
                    writer.close();

                    // get response
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null){
                        if(!inputLine.equals("")){
                            if(inputLine.contains("content")){
                                String token = inputLine.split("\"content\":\"")[1].split("\"")[0];
                                if(!token.equals("<|eot_id|>")){
                                    token = token.replaceAll("\\\\n", "\n");
                                    System.out.println(inputLine);
                                    response.append(token);
                                    AIController.streamAnswer(answerBox, response.toString());
                                }
                            }else{
                                System.out.println();
                                break;
                            }
                        }
                    }

                    AIController.finishLoading(loadingBar, sendButton);
                } catch (IOException e) {
                    e.printStackTrace();
                    AIController.showAlert("Error Communicating With AI Assistant");
                }
            }
        }).start();

    }

}
