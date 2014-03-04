/* Zappos Challenge
 * Author: Ashwini Rajendran
 * This class is used to find the different combinations of product indexes for the user given input number of products with
 * the equal or closest to the user input sum of prices of the products. 
 * */

package SearchItems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// This class is used to find the combinations of sum of prices equal to or closest to the user's given price
public class ProductCombination {

	private static List<ArrayList<Double>> Subsets;
	private static double[] Input_prices = null;
	public static List<ArrayList<Integer>> indexes;
	private static ArrayList<Double> val_w;

	private static void subSetCombination(double sum, ArrayList<Double> sets,
			ArrayList<Integer> indx, int index, int ItemsCount) {
		if (0 > sum) {
			return;
		}
		if (sum == 0) {
			if (ItemsCount != 0) {
				if (sets.size() == ItemsCount) {
					Subsets.add(sets);
					indexes.add(indx);
				}
			} else {
				Subsets.add(sets);
				indexes.add(indx);
			}
			return;
		}
		for (int i = index; i < Input_prices.length; i++) {
			double List = Input_prices[i];
			ArrayList<Double> newList = new ArrayList<Double>(sets);
			newList.add(List);
			ArrayList<Integer> newIndex = new ArrayList<Integer>(indx);
			newIndex.add(i);
			subSetCombination(sum - List, newList, newIndex, ++index, ItemsCount);
		}
	}

	// Generates the next closest value incase of no combination of sum equal to user's input
	// closest values are generated in price -0.01 and price+0.01 pattern until a combination is found
	static void getValues(int i) {
		double weight_d = 0.0;
		double weight_i = val_w.get(i) + 0.01;
		if (i == 0)
			weight_d = val_w.get(i) - 0.01;
		else
			weight_d = val_w.get(i - 1) - 0.01;
		BigDecimal valu = new BigDecimal(weight_d).setScale(2,
				BigDecimal.ROUND_HALF_UP);
		weight_d = new Double(valu.doubleValue());
		val_w.add(weight_d);
		valu = new BigDecimal(weight_i).setScale(2, BigDecimal.ROUND_HALF_UP);
		weight_i = new Double(valu.doubleValue());
		val_w.add(weight_i);

	}

	//finding all the combinations for the given price
	static List<ArrayList<Double>> combination(double[] prices, double weight,
			int ItemsCount) {
		Subsets = new ArrayList<>();
		indexes = new ArrayList<>();
		val_w = new ArrayList<Double>();
		Input_prices = prices;
		int i = 1;
		int get_val = 2;
		val_w.add(weight);
		for (i = 0; i < val_w.size(); i++) {
			subSetCombination(val_w.get(i), new ArrayList<Double>(),
					new ArrayList<Integer>(), 0, ItemsCount);
			if (Subsets.size() > 0)
				break;
			if (get_val % 2 == 0) {
				getValues(i);
			}
			get_val++;
		}
		if (Subsets.size() == 0)
			System.out.println("no solution");
		else {
			if (val_w.get(i) != weight)
				System.out
						.println("We're sorry, we couldn't find an exact match. But we got as close as we can get:"
								+ val_w.get(i));
		}
		return Subsets;
	}
}
