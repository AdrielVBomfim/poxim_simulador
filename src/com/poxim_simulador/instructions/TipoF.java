package com.poxim_simulador.instructions;

import java.io.*;
import java.util.ArrayList;
import java.util.Queue;
import com.poxim_simulador.utils.Utilidades;
import com.poxim_simulador.globals.Globals;

public class TipoF extends Instrucao{

    private int OP;
    private int Rx;
    private int Ry;
    private int IM16;

    public TipoF(char tipo, int palavra,int OP, int rx, int ry, int IM16) {
        super("", tipo, palavra);
        this.OP = OP;
        Rx = rx;
        Ry = ry;
        this.IM16 = IM16;

        switch (this.OP){
            case 16: nome = "addi";
                break;
            case 17: nome = "subi";
                break;
            case 18: nome = "muli";
                break;
            case 19: nome = "divi";
                break;
            case 20: nome = "cmpi";
                break;
            case 21: nome = "andi";
                break;
            case 22: nome = "noti";
                break;
            case 23: nome = "ori";
                break;
            case 24: nome = "xori";
                break;
            case 25: nome = "ldw";
                break;
            case 26: nome = "stw";
                break;
            case 27: nome = "ldb";
                break;
            case 28: nome = "stb";
                break;
            case 48: nome = "call";
                break;
            case 49: nome = "ret";
                break;
            case 50: nome = "isr";
                break;
            case 51: nome = "reti";
                break;
            default: System.out.println("erro");
                break;
        }

    }

    public int getOP() {
        return OP;
    }

    public int getRx() {
        return Rx;
    }

    public int getRy() {
        return Ry;
    }

    public int getIM16() {
        return IM16;
    }

    public void Executar(int[] R, int[] G, ArrayList<Instrucao> memoria, BufferedWriter bw, Queue<Integer> terminal) throws IOException{                                                   //WORK IN PROGRESS!!!
        if(this.nome.equals("addi")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry) + "," + this.IM16;

            long soma = Integer.toUnsignedLong(R[Ry]) + Integer.toUnsignedLong(IM16);
            long excesso = soma >>> 32;

            if(Long.compareUnsigned(excesso, 0) > 0)
                R[35] = R[35] | 0x00000010;
            else
                R[35] = R[35] & 0xFFFFFFEF;

            R[Rx] = (int) soma;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tFR=0x" + String.format("%08X", R[35]) + ",");
            bw.write(Utilidades.RtoString(this.Rx).toUpperCase() + "=" + Utilidades.RtoString(this.Ry).toUpperCase() + "+0x" + String.format("%04X", IM16) + "=0x" + String.format("%08X", R[Rx]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("subi")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry) + "," + this.IM16;

            long sub = Integer.toUnsignedLong(R[Ry]) - Integer.toUnsignedLong(IM16);
            long excesso = sub >>> 32;

            if(Long.compareUnsigned(excesso, 0) > 0)
                R[35] = R[35] | 0x00000010;
            else
                R[35] = R[35] & 0xFFFFFFEF;

            R[Rx] = (int) sub;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tFR=0x" + String.format("%08X", R[35]) + ",");
            bw.write(Utilidades.RtoString(this.Rx).toUpperCase() + "=" + Utilidades.RtoString(this.Ry).toUpperCase() + "-0x" + String.format("%04X", IM16) + "=0x" + String.format("%08X", R[Rx]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("muli")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry) + "," + this.IM16;

            long mult = Integer.toUnsignedLong(R[Ry]) * Integer.toUnsignedLong(IM16);;
            long excesso = mult >>> 32;

            if(Long.compareUnsigned(excesso, 0) > 0) {
                R[35] = R[35] | 0x00000010;
                R[34] = (int) excesso;
            }
            else {
                R[35] = R[35] & 0xFFFFFFEF;
                R[34] = 0;
            }

            R[Rx] = (int) mult;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tFR=0x" + String.format("%08X", R[35]) + ",");
            bw.write("ER=0x" + String.format("%08X", R[34]) + ",");
            bw.write(Utilidades.RtoString(this.Rx).toUpperCase() + "=" + Utilidades.RtoString(this.Ry).toUpperCase() + "*0x" + String.format("%04X", IM16) + "=0x" + String.format("%08X", R[Rx]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("divi")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry) + "," + this.IM16;

            if(Integer.compareUnsigned(IM16, 0) == 0) {
                R[35] = R[35] | 0x00000008;
                R[34] = 0;
            }
            else {
                R[35] = R[35] & 0xFFFFFFE7;
                R[34] = Integer.remainderUnsigned(R[Ry], IM16);
                R[Rx] = Integer.divideUnsigned(R[Ry], IM16);
            }

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tFR=0x" + String.format("%08X", R[35]) + ",");
            bw.write("ER=0x" + String.format("%08X", R[34]) + ",");
            bw.write(Utilidades.RtoString(this.Rx).toUpperCase() + "=" + Utilidades.RtoString(this.Ry).toUpperCase() + "/0x" + String.format("%04X", IM16) + "=0x" + String.format("%08X", R[Rx]));

            if(Integer.compareUnsigned(IM16, 0) == 0 && Integer.compareUnsigned(R[35] & 0x00000040, 0) > 0){
                R[36] = 0x00000001;
                R[37] = R[32] + 1;
                R[32] = 0x00000003;

                bw.newLine();
                bw.write("[SOFTWARE INTERRUPTION]");
            }
            else
                R[32]++;

            bw.newLine();
        }
        else if(this.nome.equals("cmpi")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + this.IM16;

            //System.out.println(this.IM16 + " " + String.format("%08X", R[Rx]));

            if(Integer.compareUnsigned(R[Rx], IM16) == 0)
                R[35] = R[35] | 0x00000001;
            else
                R[35] = R[35] & 0xFFFFFFFE;

            if(Integer.compareUnsigned(R[Rx], IM16) < 0)
                R[35] = R[35] | 0x00000002;
            else
                R[35] = R[35] & 0xFFFFFFFD;

            if(Integer.compareUnsigned(R[Rx], IM16) > 0)
                R[35] = R[35] | 0x00000004;
            else
                R[35] = R[35] & 0xFFFFFFFB;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tFR=0x" + String.format("%08X", R[35]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("andi")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t" );
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry) + "," + this.IM16;

            R[Rx] = R[Ry] & IM16;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rx).toUpperCase() + "=" + Utilidades.RtoString(this.Ry).toUpperCase() + "&0x" + String.format("%04X", IM16) + "=0x" + String.format("%08X", R[Rx]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("noti")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + this.IM16;

            R[Rx] = ~IM16;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rx).toUpperCase() + "=~0x" + String.format("%04X", IM16) + "=0x" + String.format("%08X", R[Rx]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("ori")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry) + "," + this.IM16;

            R[Rx] = R[Ry] | IM16;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rx).toUpperCase() + "=" + Utilidades.RtoString(this.Ry).toUpperCase() + "|0x" + String.format("%04X", IM16) + "=0x" + String.format("%08X", R[Rx]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("xori")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry) + "," + this.IM16;

