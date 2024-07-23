package com.example.javaOnlineComplier;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompilerController {

	@PostMapping("/run")
	public String runCode(@RequestBody CodeRequest codeRequest) {
		String code = codeRequest.getCode();
		try {
			// Javaファイルの作成
			FileWriter fileWriter = new FileWriter("Main.java");
			fileWriter.write(code);
			fileWriter.close();

			// コンパイル
			Process compileProcess = Runtime.getRuntime().exec("javac Main.java");
			compileProcess.waitFor();

			// 実行
			Process runProcess = Runtime.getRuntime().exec("java Main");
			BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
			StringBuilder output = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}
			return output.toString();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@ExceptionHandler(Exception.class)
	public String handleException(Exception e) {
		e.printStackTrace();
		return e.getMessage();
	}
}

class CodeRequest {
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}