package com.poxim_simulador.masks;

public class Mascaras {

    public static int Op(int hex){
        return (hex & 0xFC000000) >>> 26;
    }

    public static int Rx(int hex){
        return (hex & 0x000003E0) >>> 5;
    }

    public static int Ry(int hex){
        return (hex & 0x0000001F);
    }

    public static int Rz(int hex){
        return (hex & 0x00007C00) >>> 10;
    }

    public static int Ex(int hex){
        return (hex & 0x00038000) >>> 15;
    }

    public static int IM16(int hex){
        return (hex & 0x03FFFC00) >>> 10;
    }

    public static int IM26(int hex){
        return (hex & 0x03FFFFFF);
    }

}
