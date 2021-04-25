package com.poxim_simulador;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import com.poxim_simulador.instructions.Instrucao;
import com.poxim_simulador.instructions.TipoU;
import com.poxim_simulador.instructions.TipoF;
import com.poxim_simulador.instructions.TipoS;
import com.poxim_simulador.masks.Mascaras;
import com.poxim_simulador.globals.Globals;

public class Main {

    public static void main(String[] args) throws IOException{

        long inicio = System.currentTimeMillis();
        FileInputStream fis = new FileInputStream(args[0]);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis)); 			//Linha para que a leitura de .txt seja possivel.

        FileWriter fw = new FileWriter(args[1]);
        BufferedWriter bw = new BufferedWriter(fw);

        Globals.inientrada();
        int[] R = new int[44];
        int[] G = new int[8];
        ArrayList<Instrucao> memoria = new ArrayList<>();
        Queue<Integer> terminal = new LinkedList<>();
        String linha;
        int input;

        while((linha = br.readLine()) != null){              //Leitura de instrucoes
            input = (int) Long.decode(linha).longValue();
            //System.out.println(linha);

            if(Mascaras.Op(input) <= 12)
                memoria.add(new TipoU('U', input, Mascaras.Op(input), Mascaras.Ex(input), Mascaras.Rx(input), Mascaras.Ry(input), Mascaras.Rz(input)));
            else if((Mascaras.Op(input) >= 16 && Mascaras.Op(input) <= 28) || (Mascaras.Op(input) >= 48 && Mascaras.Op(input) <= 51))
                memoria.add(new TipoF('F', input,Mascaras.Op(input), Mascaras.Rx(input), Mascaras.Ry(input), Mascaras.IM16(input)));
            else if((Mascaras.Op(input) >= 32 && Mascaras.Op(input) <= 42) || Mascaras.Op(input) == 63)
                memoria.add(new TipoS('S', input,Mascaras.Op(input), Mascaras.IM26(input)));
            else
                memoria.add(new Instrucao("dado", 'D', input));

        }

        br.close();


        bw.write("[START OF SIMULATION]");          //CODIGO PARA IMPRIMIR INSTRUCOES
        bw.newLine();

        boolean interromper = false;

        do{             //Execução de codigo

            if(memoria.get(R[32]).getNome().equals("int"))
                if(((TipoS) memoria.get(R[32])).getIM26() == 0)
                    interromper = true;

            R[33] = memoria.get(R[32]).getPalavra();
            memoria.get(R[32]).Executar(R, G, memoria, bw, terminal);

            if(Integer.compareUnsigned(G[0] & 0x80000000, 0) > 0){          //Tratamento do Watchdog        AUAU!!!
                if (Integer.compareUnsigned(G[0] & 0x7FFFFFFF, 0) > 0){
                    G[0]--;

                    if(Integer.compareUnsigned(G[0], 0) == 0)
                        Globals.pendexecucao[0] = true;
                }
                else if (Integer.compareUnsigned(R[35] & 0x00000040, 0) > 0) {
                    G[0] = 0x00000000;
                    R[36] = 0xE1AC04DA;
                    R[37] = R[32];
                    R[32] = 0x00000001;

                    Globals.emexecucao[0] = true;
                    Globals.pendexecucao[0] = false;
                    Globals.pendcontinuar[0] = false;

                    if(Globals.emexecucao[1]){
                        Globals.emexecucao[1] = false;
                        Globals.pendexecucao[1] = false;
                        Globals.pendcontinuar[1] = true;
                    }

                    bw.write("[HARDWARE INTERRUPTION 1]");
                    bw.newLine();
                }
            }

            if(Integer.compareUnsigned(G[4] & 0x0000001F, 0) > 0){          //Tratamento da FPU
                if(Integer.compareUnsigned(G[6], 0) == 0 && !Globals.emexecucao[0] && Integer.compareUnsigned(R[35] & 0x00000040, 0) > 0){
                    int OP = G[4] & 0x0000001F;
                    G[4] = 0;

                    if(OP == 0b00001){
                        G[3] = Globals.resfuturo;
                        Globals.zieee = true;
                    }
                    else if(OP == 0b00010){
                        G[3] = Globals.resfuturo;
                        Globals.zieee = true;
                    }
                    else if(OP == 0b00011){
                        G[3] = Globals.resfuturo;
                        Globals.zieee = true;
                    }
                    else if(OP == 0b00100){
                        if(Globals.fpuerro)
                            G[4] = 0x00000020;
                        else {
                            G[3] = Globals.resfuturo;
                            Globals.zieee = true;
                        }
                    }
                    else if(OP == 0b00101) {
                        G[1] = Globals.resfuturo;
                        Globals.xieee = true;
                    }
                    else if(OP == 0b00110) {
                        G[2] = Globals.resfuturo;
                        Globals.yieee = true;
                    }
                    else if(OP == 0b00111){
                        G[5] = Globals.resfuturo;
                        Globals.zieee = false;
                    }
                    else if(OP == 0b01000) {
                        G[5] = Globals.resfuturo;
                        Globals.zieee = false;
                    }
                    else if(OP == 0b01001) {
                        G[5] = Globals.resfuturo;
                        Globals.zieee = false;
                    }
                    else
                        G[4] = 0x00000020;

                    if(OP >= 0b00001 && OP <= 0b01001)
                        Globals.UltOp = OP;

                    if(!Globals.emexecucao[1]){
                        R[36] = 0x01EEE754;
                        R[37] = R[32];
                        R[32] = 0x00000002;

                        Globals.emexecucao[1] = true;
                        Globals.pendexecucao[1] = false;
                        Globals.pendcontinuar[1] = false;

                        bw.write("[HARDWARE INTERRUPTION 2]");
                        bw.newLine();
                    }
                }
                else if(Integer.compareUnsigned(G[6], 0) != 0) {
                    G[6]--;

                    if(Integer.compareUnsigned(G[6], 0) == 0)
                        Globals.pendexecucao[1] = true;
                }
            }

        }while(!interromper);

        if(terminal.peek() != null) {           //Execução do Terminal
            bw.write("[TERMINAL]");
            bw.newLine();

            while (terminal.peek() != null)
                bw.write((char) terminal.remove().intValue());

            bw.newLine();
        }



        bw.write("[END OF SIMULATION]");
        bw.close();

        System.out.println("Tempo: " + (System.currentTimeMillis() - inicio) / 1000.0);

    }
}
