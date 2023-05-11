import java.util.*;

public class transportowy2 {
    public static int min(int a, int b) {
        if (a < b)
            return a;
        else
            return b;
    }
    public static void main(String[] args) {
        System.out.println("Problem transportowy");
        int ilosc_punktow_nadania = 3, ilosc_punktow_odbioru = 4, ilosc_osobnikow = 5;
        int ilosc_genow = ilosc_punktow_nadania * ilosc_punktow_odbioru;
        int[][] tab_koszt  = {
                {10, 0, 20, 11},
                {12, 7, 9, 20},
                {0, 14, 16, 18}
        };
        int[] sour = {15,25,5}; //tablice punktÃ³w nadania
        int[] dest = {5,15,15,10};//odbioru
        //Losowanie osobnikow
        int populacja [][] = new int [ilosc_osobnikow][ilosc_genow];
        Random r = new Random();
        for (int i = 0; i < ilosc_osobnikow; i++) {
            Set<Integer>set = new LinkedHashSet<Integer>();
            while (set.size() < ilosc_genow) {
                set.add(r.nextInt(ilosc_genow));
            }
            ArrayList<Integer> al = new ArrayList<>(set);
            int o = 0;
            for (int lczb : al) {
                populacja[i][o] = lczb;
                o++;
            }
        }
        System.out.print(" \\\t");
        for (int i = 0; i<ilosc_genow; i++){
            System.out.print((i+1)+".\t");
        }
        System.out.println();
        for (int i = 0; i<ilosc_osobnikow; i++){
            System.out.print((i+1)+".\t");
            for (int j  = 0; j<ilosc_genow; j++){
                System.out.print(populacja[i][j]+"\t");
            }
            System.out.println();
        }
        System.out.println("\nTablica kosztÃ³w");
        System.out.print(" \\\t");
        for (int i = 0; i<ilosc_punktow_odbioru; i++){
            System.out.print(dest[i]+"\t");
        }
        System.out.println();
        for (int i = 0; i<ilosc_punktow_nadania; i++){
            System.out.print(sour[i]+"\t");
            for (int j = 0; j<ilosc_punktow_odbioru; j++){
                System.out.print(tab_koszt[i][j]+"\t");
            }
            System.out.println();
        }
        System.out.println("\nTablice przewozÃ³w");
        int a = 0, b = 0;
        int ocena[] = new int [ilosc_genow];
        //przypisanie tablic przewozow do jednej tablicy
        int tablica [][][] = new int [ilosc_genow][sour.length][dest.length];
        for (int n = 0; n < ilosc_osobnikow; n++) {
            int[] temp_punkty_nadania =  {15, 25, 5};
            int[] temp_punkty_odbioru = {5, 15, 15, 10};
            for (int k = 0; k<ilosc_genow; k++) {
                int pomocnicza = populacja[n][k];
                a = 0;
                b = 0;
                if (pomocnicza < dest.length) {
                    a = 0;
                    b = pomocnicza;
                }
                else {
                    while(pomocnicza >= dest.length) {
                        pomocnicza -= dest.length;
                        a++;
                    }
                    b = pomocnicza;
                }
                tablica[n][a][b] = min(temp_punkty_nadania[a], temp_punkty_odbioru[b]);
                temp_punkty_nadania[a] -= tablica[n][a][b]; //aktualizowanie wartosci punktow nadania
                temp_punkty_odbioru[b] -= tablica[n][a][b]; // aktualizowanie wartosci punktow odbioru
                ocena[n] += tablica[n][a][b] * tab_koszt[a][b];
            }
            System.out.println("Tablica przewozÃ³w nr "+(n+1));
            System.out.print(" \\\t");
            for (int i = 0; i<ilosc_punktow_odbioru; i++){
                System.out.print(dest[i]+"\t");
            }
            System.out.println();
            for (int i = 0; i < sour.length; i++) {
                System.out.print(sour[i]+"\t");
                for (int j = 0; j < dest.length; j++) {
                    System.out.print(tablica[n][i][j] + "\t");
                }
                System.out.println();
            }
            System.out.println("Funkcja oceny: " + ocena[n] + "\n");
        }
        System.out.println("MUTACJA");
        System.out.print(" \\\t");
        for (int i = 0; i<ilosc_genow; i++){
            System.out.print((i+1)+".\t");
        }
        System.out.println();
        for (int i = 0; i<ilosc_osobnikow; i++){
            System.out.print((i+1)+".\t");
            for (int j  = 0; j<ilosc_genow; j++){
                System.out.print(populacja[i][j]+"\t");
            }
            System.out.println();
        }
        //Losowanie podmacierz populacji
        int k1, k2, w1, w2, min;
        ArrayList<Integer> wiersze = new ArrayList<Integer>();
        ArrayList<Integer> kolumny = new ArrayList<Integer>();
        min = ilosc_genow/2; //minimalna wylosowana wartosc pierwszego losowania wynosi polowa ilosci genow
        k2 = r.nextInt(((ilosc_genow) - min)+1)+min;
        kolumny.add(k2);
        while(true){
            k1 = r.nextInt(k2);
            k2 = k1;
            if(k1 == 0){
                break;
            }
            kolumny.add(k1);
        }
        min = ilosc_osobnikow/2; //minimalna wylosowana wartosc pierwszego losowania wynosi polowa ilosci osobnikow
        w2 = r.nextInt(((ilosc_osobnikow) - min)+1)+min;
        wiersze.add(w2);
        while(true){
            w1 = r.nextInt(w2);
            w2 = w1;
            if(w1 == 0){
                break;
            }
            wiersze.add(w1);
        }
        Collections.sort(kolumny); //posortowanie indeksow rosnÄ…co
        Collections.sort(wiersze);
        System.out.print("Wylosowano kolumny {");
        for (int i = 0; i<kolumny.size(); i++){
            if(i==0)
                System.out.print(kolumny.get(i));
            else
                System.out.print(", "+kolumny.get(i));
        }
        System.out.print("}");
        System.out.println();
        System.out.print("Wylosowano wiersze {");
        for (int i = 0; i<wiersze.size(); i++){
            if(i==0)
                System.out.print(wiersze.get(i));
            else
                System.out.print(", "+wiersze.get(i));
        }
        System.out.print("}\n");
        System.out.println("Podmacierz wylosowanych punktÃ³w");
        int[][] mutacja = new int[wiersze.size()][kolumny.size()];
        System.out.print(" \\\t");
        for (int i = 0; i<kolumny.size(); i++){
            System.out.print(kolumny.get(i)+".\t");
        }
        System.out.println();
        for (int i = 0; i<wiersze.size(); i++){
            System.out.print(wiersze.get(i)+".\t");
            for (int j  = 0; j<kolumny.size(); j++){
                mutacja[i][j] = populacja[wiersze.get(i)-1][kolumny.get(j)-1];
                System.out.print(populacja[wiersze.get(i)-1][kolumny.get(j)-1]+"\t");
            }
            System.out.println();
        }
        //ponowna inicjalizacja
        //ograniczenia
        int[] destw = new int[kolumny.size()]; //suma rzÄ™du kolumn
        int[] sourw = new int[wiersze.size()]; //suma rzÄ™du wierszy
        System.out.print("ZauwaÅ¼my, Å¼e ");
        for (int i = 0; i<wiersze.size(); i++){
            sourw[i] = 0;
            for (int j = 0; j<kolumny.size(); j++){
                sourw[i] += mutacja[i][j];
            }
            if (i == 0)
                System.out.print("sourw["+(i+1)+"] = "+sourw[i]);
            else
                System.out.print(", sourw["+(i+1)+"] = "+sourw[i]);
        }
        for (int i = 0; i<kolumny.size(); i++){
            destw[i] = 0;
            for (int j = 0; j<wiersze.size(); j++){
                destw[i] += mutacja[j][i];
            }
            System.out.print(", destw["+(i+1)+"] = "+destw[i]);
        }
        //------------------------------------- do zrobienia
        System.out.println();
        int tablicaw [][][] = new int [mutacja[0].length+1][sourw.length+2][destw.length+1];
        double ocenaw = 0;
        for (int n = 0; n < sourw.length; n++) {
//            int[] temp_punkty_nadania =  {15, 25, 5};
            int[] temp_punkty_nadania = new int[sourw.length];
            for (int i = 0; i < sourw.length; i++) {
                temp_punkty_nadania[i] = sourw[i];
            }
//            int[] temp_punkty_odbioru = {5, 15, 15, 10};
            int[] temp_punkty_odbioru = new int[destw.length];
            for (int i = 0; i < destw.length; i++) {
                temp_punkty_odbioru[i] = destw[i];
            }
            for (int k = 0; k < destw.length; k++) {
                int pomocnicza = mutacja[n][k];
                a = 0;
                b = 0;
                if (pomocnicza < destw.length) {
                    a = 0;
                    b = pomocnicza;
                } else {
                    while (pomocnicza >= destw.length) {
                        pomocnicza -= destw.length;
                        a++;
                    }
                    b = pomocnicza;
                }
                tablicaw[n][a][b] = min(temp_punkty_nadania[a], temp_punkty_odbioru[b]);
                temp_punkty_nadania[a] -= tablicaw[n][a][b]; //aktualizowanie wartosci punktow nadania
                temp_punkty_odbioru[b] -= tablicaw[n][a][b]; // aktualizowanie wartosci punktow odbioru
                ocenaw += tablicaw[n][a][b] * tab_koszt[a][b];
            }
            System.out.println("Tablica przewozÃ³w nr " + (n + 1));
            System.out.print(" \\\t");
            for (int i = 0; i < destw.length; i++) {
                System.out.print(destw[i] + "\t");
            }
            System.out.println();
            for (int i = 0; i < sourw.length; i++) {
                System.out.print(sourw[i] + "\t");
                for (int j = 0; j < destw.length; j++) {
                    System.out.print(tablicaw[n][i][j] + "\t");
                }
                System.out.println();
            }
            System.out.println("Funkcja oceny: " + ocenaw + "\n");
        }







        System.out.println("\nPopulacja po wykonaniu mutacji");
        System.out.print(" \\\t");
        for (int i = 0; i<ilosc_genow; i++){
            System.out.print((i+1)+".\t");
        }
        System.out.println();
        for (int i = 0; i<ilosc_osobnikow; i++){
            System.out.print((i+1)+".\t");
            for (int j  = 0; j<ilosc_genow; j++){
                System.out.print(populacja[i][j]+"\t");
            }
            System.out.println();
        }
//        //krzyÅ¼owanie
//        double pk = 0.5;
//        double r_krzyzowanie[] = new double[ilosc_osobnikow];
//        int zmienna_pomocnicza_int = 0;
//        int[] wylosowane = new int[ilosc_osobnikow];
//        System.out.print(" \n\n\\\t");
//        for (int i = 0; i<ilosc_genow; i++){
//            System.out.print((i+1)+".\t");
//        }
//        System.out.println();
//        for (int i = 0; i<ilosc_osobnikow; i++){
//            System.out.print((i+1)+".\t");
//            for (int j  = 0; j<ilosc_genow; j++){
//                System.out.print(populacja[i][j]+"\t");
//            }
//            System.out.print("\t");
//            r_krzyzowanie[i] = r.nextDouble();
//            System.out.print(r_krzyzowanie[i]);
//            if(r_krzyzowanie[i] <= pk){
//                wylosowane[i] = i+1;
//                System.out.print("\t<=\t"+pk);
//            }
//            else{
//            System.out.print("\t>\t"+pk);
//            }
//            System.out.println();
//        }
//        zmienna_pomocnicza_int = 0;
//        int zmienna_pomocnicza2 = 0;
//        Arrays.sort(wylosowane);
//        for (int i = 0; i<ilosc_osobnikow; i++){
//            if(wylosowane[i] != 0) {
//                zmienna_pomocnicza_int += 1;
//            }
//        }
//        int tab_wylosowanych[] = new int[zmienna_pomocnicza_int];
//        int iteratorek = 0;
//        System.out.print("Wylosowane indexy: ");
//        for (int i = 0; i<ilosc_osobnikow; i++){
//            if(wylosowane[i] != 0) {
//                tab_wylosowanych[iteratorek] = wylosowane[i];
//                System.out.print(wylosowane[i]+" ");
//                iteratorek++;
//            }
//        }
//        zmienna_pomocnicza_int = 0;
//        if (iteratorek%2 == 1) {
//            int iteratorek2 = r.nextInt(iteratorek) + 1;
//            System.out.print(", nieparzysta liczba indeksÃ³w wiÄ™c usuwam losowy o nr " + iteratorek2 + "\n");
//            tab_wylosowanych[iteratorek2 - 1] = 0;
//            for (int i = 0; i<tab_wylosowanych.length; i++){
//                if(tab_wylosowanych[i] != 0) {
//                    zmienna_pomocnicza_int += 1;
//                }
//            }
//        }
//        int rozmiar_par = 0;
//        if(tab_wylosowanych.length %2 == 1)
//            rozmiar_par = tab_wylosowanych.length - 1;
//        else
//            rozmiar_par = tab_wylosowanych.length;
//        Integer[] indeksy = new Integer[rozmiar_par];
//        int zmienna_pomocnicza = 0;
//        System.out.print("Indeksy po usuniÄ™ciu: ");
//        for (int i = 0; i<tab_wylosowanych.length; i++){
//            if(tab_wylosowanych[i] != 0){
//                indeksy[zmienna_pomocnicza] = tab_wylosowanych[i];
//                System.out.print(tab_wylosowanych[i]+" ");
//                zmienna_pomocnicza += 1;
//            }
//        }
//        int pary[][] = new int[rozmiar_par/2][2];
//        int populacja_array_klon[][] = new int[ilosc_osobnikow][ilosc_genow];
//        for (int i = 0; i<ilosc_osobnikow; i++){
//            for (int j = 0; j<ilosc_genow; j++){
//                populacja_array_klon[i][j] = populacja[i][j];
//            }
//        }
//        ////////////////////////////JEDNOPUNKTOWE/////////////////////////////////////////////
//        System.out.println("\nKrzyÅ¼owanie jednopunktowe");
//        System.out.println("Å�Ä…czenie w pary:");
//        int punkt;
//        //wrzucenie wartosci tabvlicy indeksow do listy i przesortowanie losowo z uzyciem shuffle
//        List<Integer> intList = Arrays.asList(indeksy);
//        Collections.shuffle(intList);
//        //zamiana listy spowrotem na tablice Integerow
//        intList.toArray(indeksy);
//        for (int i = 0; i<rozmiar_par/2; i++){
//            punkt = r.nextInt(ilosc_genow);
//            if (punkt == 0) punkt++;
//            pary[i][0] = indeksy[i];
//            pary[i][1] = indeksy[rozmiar_par - 1 - i];
//            ////////////////////rodzice
//            System.out.println("Rodzice\t\tnr "+(i+1)+" (punkt "+punkt+")");
//            System.out.print(pary[i][0]+". ");
//            zmienna_pomocnicza_int = pary[i][0] - 1;
//            for (int j = 0; j<ilosc_genow; j++){
//                System.out.print(populacja[zmienna_pomocnicza_int][j]+" ");
//                if(j == punkt-1)
//                System.out.print("| ");
//            }
//            System.out.println();
//            System.out.print(pary[i][1]+". ");
//            zmienna_pomocnicza_int = pary[i][1] - 1;
//            for (int j = 0; j<ilosc_genow; j++){
//                System.out.print(populacja[zmienna_pomocnicza_int][j]+" ");
//                if(j == punkt-1)
//                System.out.print("| ");
//            }
//            ////////////////////potomkowie
//            System.out.println("\nPotomkowie\tnr "+(i+1));
//            System.out.print(pary[i][0]+". ");
//            zmienna_pomocnicza_int = pary[i][0] - 1;
//            for (int j = 0; j<ilosc_genow; j++){
//                if(j>punkt-1)
//                    populacja_array_klon[zmienna_pomocnicza_int][j] = populacja[pary[i][1] - 1][j];
//                System.out.print(populacja_array_klon[zmienna_pomocnicza_int][j]+" ");
//                    if(j == punkt-1) {
//                        System.out.print("| ");
//                    }
//            }
//            System.out.println();
//            System.out.print(pary[i][1]+". ");
//            zmienna_pomocnicza_int = pary[i][1] - 1;
//            for (int j = 0; j<ilosc_genow; j++){
//                if(j>punkt-1)
//                    populacja_array_klon[zmienna_pomocnicza_int][j] = populacja[pary[i][0] - 1][j];
//                System.out.print(populacja_array_klon[zmienna_pomocnicza_int][j]+" ");
//                if(j == punkt-1) {
//                    System.out.print("| ");
//                }
//            }
//            System.out.println("\n");
//        }
//        //zapisanie krzyzowania do glownej populacji
//        for (int i = 0; i<ilosc_osobnikow; i++){
//            for (int j = 0; j<ilosc_genow; j++){
//                populacja[i][j] = populacja_array_klon[i][j];
//            }
//        }
//        System.out.print(" \\\t");
//        for (int i = 0; i<ilosc_genow; i++){
//            System.out.print((i+1)+".\t");
//        }
//        System.out.println();
//        for (int i = 0; i<ilosc_osobnikow; i++){
//            System.out.print((i+1)+".\t");
//            for (int j  = 0; j<ilosc_genow; j++){
//                System.out.print(populacja[i][j]+"\t");
//            }
//            System.out.println();
//        }
    }
}
