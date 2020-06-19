package application;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import model.ControlerQueue;
import model.Parse;
import model.Writer;

import java.io.FileWriter;
import model.ControlerQueue;
import model.Json;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class ControlerMain {

    @FXML
    private TextField TxtCsv;

    @FXML
    private TextField TxtJson;

    @FXML
    private Button BtCsv;

    @FXML
    private Button BtJson;

    @FXML
    private Button BtConvert;

    @FXML
    private TextArea TxtArea;
    
    
    long start = 0;
    long finish = 0;
    
    
    @FXML
    private String ProcurarArquivo() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV", "*.csv"));
		File file = fileChooser.showOpenDialog(new Stage());	
		if(file != null) {
			TxtCsv.setText(file.getAbsolutePath());
			return file.getAbsolutePath();	
		}
		return null;
	}
    @FXML
    private String ProcurarPasta() {
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	File file = directoryChooser.showDialog(new Stage());
		if(file != null) {
			TxtJson.setText(file.getAbsolutePath());
		}
		return null;
	}
    
    private String getHora() {
		return new SimpleDateFormat("HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}
    
    private void TempoExecucao() {
		TxtArea.appendText("Tempo de execução: " + (finish - start) / 1000d + " ms\n");
    }
    
    private void ComecarConvert() {
    	ControlerQueue cont = new ControlerQueue();
    	Reader reader;
    	Parse parse = new Parse();
    	try { 
    		reader = Files.newBufferedReader(Paths.get(TxtCsv.getText()));
    		CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
    		List<String[]> lines = csvReader.readAll();
    		start = System.currentTimeMillis();
    		TxtArea.appendText("Começar a leitura: " + getHora() + "\n");
    		cont.Leitura(lines);
    		TxtArea.appendText("Terminar leitura: " + getHora() + "\n");
    		finish = System.currentTimeMillis();
    		TempoExecucao();
    		Thread t = new Thread() {
    			@Override
    			public void run() {
    				parse.Conversao();
    			}
    		};
    		TxtArea.appendText("Começar a conversao: " + getHora() + "\n");
    		start = System.currentTimeMillis();
    		t.start();
    		try {
				t.join();
	    		TxtArea.appendText("Converão finalizada: " + getHora() + "\n");
	    		finish = System.currentTimeMillis();
	    		TempoExecucao();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		try {
    			Writer wd = new Writer(new FileWriter(TxtJson.getText() + "\\arquivo.json"));
    			TxtArea.appendText("Começar a escrita: " + getHora() + "\n");
        		start = System.currentTimeMillis();
    			wd.Escrever(lines);
    			TxtArea.appendText("Escrita finalizada: " + getHora() + "\n");
        		finish = System.currentTimeMillis();
        		TempoExecucao();
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
    		TxtArea.appendText("Conversão realizada com sucesso !!!!");
    	}
    	catch (IOException e) {
		e.printStackTrace();
	}
	 
    }
    @FXML
    public void Iniciar() {
    	
    	File file = new File(TxtCsv.getText());
		
		if(file.exists() && !TxtCsv.getText().equals("")) {
			ComecarConvert();
		}
    }

}
