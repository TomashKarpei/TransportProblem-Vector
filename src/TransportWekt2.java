import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Random;

public class TransportWekt2 {

	public static int min(int x1, int x2) {
		if (x1 < x2) {
			return x1;
		} else {
			return x2;
		}
	}

	public static void main(String[] args) {
		float pi = 0.2f, pm = 0.2f, pk = 0.5f;
		boolean min = true;
		int il_p_nad = 4, il_p_odbioru = 5, il_osob = 10, elitKr = 3, il_epok = 500;
		int il_poz = il_p_nad * il_p_odbioru;
		int[][] t_koszt = {
				{ 10, 0, 20, 11, 5 },
				{ 12, 7, 9, 20, 5 },
				{ 0, 14, 16, 18, 5 },
				{ 0, 14, 16, 18, 5 }

		};
		int[] zapasy = { 15, 25, 5, 10 };
		int[] zapotrzeb = { 5, 15, 15, 10, 5 };

		// Losowanie osobnikow
		int Sat_osob[][] = new int[il_osob][il_poz];
		int los_c[][] = new int[il_osob * elitKr][il_poz];
		Random rand = new Random();

		for (int i = 0; i < il_osob; i++) {
			System.out.print("Osobnik numer " + i + " : ");
			Set<Integer> set = new LinkedHashSet<Integer>();
			while (set.size() < il_poz) {
				set.add(rand.nextInt(il_poz));
			}
			ArrayList<Integer> al = new ArrayList<>(set);
			// Collections.sort(al);
			int o = 0;
			for (int lczb : al) {
				Sat_osob[i][o] = lczb;
				System.out.print(Sat_osob[i][o] + " ");
				o++;
			}
			System.out.println();
		}
		System.out.println("\n--------------------------------------------------\n");

		int temp_i = 0, temp_j = 0;
		// int [] temp_zapas = new int[zapasy.length];
		// int [] temp_zapot = new int[zapotrzeb.length];
		int stat_f_oceny[] = new int[il_poz];
		int stat_tablica[][][] = new int[il_poz][zapasy.length][zapotrzeb.length];

		//// epoki
		for (int z = 0; z < il_epok + 1; z++) {
			int t_max = 0;
			int f_oceny[] = new int[il_poz * elitKr];
			int tablica[][][] = new int[il_poz * elitKr][zapasy.length][zapotrzeb.length];

			///// kopiowanie tablicy
			int temp_los_c[][] = new int[il_osob * elitKr][il_poz];

			for (int n = 0; n < il_osob; n++) {
				for (int k = 0; k < il_poz; k++) {
					temp_los_c[n][k] = Sat_osob[n][k];
					los_c[n][k] = Sat_osob[n][k];
				}
			}

			///// obliczenia
			for (int n = 0; n < il_osob; n++) {

				int[] temp_zapas = { 15, 25, 5, 10 };
				int[] temp_zapot = { 5, 15, 15, 10, 5 };
				// temp_zapas = zapasy;
				// temp_zapot = zapotrzeb;
				// System.out.println("N = " + temp_zapas[temp_i] + " " + temp_zapot[temp_j]);
				System.out.println("Osobnik numer " + n + ": ");
				for (int k = 0; k < il_poz; k++) {

					int temp_los = los_c[n][k];
					temp_i = 0;
					temp_j = 0;
					if (temp_los < zapotrzeb.length) {
						temp_i = 0;
						temp_j = temp_los;
						// System.out.println(temp_i + " " + temp_j);
					} else {
						while (temp_los >= zapotrzeb.length) {
							temp_los -= zapotrzeb.length;
							temp_i++;
						}
						temp_j = temp_los;
						// System.out.println(temp_i + " " + temp_j);
					}
					tablica[n][temp_i][temp_j] = min(temp_zapas[temp_i], temp_zapot[temp_j]);
					stat_tablica[n][temp_i][temp_j] = min(temp_zapas[temp_i], temp_zapot[temp_j]);
					temp_zapas[temp_i] -= tablica[n][temp_i][temp_j];
					temp_zapot[temp_j] -= tablica[n][temp_i][temp_j];
					f_oceny[n] += tablica[n][temp_i][temp_j] * t_koszt[temp_i][temp_j];
					if (t_max < f_oceny[n]) {
						t_max = f_oceny[n];
					}
					stat_f_oceny[n] += tablica[n][temp_i][temp_j] * t_koszt[temp_i][temp_j];
				}
				for (int i = 0; i < zapasy.length; i++) {
					for (int j = 0; j < zapotrzeb.length; j++) {
						System.out.print(tablica[n][i][j] + " ");
					}
					System.out.println();
				}
				System.out.println("Funkcja oceny: " + f_oceny[n] + "\n");
			}
			if (z == il_epok) {
				break;
			}

			//////// seleckja_turn_min
			System.out.println("---------------Selekcja turniejowa minimum-------------- ");
			int ilTurn = 2;

			int temp_f_oceny[] = new int[il_osob * elitKr];
			int temp2_los_c[][] = new int[il_osob * elitKr][il_poz];
			for (int n = 0; n < il_osob; n++) {
				for (int k = 0; k < il_poz; k++) {
					temp2_los_c[n][k] = los_c[n][k];
					temp_f_oceny[n] = f_oceny[n];
				}
			}

			for (int n = 0; n < il_osob * elitKr; n++) {
				int tempFunkOc[] = new int[il_osob * elitKr];
				int[] liczbTurn = new int[ilTurn];
				int[] fOcTurn = new int[ilTurn];
				int[] min_id = new int[il_osob * elitKr];
				for (int j = 0; j < ilTurn; j++) {
					liczbTurn[j] = rand.nextInt(il_osob);
					fOcTurn[j] = temp_f_oceny[liczbTurn[j]];

					if (j > 0) {
						if (fOcTurn[j] < fOcTurn[j - 1]) {
							min_id[n] = liczbTurn[j];
							tempFunkOc[n] = fOcTurn[j];
						}
					} else if (j == 0) {
						min_id[n] = liczbTurn[j];
						tempFunkOc[n] = fOcTurn[j];
					}
					// System.out.println("Wylosowana liczba turniejowa: " + liczbTurn[j] + ".
					// Funkcja oceny tej liczby: "
					// + fOcTurn[j]);
				}
				// System.out.println("Wylosowana minimalna liczba turniejowa nr " + n + " : " +
				// min_id[n]
				// + ". Funkcja oceny tej liczby: " + tempFunkOc[n]);
				for (int k = 0; k < il_poz; k++) {
					los_c[n][k] = temp2_los_c[min_id[n]][k];
				}
				int[] temp_zapas = { 15, 25, 5, 10 };
				int[] temp_zapot = { 5, 15, 15, 10, 5 };
				f_oceny[n] = 0;
				// temp_zapas = zapasy;
				// temp_zapot = zapotrzeb;
				// System.out.println("N = " + temp_zapas[temp_i] + " " + temp_zapot[temp_j]);
				// System.out.println("Osobnik numer " + n + ": ");
				for (int k = 0; k < il_poz; k++) {

					int temp_los = los_c[n][k];
					temp_i = 0;
					temp_j = 0;
					if (temp_los < zapotrzeb.length) {
						temp_i = 0;
						temp_j = temp_los;
						// System.out.println(temp_i + " " + temp_j);
					} else {
						while (temp_los >= zapotrzeb.length) {
							temp_los -= zapotrzeb.length;
							temp_i++;
						}
						temp_j = temp_los;
						// System.out.println(temp_i + " " + temp_j);
					}
					tablica[n][temp_i][temp_j] = min(temp_zapas[temp_i], temp_zapot[temp_j]);

					temp_zapas[temp_i] -= tablica[n][temp_i][temp_j];
					temp_zapot[temp_j] -= tablica[n][temp_i][temp_j];
					f_oceny[n] += tablica[n][temp_i][temp_j] * t_koszt[temp_i][temp_j];
					if (t_max < f_oceny[n]) {
						t_max = f_oceny[n];
					}
				}
				for (int i = 0; i < zapasy.length; i++) {
					for (int j = 0; j < zapotrzeb.length; j++) {
						// System.out.print(tablica[n][i][j] + " ");
					}
					// System.out.println();
				}
				// System.out.println("Funkcja oceny: " + f_oceny[n] + "\n");
			}

			//////// inwersja
			System.out.println("---------------Inwersja--------------- ");

			for (int n = 0; n < il_osob * elitKr; n++) {
				for (int k = 0; k < il_poz; k++) {
					temp_los_c[n][k] = los_c[n][k];
				}
			}

			for (int n = 0; n < il_osob * elitKr; n++) {
				float los = rand.nextFloat(1);
				// System.out.println ("Wylosowano " + los);
				if (los < pi) {

					System.out.println("Inwersja nr " + n + " : ");
					for (int k = 0; k < il_poz; k++) {
						los_c[n][k] = temp_los_c[n][il_poz - 1 - k];
						System.out.print(los_c[n][k] + " ");
					}
					System.out.println("\n");
					int[] temp_zapas = { 15, 25, 5, 10 };
					int[] temp_zapot = { 5, 15, 15, 10, 5 };
					f_oceny[n] = 0;
					for (int k = 0; k < il_poz; k++) {
						int temp_los = los_c[n][k];
						temp_i = 0;
						temp_j = 0;
						if (temp_los < zapotrzeb.length) {
							temp_i = 0;
							temp_j = temp_los;
							// System.out.println(temp_i + " " + temp_j);
						} else {
							while (temp_los >= zapotrzeb.length) {
								temp_los -= zapotrzeb.length;
								temp_i++;
							}
							temp_j = temp_los;
							// System.out.println(temp_i + " " + temp_j);
						}
						tablica[n][temp_i][temp_j] = min(temp_zapas[temp_i], temp_zapot[temp_j]);

						temp_zapas[temp_i] -= tablica[n][temp_i][temp_j];
						temp_zapot[temp_j] -= tablica[n][temp_i][temp_j];
						f_oceny[n] += tablica[n][temp_i][temp_j] * t_koszt[temp_i][temp_j];
						if (t_max < f_oceny[n]) {
							t_max = f_oceny[n];
						}
					}
					for (int i = 0; i < zapasy.length; i++) {
						for (int j = 0; j < zapotrzeb.length; j++) {
							System.out.print(tablica[n][i][j] + " ");
						}
						System.out.println();
					}
					System.out.println("Funkcja oceny: " + f_oceny[n] + "\n");
				}
			}

			///// mutacja
			System.out.println("---------------Mutacja-------------- ");
			for (int n = 0; n < il_osob * elitKr; n++) {
				float los = rand.nextFloat(1);
				// System.out.println ("Wylosowano " + los);
				if (los < pm) {
					System.out.println("Mutacja nr " + n + " : ");
					int w1, w2, l1, l2;
					l1 = rand.nextInt(il_poz);
					l2 = l1;
					while (l2 == l1) {
						l2 = rand.nextInt(il_poz);
					}
					// System.out.println ("l1 " + los_c [n][l1] + " l2 " + los_c [n][l2]);
					w1 = los_c[n][l1];
					w2 = los_c[n][l2];
					los_c[n][l1] = w2;
					los_c[n][l2] = w1;
					// System.out.println ("l1 " + los_c [n][l1] + " l2 " + los_c [n][l2]);
					int[] temp_zapas = { 15, 25, 5, 10 };
					int[] temp_zapot = { 5, 15, 15, 10, 5 };
					f_oceny[n] = 0;
					for (int k = 0; k < il_poz; k++) {
						int temp_los = los_c[n][k];
						temp_i = 0;
						temp_j = 0;
						if (temp_los < zapotrzeb.length) {
							temp_i = 0;
							temp_j = temp_los;
							// System.out.println(temp_i + " " + temp_j);
						} else {
							while (temp_los >= zapotrzeb.length) {
								temp_los -= zapotrzeb.length;
								temp_i++;
							}
							temp_j = temp_los;
							// System.out.println(temp_i + " " + temp_j);
						}
						System.out.print(los_c[n][k] + " ");
						tablica[n][temp_i][temp_j] = min(temp_zapas[temp_i], temp_zapot[temp_j]);

						temp_zapas[temp_i] -= tablica[n][temp_i][temp_j];
						temp_zapot[temp_j] -= tablica[n][temp_i][temp_j];
						f_oceny[n] += tablica[n][temp_i][temp_j] * t_koszt[temp_i][temp_j];
						if (t_max < f_oceny[n]) {
							t_max = f_oceny[n];
						}
					}
					System.out.println("\n");
					for (int i = 0; i < zapasy.length; i++) {
						for (int j = 0; j < zapotrzeb.length; j++) {
							System.out.print(tablica[n][i][j] + " ");
						}
						System.out.println();
					}
					System.out.println("Funkcja oceny: " + f_oceny[n] + "\n");
				}

			}

			///// krzyz
			System.out.println("---------------Krzyzowanie-------------- ");
			int NrRodzicow[] = new int[il_osob * elitKr];
			int losIlRodz = 0;

			for (int n = 0; n < il_osob * elitKr; n++) {
				float los = rand.nextFloat(1);
				// System.out.println ("Wylosowano " + los);
				if (los < pk) {
					losIlRodz++;
				}
			}
			if (losIlRodz % 2 == 1) {
				losIlRodz = losIlRodz - 1;
			}
			if (losIlRodz > 1) {
				Set<Integer> setRodz = new LinkedHashSet<Integer>();
				while (setRodz.size() < losIlRodz) {
					setRodz.add(rand.nextInt(il_osob * elitKr));
				}
				int o = 0;
				for (int lczb : setRodz)
					NrRodzicow[o++] = lczb;
				System.out.println();
				System.out.println("Rodzice = " + setRodz);
				o = 0;
				/*
				 * while(i != (il_osob-il_osob%2)){
				 * System.out.print(" "+Rodzice[i]);
				 * i++;
				 * }
				 */
				int TempRodzice[][] = new int[losIlRodz + 1][il_poz];
				for (int i = 0; i < losIlRodz; i++) {
					System.out.println("Rodzic nr = " + NrRodzicow[i] + " :");
					for (int j = 0; j < il_poz; j++) {
						TempRodzice[i][j] = los_c[NrRodzicow[i]][j];
						System.out.print(TempRodzice[i][j] + " ");
					}
					System.out.println("\n");
				}
				int losPunkt1 = 0;
				int losPunkt2 = 0;
				for (int i = 0; i < losIlRodz; i++) {
					if (i % 2 == 0) {
						losPunkt1 = rand.nextInt(il_poz - 1) + 1;
						losPunkt2 = losPunkt1;
						while (losPunkt2 == losPunkt1) {
							losPunkt2 = rand.nextInt(il_poz - 1) + 1;
						}
						if (losPunkt2 < losPunkt1) {
							int tempPunkt = losPunkt1;
							losPunkt1 = losPunkt2;
							losPunkt2 = tempPunkt;
						}
						System.out.println(
								"losowany punkt 1 = " + losPunkt1 + ", Losowany punkt 2 = " + losPunkt2 + "\n");
					}
					System.out.println("Potomek nr = " + NrRodzicow[i] + " :");
					for (int j = 0; j < il_poz; j++) {
						if (i % 2 == 0) {
							if (j < losPunkt1 || j > losPunkt2) {
								int powt = 0;
								for (int p = losPunkt1; p <= losPunkt2; p++) {
									if (los_c[NrRodzicow[i]][j] == TempRodzice[i + 1][p]) {
										los_c[NrRodzicow[i]][j] = 9999999;
										powt = 1;
										break;
									}
								}
								if (powt == 0) {
									los_c[NrRodzicow[i]][j] = TempRodzice[i][j];
								}
							} else {
								los_c[NrRodzicow[i]][j] = TempRodzice[i + 1][j];
							}
						} else {
							if (j < losPunkt1 || j > losPunkt2) {
								int powt = 0;
								for (int p = losPunkt1; p <= losPunkt2; p++) {
									if (los_c[NrRodzicow[i]][j] == TempRodzice[i - 1][p]) {
										los_c[NrRodzicow[i]][j] = 9999999;
										powt = 1;
										break;
									}
								}
								if (powt == 0) {
									los_c[NrRodzicow[i]][j] = TempRodzice[i][j];
								}

							} else {
								los_c[NrRodzicow[i]][j] = TempRodzice[i - 1][j];
							}
						}
						System.out.print(los_c[NrRodzicow[i]][j] + " ");
						if (j == losPunkt1 - 1 || j == losPunkt2) {
							System.out.print("| ");
						}
					}
					System.out.println("\n");
				}

				for (int i = 0; i < losIlRodz; i++) {
					System.out.println("Potomek nr = " + NrRodzicow[i] + " :");
					for (int j = 0; j < il_poz; j++) {
						if (los_c[NrRodzicow[i]][j] == 9999999) {
							for (int q = 0; q < il_poz; q++) {
								boolean CzyJest = false;
								for (int w = 0; w < il_poz; w++) {
									los_c[NrRodzicow[i]][j] = q;
									if (q == los_c[NrRodzicow[i]][w] && w != j) {
										CzyJest = true;
										break;
									}
								}
								if (CzyJest == false) {
									break;
								}
							}
						}
						System.out.print(los_c[NrRodzicow[i]][j] + " ");
					}
					System.out.println("\n");

					int[] temp_zapas = { 15, 25, 5, 10 };
					int[] temp_zapot = { 5, 15, 15, 10, 5 };
					// temp_zapas = zapasy;
					// temp_zapot = zapotrzeb;
					// System.out.println("N = " + temp_zapas[temp_i] + " " + temp_zapot[temp_j]);
					// System.out.println ("Osobnik numer " + NrRodzicow[i] + ": ");
					f_oceny[NrRodzicow[i]] = 0;
					for (int k = 0; k < il_poz; k++) {

						int temp_los = los_c[NrRodzicow[i]][k];
						temp_i = 0;
						temp_j = 0;
						if (temp_los < zapotrzeb.length) {
							temp_i = 0;
							temp_j = temp_los;
							// System.out.println(temp_i + " " + temp_j);
						} else {
							while (temp_los >= zapotrzeb.length) {
								temp_los -= zapotrzeb.length;
								temp_i++;
							}
							temp_j = temp_los;
							// System.out.println(temp_i + " " + temp_j);
						}
						tablica[NrRodzicow[i]][temp_i][temp_j] = 0;
						tablica[NrRodzicow[i]][temp_i][temp_j] = min(temp_zapas[temp_i], temp_zapot[temp_j]);

						temp_zapas[temp_i] -= tablica[NrRodzicow[i]][temp_i][temp_j];
						temp_zapot[temp_j] -= tablica[NrRodzicow[i]][temp_i][temp_j];
						f_oceny[NrRodzicow[i]] += tablica[NrRodzicow[i]][temp_i][temp_j] * t_koszt[temp_i][temp_j];
						if (t_max < f_oceny[NrRodzicow[i]]) {
							t_max = f_oceny[NrRodzicow[i]];
						}
					}
					for (int m = 0; m < zapasy.length; m++) {
						for (int j = 0; j < zapotrzeb.length; j++) {
							System.out.print(tablica[NrRodzicow[i]][m][j] + " ");
						}
						System.out.println();
					}
					System.out.println("Funkcja oceny: " + f_oceny[NrRodzicow[i]] + "\n");
				}

			}

			//// sukcesja
			System.out.println("---------------Sukcesja-------------- ");
			for (int i = 0; i < il_osob; i++) {
				int t_min = 0, id_min = 0;
				for (int n = 0; n < il_osob * elitKr; n++) {
					if (n == 0) {
						t_min = f_oceny[n];
						id_min = n;
					} else if (n != 0 && f_oceny[n] < t_min) {
						t_min = f_oceny[n];
						id_min = n;
					}
				}
				System.out.println("Minimalna funkcja oceny:  " + t_min + " pod numerem " + id_min);
				for (int k = 0; k < il_poz; k++) {
					Sat_osob[i][k] = los_c[id_min][k];
					f_oceny[id_min] = t_max + 1;
				}

			}
		}
	}

}
