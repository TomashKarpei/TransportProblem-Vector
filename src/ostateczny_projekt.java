import java.sql.SQLOutput;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
public class ostateczny_projekt {
    public static int min(int a, int b) {
        if (a < b)
            return a;
        else
            return b;
    }
    public static void main(String[] args) throws IOException{
        //Instantiating the File class
//        File file = new File("wynik.txt");
//        //Instantiating the PrintStream class
//        PrintStream stream = new PrintStream(file);
//        System.out.println("Plik z wynikami znajduje się w: "+file.getAbsolutePath());
//        System.setOut(stream);
        System.out.println("Problem transportowy");
        int ilosc_punktow_nadania = 3, ilosc_punktow_odbioru = 4, ilosc_wierszy = 5, osobnikow_w_populacji = 3;
        int ilosc_genow = ilosc_punktow_nadania * ilosc_punktow_odbioru;
        int[][] tab_koszt = {
                {10, 0, 20, 11},
                {12, 7, 9, 20},
                {0, 14, 16, 18}
        };
        int[] sour = {15, 25, 5}; //tablice punktów nadania
        int[] dest = {5, 15, 15, 10};//odbioru
        //////////////////////////////////////////////////////////////Losowanie osobnikow
        int populacja[][][] = new int[osobnikow_w_populacji][ilosc_wierszy][ilosc_genow];
        int ocena[][] = new int[osobnikow_w_populacji][ilosc_genow];
        int tab_przewozow[][][][] = new int[osobnikow_w_populacji][ilosc_genow][sour.length][dest.length];
        Random r = new Random();
        for (int i = 0; i < ilosc_wierszy; i++) {
            Set<Integer> set = new LinkedHashSet<Integer>();
            while (set.size() < ilosc_genow) {
                set.add(r.nextInt(ilosc_genow));
            }
            ArrayList<Integer> al = new ArrayList<>(set);
            int o = 0;
            for (int lczb : al) {
                populacja[0][i][o] = lczb;
                o++;
            }
        }
        int suma_sour_pierwszej_tabeli = 0;
        for (int test = 0; test < ilosc_genow; test++) {
            suma_sour_pierwszej_tabeli += populacja[0][0][test];
        }
        ////////////////////////////////////////////////////////////ALGORYTM GENETYCZNY
        for (int osobnik = 0; osobnik < osobnikow_w_populacji; osobnik++) {
            int[] dest_populacja = new int[ilosc_genow];
            int[] sour_populacja = new int[ilosc_wierszy];
            System.out.println("Osobnik nr " + (osobnik + 1));
            ArrayList<Integer> indeksy_wierszy = new ArrayList<>();
            Set<Integer> set2 = new LinkedHashSet<Integer>();
            while (set2.size() < ilosc_wierszy) {
                set2.add(r.nextInt(ilosc_wierszy));
            }
            ArrayList<Integer> al2 = new ArrayList<>(set2);
            int o = 0;
            for (int lczb : al2) {
                indeksy_wierszy.add(lczb);
                o++;
            }
            Collections.shuffle(indeksy_wierszy);
            for (int z = 0; z < ilosc_wierszy; z++) {
                for (int zz = 0; zz < ilosc_genow; zz++) {
                    populacja[osobnik][z][zz] = populacja[0][indeksy_wierszy.get(z)][zz];
                }
            }
            int suma_dest = 0;
            int suma_sour = 0;
            System.out.print(" \\\t");
            for (int i = 0; i < ilosc_genow; i++) {
                System.out.print((i + 1) + ".\t");
            }
            System.out.println();
            for (int i = 0; i < ilosc_wierszy; i++) {
                System.out.print((i + 1) + ".\t");
                for (int j = 0; j < ilosc_genow; j++) {
                    System.out.print(populacja[osobnik][i][j] + "\t");
                    sour_populacja[i] += populacja[osobnik][i][j];
                    dest_populacja[j] += populacja[osobnik][i][j];
                }
                System.out.println("\t" + sour_populacja[i] + "+");
                suma_sour += sour_populacja[i];
            }
            for (int j = 0; j < ilosc_genow + 2; j++) {
                System.out.print("\t");
            }
            System.out.print("=" + suma_sour);
            System.out.println();
            System.out.print("\t");
            for (int i = 0; i < ilosc_genow; i++) {
                if (i == ilosc_genow - 1)
                    System.out.print(dest_populacja[i] + "\t");
                else
                    System.out.print(dest_populacja[i] + "+" + "\t");
                suma_dest += dest_populacja[i];
            }
            System.out.println("=" + suma_dest);
            System.out.println("\nTablica kosztów");
            System.out.print(" \\\t");
            for (int i = 0; i < ilosc_punktow_odbioru; i++) {
                System.out.print(dest[i] + "\t");
            }
            System.out.println();
            for (int i = 0; i < ilosc_punktow_nadania; i++) {
                System.out.print(sour[i] + "\t");
                for (int j = 0; j < ilosc_punktow_odbioru; j++) {
                    System.out.print(tab_koszt[i][j] + "\t");
                }
                System.out.println();
            }
            System.out.println("\nTablica przewozów");
            int a = 0;
            int b = 0;
            //przypisanie tablic przewozow do jednej tablicy
            int najnisza_wartosc = 1000;
            for (int n = 0; n < ilosc_wierszy; n++) {
                int[] temp_punkty_nadania = {15, 25, 5};
                int[] temp_punkty_odbioru = {5, 15, 15, 10};
                for (int k = 0; k < ilosc_genow; k++) {
                    int pomocnicza = populacja[osobnik][n][k];
                    a = 0;
                    b = 0;
                    if (pomocnicza < dest.length) {
                        a = 0;
                        b = pomocnicza;
                    } else {
                        while (pomocnicza >= dest.length) {
                            pomocnicza -= dest.length;
                            a++;
                        }
                        b = pomocnicza;
                    }
                    tab_przewozow[osobnik][n][a][b] = min(temp_punkty_nadania[a], temp_punkty_odbioru[b]);
                    temp_punkty_nadania[a] -= tab_przewozow[osobnik][n][a][b]; //aktualizowanie wartosci punktow nadania
                    temp_punkty_odbioru[b] -= tab_przewozow[osobnik][n][a][b]; // aktualizowanie wartosci punktow odbioru
                    ocena[osobnik][n] += tab_przewozow[osobnik][n][a][b] * tab_koszt[a][b];
                }
                if (ocena[osobnik][n] < najnisza_wartosc) {
                    najnisza_wartosc = ocena[osobnik][n];
                }
            }
            int najnisza_wartosc_index = 0;
            for (int i = 0; i < ocena[osobnik].length; i++) {
                if (ocena[osobnik][i] == najnisza_wartosc)
                    najnisza_wartosc_index = i;
            }
            System.out.println("Optymalne rozwiązanie");
            System.out.print(" \\\t");
            for (int i = 0; i < ilosc_punktow_odbioru; i++) {
                System.out.print(dest[i] + "\t");
            }
            System.out.println();
            for (int i = 0; i < sour.length; i++) {
                System.out.print(sour[i] + "\t");
                for (int j = 0; j < dest.length; j++) {
                    System.out.print(tab_przewozow[osobnik][najnisza_wartosc_index][i][j] + "\t");
                }
                System.out.println();
            }
            System.out.println("Funkcja oceny: " + ocena[osobnik][najnisza_wartosc_index] + "\n");
        }

        System.out.println("Algorytm genetyczny: \n");
        for (int epoka = 1; epoka <= 100; epoka++){

            int suma_sour = 0;
            int suma_dest = 0;
            int[] sour_populacja = new int[ilosc_wierszy];
            int[] dest_populacja = new int[ilosc_genow];
            System.out.println("Epoka nr "+epoka+"\n");
            ////////////////////////////////mutacja
    //        Random r = new Random();
            int wybrany_osobnik = r.nextInt(osobnikow_w_populacji);
            System.out.println("MUTACJA dla osobnika nr " + (wybrany_osobnik + 1));
            System.out.print(" \\\t");
            for (int i = 0; i < ilosc_genow; i++) {
                System.out.print((i + 1) + ".\t");
            }
            System.out.println();
            for (int i = 0; i < ilosc_wierszy; i++) {
                System.out.print((i + 1) + ".\t");
                for (int j = 0; j < ilosc_genow; j++) {
                    System.out.print(populacja[wybrany_osobnik][i][j] + "\t");
                    sour_populacja[i] += populacja[wybrany_osobnik][i][j];
                    dest_populacja[j] += populacja[wybrany_osobnik][i][j];
                }
                System.out.println("\t" + sour_populacja[i] + "+");
                suma_sour += sour_populacja[i];
            }
            for (int j = 0; j < ilosc_genow + 2; j++) {
                System.out.print("\t");
            }
            System.out.print("=" + suma_sour);
            System.out.println();
            System.out.print("\t");
            for (int i = 0; i < ilosc_genow; i++) {
                if (i == ilosc_genow - 1)
                    System.out.print(dest_populacja[i] + "\t");
                else
                    System.out.print(dest_populacja[i] + "+" + "\t");
                suma_dest += dest_populacja[i];
            }
            System.out.println("=" + suma_dest);
            //Losowanie podmacierz populacji
            int k1, k2, w1, w2, min;
            ArrayList<Integer> wiersze = new ArrayList<Integer>();
            ArrayList<Integer> kolumny = new ArrayList<Integer>();
    //        kolumny.add(1);kolumny.add(2);kolumny.add(3);kolumny.add(4);

            Set<Integer> set = new LinkedHashSet<Integer>();
            while (set.size() < ilosc_punktow_nadania) {
                set.add(r.nextInt(ilosc_wierszy - 1) + 1);
            }
            ArrayList<Integer> al = new ArrayList<>(set);
            int o = 0;
            for (int lczb : al) {
                wiersze.add(lczb);
                o++;
            }

            Set<Integer> set2 = new LinkedHashSet<Integer>();
            while (set2.size() < ilosc_punktow_odbioru) {
                set2.add(r.nextInt(ilosc_genow - 1) + 1);
            }
            ArrayList<Integer> al2 = new ArrayList<>(set2);
            o = 0;
            for (int lczb : al2) {
                kolumny.add(lczb);
                o++;
            }
            int it = 0;
            Collections.sort(kolumny); //posortowanie indeksow rosnąco
            Collections.sort(wiersze);
            System.out.print("Wylosowano kolumny {");
            it = 0;
            for (int i = 0; i < kolumny.size(); i++) {
                if (i == 0 || it == dest.length)
                    System.out.print(kolumny.get(i));
                else
                    System.out.print(", " + kolumny.get(i));
                it++;
            }
            System.out.print("}");
            System.out.println();
            System.out.print("Wylosowano wiersze {");
            for (int i = 0; i < wiersze.size(); i++) {
                if (i == 0)
                    System.out.print(wiersze.get(i));
                else
                    System.out.print(", " + wiersze.get(i));
            }
            System.out.print("}\n");
            System.out.println("Podmacierz W wylosowanych punktów");
            int[][] mutacja = new int[wiersze.size()][kolumny.size()];
            System.out.print(" \\\t");
            for (int i = 0; i < kolumny.size(); i++) {
                System.out.print(kolumny.get(i) + ".\t");
            }
            System.out.println();
            for (int i = 0; i < wiersze.size(); i++) {
                System.out.print(wiersze.get(i) + ".\t");
                for (int j = 0; j < kolumny.size(); j++) {
                    mutacja[i][j] = populacja[wybrany_osobnik][wiersze.get(i) - 1][kolumny.get(j) - 1];
                    System.out.print(mutacja[i][j] + "\t");
                }
                System.out.println();
            }
            //ponowna inicjalizacja
            //ograniczenia
            int[] destw = new int[kolumny.size()]; //suma rzędu kolumn
            int[] sourw = new int[wiersze.size()]; //suma rzędu wierszy
            System.out.print("Zauważmy, że ");
            for (int i = 0; i < wiersze.size(); i++) {
                sourw[i] = 0;
                for (int j = 0; j < kolumny.size(); j++) {
                    sourw[i] += mutacja[i][j];
                }
                if (i == 0)
                    System.out.print("sourw[" + (i + 1) + "] = " + sourw[i]);
                else
                    System.out.print(", sourw[" + (i + 1) + "] = " + sourw[i]);
            }
            for (int i = 0; i < kolumny.size(); i++) {
                destw[i] = 0;
                for (int j = 0; j < wiersze.size(); j++) {
                    destw[i] += mutacja[j][i];
                }
                System.out.print(", destw[" + (i + 1) + "] = " + destw[i]);
            }
            System.out.println();
            int a = 0;
            int b = 0;
            int ocenaw[] = new int[ilosc_genow];
            for (int i = 0; i < ilosc_genow; i++) {
                ocenaw[i] = 0;
            }//wyzerowanie tabicy kosztow
            int[][] tab_kosztw = new int[sour.length][dest.length];
            for (int i = 0; i < mutacja.length; i++) {
                for (int j = 0; j < mutacja[0].length; j++) {
                    tab_kosztw[i][j] = mutacja[i][j];
                }
            }
            //przypisanie tablic przewozow do jednej tablicy
            int tablicaw[][][] = new int[ilosc_genow][ilosc_punktow_nadania][ilosc_punktow_odbioru];
            int najmniejsza_index = 0;
            int najmniejsza_wartosc = 10000;
            for (int n = 0; n < ilosc_wierszy; n++) {
                int[] temp_punkty_nadania = new int[sour.length];
                for (int i = 0; i < sourw.length; i++) {
                    temp_punkty_nadania[i] = sourw[i];
                }
                int[] temp_punkty_odbioru = new int[dest.length];
                for (int i = 0; i < destw.length; i++) {
                    temp_punkty_odbioru[i] = destw[i];
                }
                for (int k = 0; k < ilosc_genow; k++) {
                    int pomocnicza = populacja[wybrany_osobnik][n][k];
                    a = 0;
                    b = 0;
                    if (pomocnicza < dest.length) {
                        a = 0;
                        b = pomocnicza;
                    } else {
                        while (pomocnicza >= dest.length) {
                            pomocnicza -= dest.length;
                            a++;
                        }
                        b = pomocnicza;
                    }
                    if (a<ilosc_punktow_nadania && b<ilosc_punktow_odbioru) {
                        tablicaw[n][a][b] = min(temp_punkty_nadania[a], temp_punkty_odbioru[b]);
                        temp_punkty_nadania[a] -= tablicaw[n][a][b]; //aktualizowanie wartosci punktow nadania
                        temp_punkty_odbioru[b] -= tablicaw[n][a][b]; // aktualizowanie wartosci punktow odbioru
                        ocenaw[n] += tablicaw[n][a][b] * tab_kosztw[a][b];
                    }
                }
                boolean poprawna_tablica = true;
                int dodano_destw = 0;
                int dodano_sourw = 0;
//                System.out.println("Tablica przewozów nr "+(n+1));
//                System.out.print(" \\\t");
                for (int i = 0; i < ilosc_punktow_odbioru; i++) {
//                    System.out.print(destw[i]+"\t");
                }
//                System.out.println();
                for (int i = 0; i < sourw.length; i++) {
//                    System.out.print(sourw[i]+"\t");
                    int warunek_sour = 0;
                    for (int j = 0; j < dest.length; j++) {
                        warunek_sour += tablicaw[n][i][j];
                        dodano_sourw+=1;
//                        System.out.print(tablicaw[n][i][j] + "\t");
                    }
//                    System.out.print(warunek_sour);
                    if(sourw[i] == warunek_sour)
                        poprawna_tablica = true;
                    else{
                        poprawna_tablica = false;
                        break;
                    }
//                    System.out.println(poprawna_tablica);
                }

                System.out.print("\t");
                for (int i = 0; i < destw.length; i++) {
                    int warunek_dest = 0;
                    for (int j = 0; j < sourw.length; j++) {
                        warunek_dest += tablicaw[n][j][i];
                        dodano_destw+=1;
                    }
//                    System.out.print(warunek_dest+"-");
                    if(destw[i] == warunek_dest)
                        poprawna_tablica = true;
                    else {
                        poprawna_tablica = false;
                        break;
                    }
//                    System.out.print(poprawna_tablica+"\t");
                }
//                    if (ocenaw[n] < najmniejsza_wartosc) {
//                        najmniejsza_wartosc = ocenaw[n];
//                        najmniejsza_index = n;
//                    }
//                System.out.println(dodano_destw+"-dest.length == "+dodano_sourw+"-sour.length");
//                System.out.println("Funkcja oceny: " + ocenaw[n] + "\n");
                if (dodano_sourw == dodano_destw){
                    najmniejsza_index = n;
                    najmniejsza_wartosc = ocenaw[n];
                }
            }
            ////wybranie najlepszej tablicy przewozów
//            for (int i = 0; i<ilosc_wierszy; i++){
//                for (int j = 0; j < sourw.length; j++) {
//                    System.out.print(wiersze.get(j) + ".\t");
//                    for (int k = 0; k < dest.length; k++) {
//                        System.out.print(tablicaw[najmniejsza_index][j][k] + "\t");
//                    }
//                    System.out.println();
//                }
//            }

            ////
            if (najmniejsza_wartosc == 10000) {
                System.out.println("\n\nPoprawność danych jest zachowana maksymalnie do końca poprzedniej epoki, łączna ilość poprawnie wykonanych epok wynosi = "+(epoka-1));
                break;
            }
//            System.out.println("Najmniejsza ocena wynosi = "+najmniejsza_wartosc+" i odpowiada ona tablicy nr "+(najmniejsza_index+1));
            System.out.println("\nPo ponownej inicjalizacji macierzy W może ona przyjąć nastepującą postać");
            System.out.print(" \\\t");
            for (int i = 0; i < ilosc_punktow_odbioru; i++) {
                System.out.print(kolumny.get(i) + ".\t");
            }
            System.out.println();
            for (int i = 0; i < sourw.length; i++) {
                System.out.print(wiersze.get(i) + ".\t");
                for (int j = 0; j < dest.length; j++) {
                    System.out.print(tablicaw[najmniejsza_index][i][j] + "\t");
                }
                System.out.println();
            }
            //podmienić wartości po wykonanej mutacji w całej populacji
            for (int i = 0; i < sourw.length; i++) {
                for (int j = 0; j < dest.length; j++) {
    //                System.out.println("("+wiersze.get(i)+", "+kolumny.get(j)+") = "+tablicaw[najmniejsza_index][i][j]);
                    populacja[wybrany_osobnik][wiersze.get(i) - 1][kolumny.get(j) - 1] = tablicaw[najmniejsza_index][i][j];
                }
            }
            int dest_warunek[] = new int[ilosc_genow];
            int sour_warunek[] = new int[ilosc_wierszy];
            System.out.println("\nPopulacja nr " + (wybrany_osobnik + 1) + " po wykonaniu mutacji");
            System.out.print(" \\\t");
            for (int i = 0; i < ilosc_genow; i++) {
                System.out.print((i + 1) + ".\t");
            }
            System.out.println();
            suma_sour = 0;
            suma_dest = 0;
            for (int i = 0; i < ilosc_wierszy; i++) {
                System.out.print((i + 1) + ".\t");
                sour_warunek[i] = 0;
                for (int j = 0; j < ilosc_genow; j++) {
                    System.out.print(populacja[wybrany_osobnik][i][j] + "\t");
                    sour_warunek[i] += populacja[wybrany_osobnik][i][j];
                    suma_sour += populacja[wybrany_osobnik][i][j];
                }
                System.out.print(sour_warunek[i]);
                System.out.println();
            }
            for (int i = 0; i < ilosc_genow; i++) {
                dest_warunek[i] = 0;
                for (int j = 0; j < ilosc_wierszy; j++) {
                    dest_warunek[i] += populacja[wybrany_osobnik][j][i];
                    suma_dest += populacja[wybrany_osobnik][j][i];
                }

            }
            for (int j = 0; j < ilosc_genow+1; j++) {
                System.out.print("\t");
            }
            System.out.print("="+suma_sour);
            System.out.println();
            for (int j = 0; j < ilosc_genow; j++) {
                System.out.print(dest_warunek[j] + "\t");
            }
            System.out.print("="+suma_dest);
            //////////////////////////////krzyżowanie
            int losowy_krzyzowania2 = r.nextInt(osobnikow_w_populacji - 1) + 1;
            int losowy_krzyzowania = r.nextInt(losowy_krzyzowania2);
            System.out.println("\n\nKRZYŻOWANIE dla osobnika " + (losowy_krzyzowania + 1) + "ego z " + (losowy_krzyzowania2 + 1) + "im");
            int[][] V1 = new int[ilosc_wierszy][ilosc_genow];
            int[][] V2 = new int[ilosc_wierszy][ilosc_genow];
            int[][] DIV = new int[ilosc_wierszy][ilosc_genow];
            int[][] REM = new int[ilosc_wierszy][ilosc_genow];
            int[][] REM1 = new int[ilosc_wierszy][ilosc_genow];
            int[][] REM2 = new int[ilosc_wierszy][ilosc_genow];
            int[][] V3 = new int[ilosc_wierszy][ilosc_genow];
            int[][] V4 = new int[ilosc_wierszy][ilosc_genow];
            int[] sourm = new int[ilosc_wierszy];
            int[] destm = new int[ilosc_genow];
            int[] sour_rem = new int[ilosc_wierszy];
            int[] dest_rem = new int[ilosc_genow];
            int[] sour_rem1 = new int[ilosc_wierszy];
            int[] dest_rem1 = new int[ilosc_genow];
            int[] sour_rem2 = new int[ilosc_wierszy];
            int[] sour_rem_test = new int[ilosc_wierszy];
            int[] dest_rem2 = new int[ilosc_genow];
            for (int i = 0; i<ilosc_genow; i++){
                dest_rem[i] = 0;
                dest_rem1[i] = 0;
                dest_rem2[i] = 0;
            }
            for (int i = 0; i<ilosc_wierszy; i++){
                sour_rem[i] = 0;
                sour_rem1[i] = 0;
                sour_rem2[i] = 0;
            }
            for (int i = 0; i < ilosc_wierszy; i++) {
                for (int j = 0; j < ilosc_genow; j++) {
                    //uzupełnienie wybranego osobnika dla macierzy V1, a potem drugiego do V2
                    V1[i][j] = populacja[losowy_krzyzowania][i][j];
                    V2[i][j] = populacja[losowy_krzyzowania2][i][j];
                    //div = (v1+v2)/2
                    DIV[i][j] = (V1[i][j] + V2[i][j]) / 2;
                    sourm[i] += DIV[i][j];
                    destm[j] += DIV[i][j];
                    //rem = (v1+v2)%2
                    REM[i][j] = (V1[i][j] + V2[i][j]) % 2;
                    sour_rem[i] += REM[i][j];
                    dest_rem[j] += REM[i][j];
                    //wypelnienie zerami macierzy rem1 i rem2

                }
            }
            //wypisanie
            System.out.println("\t\t\t\tOSOBNIK NR" + (losowy_krzyzowania + 1) + " - V1 \t\t\t\t\t\t\t\t\t\t\tOSOBNIK NR" + (losowy_krzyzowania2 + 1) + " - V2");
            System.out.print(" \\\t");
            for (int i = 0; i < ilosc_genow; i++) {
                System.out.print((i + 1) + ".\t");
            }
            System.out.print("\t\t \\\t");
            for (int i = 0; i < ilosc_genow; i++) {
                System.out.print((i + 1) + ".\t");
            }
            System.out.println();
            for (int i = 0; i < ilosc_wierszy; i++) {
                System.out.print((i + 1) + ".\t");
                for (int j = 0; j < ilosc_genow; j++) {
                    System.out.print(V1[i][j] + "\t");
                }
                System.out.print("\t\t");
                System.out.print((i + 1) + ".\t");
                for (int j = 0; j < ilosc_genow; j++) {
                    System.out.print(V2[i][j] + "\t");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("\t\t\t\tOSOBNIK NR" + (losowy_krzyzowania + 1) + " - DIV \t\t\t\t\t\t\t\t\t\t\tOSOBNIK NR" + (losowy_krzyzowania2 + 1) + " - REM");
            System.out.print(" \\\t");
            for (int i = 0; i < ilosc_genow; i++) {
                System.out.print((i + 1) + ".\t");
            }
            System.out.print("\t\t \\\t");
            for (int i = 0; i < ilosc_genow; i++) {
                System.out.print((i + 1) + ".\t");
            }
            System.out.println();
//            int rem_sour[] = new int[ilosc_genow];
//            int rem_dest[] = new int[ilosc_wierszy];
            for (int i = 0; i < ilosc_wierszy; i++) {
                System.out.print((i + 1) + ".\t");
                for (int j = 0; j < ilosc_genow; j++) {
                    System.out.print(DIV[i][j] + "\t");
                }
                System.out.print("\t\t");
                System.out.print((i + 1) + ".\t");
                for (int j = 0; j < ilosc_genow; j++) {
                    System.out.print(REM[i][j] + "\t");
//                    rem_sour[i] += REM[i][j];
                }
//                if (rem_sour[i]%2 == 1) {
//                    System.out.println("\n\nPoprawność danych jest zachowana maksymalnie do końca poprzedniej epoki, łączna ilość poprawnie wykonanych epok wynosi = "+(epoka-1));
//                    break;
//                }
                System.out.println("" + sour_rem[i]);
            }
            for (int i = 0; i < ilosc_genow + 3; i++) {
                System.out.print("\t");
            }
            boolean blad_rem = false;
            for (int i = 0; i < ilosc_genow; i++) {
                for (int j = 0; j < ilosc_wierszy; j++) {
//                    rem_dest[i] += REM[j][i];
                }
//                if (rem_dest[i]%2 == 1) {
//                    System.out.print("\t"+dest_rem[i]+"-nieparzysta");
//                    System.out.println("\n\nPoprawność danych jest zachowana maksymalnie do końca poprzedniej epoki, łączna ilość poprawnie wykonanych epok wynosi = "+(epoka-1));
//                    blad_rem = true;
//                    break;
//                }
                if (blad_rem == true)
                    break;
                System.out.print("\t" + dest_rem[i]);
            }
            if (blad_rem == true)
                break;
            System.out.println();
            ////////////////////////////////////////////////////////////////////////////////////////
            // rem1 i rem2
            // sour_rem[i]
            // dest_rem[j]


            int[] dest_testowy = new int[ilosc_genow];
            int[] sour_testowy = new int[ilosc_wierszy];
            int[] temp_dest_testowy = new int[ilosc_genow];
            int[] temp_sour_testowy = new int[ilosc_wierszy];
            //najpierw REM1
            //utworzenie kopii
            for (int i = 0 ; i<ilosc_genow; i++){
                dest_testowy[i] = dest_rem[i]/2;
                temp_dest_testowy[i] = dest_rem[i]/2;
                System.out.print(dest_testowy[i]+" ");
            }
            System.out.println();
            for (int i = 0 ; i<ilosc_wierszy; i++){
                sour_testowy[i] = sour_rem[i]/2;
                temp_sour_testowy[i] = sour_rem[i]/2;
                System.out.print(sour_testowy[i]+" ");
            }
            System.out.println();
            for (int i = 0; i<ilosc_wierszy; i++) {
                for (int j = 0; j<ilosc_genow; j++){
                	//System.out.print(REM[i][j]+" ");
                	//System.out.print(i+" ");
                    if (REM[i][j]==1) {
                        if (temp_sour_testowy[i] > 0 && temp_dest_testowy[j] > 0) {
                            REM1[i][j] = REM[i][j];                            
                            REM[i][j] = 0;
                            temp_sour_testowy[i] -= 1;
                            temp_dest_testowy[j] -= 1;
                        }
                    }
                    //System.out.print(REM1[i][j]+" ");
                }
                System.out.println();
            }
            System.out.println();
            for (int i = 0; i<ilosc_wierszy; i++) {
                for (int j = 0; j<ilosc_genow; j++){
                	//System.out.print(REM[i][j]+" ");
                	//System.out.print(i+" ");
                    if (REM[i][j]==1) {
                        if (sour_testowy[i] > 0 && dest_testowy[j] > 0) {
                            REM2[i][j] = REM[i][j];
                            REM[i][j] = 0;
                            sour_testowy[i] -= 1;
                            dest_testowy[j] -= 1;
                        }
                    }
                    //System.out.print(REM2[i][j]+" ");
                }
                System.out.println();
            }
            System.out.println();
//            for (int i = 0; i<ilosc_genow; i++) {
//                int suma_dest_test = 0;
//                int suma_sour_test = 0;
////                sour_testowy[i] = 0;
//                for (int j = 0; j < ilosc_wierszy; j++) {
//                    suma_dest_test += REM[j][i];
//                    suma_sour_test += REM[j][i];
//                    if (REM1[j][i] != 1)
//                        REM2[j][i] = REM[j][i];
//                }
//            }
            ///////////////////zamiana wartosci w kazdym wierszu aby dopasowac poniewaz sour_rem1 = sour_rem2, nalezy znalezc kombinacje odpowiadajaca dest_rem1 = dest_rem2
//            int przerwij_po = 0;
//            int rem1_kolumna = 0;
//            int rem2_kolumna = 0;
//            while(przerwij_po < 500){
//                przerwij_po += 1;
//            }



            /////////////////////////////////////////////////////////////////////////////////////


            System.out.println("\t\t\t\tOSOBNIK NR" + (losowy_krzyzowania + 1) + " - REM1 \t\t\t\t\t\t\t\t\t\t\tOSOBNIK NR" + (losowy_krzyzowania2 + 1) + " - REM2");
            System.out.print(" \\\t");
            for (int i = 0; i < ilosc_genow; i++) {
                System.out.print((i + 1) + ".\t");
            }
            System.out.print("\t\t \\\t");
            for (int i = 0; i < ilosc_genow; i++) {
                System.out.print((i + 1) + ".\t");
            }
            System.out.println();
            for (int i = 0; i < ilosc_wierszy; i++) {
                System.out.print((i + 1) + ".\t");
                for (int j = 0; j < ilosc_genow; j++) {
                    System.out.print(REM1[i][j] + "\t");
                    sour_rem1[i]+=REM1[i][j];
                    dest_rem1[j]+=REM1[i][j] ;
                }
                System.out.print(sour_rem1[i]);
                System.out.print("\t\t");
                System.out.print((i + 1) + ".\t");
                for (int j = 0; j < ilosc_genow; j++) {
                    System.out.print(REM2[i][j] + "\t");
                    sour_rem2[i]+=REM2[i][j];
                    dest_rem2[j] +=REM2[i][j] ;
                }
                System.out.print(sour_rem2[i]);
                System.out.println();
            }
            System.out.print("\t");
            for (int k = 0; k < ilosc_genow; k++) {
                System.out.print(dest_rem1[k]+"\t");
            }
            System.out.print("\t\t \t");
            for (int k = 0; k < ilosc_genow; k++) {
                System.out.print(dest_rem2[k]+"\t");
            }
            System.out.println();
            System.out.println();


            ////////////////V3 i V4
            for (int i = 0; i < ilosc_wierszy; i++) {
                for (int j = 0; j < ilosc_genow; j++) {
                    V3[i][j] = DIV[i][j] + REM1[i][j];
                    V4[i][j] = DIV[i][j] + REM2[i][j];
                }
            }
            System.out.println("\t\t\t\tOSOBNIK NR" + (losowy_krzyzowania + 1) + " - V3 \t\t\t\t\t\t\t\t\t\t\tOSOBNIK NR" + (losowy_krzyzowania2 + 1) + " - V4");
            System.out.print(" \\\t");
            int suma_dest1 = 0;
            int suma_sour1 = 0;
            int suma_dest2 = 0;
            int suma_sour2 = 0;
            int[] sour_warunek1 = new int[ilosc_genow];
            int[] dest_warunek1 = new int[ilosc_wierszy];
            int[] sour_warunek2 = new int[ilosc_genow];
            int[] dest_warunek2 = new int[ilosc_wierszy];
            for (int i = 0; i < ilosc_genow; i++) {
                System.out.print((i + 1) + ".\t");
            }
            System.out.print("\t\t \\\t");
            for (int i = 0; i < ilosc_genow; i++) {
                System.out.print((i + 1) + ".\t");
            }
            System.out.println();
            for (int i = 0; i < ilosc_wierszy; i++) {
                sour_warunek1[i] = 0;
                System.out.print((i + 1) + ".\t");
                for (int j = 0; j < ilosc_genow; j++) {
                    System.out.print(V3[i][j] + "\t");
                    sour_warunek1[i] += V3[i][j];
                    suma_sour1 += V3[i][j];
//                    populacja[losowy_krzyzowania][i][j] = V3[i][j];
                }
//                System.out.println();
//                for (int ii = 0; ii < ilosc_genow; ii++) {
//                    dest_warunek1[ii] = 0;
//                    for (int jj = 0; jj<ilosc_wierszy; jj++){
//                        dest_warunek1[ii] += V3[jj][ii];
//                        suma_dest1 += V3[jj][ii];
//                    }
//                }
                System.out.print("\t\t");
                System.out.print((i + 1) + ".\t");
                for (int j = 0; j < ilosc_genow; j++) {
                    System.out.print(V4[i][j] + "\t");
                    sour_warunek2[i] += V4[i][j];
                    suma_sour2 += V4[i][j];
//                    populacja[losowy_krzyzowania2][i][j] = V4[i][j];
                }
                System.out.println();
//                for (int ii = 0; ii < ilosc_genow; ii++) {
//                    dest_warunek2[ii] = 0;
//                    for (int jj = 0; jj<ilosc_wierszy; jj++){
//                        dest_warunek2[ii] += V4[jj][ii];
//                        suma_dest2 += V4[jj][ii];
//                    }
//                }
            }
            System.out.print("\t");
//            for (int k = 0; k < ilosc_genow; k++) {
//                System.out.print(dest_warunek1[k] + "\t");
//            }
//            System.out.print("\t\t \t");
//            for (int k = 0; k < ilosc_genow; k++) {
//                System.out.print(dest_warunek2[k] + "\t");
//            }
            System.out.println();
        }
    }
}
