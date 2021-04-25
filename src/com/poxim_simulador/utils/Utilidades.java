package com.poxim_simulador.utils;

public class Utilidades {

    public static String RtoString(int R){
        if(R <= 31)
            return "r" + R;
        else if(R == 32)
            return "pc";
        else if(R == 33)
            return "ir";
        else if(R == 34)
            return "er";
        else if(R == 35)
            return "fr";
        else if(R == 36)
            return "cr";
        else if(R == 37)
            return "ipc";
        else{
            System.out.println("Erro R para string " + R);
            return "rx";
        }
    }

    public static String Espacamento(String instrucao, int tamanho){
        return String.format("%1$-" + tamanho + "s", instrucao);
    }

    public static int Exp(int Reg, boolean ieee){
        if(ieee)
            return (Reg & 0x7F800000) >>> 23;
        else
            return (Float.floatToIntBits((float) Reg) & 0x7F800000) >>> 23;

    }
}
