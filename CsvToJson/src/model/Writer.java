package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

import com.google.gson.Gson;
import java.util.Date;
import java.util.List;
import model.Json;

import model.ControlerQueue;

public class Writer {
	
	FileWriter write;
	Gson gson = new Gson();
	public Writer(FileWriter wf) {
	  this.write = wf;
	}
	
	private String getHora() {
		return new SimpleDateFormat("HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}

	public void Escrever(Object objectList) {

		System.out.println("Escrevendo " + getHora());
		long start = System.currentTimeMillis();
		String JSON = gson.toJson(objectList);
		do {
			
			String tarefa = ControlerQueue.getTarefaEscrever();

			if (tarefa != null) {
				try {
					write.write(JSON + "\n");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} while (ControlerQueue.AcabouEscrever());
		try {
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("Escrita finalizada " + getHora());
		long finish = System.currentTimeMillis();
		System.out.printf("Tempo de escrição: " + "%.3f ms%n", (finish - start) / 1000d);
	}
}
