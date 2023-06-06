/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package buscaminas;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author elena
 * @code Minesweeper
 */
public class Buscaminas {

    /**
     * @param args
     * @param @author Elena
     * @code Minesweeper
     */
    public static void main(String[] args) {
        //Començament del joc
        // Temps entre selecció i selecció: 
        long inici = System.currentTimeMillis();
        // Començament - Menú:
        menu(inici);
        // Fi del joc
    }

    // Métodes específics pel joc Buscamines
    public static void menu(long inici) {
        Scanner sc = new Scanner(System.in);
        System.out.println("What size do you want the board to be? \n\n1. 8x8 board with 10 mines.\n"
                + "2. 16x20 board with 50 mines.\n3. Custom. \n\ta. Rows: min. 4 max. 50.\n\tb. "
                + "Columns: min. 4 max. 50.\n\tc. Mines: min. 3 max. 80% of the board.\n");
        int num = demanarInteger();

        switch (num) {
            case 1:
                // Crida métode crear taulell 8x8 amb 10 bombes: 
                tauler(8, 8, 10, inici);
                break;
            case 2:
                // Crida métode crear taulell 16x20 amb 50 bombes:
                tauler(16, 20, 50, inici);
                break;
            case 3:
                // Crida métode crear taulell personalitzable:
                System.out.println("");
                int files = 0;
                int columnes = 0;
                int bombes = 0;
                do {
                    System.out.println("Enter the number of rows for the board: (min. 4 max. 50)");
                    files = demanarInteger();
                } while (files > 50 || files < 4);
                do {
                    System.out.println("Enter the number of columns for the board: (min. 4 max. 50)");
                    columnes = demanarInteger();
                } while (columnes > 50 || columnes < 4);
                double calculBombes = (files * columnes * 0.80);
                double calculBombesRound = Math.floor(calculBombes);
                do {
                    System.out.print("Enter the number of mines for the board: (min. 3 max. 80% of the board), in this case the maximum is -->");
                    System.out.printf("%.0f%n", calculBombesRound);
                    bombes = demanarInteger();
                } while (bombes > calculBombesRound || bombes < 3);
                tauler(files, columnes, bombes, inici);
                break;
            default:
                System.out.println("This number is not between 1 and 3. Please enter it again.\n");
                menu(inici);
        }

    }

    public static void tauler(int files, int columnes, int bombes, long inici) {
        Random rd = new Random();
        char[][] tauler = new char[files][columnes];

        for (int f = 0; f < tauler.length; f++) {
            for (int c = 0; c < tauler[f].length; c++) {
                tauler[f][c] = '-';
            }
        }

        int contador = bombes;
        while (contador > 0) {
            int fila = rd.nextInt(files);
            int columna = rd.nextInt(columnes);
            if (tauler[fila][columna] == '-') {
                tauler[fila][columna] = 'X';
                contador--;
            }
        }

        for (int f = 0; f < tauler.length; f++) {
            for (int c = 0; c < tauler[f].length; c++) {
                if (tauler[f][c] != 'X') {
                    int contadorBombas = ComptadorBombesCostats(tauler, f, c);
                    if (contadorBombas > 0) {
                        tauler[f][c] = asignacionNumTauler(contadorBombas);
                    }
                }
            }
        }

        System.out.println("\n GAME BOARD \n");
        imprimirTaulerBidi(tauler, files, columnes);
        taulerJugador(tauler, files, columnes, bombes, inici);
    }

    public static int ComptadorBombesCostats(char[][] tauler, int fila, int columna) {
        int ComptadorBombes = 0;
        int files = tauler.length;
        int columnes = tauler[0].length;

        for (int i = fila - 1; i <= fila + 1; i++) {
            for (int j = columna - 1; j <= columna + 1; j++) {
                if (i >= 0 && i < files && j >= 0 && j < columnes && tauler[i][j] == 'X') {
                    ComptadorBombes++;
                }
            }
        }

        return ComptadorBombes;
    }

    public static char asignacionNumTauler(int num) {
        switch (num) {
            case 1:
                return '1';
            case 2:
                return '2';
            case 3:
                return '3';
            case 4:
                return '4';
            case 5:
                return '5';
            case 6:
                return '6';
            case 7:
                return '7';
            default:
                return '8';
        }
    }

    public static void imprimirTaulerBidi(char tauler[][], int files, int columnes) {
        for (int i = 0; i < columnes; i++) {
            System.out.print("=========");
        }
        System.out.println("\n");
        System.out.print("\t");
        for (int i = 0; i < columnes; i++) {
            System.out.print((i + 1) + "\t");
        }
        System.out.println("\n");

        for (int f = 0; f < tauler.length; f++) {
            System.out.print((f + 1) + "\t");
            for (int c = 0; c < tauler[f].length; c++) {
                System.out.print("|" + tauler[f][c] + "|" + "\t");
            }
            System.out.println("\n");
        }
    }

