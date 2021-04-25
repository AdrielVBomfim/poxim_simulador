package com.poxim_simulador.globals;

public class Globals {
    public static int[] entrada = new int[404];
    public static int indicent = 0;
    public static boolean[] emexecucao = new boolean[2];
    public static boolean[] pendexecucao = new boolean[2];
    public static boolean[] pendcontinuar = new boolean[2];
    public static int resfuturo;
    public static boolean fpuerro;
    public static boolean xieee;
    public static boolean yieee;
    public static boolean zieee;
    public static int UltOp;

    public static void inientrada(){
        entrada[0] = 0;
        entrada[1] = 0;
        entrada[2] = 0;
        entrada[3] = 100;

        for(int i = 1; i <= 100; i++){
            entrada[i * 4] = 0;
            entrada[(i * 4) + 1] = 0;
            entrada[(i * 4) + 2] = 0;
            entrada[(i * 4) + 3] = i - 1;
        }

        entrada[20] = 0;
        entrada[21] = 0xBA;
        entrada[22] = 0xBA;
        entrada[23] = 0xCA;
    }
}
