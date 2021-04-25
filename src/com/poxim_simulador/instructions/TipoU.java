package com.poxim_simulador.instructions;

import java.io.*;
import java.util.ArrayList;
import java.util.Queue;
import com.poxim_simulador.utils.Utilidades;

public class TipoU extends Instrucao {

    private int OP;
    private int E;
    private int Rx;
    private int Ry;
    private int Rz;

    public TipoU(char tipo, int palavra, int OP, int e, int rx, int ry, int rz) {
        super("", tipo, palavra);
        this.OP = OP;
        E = e;
        Rx = rx + ((e & 0b010) << 4);
        Ry = ry + ((e & 0b001) << 5);
        Rz = rz + ((e & 0b100) << 3);

        switch (this.OP){
            case 0: nome = "add";
                break;
            case 1: nome = "sub";
                break;
            case 2: nome = "mul";
                break;
            case 3: nome = "div";
                break;
            case 4: nome = "cmp";
                break;
            case 5: nome = "shl";
                break;
            case 6: nome = "shr";
                break;
            case 7: nome = "and";
                break;
            case 8: nome = "not";
                break;
            case 9: nome = "or";
                break;
            case 10: nome = "xor";
                break;
            case 11: nome = "push";
                break;
            case 12: nome = "pop";
                break;
            default: System.out.println("erro");
                break;
        }
    }

    public int getOP() {
        return OP;
    }

    public int getE() {
        return E;
    }

    public int getRx() {
        return Rx;
    }

    public int getRy() {
        return Ry;
    }

    public int getRz() {
        return Rz;
    }

    public void Executar(int[] R, int[] G, ArrayList<Instrucao> memoria, BufferedWriter bw, Queue<Integer> terminal) throws IOException{
        if(this.nome.equals("add")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rz) + "," + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry);

            long soma = Integer.toUnsignedLong(R[Rx]) + Integer.toUnsignedLong(R[Ry]);
            long excesso = soma >>> 32;

            if(Long.compareUnsigned(excesso, 0) > 0)
                R[35] = R[35] | 0x00000010;
            else
                R[35] = R[35] & 0xFFFFFFEF;

            R[Rz] = (int) soma;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tFR=0x" + String.format("%08X", R[35]) + ",");
            bw.write(Utilidades.RtoString(this.Rz).toUpperCase() + "=" + Utilidades.RtoString(this.Rx).toUpperCase() + "+" + Utilidades.RtoString(this.Ry).toUpperCase() + "=0x" + String.format("%08X", R[Rz]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("sub")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rz) + "," + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry);

            long sub = Integer.toUnsignedLong(R[Rx]) - Integer.toUnsignedLong(R[Ry]);
            long excesso = sub >>> 32;

            if(Long.compareUnsigned(excesso, 0) > 0)
                R[35] = R[35] | 0x00000010;
            else
                R[35] = R[35] & 0xFFFFFFEF;

            R[Rz] = (int) sub;
            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tFR=0x" + String.format("%08X", R[35]) + ",");
            bw.write(Utilidades.RtoString(this.Rz).toUpperCase() + "=" + Utilidades.RtoString(this.Rx).toUpperCase() + "-" + Utilidades.RtoString(this.Ry).toUpperCase() + "=0x" + String.format("%08X", R[Rz]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("mul")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t" );
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rz) + "," + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry);

            long mult = Integer.toUnsignedLong(R[Rx]) * Integer.toUnsignedLong(R[Ry]);
            long excesso = mult >>> 32;

            if(Long.compareUnsigned(excesso, 0) > 0) {
                R[35] = R[35] | 0x00000010;
                R[34] = (int) excesso;
            }
            else {
                R[35] = R[35] & 0xFFFFFFEF;
                R[34] = 0;
            }

            R[Rz] = (int) mult;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tFR=0x" + String.format("%08X", R[35]) + ",");
            bw.write("ER=0x" + String.format("%08X", R[34]) + ",");
            bw.write(Utilidades.RtoString(this.Rz).toUpperCase() + "=" + Utilidades.RtoString(this.Rx).toUpperCase() + "*" + Utilidades.RtoString(this.Ry).toUpperCase() + "=0x" + String.format("%08X", R[Rz]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("div")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rz) + "," + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry);

            if(Integer.compareUnsigned(R[Ry], 0) == 0) {
                R[35] = R[35] | 0x00000008;
                R[34] = 0;
            }
            else {
                R[35] = R[35] & 0xFFFFFFE7;
                R[34] = Integer.remainderUnsigned(R[Rx], R[Ry]);
                R[Rz] = Integer.divideUnsigned(R[Rx], R[Ry]);
            }

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tFR=0x" + String.format("%08X", R[35]) + ",");
            bw.write("ER=0x" + String.format("%08X", R[34]) + ",");
            bw.write(Utilidades.RtoString(this.Rz).toUpperCase() + "=" + Utilidades.RtoString(this.Rx).toUpperCase() + "/" + Utilidades.RtoString(this.Ry).toUpperCase() + "=0x" + String.format("%08X", R[Rz]));

