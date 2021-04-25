package com.poxim_simulador.instructions;

import java.io.*;
import java.util.ArrayList;
import java.util.Queue;

public class Instrucao {

    protected String nome;
    protected char tipo;
    protected int palavra;

    public Instrucao(String nome, char tipo, int palavra) {
        this.nome = nome;
        this.tipo = tipo;
        this.palavra = palavra;
    }

    public String getNome() {
        return nome;
    }

    public char getTipo() {
        return tipo;
    }

    public int getPalavra() {
        return palavra;
    }

    public void setPalavra(int palavra) {
        this.palavra = palavra;
    }

    public void Executar(int[] R, int[] G, ArrayList<Instrucao> memoria, BufferedWriter bw, Queue<Integer> terminal) throws IOException{
        bw.write("[INVALID INSTRUCTION @ 0x" + String.format("%08X", R[32] * 4) + "]");


        R[36] = R[32];
        R[37] = R[32] + 1;
        R[32] = 0x00000003;
        R[35] |= 0x00000020;

        bw.newLine();
        bw.write("[SOFTWARE INTERRUPTION]");

        bw.newLine();
    }
}
