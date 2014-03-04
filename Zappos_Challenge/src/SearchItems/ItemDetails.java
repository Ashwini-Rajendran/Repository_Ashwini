/* Zappos Challenge
 * Author: Ashwini Rajendran
 * Program to display the available products details from the catalogue with the sum of prices equal or closest to the 
 * user input price and number of products.
 * */
 
package SearchItems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class ItemDetails {
	public static void main(String[] args) throws ClientProtocolException,
			IOException {

		Boolean cont = true;
		int no_of_items;
		double total_price_of_all_products = 0.0;
		double total_cost = 0.0;
		double prices[] = { 0.0 };
		Boolean iterate1, iterate2;
		JSONObject object1, object2;
		Object object;
		JSONParser parser;
		JSONArray array = null;
		Scanner scandata;

		// Using HttpClient to connect to the Zappos API
		// Getting the list of available product's details using "Search?"
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(
					"http://api.zappos.com/Search?&key=5b8384087156eb88dce1a1d321c945564f4d858e");
			HttpResponse response = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = "", response_data = "";
			while ((line = reader.readLine()) != null) {
				response_data = line;
			}

			System.out.println("Search Started...");
			System.out.println();
			
			// Using the JSONParser , decoding the JSON data and fetching the prices of all the products.
			// Placing all results in the JSONarray.
			parser = new JSONParser();
			object = parser.parse(response_data);
			object1 = (JSONObject) object;
			array = (JSONArray) object1.get("results");
			prices = new double[array.size() + 1];
			//Calculating the total price of the products in the catalogue
			for (int i = 0; i < array.size(); i++) {
				object2 = (JSONObject) array.get(i);
				prices[i] = Double.parseDouble(object2.get("price").toString()
						.substring(1));
				total_price_of_all_products = total_price_of_all_products
						+ prices[i];
			}
			//formatting the double value with 2 precision
			BigDecimal valu = new BigDecimal(total_price_of_all_products)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			total_price_of_all_products = new Double(valu.doubleValue());
		} catch (Exception e) {
			System.out.println("Error in connection.Please try again");
			return;
		}

		while (cont) {
			iterate1 = true;
			iterate2 = true;
			no_of_items = 0;
			total_cost = 0.0;
			scandata = new Scanner(System.in);
			try {
				// Obtaining the number of items and total price from the user
				while (iterate1) {
					System.out.print("Enter the number of items:");
					no_of_items = scandata.nextInt();

					if (no_of_items > array.size()) {
						System.out
								.println("Sorry.Number of items is too high.Please enter number of items below "
										+ (array.size() + 1));

					} else if (no_of_items <= 0) {
						System.out
								.println("Sorry.Number of items must be greater than zero");

					} else
						iterate1 = false;

				}
				while (iterate2) {
					System.out.print("Enter the total price:");
					total_cost = scandata.nextDouble();

					if (total_cost > total_price_of_all_products) {
						System.out
								.println("Sorry.Total price is exceeding the total price of products in the catalogue.Maximum limit: "
										+ (total_price_of_all_products));

					} else if (total_cost <= 0) {
						System.out
								.println("Sorry.Total cost must be greater than 0");

					} else
						iterate2 = false;

				}
				System.out.println();
				System.out.println("Please Wait...Searching for the combination......");
				System.out.println();
				// Finding the products with given sum using below function
				ProductCombination.combination(prices, total_cost, no_of_items);
				System.out.println();
				System.out
						.println("Following combinations of products are found");
				System.out.println();
				
				//Displaying the obtained list of product's details
				for (int k = 0; k < ProductCombination.indexes.size(); k++) {
					ArrayList<Integer> index_values = ProductCombination.indexes
							.get(k);
					System.out.println("Products Combination :" + (k + 1));
					for (int i = 0; i < index_values.size(); i++) {
						object2 = (JSONObject) array.get(index_values.get(i));
						System.out.println("**Product Name: "
								+ object2.get("productName") + "  **ProductID: "
								+ object2.get("productId")+ "  **Price: "
								+ object2.get("price") + "  **BrandName: "
								+ object2.get("brandName") + "  **SytleID: "
								+ object2.get("styleId") + "  **ProductURL: "
								+ object2.get("productUrl")+ "  **ColourID: "
								+ object2.get("colorId"));
						

					}
					System.out.println();
				}

				System.out.print("Do you want to continue(y/n):");
				String user_decision = scandata.next();
				if (user_decision.equalsIgnoreCase("n")) {
					System.out.println("Search Ended...");
					cont = false;
				}
			}

			catch (Exception e) {
				System.out.println("Something went wrong. Please try again.");
				System.out.println();
			}
		}
	}
}