    public static void taulerJugador(char tauler[][], int files, int columnes, int bombes, long inici) {
        int posicioFila = 0;
        int posicioColumna = 0;
        int tornarAJugar;
        int contadorCasellesDestapades = 0;
        boolean sortida = false; // sortida while

        // Creem el tauler del jugador: 
        char[][] tJugador = new char[files][columnes];

        // Omplim el taulell amb punts per poder diferenciar dels guionets i els de més elements:
        for (int f = 0; f < tJugador.length; f++) {
            for (int c = 0; c < tJugador[f].length; c++) {
                tJugador[f][c] = '·';
            }
        }
        System.out.println("\n PLAYER'S BOARD \n");
        do {

            imprimirTaulerBidi(tJugador, files, columnes);

            System.out.println("What position do you want to discover?");

            do {
                System.out.println("Enter Row (remember that entering a -1 will show the Game panel with uncovered bombs: ");
                posicioFila = demanarInteger();
                // Opció pirata
                if (posicioFila == -1) {
                    System.out.println("You decided to look at the counter: \n");
                    imprimirTaulerBidi(tauler, files, columnes);
                    System.out.println("\n");
                }
            } while ((posicioFila - 1) > (files - 1) || (posicioFila - 1) < 0 || posicioFila == -1);

            do {
                System.out.println("Enter Column: ");
                posicioColumna = demanarInteger();
            } while ((posicioColumna - 1) > (columnes - 1) || (posicioColumna - 1) < 0);

            // Comprobacions per donar per vàlida la opció seleccionada:
            if (tJugador[posicioFila - 1][posicioColumna - 1] == '·' && tauler[posicioFila - 1][posicioColumna - 1] != 'X') {
                tJugador[posicioFila - 1][posicioColumna - 1] = tauler[posicioFila - 1][posicioColumna - 1];
                contadorCasellesDestapades++;
                if (contadorCasellesDestapades == ((files * columnes) - bombes)) {
                    imprimirTaulerBidi(tJugador, files, columnes);
                    System.out.println("\nCONGRATULATIONS! You uncovered all the cells that had no bomb! Your total score was: " + puntuaciotJugador(tJugador) + "\n");
                    do {
                        System.out.println("Do you want to play again? Enter \n1. If you want to play again \n2. If you want to get out of the game.\n");
                        tornarAJugar = demanarInteger();
                        if (tornarAJugar != 1 && tornarAJugar != 2) {
                            System.out.println("Meeeeeccc! This character is not correct, try again.");
                        }
                    } while (tornarAJugar != 1 && tornarAJugar != 2);
                    if (tornarAJugar == 1) {
                        System.out.println("\nLET'S START AGAIN! \n");
                        menu(inici);
                    } else {
                        System.out.println("Game Over.");
                        sortida = true;
                    }
                }
                // final de temps entre selecció de casella i casella 
                long fi = System.currentTimeMillis();
                double temps = (double) ((fi - inici) / 1000);
                System.out.println("\nThe time elapsed since the start of the game was " + temps + " seconds.\n");
            } else if (tJugador[posicioFila - 1][posicioColumna - 1] == '·' && tauler[posicioFila - 1][posicioColumna - 1] == 'X') {
                tJugador[posicioFila - 1][posicioColumna - 1] = tauler[posicioFila - 1][posicioColumna - 1];
                System.out.println("\nBOMB! The game is over. \n");
                System.out.println("Your total score was: " + puntuaciotJugador(tJugador) + "\n");
                do {
                    System.out.println("Do you want to play again? Enter \n1. If you want to play again \n2. If you want to get out of the game.\n");
                    tornarAJugar = demanarInteger();
                    if (tornarAJugar != 1 && tornarAJugar != 2) {
                        System.out.println("Meeeeeccc! This character is not correct, try again.");
                    }
                } while (tornarAJugar != 1 && tornarAJugar != 2);
                if (tornarAJugar == 1) {
                    System.out.println("\nLET'S START AGAIN! \n");
                    menu(inici);
                } else {
                    System.out.println("Game Over.");
                    sortida = true;
                }

            } else if (tJugador[posicioFila - 1][posicioColumna - 1] != '·') {
                System.out.println("This position has already been selected. Try again.");
                // final de temps entre selecció de casella i casella 
                long fi = System.currentTimeMillis();
                double temps = (double) ((fi - inici) / 1000);
                System.out.println("\nThe time elapsed since the start of the game was " + temps + " seconds.\n");
            }

        } while (sortida == false);

    }

    // Métode per calcular la puntuació:
    public static int puntuaciotJugador(char tJugador[][]) {
        int puntuacio = 0;
        for (int f = 0; f < tJugador.length; f++) {
            for (int c = 0; c < tJugador[f].length; c++) {
                if (tJugador[f][c] != 'X' && tJugador[f][c] != '-' && tJugador[f][c] != '·') {
                    int numero = Character.getNumericValue(tJugador[f][c]);
                    puntuacio += numero;
                }
            }
        }

        return puntuacio;
    }

    // Métodes estándar: 
    public static int demanarInteger() {
        int num;
        Scanner sc = new Scanner(System.in);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.println("Error! The character entered must be a number. Enter another:");
        }
        return num = sc.nextInt();
    }

}
