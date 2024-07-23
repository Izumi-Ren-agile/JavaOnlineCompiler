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
		StringBuilder output = new StringBuilder();
		try {
			// Javaファイルの作成
			FileWriter fileWriter = new FileWriter("Main.java");
			fileWriter.write(code);
			fileWriter.close();

			// コンパイル
			Process compileProcess = Runtime.getRuntime().exec("javac Main.java");
			BufferedReader compileErrorReader = new BufferedReader(
					new InputStreamReader(compileProcess.getErrorStream()));
			String compileError;
			while ((compileError = compileErrorReader.readLine()) != null) {
				output.append(compileError).append("\n");
			}
			compileProcess.waitFor();

			// コンパイルエラーがあれば実行しない
			if (compileProcess.exitValue() != 0) {
				return output.toString();
			}

			// 実行
			Process runProcess = Runtime.getRuntime().exec("java Main");
			BufferedReader runOutputReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
			BufferedReader runErrorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
			String runOutput;
			while ((runOutput = runOutputReader.readLine()) != null) {
				output.append(runOutput).append("\n");
			}
			String runError;
			while ((runError = runErrorReader.readLine()) != null) {
				output.append(runError).append("\n");
			}
			runProcess.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return output.toString();
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
