package com.poxim_simulador.instructions;

import java.io.*;
import java.util.ArrayList;
import java.util.Queue;
import com.poxim_simulador.utils.Utilidades;

public class TipoS extends Instrucao{

    private int OP;
    private int IM26;

    public TipoS(char tipo, int palavra, int OP, int IM26) {
        super("", tipo, palavra);
        this.OP = OP;
        this.IM26 = IM26;

        switch (this.OP){
            case 32: nome = "bun";
                break;
            case 33: nome = "beq";
                break;
            case 34: nome = "blt";
                break;
            case 35: nome = "bgt";
                break;
            case 36: nome = "bne";
                break;
            case 37: nome = "ble";
                break;
            case 38: nome = "bge";
                break;
            case 39: nome = "bzd";
                break;
            case 40: nome = "bnz";
                break;
            case 41: nome = "biv";
                break;
            case 42: nome = "bni";
                break;
            case 63: nome = "int";
                break;
            default: System.out.println("erro");
                break;
        }
    }

    public int getOP() {
        return OP;
    }

    public int getIM26() {
        return IM26;
    }

    public void Executar(int[] R, int[] G, ArrayList<Instrucao> memoria, BufferedWriter bw, Queue<Integer> terminal) throws IOException{                                                   //WORK IN PROGRESS!!!
        if(this.nome.equals("bun")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " 0x" + String.format("%08X", this.IM26);

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=0x" + String.format("%08X", this.IM26 << 2));
            bw.newLine();
            R[32] = this.IM26;
        }
        else if(this.nome.equals("beq")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " 0x" + String.format("%08X", this.IM26);

            if(Integer.compareUnsigned(R[35] & 0x00000001, 0) > 0)
                R[32] = IM26;
            else
                R[32]++;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=0x" + String.format("%08X", R[32] << 2));
            bw.newLine();

        }
        else if(this.nome.equals("blt")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " 0x" + String.format("%08X", this.IM26);

            if(Integer.compareUnsigned(R[35] & 0x00000002, 0) > 0)
                R[32] = IM26;
            else
                R[32]++;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=0x" + String.format("%08X", R[32] << 2));
            bw.newLine();

        }
        else if(this.nome.equals("bgt")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " 0x" + String.format("%08X", this.IM26);

            if(Integer.compareUnsigned(R[35] & 0x00000004, 0) > 0)
                R[32] = IM26;
            else
                R[32]++;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=0x" + String.format("%08X", R[32] << 2));
            bw.newLine();
        }
        else if(this.nome.equals("bne")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " 0x" + String.format("%08X", this.IM26);

            if(Integer.compareUnsigned(R[35] & 0x00000001, 0) == 0)
                R[32] = IM26;
            else
                R[32]++;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=0x" + String.format("%08X", R[32] << 2));
            bw.newLine();
        }
        else if(this.nome.equals("ble")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " 0x" + String.format("%08X", this.IM26);

            if(Integer.compareUnsigned(R[35] & 0x00000001, 0) > 0 || Integer.compareUnsigned(R[35] & 0x00000002, 0) > 0)
                R[32] = IM26;
            else
                R[32]++;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=0x" + String.format("%08X", R[32] << 2));
            bw.newLine();
        }
        else if(this.nome.equals("bge")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " 0x" + String.format("%08X", this.IM26);

            if(Integer.compareUnsigned(R[35] & 0x00000001, 0) > 0 || Integer.compareUnsigned(R[35] & 0x00000004, 0) > 0)
                R[32] = IM26;
            else
                R[32]++;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=0x" + String.format("%08X", R[32] << 2));
            bw.newLine();
        }
        else if(this.nome.equals("bzd")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " 0x" + String.format("%08X", this.IM26);

            if(Integer.compareUnsigned(R[35] & 0x00000008, 0) > 0)
                R[32] = IM26;
            else
                R[32]++;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=0x" + String.format("%08X", R[32] << 2));
            bw.newLine();
        }
        else if(this.nome.equals("bnz")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " 0x" + String.format("%08X", this.IM26);

            if(Integer.compareUnsigned(R[35] & 0x00000008, 0) == 0)
                R[32] = IM26;
            else
                R[32]++;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=0x" + String.format("%08X", R[32] << 2));
            bw.newLine();
        }
        else if(this.nome.equals("biv")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " 0x" + String.format("%08X", this.IM26);

            if(Integer.compareUnsigned(R[35] & 0x00000020, 0) > 0)
                R[32] = IM26;
            else
                R[32]++;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=0x" + String.format("%08X", R[32] << 2));
            bw.newLine();
        }
        else if(this.nome.equals("bni")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " 0x" + String.format("%08X", this.IM26);

            if(Integer.compareUnsigned(R[35] & 0x00000020, 0) == 0)
                R[32] = IM26;
            else
                R[32]++;

            bw.write(Utilidades.Espacamento(escrita, 20));
            bw.write("\tPC=0x" + String.format("%08X", R[32] << 2));
            bw.newLine();
        }
        else if(this.nome.equals("int")){
            bw.write("[0x" + String.format("%08X", R[32] * 4)+"]\t");
            String escrita = this.nome + " " + this.IM26;

            bw.write(Utilidades.Espacamento(escrita, 20));

            if(IM26 == 0){
                bw.write("\tCR=0x00000000,PC=0x00000000");
                R[32] = 0x00000000;
            }
            else{
                bw.write("\tCR=0x" + String.format("%08X", this.IM26) + ",PC=0x0000000C");

                R[36] = this.IM26;
                R[37] = R[32] + 1;
                R[32] = 0x00000003;

                bw.newLine();
                bw.write("[SOFTWARE INTERRUPTION]");
            }

            bw.newLine();
        }
        else
            System.out.println("Erro Exec S" + " " + String.format("%08X", palavra));
    }
}