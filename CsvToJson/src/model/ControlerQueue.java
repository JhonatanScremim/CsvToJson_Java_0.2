package model;

import java.awt.TextArea;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javafx.fxml.FXML;


public class ControlerQueue {
	
	@FXML
	public TextArea TxtArea;
	

	private static boolean FinalizarLeitura = true;
	private static boolean FinalizarConversao = true;
	
	private static List<String[]> FilaParse;
	private static List<String> FilaWrite;
	
	public ControlerQueue() {
		FilaParse = new Vector<String[]>();
		FilaWrite = new Vector<String>();
		
	}
	public static void setFinalizarConvert(boolean convert) {
		FinalizarConversao = convert;
	}
	private void AddTarefa(String[] task) {
		FilaParse.add(task);
	}
	
	public static synchronized String[] getTarefa() {
		if (FilaParse.size() > 0)
			return FilaParse.remove(0);
		return null;
	}
	public static synchronized String getTarefaEscrever() {
		if (FilaWrite.size() > 0)
			return FilaWrite.remove(0);
		return null;
	}
	public static void AddTarefaEscrever(String task) {
		FilaWrite.add(task);
	}
	public static boolean AcabouEscrever() {
		if(FinalizarLeitura) {
			return true;
		} 
		if(FinalizarConversao) {
			return true;
		} 
		return  !FilaParse.isEmpty();		
	}
	
	public static boolean Acabou() {
		if(FinalizarLeitura) {
			return true;
		}
		if(!FilaParse.isEmpty()) {
			return true;
		}
		else {
			return !FilaParse.isEmpty();
		}	
	}
	public void Leitura(List<String[]> lines) {
		System.out.println("Começar leitura " + getHora());
		long start = System.currentTimeMillis();
		int quantTotalRegistro = lines.size();
		int quantRegistros = 0;
		do {
			AddTarefa(lines.get(quantRegistros));
			quantRegistros++;
		}while(quantRegistros < quantTotalRegistro );
		FinalizarLeitura = false;
		System.out.println("Leitura finalizada " + getHora());
		long finish = System.currentTimeMillis();
		System.out.printf("Tempo de leitura: " + "%.3f ms%n", (finish - start) / 1000d);
	}
	
	private String getHora() {
		return new SimpleDateFormat("HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}

}