            if(Integer.compareUnsigned(R[Ry], 0) == 0 && Integer.compareUnsigned(R[35] & 0x00000040, 0) > 0){
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
        else if(this.nome.equals("cmp")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry);

            if(Integer.compareUnsigned(R[Rx], R[Ry]) == 0)
                R[35] = R[35] | 0x00000001;
            else
                R[35] = R[35] & 0xFFFFFFFE;

            if(Integer.compareUnsigned(R[Rx], R[Ry]) < 0)
                R[35] = R[35] | 0x00000002;
            else
                R[35] = R[35] & 0xFFFFFFFD;

            if(Integer.compareUnsigned(R[Rx], R[Ry]) > 0)
                R[35] = R[35] | 0x00000004;
            else
                R[35] = R[35] & 0xFFFFFFFB;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tFR=0x" + String.format("%08X", R[35]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("shl")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rz) + "," + Utilidades.RtoString(this.Rx) + "," + this.Ry;

            long res = ((Integer.toUnsignedLong(R[34]) << 32) | Integer.toUnsignedLong(R[Rx])) << Ry + 1;
            long excesso = res >>> 32;

            if(Long.compareUnsigned(excesso, 0) > 0)
                R[34] = (int) excesso;
            else
                R[34] = 0;

            R[Rz] = (int) res;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tER=0x" + String.format("%08X", R[34]) + ",");
            bw.write(Utilidades.RtoString(this.Rz).toUpperCase() + "=" + Utilidades.RtoString(this.Rx).toUpperCase() + "<<" + (this.Ry + 1) + "=0x" + String.format("%08X", R[Rz]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("shr")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rz) + "," + Utilidades.RtoString(this.Rx) + "," + this.Ry;

            long res = ((Integer.toUnsignedLong(R[34]) << 32) | Integer.toUnsignedLong(R[Rx])) >>> Ry + 1;
            long excesso = res >>> 32;

            if(Long.compareUnsigned(excesso, 0) > 0)
                R[34] = (int) excesso;
            else
                R[34] = 0;

            R[Rz] = (int) res;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tER=0x" + String.format("%08X", R[34]) + ",");
            bw.write(Utilidades.RtoString(this.Rz).toUpperCase() + "=" + Utilidades.RtoString(this.Rx).toUpperCase() + ">>" + (this.Ry + 1) + "=0x" + String.format("%08X", R[Rz]));

            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("and")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rz) + "," + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry);

            R[Rz] = R[Rx] & R[Ry];

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rz).toUpperCase() + "=" + Utilidades.RtoString(this.Rx).toUpperCase() + "&" + Utilidades.RtoString(this.Ry).toUpperCase() + "=0x" + String.format("%08X", R[Rz]));
            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("not")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry);

            R[Rx] = ~R[Ry];

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rx).toUpperCase() + "=~" + Utilidades.RtoString(this.Ry).toUpperCase() + "=0x" + String.format("%08X", R[Rx]));
            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("or")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rz) + "," + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry);

            R[Rz] = R[Rx] | R[Ry];

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rz).toUpperCase() + "=" + Utilidades.RtoString(this.Rx).toUpperCase() + "|" + Utilidades.RtoString(this.Ry).toUpperCase() + "=0x" + String.format("%08X", R[Rz]));
            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("xor")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rz) + "," + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry);

            R[Rz] = R[Rx] ^ R[Ry];

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rz).toUpperCase() + "=" + Utilidades.RtoString(this.Rx).toUpperCase() + "^" + Utilidades.RtoString(this.Ry).toUpperCase() + "=0x" + String.format("%08X", R[Rz]));
            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("push")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry);

            Instrucao endereco = memoria.get(R[Rx]);
            endereco.setPalavra(R[Ry]);

            //System.out.println("####" + " " + String.format("%08X", R[Rx]) + " " + String.format("%08X", R[Ry]) + " " + "####");

            R[Rx]--;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tMEM[" + Utilidades.RtoString(this.Rx).toUpperCase() + "->0x" + String.format("%08X", (R[Rx] + 1) * 4) + "]=" + Utilidades.RtoString(this.Ry).toUpperCase() + "=0x" + String.format("%08X", endereco.getPalavra()));
            bw.newLine();
            R[32]++;
        }
        else if(this.nome.equals("pop")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + Utilidades.RtoString(this.Rx) + "," + Utilidades.RtoString(this.Ry);

            R[Ry]++;
            R[Rx] = memoria.get(R[Ry]).getPalavra();
            int resu = memoria.get(R[Ry]).getPalavra();

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\t" + Utilidades.RtoString(this.Rx).toUpperCase() + "=MEM[" + Utilidades.RtoString(this.Ry).toUpperCase() + "->0x" + String.format("%08X", (R[Ry]) * 4) + "]=0x" + String.format("%08X", resu));
            bw.newLine();
            R[32]++;
        }
        else
            System.out.println("Erro Exec U");
    }
}
