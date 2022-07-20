package com.spring.tobbyspringv1example.learningtest.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.nio.Buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalSumTest {

    Calculator calculator;
    String numFilePath;

    @BeforeEach
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilePath = getClass().getClassLoader().getResource("numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException {
        int sum = calculator.calcSum(numFilePath);
        assertEquals(sum, 10);
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        int multiply = calculator.calcMultiply(numFilePath);
        assertEquals(multiply, 24);
    }

    @Test
    public void concatenateStrings() throws IOException {
        assertEquals(calculator.concatenate(numFilePath), "1234");
    }
}

class Calculator {
    public Integer calcSum(String filePath) throws IOException {
        return lineReadTemplate(filePath, new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value += Integer.valueOf(line);
            }
        }, 0);
    }

    public Integer calcMultiply(String filePath) throws IOException {
        return lineReadTemplate(filePath, new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value *= Integer.valueOf(line);
            }
        }, 1);
    }

    public String concatenate(String filePath) throws IOException {
        return lineReadTemplate(filePath, new LineCallback<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value += line;
            }
        }, "");
    }

    public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            T result = initVal;

            String line = null;
            while((line = br.readLine()) != null) {
                result = callback.doSomethingWithLine(line, result);
            }
            return result;
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        finally {
            if(br != null){
                try{
                    br.close();
                }
                catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public Integer fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException{
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            Integer result = callback.doSomethingWithReader(br);
            return result;
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        finally {
            if(br != null){
                try{
                    br.close();
                }
                catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}

interface BufferedReaderCallback {
    Integer doSomethingWithReader(BufferedReader br) throws IOException;
}

interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}