            R[Rx] = R[Ry] ^ IM16;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rx).toUpperCase() + "=" + Utilidades.RtoString(this.Ry).toUpperCase() + "^0x" + String.format("%04X", IM16) + "=0x" + String.format("%08X", R[Rx]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("ldw")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry) + "," + "0x" + String.format("%04X", this.IM16);

            if((R[Ry] + IM16) << 2  == 0x00008080)
                R[Rx] = G[0];
            else if((R[Ry] + IM16) << 2 == 0x00008800)
                R[Rx] = G[1];
            else if((R[Ry] + IM16) << 2 == 0x00008804)
                R[Rx] = G[2];
            else if((R[Ry] + IM16) << 2 == 0x00008808) {
                if(Globals.UltOp >= 0b00111 && Globals.UltOp <= 0b01001)
                    R[Rx] = G[5];
                else
                    R[Rx] = G[3];
            }
            else if((R[Ry] + IM16) << 2 == 0x0000880C)
                R[Rx] = G[4];
            else if((R[Ry] + IM16) << 2 == 0x00008888)
                R[Rx] = G[7];
            else
                R[Rx] = (memoria.get(R[Ry] + IM16)).getPalavra();

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rx).toUpperCase() + "=MEM[(" + Utilidades.RtoString(this.Ry).toUpperCase() + "+0x" + String.format("%04X", IM16) + ")<<2]=0x" + String.format("%08X", R[Rx]));
            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("stw")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + "0x" + String.format("%04X", this.IM16) + "," + Utilidades.RtoString(this.Ry);

            if((R[Rx] + IM16) << 2 == 0x00008080) {
                G[0] = R[Ry];

                if(Integer.compareUnsigned(G[0] & 0x7FFFFFFF, 0) > 0)
                    Globals.pendexecucao[0] = false;
            }
            else if((R[Rx] + IM16) << 2 == 0x00008800)
                G[1] = R[Ry];
            else if((R[Rx] + IM16) << 2 == 0x00008804)
                G[2] = R[Ry];
            else if((R[Rx] + IM16) << 2 == 0x00008808)
                G[3] = R[Ry];
            else if((R[Rx] + IM16) << 2 == 0x0000880C){
                G[4] = R[Ry];

                int OPFpu = R[Ry] & 0x0000001F;
                float fpux = 0;
                float fpuy = 0;
                float fpuz = 0;

                Globals.pendexecucao[1] = false;
                Globals.pendcontinuar[1] = false;

                if(OPFpu >= 0b00001 && OPFpu <= 0b00100)          //Definição do numero de ciclos
                    if(OPFpu == 0b00100 && G[2] == 0)
                        G[6] = 1;
                    else
                        G[6] = Math.abs(Utilidades.Exp(G[1], Globals.xieee) - Utilidades.Exp(G[2], Globals.yieee)) + 1;
                else if(OPFpu != 0)
                    G[6] = 1;

                if(Globals.xieee)
                    fpux = Float.intBitsToFloat(G[1]);
                else
                    fpux = G[1];

                if(Globals.yieee)
                    fpuy = Float.intBitsToFloat(G[2]);
                else
                    fpuy = G[2];

                fpuz = Float.intBitsToFloat(G[3]);

                if(Integer.compareUnsigned(OPFpu, 0) > 0)
                    if(OPFpu == 0b00001){
                        Globals.resfuturo = Float.floatToIntBits(fpux + fpuy);
                        Globals.fpuerro = false;
                    }
                    else if(OPFpu == 0b00010){
                        Globals.resfuturo = Float.floatToIntBits(fpux - fpuy);
                        Globals.fpuerro = false;
                    }
                    else if(OPFpu == 0b00011){
                        Globals.resfuturo = Float.floatToIntBits(fpux * fpuy);
                        Globals.fpuerro = false;
                    }
                    else if(OPFpu == 0b00100){
                        if(fpuy != 0) {
                            Globals.resfuturo = Float.floatToIntBits((float) fpux / (float) fpuy);
                            Globals.fpuerro = false;
                        }
                        else
                            Globals.fpuerro = true;
                    }
                    else if(OPFpu == 0b00101) {
                        Globals.resfuturo = G[3];
                        Globals.fpuerro = false;
                    }
                    else if(OPFpu == 0b00110) {
                        Globals.resfuturo = G[3];
                        Globals.fpuerro = false;
                    }
                    else if(OPFpu == 0b00111){
                        Globals.resfuturo = (int) Math.ceil(fpuz);
                        Globals.fpuerro = false;
                    }
                    else if(OPFpu == 0b01000) {
                        Globals.resfuturo = (int) Math.floor(fpuz);
                        Globals.fpuerro = false;
                    }
                    else if(OPFpu == 0b01001) {
                        Globals.resfuturo = Math.round(fpuz);
                        Globals.fpuerro = false;
                    }
                    else
                        Globals.fpuerro = true;
            }
            else if((R[Rx] + IM16) << 2 == 0x00008888)
                G[7] = R[Ry];
            else {
                Instrucao endereco = memoria.get(R[Rx] + IM16);
                endereco.setPalavra(R[Ry]);
            }

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tMEM[(" + Utilidades.RtoString(this.Rx).toUpperCase() + "+0x" + String.format("%04X", IM16) + ")<<2]=" + Utilidades.RtoString(this.Ry).toUpperCase() + "=0x" + String.format("%08X",R[Ry]));
            bw.newLine();
            R[32]++;

            if(R[Rx] == 0x00008888 && Integer.compareUnsigned(R[Ry] & 0x000000FF, 0) > 0) {
                terminal.add(R[Ry] & 0x000000FF);
                //System.out.println((char) conteudo);
            }
        }
        else if(this.nome.equals("ldb")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry) + "," + "0x" + String.format("%04X", this.IM16);

            int index = Integer.divideUnsigned(R[Ry] + IM16, 4);
            int byteindex = Integer.remainderUnsigned(R[Ry] + IM16, 4);

            int conteudo;

            if((index << 2) + byteindex == 0x0000888A){
                conteudo = Globals.entrada[Globals.indicent];
                Globals.indicent++;
                byteindex = 3;
            }
            else if(index << 2 == 0x00008080)
                conteudo = G[0];
            else if(index << 2 == 0x00008800)
                conteudo = G[1];
            else if(index << 2 == 0x00008804)
                conteudo = G[2];
            else if(index << 2 == 0x00008808)
                conteudo = G[3];
            else if(index << 2 == 0x0000880C)
                conteudo = G[4];
            else if(index << 2 == 0x00008888)
                conteudo = G[7];
            else
                conteudo = (memoria.get(index)).getPalavra();

            conteudo &= (0xFF000000 >>> (8 * (byteindex)));
            conteudo = conteudo >>> 8 * (3 - byteindex);

            R[Rx] = conteudo;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rx).toUpperCase() + "=MEM[" + Utilidades.RtoString(this.Ry).toUpperCase() + "+0x" + String.format("%04X", IM16) + "]=0x" + String.format("%02X", conteudo));
            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("stb")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + "0x" + String.format("%04X", this.IM16) + "," + Utilidades.RtoString(this.Ry);

            int index = Integer.divideUnsigned(R[Rx] + IM16, 4);
            int byteindex = Integer.remainderUnsigned(R[Rx] + IM16, 4);

            //Instrucao endereco = memoria.get(index);

            int conteudo;

            if(index << 2 == 0x00008080)
                conteudo = G[0];
            else if(index << 2 == 0x00008800)
                conteudo = G[1];
            else if(index << 2 == 0x00008804)
                conteudo = G[2];
            else if(index << 2 == 0x00008808)
                conteudo = G[3];
            else if(index << 2 == 0x0000880C)
                conteudo = G[4];
            else if(index << 2 == 0x00008888)
                conteudo = G[7];
            else
                conteudo = memoria.get(index).getPalavra();

            int novo;

            if(byteindex == 3)
                novo = conteudo & 0xFFFFFF00;
            else if(byteindex == 2)
                novo = conteudo & 0xFFFF00FF;
            else if(byteindex == 1)
                novo = conteudo & 0xFF00FFFF;
            else
                novo = conteudo & 0x00FFFFFF;


            int store = R[Ry] & 0x000000FF;

            novo |= store << (8 * (3 - byteindex));

            if(index << 2 == 0x00008080)
                G[0] = novo;
            else if(index << 2 == 0x00008800)
                G[1] = novo;
            else if(index << 2 == 0x00008804)
                G[2] = novo;
            else if(index << 2 == 0x00008808)
                G[3] = novo;
            else if(index << 2 == 0x0000880C)
                G[4] = novo;
            else if(index << 2 == 0x00008888)
                G[7] = novo;
            else
                memoria.get(index).setPalavra(novo);

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tMEM[" + Utilidades.RtoString(this.Rx).toUpperCase() + "+0x" + String.format("%04X", IM16) + "]=" + Utilidades.RtoString(this.Ry).toUpperCase() + "=0x" + String.format("%02X", R[Ry] & 0x000000FF));
            bw.newLine();
            R[32]++;

            if((index << 2) + byteindex == 0x0000888B) {
                terminal.add(store);

                //System.out.println((char) conteudo);
            }
        }
        else if(this.nome.equals("call")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry) + "," + "0x" + String.format("%04X", IM16);

            if(Integer.compareUnsigned(Rx, 0) > 0)
                R[Rx] = R[32] + 1;

            R[32] = R[Ry] + IM16;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rx).toUpperCase() + "=(PC+4)>>2=0x" + String.format("%08X", R[Rx]) + ",PC=(" + Utilidades.RtoString(this.Ry).toUpperCase() + "+0x" + String.format("%04X", IM16) + ")<<2=0x" + String.format("%08X", R[32] * 4));
            bw.newLine();
        }
        else if(this.nome.equals("isr")){

            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry) + "," + "0x" + String.format("%04X", IM16);

            R[Rx] = R[37];
            R[Ry] = R[36];
            R[32] = IM16;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rx).toUpperCase() + "=IPC>>2=0x" + String.format("%08X", R[37]) + "," + Utilidades.RtoString(this.Ry).toUpperCase() + "=CR=0x" + String.format("%08X", R[36]) + ",PC=0x" + String.format("%08X", IM16 * 4));
            bw.newLine();
        }
        else if(this.nome.equals("ret")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx);

            if(R[Rx] <= 50000)
                R[32] = R[Rx];

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=" + Utilidades.RtoString(this.Rx).toUpperCase() + "<<2=0x" + String.format("%08X", R[Rx] * 4));
            bw.newLine();
        }
        else if(this.nome.equals("reti")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx);

            R[32] = R[Rx];

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=" + Utilidades.RtoString(this.Rx).toUpperCase() + "<<2=0x" + String.format("%08X", R[Rx] * 4));
            bw.newLine();

            if(Globals.pendexecucao[1]){
                R[37] = R[32];
                R[32] = 0x00000002;
                R[36] = 0x01EEE754;

                bw.write("[HARDWARE INTERRUPTION 2]");
                bw.newLine();
            }

            if(Globals.emexecucao[0]) {
                Globals.emexecucao[0] = false;

                if(Globals.pendexecucao[1] || Globals.pendcontinuar[1]){
                    Globals.pendexecucao[1] = false;
                    Globals.emexecucao[1] = true;
                    Globals.pendcontinuar[1] = false;
                }
            }
            else if(Globals.emexecucao[1])
                Globals.emexecucao[1] = false;
        }
        else
            System.out.println("Erro Exec F");
    }
}
