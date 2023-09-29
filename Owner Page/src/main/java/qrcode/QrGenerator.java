package qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QrGenerator {

    private String data;
    private String tempPath;
    private Alert alert;

    public QrGenerator(){
        tempPath = System.getProperty("user.dir");
        alert = new Alert(Alert.AlertType.ERROR);
    }

    public Image generateQrCode(String data){
        try {
            Path tempDirectory = Paths.get(tempPath + "\\Temp");
            // Checks of the temp file exists.
            if(!Files.exists(tempDirectory)){
                // If the temp does not exist, it creates one
                Files.createDirectories(tempDirectory);
            }
            tempDirectory = Paths.get(tempPath + "\\Temp\\temp.jpg");
            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500);

            MatrixToImageWriter.writeToPath(matrix, "jpg", tempDirectory);
            this.data = data;
            Image tempImage = new Image(tempDirectory.toString());
            return tempImage;
        }catch (WriterException e) {
            e.printStackTrace();
            createAlert("Error creating qr code");
            alert.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
            createAlert("Error Saving qr code");
            alert.showAndWait();
        }
        return null;
    }

    public void saveQrCode(File file, String name){
        try{
            String fullPath = file.getPath() + "\\" + name;
            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500);

            MatrixToImageWriter.writeToPath(matrix, "jpg", Paths.get(fullPath));
            System.out.println(fullPath);
        }catch (WriterException e) {
            e.printStackTrace();
            createAlert("Error creating qr code");
            alert.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
            createAlert("Error Saving qr code");
            alert.showAndWait();
        }
    }

    private void createAlert(String context){
        alert.setTitle("Error");
        alert.setContentText(context);
    }

}
